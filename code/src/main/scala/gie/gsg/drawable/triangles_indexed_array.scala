//package gie.gsg
//
//import gie.gsg.state_attribute.VertexAttributeAttributeComponent
//import gie.sml.MatrixRead4F
//import slogging.LoggerHolder
//
//trait TrianglesIndexedArrayComponent {
//  this: RenderContext with ProgramHolderComponent with state_attribute.GlProgramAttributeComponent with VertexAttributeAttributeComponent with DrawableComponent with GeometryComponent with DrawableVisitorComponent with StateSetComponent with WithStateSetComponent with LoggerHolder=>
//
//  class TrianglesIndexedArray(elementsData: Array[Float], vertexData: Array[Float], texCoordData: Option[Array[Float]] = None, vertexColorData: Option[Array[Float]] = None) extends Geometry with WithStateSetImpl {
//
//    private var init: StateSet=>Unit = doInit _
//
//    val verticesCount = elementsData.size/3
//
//    val m_vertexData = {
//      assume(elementsData.size%3==0)
//      gl.createBuffer(gl.const.ARRAY_BUFFER, vertexData, gl.const.STATIC_DRAW)
//    }
//
//    val m_elementsData = {
//      assume(elementsData.size % 3 == 0)
//      gl.createBuffer(gl.const.ELEMENT_ARRAY_BUFFER, elementsData, gl.const.STATIC_DRAW)
//    }
//
//    val m_texCoordData = texCoordData.map{tx=>
//      assume(tx.size%2==0)
//      assume(tx.size/2==vertexData.size/3)
//      gl.createBuffer(gl.const.ARRAY_BUFFER, tx, gl.const.STATIC_DRAW)
//    }
//
//    val m_vertexColorData = vertexColorData.map{cd=>
//      assume(cd.size%3==0)
//      assume(cd.size/3==vertexData.size/3, s"${cd.size} != ${vertexData.size}")
//      gl.createBuffer(gl.const.ARRAY_BUFFER, cd, gl.const.STATIC_DRAW)
//    }
//
//    private def doInit(parentMergedStateSet: StateSet): Unit ={
//      logger.debug("TrianglesArray.doInit()")
//
//      val program = StateSet.getProgram(this.stateSet, parentMergedStateSet)
//      if (program eq null) throw new Exception("No program specified for drawable")
//
//      this.addVertexAttributeValue(program.vertexCoordinatesAttribute.name, 3, gl.const.FLOAT){m_vertexData}
//
//      m_texCoordData.foreach{ tx=>
//        this.addVertexAttributeValue(program.vertexTextureCoordinatesAttribute.name, 2, gl.const.FLOAT ){tx}
//      }
//
//      m_vertexColorData.foreach{ color=>
//        this.addVertexAttributeValue(program.vertexColorAttribute.name, 3, gl.const.FLOAT){color}
//      }
//
//      init = (_)=>{}
//      logger.debug("TrianglesArray.doInit() exit")
//    }
//
//    private [gsg] def draw(parentMergedStateSet: StateSet, transformation: MatrixRead4F): Unit={
//      //logger.debug("TrianglesArray.draw(...)")
//      init(parentMergedStateSet)
////      api_renderTrianglesArray(this, parentMergedStateSet, transformation)
//      //logger.debug("TrianglesArray.draw(...) exit")
//    }
//
//    private [gsg] def prepareDraw(): Unit={
//    }
//
//
//  }
//
//}