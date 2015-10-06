package gie.yaro


import gie.geom.square
import org.scalajs.dom.raw.WebGLProgram
import slogging._


import scala.collection.mutable
import scala.concurrent.ExecutionContext
import scala.scalajs.js.JSApp
import scala.scalajs.js.typedarray.{Int8Array, ArrayBuffer, Uint8Array}

import scalajs.js
import org.scalajs.dom
import scala.async.Async._
import gie.gsg

object shaderSource {

  val vertexShaderAA =
    """
      |invariant gl_Position;
      |
      |uniform mat4 u_mv;
      |uniform mat4 u_projection;
      |
      |attribute vec3 a_position;
      |attribute vec2 a_tex_coordinate;
      |attribute vec4 a_color;
      |
      |varying vec4 v_color;
      |varying vec2 v_tex_coordinate;
      |
      |void main() {
      |   v_color = a_color;
      |   v_tex_coordinate = a_tex_coordinate;
      |   gl_Position = u_projection*u_mv*vec4(a_position, 1);
      |}
      |
    """.stripMargin

  val fragmentShaderAA =
    """
      |precision mediump float;
      |
      |varying vec4 v_color;
      |varying vec2 v_tex_coordinate;
      |
      |uniform sampler2D u_texture;
      |
      |void main() {
      |   gl_FragColor = texture2D(u_texture, v_tex_coordinate);
      |}
      |
    """.stripMargin


  val vertexShader =
    """
      |invariant gl_Position;
      |
      |uniform mat4 u_mv;
      |uniform mat4 u_projection;
      |
      |attribute vec3 a_color;
      |attribute vec2 a_tex_coordinate;
      |attribute vec3 a_position;
      |
      |varying vec4 v_color;
      |varying vec2 v_tex_coordinate;
      |
      |void main() {
      |   v_color = vec4(a_color, 1);
      |   v_tex_coordinate = a_tex_coordinate;
      |   gl_Position = u_projection*u_mv*vec4(a_position, 1);
      |}
      |
    """.stripMargin

  val fragmentShader =
    """
      |precision mediump float;
      |
      |varying vec4 v_color;
      |varying vec2 v_tex_coordinate;
      |
      |uniform sampler2D u_texture;
      |
      |void main() {
      |   gl_FragColor = texture2D(u_texture, v_tex_coordinate);
      |}
      |
    """.stripMargin
}



object app extends JSApp with LazyLogging {

  implicit val appExecutionContext:ExecutionContext = scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  final val NANOS_IN_SEC      = 1000000000L
  final val NANOS_IN_MILLISEC = 1000000L

  class OpenGLContext(canvas: dom.raw.WebGLRenderingContext)
    extends gie.gl.WebGLContext( canvas )
    with gie.gl.RichContext
    with gie.gl.SML_Matrix4FRich
    with LazyLogging {

    @inline override def optCheckGlError(): Unit = { /*noop*/ }
  }

  type Renderer = gsg.Renderer[OpenGLContext]


  def newRoServices(rendererP: Renderer) ={

    val roServices = new TextureManagerComponent
      with RendererContextComponent
      with RsmLoaderComponent
      with RoStoreComponent
      with RoResourceComponent
      with ExecutionContextComponent
      with LazyLogging
    {
      val renderer:rendererP.type = rendererP
      implicit def executionContext: ExecutionContext = appExecutionContext
    }

    roServices
  }

