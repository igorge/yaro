package gie.gsg.state_attribute

import gie.gl.Context
import gie.gsg.{RenderContext}

trait GlProgramComponent {
  this: RenderContext with StateAttributeComponent =>


  abstract class GlProgramHolder {
    val program: gl.GLProgram
    def applied(): Unit
  }

  class GlProgramAttribute(programHolderCtor: => GlProgramHolder) extends StateAttribute {

    lazy val m_programHolder: GlProgramHolder = programHolderCtor

    private def applied(): Unit = m_programHolder.applied()
    private def m_program = m_programHolder.program

    final val index = 0

    def ===(y: StateAttribute): Boolean = if (index != y.index) false else { m_program == y.asInstanceOf[GlProgramAttribute].m_program }

    def apply(): Unit={
      gl.useProgram(m_program)
      applied()
    }

    def unapply(): Unit ={
      gl.useNullProgram()
    }

  }

}

