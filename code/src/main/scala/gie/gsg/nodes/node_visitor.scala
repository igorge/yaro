package gie.gsg

import gie.sml.MatrixRead4F

trait NodeVisitorComponent {
  this: RenderContext with NodeComponent with GroupComponent with TransformComponent with GeodeComponent with OwnerDrawComponent with StateSetComponent =>

  trait NodeVisitor {
    def visit(n: Transform, parentMergedStateSet: StateSet, transformation: MatrixRead4F): Unit
    def visit(n: Group, parentMergedStateSet: StateSet, transformation: MatrixRead4F): Unit
    def visit(n: Geode, parentMergedStateSet: StateSet, transformation: MatrixRead4F): Unit
    def visit(n: OwnerDraw, parentMergedStateSet: StateSet, transformation: MatrixRead4F): Unit
  }

}