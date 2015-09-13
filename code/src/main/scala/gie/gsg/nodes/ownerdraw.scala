package gie.gsg

trait OwnerDrawComponent {
  this: NodeComponent with WithStateSetComponent with NodeVisitorComponent =>

  class OwnerDraw(val f: OwnerDraw=>Unit) extends Node with WithStateSetImpl {
    override def accept(visitor: NodeVisitor): Unit = ???
  }

}