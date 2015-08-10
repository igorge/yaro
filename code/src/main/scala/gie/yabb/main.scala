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

object app extends JSApp with LazyLogging {

  def main(): Unit = {

    LoggerConfig.factory = ConsoleLoggerFactory
    LoggerConfig.level = LogLevel.TRACE

    logger.info("gie.yaro.app.main()")

    //dom.alert("Hi from Scala-js-dom")


    dom.document.addEventListener("DOMContentLoaded", (e:dom.Event)=>{

      val canvas = dom.document.getElementById("render-canvas").asInstanceOf[dom.html.Canvas]
      assume(canvas ne null)

      val gl = new gie.gl.WebGLContext( canvas.getContext("webgl").asInstanceOf[dom.raw.WebGLRenderingContext] ) with gie.gl.WebGlResourceContext with gie.gl.Resources with gie.gl.Simplex3D

      def ff(t:Double): Unit ={

        dom.window.requestAnimationFrame(ff _)

        gl.clearColor(Random.nextFloat(), Random.nextFloat(), Random.nextFloat(),1f)
        gl.clear(COLOR_BUFFER_BIT | DEPTH_BUFFER_BIT)
      }

      ff(0)



//      gl.asInstanceOf[js.Dynamic].viewportWidth = canvas.width
//      gl.asInstanceOf[js.Dynamic].viewportHeight = canvas.height


    })

    //val m2 = Mat2(1, 2, 3, 4)
    //val model = Mat4x3.scale(Vec3(1, 1, 3)) rotateZ(radians(45)) translate(Vec3(10, 5, 10))



  }

}

