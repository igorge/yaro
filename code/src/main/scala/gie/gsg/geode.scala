package gie.gsg


class Geode extends Node {

  def accept(visitor: NodeVisitor): Unit ={
    visitor.visit(this)
  }

}