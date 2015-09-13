package gie.gsg

trait TransformComponent {
  this: NodeComponent with GroupComponent with NodeVisitorComponent =>

  class Transform extends Group {
    override def accept(visitor: NodeVisitor): Unit = {
      visitor.visit(this)
    }
  }

}