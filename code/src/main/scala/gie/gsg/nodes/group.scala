package gie.gsg


import gie.sml.MatrixRead4F

import scala.collection.generic
import scala.collection.mutable.ArrayBuffer


trait GroupComponent {
  this: NodeComponent with NodeVisitorComponent with StateSetComponent with WithStateSetComponent =>

  class Group extends Node with WithStateSetImpl with generic.Growable[Node] with collection.IndexedSeq[Node]{
    val children = new ArrayBuffer[Node]()

    @inline def clear() = children.clear()
    @inline def +=(elem: Node) ={
      children += elem
      this
    }
    @inline def apply(idx: Int) = children.apply(idx)
    @inline def length = children.length

    def accept(visitor: NodeVisitor, parentMergedStateSet: StateSet, transformation: MatrixRead4F): Unit = {
      visitor.visit(this, parentMergedStateSet, transformation)
    }

  }

}