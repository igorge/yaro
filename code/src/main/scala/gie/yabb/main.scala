package gie.yaro


import slogging._


import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport


import simplex3d.math._
import simplex3d.math.float._
import simplex3d.math.float.functions._


import scala.util.Random
import scalajs.js
import org.scalajs.dom
import dom.raw.WebGLRenderingContext._


object shaderSource {

  val vertexShader =
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

  def main(): Unit = {

    LoggerConfig.factory = ConsoleLoggerFactory
    LoggerConfig.level = LogLevel.TRACE

    logger.info("gie.yaro.app.main()")

    //dom.alert("Hi from Scala-js-dom")


    dom.document.addEventListener("DOMContentLoaded", (e:dom.Event)=>{


      val canvas = dom.document.getElementById("render-canvas").asInstanceOf[dom.html.Canvas]
      assume(canvas ne null)

      val gl = new gie.gl.WebGLContext( canvas.getContext("webgl").asInstanceOf[dom.raw.WebGLRenderingContext] ) with gie.gl.RichContext with gie.gl.Simplex3D


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
        .compile
        .get

      val fragmentShader = gl.shaderOps(gl.createFragmentShader())
        .source(shaderSource.fragmentShader)
        .compile
        .get

      program
        .attach(vertexShader).attach(fragmentShader)
        .updateAndLink(mapToLocations)

//      gl.VertexAttribute.updateLocations(program.get, attribs)
//
//      program.link()
//
//      gl.Uniform.updateLocations(program.get, uniforms)


      def ff(t:Double): Unit ={

        dom.window.requestAnimationFrame(ff _)

        gl.viewport(0,0,canvas.width, canvas.height)
        gl.clearColor(Random.nextFloat(), Random.nextFloat(), Random.nextFloat(),1f)
        gl.clear(COLOR_BUFFER_BIT | DEPTH_BUFFER_BIT)
      }

      ff(0)

    })

    //val m2 = Mat2(1, 2, 3, 4)
    //val model = Mat4x3.scale(Vec3(1, 1, 3)) rotateZ(radians(45)) translate(Vec3(10, 5, 10))



  }

}

