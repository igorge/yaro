package gie.gsg.state_attribute



trait StateAttributeVisitorComponent {
  this: StateAttributeComponent with GlProgramComponent =>

  trait StateAttributeVisitor {
    def visit(attr: GlProgramAttribute): Unit
  }


}