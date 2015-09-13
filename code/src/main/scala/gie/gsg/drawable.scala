package gie.gsg

trait DrawableComponent {
  this: WithStateSetComponent =>

  trait Drawable extends WithStateSet {
    //def bound: BoundingBox = ???
    //def shape: Shape = ???
  }

}