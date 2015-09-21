package gie.gsg.state_attribute

import gie.gl.RichContext
import gie.gsg.RenderContext

trait VertexAttributeAttributeComponent {
  this: RenderContext with StateAttributeComponent with GlProgramComponent with ShaderVariableComponent  =>

  class VertexAttributeAttribute(val vertexAttr:gl.VertexAttribute, bufferFun: ()=>gl.GLBuffer, val componentSize: Int, componentType: Int, stride: Int, offset: Int) extends ShaderVariableAttribute {
    lazy val buffer = bufferFun()
    def name = vertexAttr.name

    def apply(): Unit={
      vertexAttr.bindBuffer(buffer).vertexAttribPointer(componentSize, componentType, true, stride, offset).enable()
    }
    def unapply(): Unit={
      vertexAttr.disable()
      gl.bindNullBuffer(gl.const.ARRAY_BUFFER)
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