package gie.gsg

import gie.sml.MatrixRead4F

import scala.collection.mutable.ArrayBuffer


trait GeodeComponent {
  this: NodeComponent with WithStateSetComponent with DrawableComponent with NodeVisitorComponent with StateSetComponent =>

  class Geode(initDrawables: Drawable*) extends Node with WithStateSetImpl {

    val drawables = ArrayBuffer[Drawable](initDrawables :_*)

    def addDrawable(drawable: Drawable): this.type={
      drawables += drawable
      this
    }

    def accept(visitor: NodeVisitor, parentMergedStateSet: StateSet, transformation: MatrixRead4F): Unit = {
      visitor.visit(this, parentMergedStateSet, transformation)
    }

  }

}