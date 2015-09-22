package gie.gsg.state_attribute



trait StateAttributeVisitorComponent {
  this: StateAttributeComponent with GlProgramAttributeComponent with Texture2DComponent =>

  trait StateAttributeVisitor {
    def visit(attr: GlProgramAttribute): Unit
    def visit(attr: Texture2D): Unit
  }


}