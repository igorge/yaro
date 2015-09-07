package gie.gsg


trait Node {
  def accept(visitor: NodeVisitor): Unit
}