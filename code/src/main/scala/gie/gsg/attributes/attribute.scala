package gie.gsg.state_attribute

import gie.gl.RichContext
import gie.gsg.RenderContext

trait GlAttributeComponent {
  this: RenderContext with StateAttributeComponent with GlProgramComponent with ShaderVariableComponent =>

  class VertexAttributeAttribute(val vertexAttr:gl.VertexAttribute) extends ShaderVariableAttribute {
    def name = vertexAttr.name

    def apply(): Unit={
      vertexAttr.enable()
    }
    def unapply(): Unit={
      vertexAttr.disable()
    }

    def ===(y: ShaderVariableAttribute): Boolean = {
      assume(name==y.name) //TODO: remove

      if (name != y.name) {
        false
      } else {
        val yTyped = y.asInstanceOf[VertexAttributeAttribute]
        vertexAttr eq yTyped.vertexAttr
      }
    }

    override def toString = s"VertexAttributeAttribute(${name})"

  }
}