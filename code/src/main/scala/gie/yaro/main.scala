package gie.yaro


import gie.jsutils.XMLHttpRequestFuture
import slogging._


import scala.collection.mutable
import scala.concurrent.ExecutionContext
import scala.scalajs.js.JSApp
import scala.scalajs.js.typedarray.{Int8Array, ArrayBuffer, Uint8Array}

import scalajs.js
import org.scalajs.dom
import scala.async.Async._


object RoStore extends LazyLogging {

  import app.executionContext


  val prefix = s"${dom.location.origin}/"

  def open(path: String) = {
    val url = s"${prefix}${path}"

    logger.debug(url)

    val xhr = new dom.XMLHttpRequest()

    val f = new XMLHttpRequestFuture(xhr, Some(XMLHttpRequestFuture.responseType.arrayBuffer))

    xhr.open("GET", url)
    xhr.send()

    f.map{ r=>
      val buffer = new Int8Array ( r.asInstanceOf[ArrayBuffer] )
      new IndexedSeq[Byte] {
        def length = buffer.length
        def apply(idx: Int) = buffer(idx)
      }
    }
  }

}


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

  implicit val executionContext:ExecutionContext = scala.scalajs.concurrent.JSExecutionContext.Implicits.queue


  final val NANOS_IN_SEC      = 1000000000L
  final val NANOS_IN_MILLISEC = 1000000L

  def main(): Unit = {
    import gie.sml._
    import gie.sml.ImplicitOps._

    LoggerConfig.factory = ConsoleLoggerFactory
    LoggerConfig.level = LogLevel.TRACE

    logger.info("gie.yaro.app.main()")

    //dom.alert("Hi from Scala-js-dom")

    rsm.file.codec.test()


    dom.document.addEventListener("DOMContentLoaded", (e:dom.Event)=>{

      async {

      val canvas = dom.document.getElementById("render-canvas").asInstanceOf[dom.html.Canvas]
      assume(canvas ne null)

      val gl = new gie.gl.WebGLContext( canvas.getContext("webgl").asInstanceOf[dom.raw.WebGLRenderingContext] ) with gie.gl.RichContext with gie.gl.ContextUnbind with gie.gl.SML_Matrix4FRich {
        //@inline override def checkGlError(): Unit = { /*noop*/ }
      }

//        await(loadTexture(RoStore.open("ro-data-unpacked/texture/내부소품/gedan-side4.bmp")))

        def loadTex(path: String) = async {
          val (w,h,data) = await(loadTexture(RoStore.open(path)))
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

      val tex1 = await(loadTex("ro-data-unpacked/texture/내부소품/tor_boom.bmp")) //createSolidTexture(-1,0,0,-1)

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



      val mapToLocations = gl.nameToLocationsMaps()

      val u_mv = gl.Uniform("u_mv", mapToLocations.uniforms)
      val u_projection = gl.Uniform("u_projection", mapToLocations.uniforms)
      val u_texture = gl.Uniform("u_texture", mapToLocations.uniforms)

      val a_position = gl.VertexAttribute("a_position", mapToLocations.attributes)
      val a_color = gl.VertexAttribute("a_color", mapToLocations.attributes)
      val a_tex_coordinate = gl.VertexAttribute("a_tex_coordinate", mapToLocations.attributes)

      val program = gl.Program()

      val vertexShader = gl.shaderOps(gl.createVertexShader())
        .source(shaderSource.vertexShader)
        .compile()
        .get

      val fragmentShader = gl.shaderOps(gl.createFragmentShader())
        .source(shaderSource.fragmentShader)
        .compile()
        .get

      program
        .attach(vertexShader)
        .attach(fragmentShader)
        .updateAndLink(mapToLocations)

      gl.viewport(0,0,canvas.width, canvas.height)
      gl.clearColor(0,0,0,1f)


      program.use()

      gl.enable(gl.const.BLEND)
      gl.blendFunc(gl.const.SRC_ALPHA, gl.const.ONE_MINUS_SRC_ALPHA)

      val projection = gie.sml.Matrix4F.ortho(-1, 1, 1, -1, 1, 0)
      gl.uniformMatrix(u_projection) = projection

      a_position
        .bindBuffer(squareBuffer)
        .vertexAttribPointer(3, gl.const.FLOAT, true, 0, 0)

      a_color
        .bindBuffer(squareColors)
        .vertexAttribPointer(3, gl.const.FLOAT, true, 0, 0)

      a_tex_coordinate
        .bindBuffer(squareTexCoord)
        .vertexAttribPointer(2, gl.const.FLOAT, true, 0, 0)


      gl.bindNullBuffer(gl.const.ARRAY_BUFFER)

      var angle = 0f

      gl.uniform(u_texture) = 0
      gl.activateTexture(gl.const.TEXTURE0)
      gl.bindTexture(gl.const.TEXTURE_2D, tex1)

      def tick(oldTime: Long)(t:Double): Unit ={

        val currentTimeNano = System.nanoTime()

        dom.window.requestAnimationFrame(tick(currentTimeNano) _)

        val delta:Double  = (currentTimeNano-oldTime).toDouble / NANOS_IN_SEC

        gl.clear(gl.const.COLOR_BUFFER_BIT | gl.const.DEPTH_BUFFER_BIT)

        a_position.enable()
        a_color.enable()
        a_tex_coordinate.enable()

        val m = Matrix4F.identity()
        val rotZ = Matrix4F.zero()

        angle+=(delta*1).toFloat

        gl.uniformMatrix(u_mv) = m*Matrix4F.rotZ(angle)

        gl.drawArrays(gl.const.TRIANGLES, 0, 6)
      }

      tick(System.nanoTime())(0)

    }.onComplete( _.get )})



  }

}




