package gie.gsg.state_attribute

import gie.gl.Context
import gie.gsg.{RenderContext, AttributeVisitor}

class GlProgram[CTX <: RenderContext](val program: CTX#GLT#GLProgram) extends StateAttribute[CTX] {
  val index = 0

  def ===(y: StateAttribute[CTX]): Boolean=if (index!=y.index) false else {
    program == y.asInstanceOf[GlProgram[CTX]].program
  }

  def accept(visitor:AttributeVisitor[CTX]): Unit = visitor.visit(this)
}

