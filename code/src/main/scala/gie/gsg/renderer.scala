package gie.gsg

import gie.gl.{RichContext, RichUniformTrait, ContextUnbind, Context}
import gie.gsg.state_attribute.StateAttributeVisitorComponent
import gie.sml.{Matrix4F, MatrixRead4F}
import slogging.StrictLogging


trait RenderContext {
  this: ProgramHolderComponent  =>

  type GLT <: Context with ContextUnbind with RichContext
  val gl: GLT

  protected var rendererActiveProgram: GlProgramHolder = null
  @inline def program = rendererActiveProgram

}

class Renderer[GLType <: Context with ContextUnbind with RichContext](val gl: GLType)
  extends RenderContext
  with state_attribute.StateAttributeComponent
  with state_attribute.GlProgramAttributeComponent
  with state_attribute.Texture2DComponent
  with state_attribute.UniformValueAttributeComponent
  with state_attribute.ShaderVariableComponent
  with state_attribute.VertexAttributeAttributeComponent
  with state_attribute.IndexBufferAttributeComponent
  with ProgramHolderComponent
  with StateSetComponent
  with NodeComponent
  with GroupComponent
  with TransformComponent
  with GeodeComponent
  with OwnerDrawComponent
  with DrawableComponent with GeometryComponent with DrawableVisitorComponent
  with TrianglesArrayComponent
  with WithStateSetComponent
  with StateAttributeVisitorComponent
  with NodeVisitorComponent
  with BufferComponent
  with StrictLogging
{ renderer =>

  type GLT = GLType

  private def debug_trace[R](name: Symbol)(body: => R):R = {
    logger.debug(s"BEGIN: ${name}")
    var r = body
    logger.debug(s"END: ${name}")
    r
  }

  private def impl_genSelfStateSet(selfSS: StateSet, parentSS: StateSet): StateSet = //debug_trace('impl_genSelfStateSet)
  {
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

  private[gsg] def api_renderTrianglesArray(drawable: TrianglesArray, parentMergedStateSet: StateSet, transformation: MatrixRead4F): Unit ={
    //logger.debug(s"BEGIN: api_renderTrianglesArray(..., ${parentMergedStateSet}, ...)")
    val selfSS = impl_genSelfStateSet(drawable.stateSet, parentMergedStateSet)
    //logger.debug(s"api_renderTrianglesArray: impl_applyStateSet(...)")
    impl_applyStateSet(selfSS)
    rendererActiveProgram.transformationMatrix = transformation

    //logger.debug(s"invoking gl.drawArrays(...)")
    gl.drawArrays(gl.const.TRIANGLES, 0, drawable.verticesCount)

    //logger.debug(s"END: api_renderTrianglesArray(...) exit")
  }

  private[gsg] def api_renderTrianglesIndexArray(drawable: TrianglesIndexedArray, parentMergedStateSet: StateSet, transformation: MatrixRead4F): Unit ={
    //  logger.debug(s"api_renderTrianglesArray(..., ${parentMergedStateSet}, ...)")
    val selfSS = impl_genSelfStateSet(drawable.stateSet, parentMergedStateSet)
    impl_applyStateSet(selfSS)
    rendererActiveProgram.transformationMatrix = transformation

    gl.drawElements(gl.const.TRIANGLES, drawable.verticesCount, gl.const.UNSIGNED_SHORT, 0)
  }

  private object impl_renderNodeVisitor extends NodeVisitor {

    def visit(n: Transform, parentMergedStateSet: StateSet, transformation: MatrixRead4F): Unit={
      val selfTransformation = impl_deduceTransformation(transformation, n.m)
      val selfSS = impl_genSelfStateSet(n.stateSet, parentMergedStateSet)
      n.children.foreach( impl_renderNode(_, selfSS, selfTransformation) )
    }

    def visit(n: Group, parentMergedStateSet: StateSet, transformation: MatrixRead4F): Unit={
      val selfSS = impl_genSelfStateSet(n.stateSet, parentMergedStateSet)
      n.children.foreach( impl_renderNode(_, selfSS, transformation) )
    }

    def visit(n: Geode, parentMergedStateSet: StateSet, transformation: MatrixRead4F): Unit={
      val selfSS = impl_genSelfStateSet(n.stateSet, parentMergedStateSet)
      n.drawables.foreach{ _.draw(selfSS, transformation) }
    }

    def visit(n: OwnerDraw, parentMergedStateSet: StateSet, transformation: MatrixRead4F): Unit={
      val selfSS = impl_genSelfStateSet(n.stateSet, parentMergedStateSet)
      impl_applyStateSet(selfSS)
      rendererActiveProgram.transformationMatrix = transformation
      n.f(n)
    }
  }

  @inline
  private def impl_renderNode(node: Node, parentMergedStateSet: StateSet, transformation: MatrixRead4F): Unit={
    //logger.debug(s"BEGIN: render node ${node}")
    node.accept(impl_renderNodeVisitor, parentMergedStateSet, transformation)
    //logger.debug(s"END: render node ${node}")
  }

  private var m_appliedStateSet = new StateSet()

  private object applyVisitor extends StateAttributeVisitor {
    def visit(attr: GlProgramAttribute): Unit={
      gl.useProgram(attr.m_program)
      attr.applied()
      rendererActiveProgram = attr.programHolder
    }
    def visit(attr: Texture2D): Unit ={
      gl.activateTexture(gl.const.TEXTURE0 + attr.m_textureUnit)
      gl.bindTexture(gl.const.TEXTURE_2D, attr.m_texture)
    }
    def visit(attr: IndexBufferAttribute): Unit={
      gl.bindBuffer(gl.const.ELEMENT_ARRAY_BUFFER, attr.buffer)
    }
  }

  private object unapplyVisitor extends StateAttributeVisitor {
    def visit(attr: GlProgramAttribute): Unit={
      gl.useNullProgram()
      rendererActiveProgram = null
    }
    def visit(attr: Texture2D): Unit ={
      gl.activateTexture(gl.const.TEXTURE0 + attr.m_textureUnit)
      gl.bindNullTexture(gl.const.TEXTURE_2D)
      gl.activateTexture(gl.const.TEXTURE0)
    }
    def visit(attr: IndexBufferAttribute): Unit={
      gl.bindNullBuffer(gl.const.ELEMENT_ARRAY_BUFFER)
    }
  }


  private def impl_applyStateSet(ss: StateSet): Unit={

    assume(ss ne null, "Non null state set is required.")

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
        //logger.debug(s"applying uniform/attribute: ${l}")
        l.apply(r)
        newApplied.variables_! += l
      }
    }
    def outLeft(l: ShaderVariableAttribute): Unit ={
      //logger.debug(s"applying uniform/attribute: ${l}")
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
        //logger.debug(s"applying attribute: ${l}")
        l.accept(applyVisitor)
        newApplied.m_attributes += l
      }
    }
    def outLeft(l: StateAttribute): Unit ={
      //logger.debug(s"applying attribute: ${l}")
      l.accept(applyVisitor)
      newApplied.m_attributes += l
    }
    def outRight(r: StateAttribute): Unit ={
      //logger.debug(s"unapplying attribute: ${r}")
      r.accept(unapplyVisitor)
    }

    gie.sorted_merge.mergedForeachOptSeq(ss.m_attributes, m_appliedStateSet.m_attributes)(cmp)(outEq)(outLeft)(outRight)
  }

  private val m_identity =  Matrix4F.identity()
  def render(node: Node): Unit ={
    //logger.debug("BEGIN: render(...)")
    impl_renderNode(node, null, null)
    //logger.debug("END: render(...)")
  }


}