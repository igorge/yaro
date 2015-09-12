package gie.gsg

import gie.gl.Context


trait AttributeVisitor[CTX <: RenderContext] {
  def visit(n: state_attribute.GlProgram[CTX]): Unit
}