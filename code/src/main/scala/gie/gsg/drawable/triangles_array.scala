package gie.gsg

import gie.gsg.state_attribute.VertexAttributeAttributeComponent
import gie.sml.MatrixRead4F

trait TrianglesArrayComponent {
  this: RenderContext with state_attribute.GlProgramAttributeComponent with VertexAttributeAttributeComponent with DrawableComponent with GeometryComponent with DrawableVisitorComponent with StateSetComponent with WithStateSetComponent =>

  class TrianglesArray(vertexData: Array[Float], texCoordData: Option[Array[Float]] = None, vertexColorData: Option[Array[Float]] = None) extends Geometry with WithStateSetImpl {

    val m_vertexData = {
      assume(vertexData.size%3 == 0)
      gl.createBuffer(gl.const.ARRAY_BUFFER, vertexData, gl.const.STATIC_DRAW)
    }

    private [gsg] def draw(parentMergedStateSet: StateSet, transformation: MatrixRead4F): Unit={
      api_renderTrianglesArray(this, parentMergedStateSet, transformation)
    }


  }

}