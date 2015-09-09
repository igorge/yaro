package gie.gsg

import scala.collection.mutable.ArrayBuffer


class Group extends Node with WithStateSetImpl {
  val children = new ArrayBuffer[Node]()

  def accept(visitor: NodeVisitor): Unit ={
    visitor.visit(this)
  }
}