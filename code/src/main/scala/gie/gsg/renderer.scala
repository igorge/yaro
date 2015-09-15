package gie.gsg

import gie.gl.{ContextUnbind, Context}
import gie.sml.MatrixRead4F
import slogging.StrictLogging

import scala.collection.mutable.ArrayBuffer


trait RenderContext {
  type GLT <: Context with ContextUnbind
  val gl: GLT
}

class Renderer[GLType <: Context with ContextUnbind](val gl: GLType)
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

  private def impl_genSelfStateSet(selfSS: StateSet, parentSS: StateSet): StateSet ={
    if(selfSS eq null) {
      parentSS
    } else {
      if(parentSS eq null) selfSS else {
        selfSS.mergeCopyWithParent(parentSS)
      }
    }
  }

  private def impl_renderNode(node: Node, parentMergedStateSet: StateSet): Unit= node match {
    case n: Transform => ???

    case n: Group =>
      val selfSS = impl_genSelfStateSet(n.stateSet, parentMergedStateSet)
      n.children.foreach( impl_renderNode(_, selfSS) )

    case n: Geode => ???

    case n: OwnerDraw=>
      val selfSS = impl_genSelfStateSet(n.stateSet, parentMergedStateSet)
      impl_applyStateSet(selfSS)
      n.f(n)

  }

  private var m_appliedStateSet = new StateSet()

  private def impl_applyStateSet(ss: StateSet): Unit={

    val newApplied = new StateSet()

    def cmp(l: StateAttribute, r: StateAttribute) = implicitly[Ordering[Int]].compare(l.index, r.index)
    def outEq(l: StateAttribute, r: StateAttribute): Unit ={
      if (l === r){
        //logger.debug(s"not re-applying attribute: ${l}")
        newApplied += r
      } else {
        l.apply()
        newApplied += l
      }
    }
    def outLeft(l: StateAttribute): Unit ={
      logger.debug(s"applying attribute: ${l}")
      l.apply()
      newApplied += l
    }
    def outRight(r: StateAttribute): Unit ={
      logger.debug(s"unapplying attribute: ${r}")
      r.unapply()
    }

    gie.sorted_merge.mergedForeachOptSeq(ss, m_appliedStateSet)(cmp)(outEq)(outLeft)(outRight)

    m_appliedStateSet = newApplied
  }

  def render(node: Node): Unit ={
    impl_renderNode(node, null)
  }


}