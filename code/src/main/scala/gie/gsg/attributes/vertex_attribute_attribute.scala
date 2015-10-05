package gie.gsg.state_attribute

import gie.gl.RichContext
import gie.gsg.{ProgramHolderComponent, RenderContext}

trait VertexAttributeAttributeComponent {
  this: RenderContext with ProgramHolderComponent with StateAttributeComponent with GlProgramAttributeComponent with ShaderVariableComponent  =>

  class VertexAttributeAttribute(vertexAttName: String, val buffer: gl.GLBuffer, val componentSize: Int, val componentType: Int, val stride: Int, val offset: Int) extends ShaderVariableAttribute {

    val bufferTarget: Int = gl.const.ARRAY_BUFFER

    lazy val vertexAttr = {
      val r = new gl.VertexAttribute(vertexAttName)
      m_activeProgram.resolveAttribute(r)
      r
    }
    def name = vertexAttName

    def apply(from: ShaderVariableAttribute): Unit={
      if(from eq null) {
        vertexAttr.bindBuffer(bufferTarget, buffer).vertexAttribPointer(componentSize, componentType, true, stride, offset).enable()
      } else {
        val fromT = from.asInstanceOf[VertexAttributeAttribute]
        if(buffer != fromT.buffer || bufferTarget!=fromT.bufferTarget) vertexAttr.bindBuffer(bufferTarget, buffer)
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
          (bufferTarget == yTyped.bufferTarget) &&
          (componentSize == yTyped.componentSize) &&
          (componentType == yTyped.componentType) &&
          (stride == yTyped.stride) &&
          (offset == yTyped.stride)
      }
    }

    override def toString = s"VertexAttributeAttribute(name=${name}, bufferTarget=${bufferTarget}, componentSize=${componentSize}, componentType=${componentType}, stride=${stride}, offset=${offset})"

  }
}