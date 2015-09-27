package gie.gsg

import gie.sml.MatrixRead4F

trait DrawableComponent {
  this: StateSetComponent with WithStateSetComponent with DrawableVisitorComponent  =>

  trait Drawable extends WithStateSet {
    //def bound: BoundingBox = ???
    //def shape: Shape = ???

    //def accept(visitor: DrawableVisitor): Unit
//    private [gsg] def prepareDraw(): Unit
    private [gsg] def draw(parentMergedStateSet: StateSet, transformation: MatrixRead4F): Unit
  }

}