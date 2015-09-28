package gie.gsg

import gie.sml.MatrixRead4F

trait DrawableVisitorComponent {
  this: StateSetComponent with DrawableComponent with TrianglesArrayComponent =>

  private[gsg] def api_renderTrianglesArray(drawable: TrianglesArray, parentMergedStateSet: StateSet, transformation: MatrixRead4F): Unit
  private[gsg] def api_renderTrianglesIndexArray(drawable: TrianglesIndexedArray, parentMergedStateSet: StateSet, transformation: MatrixRead4F): Unit

}