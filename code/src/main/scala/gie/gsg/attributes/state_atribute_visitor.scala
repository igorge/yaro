package gie.gsg.state_attribute



trait StateAttributeVisitorComponent {
  this: IndexBufferAttributeComponent with StateAttributeComponent with GlProgramAttributeComponent with Texture2DComponent =>

  trait StateAttributeVisitor {
    def visit(attr: GlProgramAttribute): Unit
    def visit(attr: Texture2D): Unit
    def visit(attr: IndexBufferAttribute): Unit
  }


}