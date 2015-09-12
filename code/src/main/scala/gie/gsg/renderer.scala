package gie.gsg

import gie.gl.Context
import gie.sml.MatrixRead4F
import slogging.StrictLogging

import scala.collection.mutable.ArrayBuffer


trait RenderContext {
  type GLT <: Context
}

class Renderer[GLType <: Context](val gl: GLType) extends RenderContext with StrictLogging { renderer =>

  type GLT = GLType

  object attribute {
    def createProgram(program: gl.GLProgram) = new state_attribute.GlProgram[Renderer[GLType]](program)
  }

  object ApplyAttributeVisitor extends AttributeVisitor[renderer.type] {
    def visit(n: state_attribute.GlProgram[renderer.type]): Unit = {

    }
  }

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