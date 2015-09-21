package gie.yaro


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

  def main(): Unit = {
    import gie.sml._
    import gie.sml.ImplicitOps._

    LoggerConfig.factory = ConsoleLoggerFactory
    LoggerConfig.level = LogLevel.TRACE

    logger.info("gie.yaro.app.main()")

    //dom.alert("Hi from Scala-js-dom")

    val roServices = new TextureManagerComponent
      with RsmLoaderComponent
      with RoStoreComponent
      with RoResourceComponent
      with ExecutionContextComponent
      with LazyLogging
    {
      implicit def executionContext: ExecutionContext = appExecutionContext
    }



    dom.document.addEventListener("DOMContentLoaded", (e:dom.Event)=>{


      val canvas = dom.document.getElementById("render-canvas").asInstanceOf[dom.html.Canvas]
      assume(canvas ne null)

      val gl = new gie.gl.WebGLContext( canvas.getContext("webgl").asInstanceOf[dom.raw.WebGLRenderingContext] ) with gie.gl.RichContext with gie.gl.SML_Matrix4FRich {
        //@inline override def checkGlError(): Unit = { /*noop*/ }
      }

      val geom = gie.geom.square(1,1,1)
      val squareBuffer = gl.createBuffer(gl.const.ARRAY_BUFFER, geom._1, gl.const.STATIC_DRAW)
      val squareTexCoord = gl.createBuffer(
        target = gl.const.ARRAY_BUFFER,
        usage = gl.const.STATIC_DRAW,
        data = geom._2
      )
      val squareColors = gl.createBuffer(
        target = gl.const.ARRAY_BUFFER,
        usage = gl.const.STATIC_DRAW,
        data = js.Array[Float](1f,0f,0f, 1f,0f,0f, 1f,0f,0f, 0f,1f,0f, 0f,1f,0f, 0f,1f,0f)
      )


      val renderer = new gsg.Renderer(gl)

      class Holder1 extends renderer.GlProgramHolder {

        def transformationMatrix_=(m:MatrixRead4F): m.type = {
          renderer.gl.uniformMatrix(u_mv) = m
          m
        }
        def projectionMatrix_=(m:MatrixRead4F): m.type = {
          renderer.gl.uniformMatrix(u_projection) = m
          m
        }

        val p = renderer.gl.Program()

        val mapToLocations = renderer.gl.nameToLocationsMaps()

        private val u_mv = renderer.gl.Uniform("u_mv", mapToLocations.uniforms)
        private val u_projection = renderer.gl.Uniform("u_projection", mapToLocations.uniforms)
        val u_texture = renderer.gl.Uniform("u_texture", mapToLocations.uniforms)

        val a_position = renderer.gl.VertexAttribute("a_position", mapToLocations.attributes)
        val a_color = renderer.gl.VertexAttribute("a_color", mapToLocations.attributes)
        val a_tex_coordinate = renderer.gl.VertexAttribute("a_tex_coordinate", mapToLocations.attributes)

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


          a_position
            .bindBuffer(squareBuffer)
            .vertexAttribPointer(3, gl.const.FLOAT, true, 0, 0)

          a_color
            .bindBuffer(squareColors)
            .vertexAttribPointer(3, gl.const.FLOAT, true, 0, 0)

          a_tex_coordinate
            .bindBuffer(squareTexCoord)
            .vertexAttribPointer(2, gl.const.FLOAT, true, 0, 0)

          projectionMatrix = gie.sml.Matrix4F.ortho(-1, 1, 1, -1, 1, 0)

          renderer.gl.uniform(u_texture) = 1 //DEBUG

        }

      }

      lazy val programHolder = new Holder1

      val attr_program = new renderer.GlProgramAttribute( programHolder)

      def loadTex(path: String, alpha: Int) = async {
        val (w,h,data) = await(roServices.textureManager.get(path, alpha))
        gl.withBoundTexture(gl.const.TEXTURE_2D, gl.genTextures()){ texture=>
          gl.texImage2D(gl.const.TEXTURE_2D, 0, gl.const.RGBA, w, h, 0, gl.const.RGBA, gl.const.UNSIGNED_BYTE, data)
          gl.texParameter(gl.const.TEXTURE_2D, gl.const.TEXTURE_MAG_FILTER, gl.const.NEAREST)
          gl.texParameter(gl.const.TEXTURE_2D, gl.const.TEXTURE_MIN_FILTER, gl.const.NEAREST)

          texture
        }
      }


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

        await(roServices.rsmLoader.load("글래지하수로/하수구_라이온1.rsm"))



        gl.viewport(0,0,canvas.width, canvas.height)
        gl.clearColor(0,0,0,1f)


        //program.use()//renderProg.apply()

        gl.enable(gl.const.BLEND)
        gl.blendFunc(gl.const.SRC_ALPHA, gl.const.ONE_MINUS_SRC_ALPHA)


        gl.bindNullBuffer(gl.const.ARRAY_BUFFER)

        var angle = 0f

        var delta:Double = 0

        val rootGroup = renderer.transformation {
          angle+=(delta*1).toFloat
          Matrix4F.rotZ(angle)
        }

        val node = new renderer.OwnerDraw(self=>{
          gl.clear(gl.const.COLOR_BUFFER_BIT | gl.const.DEPTH_BUFFER_BIT)

          //renderer.gl.uniform(programHolder.u_texture) = 0

//          programHolder.a_position.enable()
//          programHolder.a_color.enable()
          //programHolder.a_tex_coordinate.enable()

          gl.drawArrays(gl.const.TRIANGLES, 0, 6)
        })

        node
          .addAttribute( attr_program )
          .addAttribute(new renderer.Texture2D(tex1,0))
          .addUniformValue(programHolder.constUniformValue(programHolder.u_texture)(0))
          .addVertexAttributeValue( programHolder.a_position )
          .addVertexAttributeValue( programHolder.a_color )
          .addVertexAttributeValue( programHolder.a_tex_coordinate )

        rootGroup.children += node

        def tick(oldTime: Long)(t:Double): Unit ={

          val currentTimeNano = System.nanoTime()

          dom.window.requestAnimationFrame(tick(currentTimeNano) _)

          delta = (currentTimeNano-oldTime).toDouble / NANOS_IN_SEC

          renderer.render(rootGroup)

        }

        tick(System.nanoTime())(0)

      }.onComplete( _.get )})



  }

}




