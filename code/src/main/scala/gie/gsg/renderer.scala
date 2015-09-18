package gie.gsg

import gie.gl.{ContextUnbind, Context}
import gie.gsg.state_attribute.StateAttributeVisitorComponent
import gie.sml.{Matrix4F, MatrixRead4F}
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
  with StateAttributeVisitorComponent
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


  @inline
  private
  def impl_deduceTransformation(parent:MatrixRead4F, child:MatrixRead4F) = {
    import gie.sml.ImplicitOps._

    if(parent eq null) {
      if(child eq null) m_identity else child
    } else {
      if(child eq null) parent else { child * parent }
    }
  }

  private def impl_renderNode(node: Node, parentMergedStateSet: StateSet, transformation: MatrixRead4F): Unit= node match {
    case n: Transform =>
      val selfTransformation = impl_deduceTransformation(transformation, n.m)
      val selfSS = impl_genSelfStateSet(n.stateSet, parentMergedStateSet)
      n.children.foreach( impl_renderNode(_, selfSS, selfTransformation) )

    case n: Group =>
      val selfSS = impl_genSelfStateSet(n.stateSet, parentMergedStateSet)
      n.children.foreach( impl_renderNode(_, selfSS, transformation) )

    case n: Geode => ???

    case n: OwnerDraw=>
      val selfSS = impl_genSelfStateSet(n.stateSet, parentMergedStateSet)
      impl_applyStateSet(selfSS)
      m_activeProgram.transformationMatrix = transformation
      n.f(n)

  }

  private var m_appliedStateSet = new StateSet()
  private var m_activeProgram: GlProgramHolder = null
  @inline def program = m_activeProgram

  private object applyVisitor extends StateAttributeVisitor {
    def visit(attr: GlProgramAttribute): Unit={
      gl.useProgram(attr.m_program)
      attr.applied()
      m_activeProgram = attr.programHolder
    }
  }

  private object unapplyVisitor extends StateAttributeVisitor {
    def visit(attr: GlProgramAttribute): Unit={
      gl.useNullProgram()
      m_activeProgram = null
    }
  }


  private def impl_applyStateSet(ss: StateSet): Unit={

//    logger.debug("impl_applyStateSet >>>")

    val newApplied = new StateSet()

    def cmp(l: StateAttribute, r: StateAttribute) = implicitly[Ordering[Int]].compare(l.index, r.index)
    def outEq(l: StateAttribute, r: StateAttribute): Unit ={
      if (l === r){
        //logger.debug(s"not re-applying attribute: ${l}")
        newApplied += r
      } else {
        l.accept(applyVisitor)
        newApplied += l
      }
    }
    def outLeft(l: StateAttribute): Unit ={
      logger.debug(s"applying attribute: ${l}")
      l.accept(applyVisitor)
      newApplied += l
    }
    def outRight(r: StateAttribute): Unit ={
      logger.debug(s"unapplying attribute: ${r}")
      r.accept(unapplyVisitor)
    }

    gie.sorted_merge.mergedForeachOptSeq(ss, m_appliedStateSet)(cmp)(outEq)(outLeft)(outRight)

    m_appliedStateSet = newApplied
  }

  private val m_identity =  Matrix4F.identity()
  def render(node: Node): Unit ={
    impl_renderNode(node, null, null)
  }


}