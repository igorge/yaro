package gie.gsg

import gie.sml.MatrixRead4F

trait OwnerDrawComponent {
  this: NodeComponent with WithStateSetComponent with NodeVisitorComponent with StateSetComponent =>

  class OwnerDraw(val f: OwnerDraw=>Unit) extends Node with WithStateSetImpl {
    def accept(visitor: NodeVisitor, parentMergedStateSet: StateSet, transformation: MatrixRead4F): Unit = {
      visitor.visit(this, parentMergedStateSet, transformation)
    }
  }

}