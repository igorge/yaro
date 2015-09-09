package gie.gsg


trait Node extends WithStateSet {
  def accept(visitor: NodeVisitor): Unit
}