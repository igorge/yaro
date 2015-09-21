package gie.gsg.state_attribute

import gie.gl.RichContext
import gie.gsg.RenderContext

trait VertexAttributeAttributeComponent {
  this: RenderContext with StateAttributeComponent with GlProgramComponent with ShaderVariableComponent  =>

  class VertexAttributeAttribute(val vertexAttr:gl.VertexAttribute, bufferFun: ()=>gl.GLBuffer, val componentSize: Int, val componentType: Int, val stride: Int, val offset: Int) extends ShaderVariableAttribute {
    lazy val buffer = bufferFun()
    def name = vertexAttr.name

    def apply(from: ShaderVariableAttribute): Unit={
      if(from eq null) {
        vertexAttr.bindBuffer(buffer).vertexAttribPointer(componentSize, componentType, true, stride, offset).enable()
      } else {
        val fromT = from.asInstanceOf[VertexAttributeAttribute]
        if(buffer != fromT.buffer) vertexAttr.bindBuffer(buffer)
        vertexAttr.vertexAttribPointer(componentSize, componentType, true, stride, offset).enable()
      }
    }
    def unapply(): Unit={
      vertexAttr.disable()
      gl.bindNullBuffer(gl.const.ARRAY_BUFFER)
    }

    def ===(y: ShaderVariableAttribute): Boolean = {
      assume(name==y.name) //TODO: remove

      if (name != y.name) {
        false
      } else if (this eq y) {
        true
      } else {
        val yTyped = y.asInstanceOf[VertexAttributeAttribute]

        (vertexAttr eq yTyped.vertexAttr) &&
          (buffer == yTyped.buffer) &&
          (componentSize == yTyped.componentSize) &&
          (componentType == yTyped.componentType) &&
          (stride == yTyped.stride) &&
          (offset == yTyped.stride)
      }
    }

    override def toString = s"VertexAttributeAttribute(name=${name}, componentSize=${componentSize}, componentType=${componentType}, stride=${stride}, offset=${offset})"

  }
}