package gie.gsg

import gie.gl.{RichContext, RichUniformTrait, ContextUnbind, Context}
import gie.gsg.state_attribute.StateAttributeVisitorComponent
import gie.sml.{Matrix4F, MatrixRead4F}
import slogging.StrictLogging

import scala.collection.mutable.ArrayBuffer


trait RenderContext {
  type GLT <: Context with ContextUnbind with RichContext
  val gl: GLT
}

class Renderer[GLType <: Context with ContextUnbind with RichContext](val gl: GLType)
  extends RenderContext
  with state_attribute.StateAttributeComponent
  with state_attribute.GlProgramComponent
  with state_attribute.Texture2DComponent
  with state_attribute.UniformValueAttributeComponent
  with state_attribute.ShaderVariableComponent
  with state_attribute.VertexAttributeAttributeComponent
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
    def visit(attr: Texture2D): Unit ={
      gl.activateTexture(gl.const.TEXTURE0 + attr.m_textureUnit)
      gl.bindTexture(gl.const.TEXTURE_2D, attr.m_texture)
    }
  }

  private object unapplyVisitor extends StateAttributeVisitor {
    def visit(attr: GlProgramAttribute): Unit={
      gl.useNullProgram()
      m_activeProgram = null
    }
    def visit(attr: Texture2D): Unit ={
      gl.activateTexture(gl.const.TEXTURE0 + attr.m_textureUnit)
      gl.bindNullTexture(gl.const.TEXTURE_2D)
    }
  }


  private def impl_applyStateSet(ss: StateSet): Unit={

    val newApplied = new StateSet()

    impl_applyStateSet_attributes(ss, newApplied)
    impl_applyStateSet_uniforms(ss, newApplied)

    m_appliedStateSet = newApplied
  }


  private def impl_applyStateSet_uniforms(ss: StateSet, newApplied: StateSet): Unit={

    def outEq(l: ShaderVariableAttribute, r: ShaderVariableAttribute): Unit ={
      if (l === r){
        //logger.debug(s"not re-applying attribute: ${l}")
        newApplied.variables_! += r
      } else {
        logger.debug(s"applying uniform/attribute: ${l}")
        l.apply(r)
        newApplied.variables_! += l
      }
    }
    def outLeft(l: ShaderVariableAttribute): Unit ={
      logger.debug(s"applying uniform/attribute: ${l}")
      l.apply(null)
      newApplied.variables_! += l
    }
    def outRight(r: ShaderVariableAttribute): Unit ={
      logger.debug(s"unapplying uniform/attribute: ${r}")
      r.unapply()
    }

    gie.sorted_merge.mergedForeachOptSeq(ss.variables, m_appliedStateSet.variables)(ShaderVariableAttribute.orderingCmp)(outEq)(outLeft)(outRight)

  }

  private def impl_applyStateSet_attributes(ss: StateSet, newApplied: StateSet): Unit={

    def cmp(l: StateAttribute, r: StateAttribute) = implicitly[Ordering[Int]].compare(l.index, r.index)
    def outEq(l: StateAttribute, r: StateAttribute): Unit ={
      if (l === r){
        //logger.debug(s"not re-applying attribute: ${l}")
        newApplied.m_attributes += r
      } else {
        logger.debug(s"applying attribute: ${l}")
        l.accept(applyVisitor)
        newApplied.m_attributes += l
      }
    }
    def outLeft(l: StateAttribute): Unit ={
      logger.debug(s"applying attribute: ${l}")
      l.accept(applyVisitor)
      newApplied.m_attributes += l
    }
    def outRight(r: StateAttribute): Unit ={
      logger.debug(s"unapplying attribute: ${r}")
      r.accept(unapplyVisitor)
    }

    gie.sorted_merge.mergedForeachOptSeq(ss.m_attributes, m_appliedStateSet.m_attributes)(cmp)(outEq)(outLeft)(outRight)
  }

  private val m_identity =  Matrix4F.identity()
  def render(node: Node): Unit ={
    impl_renderNode(node, null, null)
  }


}