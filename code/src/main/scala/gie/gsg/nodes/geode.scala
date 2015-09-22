package gie.gsg

import scala.collection.mutable.ArrayBuffer


trait GeodeComponent {
  this: NodeComponent with WithStateSetComponent with DrawableComponent with NodeVisitorComponent =>

  class Geode extends Node with WithStateSetImpl {

    val drawables = new ArrayBuffer[Drawable]()

    def addDrawable(drawable: Drawable): this.type={
      drawables += drawable
      this
    }

//    def accept(visitor: NodeVisitor): Unit = {
//      visitor.visit(this)
//    }

  }

}