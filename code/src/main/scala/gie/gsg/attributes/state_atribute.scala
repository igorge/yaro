package gie.gsg.state_attribute

import gie.gl.Context
import gie.gsg.{RenderContext}


trait StateAttributeComponent {
  this: StateAttributeVisitorComponent with RenderContext =>

  trait StateAttribute{
    val index: Int

    def ===(y: StateAttribute): Boolean
    def accept(visitor: StateAttributeVisitor): Unit
  }


}


object StateAttribute {
  val attributesCount = 3
}

