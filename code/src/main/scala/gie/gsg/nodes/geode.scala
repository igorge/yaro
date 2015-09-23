package gie.gsg

import scala.collection.mutable.ArrayBuffer


trait GeodeComponent {
  this: NodeComponent with WithStateSetComponent with DrawableComponent with NodeVisitorComponent =>

  class Geode(initDrawables: Drawable*) extends Node with WithStateSetImpl {

    val drawables = ArrayBuffer[Drawable](initDrawables :_*)

    def addDrawable(drawable: Drawable): this.type={
      drawables += drawable
      this
    }

//    def accept(visitor: NodeVisitor): Unit = {
//      visitor.visit(this)
//    }

  }

}