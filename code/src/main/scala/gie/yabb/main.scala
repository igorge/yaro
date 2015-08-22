package gie.yaro


import slogging._


import scala.scalajs.js.JSApp

import scalajs.js
import org.scalajs.dom

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
      |
      |attribute vec3 a_color;
      |attribute vec3 a_position;
      |
      |varying vec4 v_color;
      |
      |void main() {
      |   v_color = vec4(a_color, 1);
      |   gl_Position = u_mv*vec4(a_position, 1);
      |}
      |
    """.stripMargin

  val fragmentShader =
    """
      |precision mediump float;
      |
      |varying vec4 v_color;
      |
      |void main() {
      |   gl_FragColor = v_color;
      |}
      |
    """.stripMargin
}

object app extends JSApp with LazyLogging {

  final val NANOS_IN_SEC      = 1000000000L
  final val NANOS_IN_MILLISEC = 1000000L

  def main(): Unit = {

    LoggerConfig.factory = ConsoleLoggerFactory
    LoggerConfig.level = LogLevel.TRACE

    logger.info("gie.yaro.app.main()")

    //dom.alert("Hi from Scala-js-dom")


    dom.document.addEventListener("DOMContentLoaded", (e:dom.Event)=>{


      val canvas = dom.document.getElementById("render-canvas").asInstanceOf[dom.html.Canvas]
      assume(canvas ne null)

      val gl = new gie.gl.WebGLContext( canvas.getContext("webgl").asInstanceOf[dom.raw.WebGLRenderingContext] ) with gie.gl.RichContext with gie.gl.ContextUnbind with gie.gl.SML_Matrix4F

      val geom = gie.geom.square(1,1,1)
      val squareBuffer = gl.createBuffer(gl.const.ARRAY_BUFFER, geom._1, gl.const.STATIC_DRAW)
      val squareColors = gl.createBuffer(
        target = gl.const.ARRAY_BUFFER,
        usage = gl.const.STATIC_DRAW,
        data = js.Array[Float](1f,0f,0f, 1f,0f,0f, 1f,0f,0f, 0f,1f,0f, 0f,1f,0f, 0f,1f,0f)
      )

      val mapToLocations = gl.nameToLocationsMaps()

      val u_mv = gl.Uniform("u_mv", mapToLocations.uniforms)
//      val u_projection = gl.Uniform("u_projection", mapToLocations.uniforms)
//      val u_texture = gl.Uniform("u_texture", mapToLocations.uniforms)

      val a_position = gl.VertexAttribute("a_position", mapToLocations.attributes)
      val a_color = gl.VertexAttribute("a_color", mapToLocations.attributes)
//      val a_tex_coordinate = gl.VertexAttribute("a_tex_coordinate", mapToLocations.attributes)

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

      a_position
        .bindBuffer(squareBuffer)
        .vertexAttribPointer(3, gl.const.FLOAT, true, 0, 0)

      a_color
        .bindBuffer(squareColors)
        .vertexAttribPointer(3, gl.const.FLOAT, true, 0, 0)


      gl.bindNullBuffer(gl.const.ARRAY_BUFFER)

      var angle = 0f

      def tick(oldTime: Long)(t:Double): Unit ={
        import gie.sml._

        val currentTimeNano = System.nanoTime()

        dom.window.requestAnimationFrame(tick(currentTimeNano) _)

        val delta:Double  = (currentTimeNano-oldTime).toDouble / NANOS_IN_SEC

        gl.clear(gl.const.COLOR_BUFFER_BIT | gl.const.DEPTH_BUFFER_BIT)

        a_position.enable()
        a_color.enable()

        val m = Matrix4F.identity()
        val rotZ = Matrix4F.zero()

        angle+=(delta*1).toFloat
        rotZ.rotZ_!(angle)

//        val m = Mat4.apply(
//          Mat4x3
//            translate( Vec3(0.5f, 0, 0) )             //rotateZ(radians(12))
//        )
        gl.uniformMatrix4fv(u_mv.get, false, m*rotZ)


        gl.drawArrays(gl.const.TRIANGLES, 0, 6)
      }

      tick(System.nanoTime())(0)

    })

    //val m2 = Mat2(1, 2, 3, 4)
//    val model = Mat4x3.scale(Vec3(1, 1, 3)) rotateZ(radians(45)) translate(Vec3(10, 5, 10))



  }

}

