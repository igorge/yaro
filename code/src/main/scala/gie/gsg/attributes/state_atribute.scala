package gie.gsg.state_attribute

import gie.gl.Context
import gie.gsg.{RenderContext}


trait StateAttributeComponent {
  this: RenderContext =>

  trait StateAttribute{
    val index: Int

    def ===(y: StateAttribute): Boolean
    def apply(): Unit
    def unapply(): Unit
  }


}


object StateAttribute {
  val attributesCount = 3
}

