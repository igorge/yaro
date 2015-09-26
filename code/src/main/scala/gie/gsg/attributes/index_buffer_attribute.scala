package gie.gsg.state_attribute

import gie.gl.Context
import gie.gsg.{ProgramHolderComponent, RenderContext}
import gie.sml.MatrixRead4F
import slogging.LoggerHolder

trait IndexBufferAttributeComponent {
  this: StateAttributeVisitorComponent with RenderContext with StateAttributeComponent with LoggerHolder =>


  class IndexBufferAttribute(val buffer: gl.GLBuffer) extends StateAttribute {

    final val index = 2

    def ===(y: StateAttribute): Boolean = if (index != y.index) false else { buffer == y.asInstanceOf[IndexBufferAttribute].buffer }

    def accept(visitor: StateAttributeVisitor): Unit= visitor.visit(this)


  }


}

