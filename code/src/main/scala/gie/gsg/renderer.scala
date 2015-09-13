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
  with StateSetComponent
  with NodeComponent
  with GroupComponent
  with TransformComponent
  with GeodeComponent
  with OwnerDrawComponent
  with DrawableComponent
  with WithStateSetComponent
  with NodeVisitorComponent
  with StrictLogging
{ renderer =>

  type GLT = GLType

  object attribute {
    def createProgram(program: gl.GLProgram) = new GlProgram(program)
  }

  private def impl_genSelfStateSet(selfSS: StateSet, parentSS: StateSet): StateSet ={
    if(selfSS eq null) {
      parentSS
    } else {
      if(parentSS eq null) selfSS else {
        selfSS.mergeCopyWithParent(parentSS)
      }
    }
  }

  private def impl_renderNode(node: Node, mergedStateSet: StateSet): Unit= node match {
    case n: Transform => ???

    case n: Group =>
      val selfSS = impl_genSelfStateSet(n.stateSet, mergedStateSet)
      n.children.foreach( impl_renderNode(_, selfSS) )

    case n: Geode => ???

    case n: OwnerDraw=>
      val selfSS = impl_genSelfStateSet(n.stateSet, mergedStateSet)
      impl_applyStateSet(selfSS)
      n.f(n)

  }

  private val m_appliedStateSet = new StateSet()

  private def impl_applyStateSet(ss: StateSet): Unit ={
    ???
  }

  def render(node: Node): Unit ={
    impl_renderNode(node, null)
  }


}