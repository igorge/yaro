package gie.gsg

trait NodeComponent {
  this: WithStateSetComponent with NodeVisitorComponent =>

  trait Node extends WithStateSet {
    //def accept(visitor: NodeVisitor): Unit
  }

}