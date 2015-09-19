package gie.gsg.state_attribute

import gie.gsg.{RenderContext}


trait Texture2DComponent {
  this: StateAttributeVisitorComponent with RenderContext with StateAttributeComponent  =>

  class Texture2D(texture: gl.GLTexture, textureUnit: Int) extends StateAttribute {
    final val index = 1

    private [gsg] val m_textureUnit: Int = textureUnit
    private [gsg] val m_texture: gl.GLTexture = texture

    def ===(y: StateAttribute): Boolean = if (index != y.index) {
      false
    } else {
      val yTyped = y.asInstanceOf[Texture2D]
      (m_texture==yTyped.m_texture) && (m_textureUnit==yTyped.m_textureUnit)
    }

    def accept(visitor: StateAttributeVisitor): Unit= visitor.visit(this)

  }

}