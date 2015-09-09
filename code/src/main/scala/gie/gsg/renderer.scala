package gie.gsg

import gie.sml.MatrixRead4F

import scala.collection.mutable.ArrayBuffer


class Renderer {

  def render(node: Node): Unit ={
    val stateSetStack = new ArrayBuffer[StateSet]()
    val transformationStack = new ArrayBuffer[MatrixRead4F]()

    @inline def beginStateStack(f: =>Unit): Unit={
      val oldSize = stateSetStack.size
      try{ val r = f } finally { stateSetStack.reduceToSize(oldSize) }
    }

    @inline def pushStates(n: WithStateSet): Unit = if((n.stateSet ne null) && (n.stateSet.size!=0)) {
      stateSetStack += n.stateSet
    }

    object renderVisitor extends NodeVisitor {
      def visit(n: Geode): Unit = beginStateStack{
        pushStates(n)
      }

      def visit(n: Group): Unit = beginStateStack{

      }
      def visit(n: Transform): Unit  = beginStateStack{

      }
    }

    node.accept(renderVisitor)
  }


}