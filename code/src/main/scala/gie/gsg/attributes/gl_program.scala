package gie.gsg.state_attribute

import gie.gl.Context
import gie.gsg.{ProgramHolderComponent, RenderContext}
import gie.sml.MatrixRead4F
import slogging.LoggerHolder

trait GlProgramAttributeComponent {
  this: ProgramHolderComponent with ShaderVariableComponent with StateAttributeVisitorComponent with RenderContext with StateAttributeComponent with UniformValueAttributeComponent with LoggerHolder =>


  class GlProgramAttribute(programHolderCtor: => GlProgramHolder) extends StateAttribute {

    lazy val programHolder: GlProgramHolder = programHolderCtor

    private[gsg] def m_program = programHolder.program

    final val index = GlProgramAttribute.index

    def ===(y: StateAttribute): Boolean = if (index != y.index) false else { m_program == y.asInstanceOf[GlProgramAttribute].m_program }

    def accept(visitor: StateAttributeVisitor): Unit= visitor.visit(this)

    def applied(): Unit={
      programHolder.applied()
    }

  }

  object GlProgramAttribute {
    val index = 0
  }

}

