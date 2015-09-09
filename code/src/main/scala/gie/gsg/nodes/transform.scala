package gie.gsg

class Transform extends Group {
  override def accept(visitor: NodeVisitor): Unit ={
    visitor.visit(this)
  }
}