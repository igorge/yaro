package gie.gsg.state_attribute

import gie.gl.Context
import gie.gsg.{RenderContext}

trait GlProgramComponent {
  this: RenderContext with StateAttributeComponent =>

  class GlProgram(val program: gl.GLProgram) extends StateAttribute {
    val index = 0

    def ===(y: StateAttribute): Boolean = if (index != y.index) false else { program == y.asInstanceOf[GlProgram].program }

    def apply(): Unit={
      gl.useProgram(program)
    }

    def unapply(): Unit ={
      gl.useNullProgram()
    }

  }

}

