package gie.gsg

import scala.collection.mutable.ArrayBuffer


class Geode extends Node with WithStateSetImpl {

  val drawables = new ArrayBuffer[Drawable]()

  def accept(visitor: NodeVisitor): Unit ={
    visitor.visit(this)
  }

}