package gie.gsg

import gie.sml.MatrixRead4F

trait NodeComponent {
  this: WithStateSetComponent with StateSetComponent with NodeVisitorComponent =>

  trait Node extends WithStateSet {
    def accept(visitor: NodeVisitor, parentMergedStateSet: StateSet, transformation: MatrixRead4F): Unit
  }

}