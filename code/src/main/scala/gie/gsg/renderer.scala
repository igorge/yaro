package gie.gsg

import gie.gl.Context
import gie.sml.MatrixRead4F
import slogging.StrictLogging

import scala.collection.mutable.ArrayBuffer


trait RenderContext {
  type GLT <: Context
  val gl: GLT
}

class Renderer[GLType <: Context](val gl: GLType)
  extends RenderContext
  with state_attribute.StateAttributeComponent
  with state_attribute.GlProgramComponent
  with StrictLogging
{ renderer =>

  type GLT = GLType

  object attribute {
    def createProgram(program: gl.GLProgram) = new GlProgram(program)
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