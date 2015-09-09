package gie.gsg


trait NodeVisitor {
  def visit(n: Group): Unit
  def visit(n: Transform): Unit
  def visit(n: Geode): Unit
}