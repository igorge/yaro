package gie.gsg.state_attribute

import gie.gl.Context
import gie.gsg.{RenderContext, AttributeVisitor}

trait StateAttribute[CTX <: RenderContext] {
  val index: Int

  def ===(y: StateAttribute[CTX]): Boolean
  def accept(visitor:AttributeVisitor[CTX]): Unit
}

object StateAttribute {
  val attributesCount = 3
}

