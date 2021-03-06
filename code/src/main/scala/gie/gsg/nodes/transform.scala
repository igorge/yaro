package gie.gsg

import gie.sml.MatrixRead4F

trait TransformComponent {
  this: NodeComponent with GroupComponent with NodeVisitorComponent with StateSetComponent =>

  abstract class Transform extends Group {

    def m: MatrixRead4F

    override def accept(visitor: NodeVisitor, parentMergedStateSet: StateSet, transformation: MatrixRead4F): Unit = {
      visitor.visit(this, parentMergedStateSet, transformation)
    }
  }

  class TransformMatrix(private var m_m: MatrixRead4F){
    def m: MatrixRead4F = m_m
    def m_=(new_m:MatrixRead4F): this.type ={
      m_m = new_m
      this
    }
  }

  def transformation(f: =>MatrixRead4F): Transform = new Transform {
    def m: MatrixRead4F = f
  }

}