  def main(): Unit = {
    import gie.sml._
    import gie.sml.ImplicitOps._

    LoggerConfig.factory = ConsoleLoggerFactory
    LoggerConfig.level = LogLevel.TRACE

    logger.info("gie.yaro.app.main()")

    //dom.alert("Hi from Scala-js-dom")


    dom.document.addEventListener("DOMContentLoaded", (e:dom.Event)=>{


      val canvas = dom.document.getElementById("render-canvas").asInstanceOf[dom.html.Canvas]
      assume(canvas ne null)

      val gl = new OpenGLContext(canvas.getContext("webgl").asInstanceOf[dom.raw.WebGLRenderingContext])

      val renderer = new Renderer(gl)
      val roServices = newRoServices(renderer)

      val geom = gie.geom.square.gen(1,1,1)
      val squareBuffer = gl.createBuffer(gl.const.ARRAY_BUFFER, geom._1, gl.const.STATIC_DRAW)
      val squareTexCoord = gl.createBuffer(
        target = gl.const.ARRAY_BUFFER,
        usage = gl.const.STATIC_DRAW,
        data = geom._2
      )
      val squareColors = gl.createBuffer(
        target = gl.const.ARRAY_BUFFER,
        usage = gl.const.STATIC_DRAW,
        data = Array[Float](1f,0f,0f, 1f,0f,0f, 1f,0f,0f, 0f,1f,0f, 0f,1f,0f, 0f,1f,0f)
      )



      object attributesNames {
        val a_position = "a_position"
        val a_color = "a_color"
        val a_tex_coordinate = "a_tex_coordinate"
      }

      class Holder1 extends renderer.GlProgramHolder {

        def transformationMatrix_=(m:MatrixRead4F): m.type = {
          renderer.gl.uniformMatrix(u_mv) = m
          m
        }
        def projectionMatrix_=(m:MatrixRead4F): m.type = {
          renderer.gl.uniformMatrix(u_projection) = m
          m
        }

        val p = roServices.renderer.gl.Program()

        val mapToLocations = renderer.gl.nameToLocationsMaps()

        def resolveAttribute(attribute: renderer.gl.VertexAttributeApiTrait): attribute.type = {
          if( attribute.isDefined ) throw new Exception(s"vertex attribute '${attribute.name}' is already defined")

          attribute.location = mapToLocations.attributes.getOrElse(attribute.name, throw new Exception(s"attribute '${attribute.name}' not found")).location

          attribute
        }


        private val u_mv = renderer.gl.Uniform("u_mv", mapToLocations.uniforms)
        private val u_projection = renderer.gl.Uniform("u_projection", mapToLocations.uniforms)
        val u_texture = renderer.gl.Uniform("u_texture", mapToLocations.uniforms)

        val a_position = renderer.gl.VertexAttribute(attributesNames.a_position, mapToLocations.attributes)
        val a_color = renderer.gl.VertexAttribute(attributesNames.a_color, mapToLocations.attributes)
        val a_tex_coordinate = renderer.gl.VertexAttribute(attributesNames.a_tex_coordinate, mapToLocations.attributes)

        def vertexCoordinatesAttribute = a_position
        def vertexTextureCoordinatesAttribute = a_tex_coordinate
        def vertexColorAttribute =  a_color


        val vertexShader = renderer.gl.shaderOps(gl.createVertexShader())
          .source(shaderSource.vertexShader)
          .compile()
          .get

        val fragmentShader = renderer.gl.shaderOps(gl.createFragmentShader())
          .source(shaderSource.fragmentShader)
          .compile()
          .get

        p
          .attach(vertexShader)
          .attach(fragmentShader)
          .updateAndLink(mapToLocations)

        val program = p.program

        def applied() {
          val s = 50
          projectionMatrix = gie.sml.Matrix4F.ortho(-s, s, -s, s, 1, 100)
          renderer.gl.uniform(u_texture) = 1 //DEBUG
          gl.enable(gl.const.DEPTH_TEST)
        }

      }

      lazy val programHolder = new Holder1

      val attr_program = new renderer.GlProgramAttribute( programHolder)

      def loadTex(path: String, alpha: Int) = roServices.textureManager.get(alpha)(path)


      def createSolidTexture(r: Byte, g: Byte, b: Byte, a: Byte)={

        val data=Array[Byte](r,g,b,a ,0,-1,0,-1)
        gl.withBoundTexture(gl.const.TEXTURE_2D, gl.genTextures()){ texture=>
          gl.texImage2D(gl.const.TEXTURE_2D, 0, gl.const.RGBA, 2, 1, 0, gl.const.RGBA, gl.const.UNSIGNED_BYTE, data)
          gl.texParameter(gl.const.TEXTURE_2D, gl.const.TEXTURE_MAG_FILTER, gl.const.NEAREST)
          gl.texParameter(gl.const.TEXTURE_2D, gl.const.TEXTURE_MIN_FILTER, gl.const.NEAREST)

          texture
        }

      }

      async {

        val tex1 = await(loadTex("내부소품/tor_boom.bmp", 255)) //createSolidTexture(-1,0,0,-1)

        val rsmNode = await(roServices.rsmLoader.load("외부소품/알데바란카프라본사.rsm"))



        gl.viewport(0,0,canvas.width, canvas.height)
        gl.clearColor(0,0,0,1f)


        gl.enable(gl.const.BLEND)
        gl.blendFunc(gl.const.SRC_ALPHA, gl.const.ONE_MINUS_SRC_ALPHA)

        gl.bindNullBuffer(gl.const.ARRAY_BUFFER)

        var angle = 0f

        var delta:Double = 0

        val rootGroup = renderer.transformation {
          angle+=(delta*1).toFloat

          Matrix4F.translation(z = -50f)*Matrix4F.rotX(-1.0f)*Matrix4F.rotZ(angle)
        }

        val node = new renderer.OwnerDraw(self=>{
          //gl.drawArrays(gl.const.TRIANGLES, 0, 6)
          gl.drawElements(gl.const.TRIANGLES, 6, gl.const.UNSIGNED_SHORT, 0)
        })

        val indexedBox = square.genIndex(1,1,1)


        val squareNode= new renderer.TrianglesArray( renderer.staticArrayBuffer(geom._1), Some(renderer.staticArrayBuffer(geom._2)))
        squareNode
          .addAttribute(new renderer.Texture2D(tex1,0))
          .addUniformValue(programHolder.u_texture.name, 0)


                node
                  .addAttribute(new renderer.Texture2D(tex1,0))
                  .addUniformValue(programHolder.u_texture.name, 0)
                  .addAttribute( new renderer.IndexBufferAttribute(gl.createBuffer(gl.const.ELEMENT_ARRAY_BUFFER, gl.AsUnsignedShort(indexedBox._1), gl.const.STATIC_DRAW)))
                  .addVertexAttributeValue( attributesNames.a_position, 3, gl.const.FLOAT){ gl.createBuffer(gl.const.ARRAY_BUFFER, indexedBox._2, gl.const.STATIC_DRAW) }
                  .addVertexAttributeValue( attributesNames.a_tex_coordinate,  2, gl.const.FLOAT ){gl.createBuffer(gl.const.ARRAY_BUFFER, indexedBox._3, gl.const.STATIC_DRAW)}
//                node
//                  .addAttribute(new renderer.Texture2D(tex1,0))
//                  .addUniformValue(programHolder.constUniformValue(programHolder.u_texture)(0))
//                  .addVertexAttributeValue( attributesNames.a_position, 3, gl.const.FLOAT){ squareBuffer}
//                  .addVertexAttributeValue( attributesNames.a_tex_coordinate,  2, gl.const.FLOAT ){squareTexCoord}

        val squareNodeIndex= {
          val n = new renderer.TrianglesIndexedArray( renderer.staticElementArrayBuffer( renderer.gl.AsUnsignedShort(indexedBox._1) ), renderer.staticArrayBuffer(indexedBox._2), Some(renderer.staticArrayBuffer(indexedBox._3)))
          n.addAttribute(new renderer.Texture2D(tex1,0))
           .addUniformValue(programHolder.u_texture.name, 0)
          n
        }


        rsmNode.addUniformValue(programHolder.u_texture.name, 0)
        //rootGroup += new renderer.Geode(squareNodeIndex)
        //rootGroup += new renderer.Geode(squareNode)
        rootGroup += rsmNode
        rootGroup.addAttribute( attr_program )

        def tick(oldTime: Long)(t:Double): Unit =try {

          val currentTimeNano = System.nanoTime()

          dom.window.requestAnimationFrame(tick(currentTimeNano) _)

          delta = (currentTimeNano-oldTime).toDouble / NANOS_IN_SEC

          gl.clear(gl.const.COLOR_BUFFER_BIT | gl.const.DEPTH_BUFFER_BIT)
          renderer.render(rootGroup)

        } catch {
          case e:Exception=>
            logger.debug(s">>> ${e} >>>")
            e.printStackTrace()
        }

        tick(System.nanoTime())(0)

      }.onComplete{r=>
        logger.debug(s">> ${r}")
        r.get
      }})



  }

}




