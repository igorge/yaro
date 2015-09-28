package gie.gsg

import gie.gsg.state_attribute.{IndexBufferAttributeComponent, VertexAttributeAttributeComponent}
import gie.sml.MatrixRead4F
import slogging.LoggerHolder

trait TrianglesArrayComponent {
  this: RenderContext with BufferComponent with IndexBufferAttributeComponent with ProgramHolderComponent with state_attribute.GlProgramAttributeComponent with VertexAttributeAttributeComponent with DrawableComponent with GeometryComponent with DrawableVisitorComponent with StateSetComponent with WithStateSetComponent with LoggerHolder=>

  abstract class TrianglesArrayAbstract(vertexData: GLBufferLike, texCoordData: Option[GLBufferLike] = None, vertexColorData: Option[GLBufferLike] = None) extends Geometry with WithStateSetImpl {
    protected var init: StateSet=>Unit = doInit _
    protected def doInit(parentMergedStateSet: StateSet): Unit
    val verticesCount:Int

    val m_vertexData = {
      assume(vertexData.size % 3 == 0)
      assume(vertexData.bufferTarget == gl.const.ARRAY_BUFFER)
      vertexData.glBuffer
    }

    val m_texCoordData = texCoordData.map{tx=>
      assume(tx.size%2==0)
      assume(tx.size/2==vertexData.size/3)
      tx.glBuffer
    }

    val m_vertexColorData = vertexColorData.map{cd=>
      assume(cd.size%3==0)
      assume(cd.size/3==vertexData.size/3, s"${cd.size} != ${vertexData.size}")
      cd.glBuffer
    }

  }

  class TrianglesArray(vertexData: GLBufferLike, texCoordData: Option[GLBufferLike] = None, vertexColorData: Option[GLBufferLike] = None) extends TrianglesArrayAbstract(vertexData, texCoordData, vertexColorData) {

    val verticesCount = vertexData.size/3

    def doInit(parentMergedStateSet: StateSet): Unit ={
      logger.debug("TrianglesArray.doInit()")

      val program = StateSet.getProgram(this.stateSet, parentMergedStateSet)
      if (program eq null) throw new Exception("No program specified for drawable")

      this.addVertexAttributeValue(program.vertexCoordinatesAttribute.name, 3, gl.const.FLOAT){m_vertexData}

      m_texCoordData.foreach{ tx=>
        this.addVertexAttributeValue(program.vertexTextureCoordinatesAttribute.name, 2, gl.const.FLOAT ){tx}
      }

      m_vertexColorData.foreach{ color=>
        this.addVertexAttributeValue(program.vertexColorAttribute.name, 3, gl.const.FLOAT){color}
      }

      init = (_)=>{}
      logger.debug("TrianglesArray.doInit() exit")
    }

    private [gsg] def draw(parentMergedStateSet: StateSet, transformation: MatrixRead4F): Unit={
      //logger.debug("TrianglesArray.draw(...)")
      init(parentMergedStateSet)
      api_renderTrianglesArray(this, parentMergedStateSet, transformation)
      //logger.debug("TrianglesArray.draw(...) exit")
    }



  }

  class TrianglesIndexedArray(elementsData: GLBufferLike, vertexData: GLBufferLike, texCoordData: Option[GLBufferLike] = None, vertexColorData: Option[GLBufferLike] = None) extends TrianglesArrayAbstract(vertexData, texCoordData, vertexColorData) {

    val verticesCount = elementsData.size

    assume(elementsData.bufferTarget==gl.const.ELEMENT_ARRAY_BUFFER)
    assume(elementsData.size % 3 == 0)
    assume(elementsData.componentType == gl.const.UNSIGNED_SHORT)

    def doInit(parentMergedStateSet: StateSet): Unit = {
      logger.debug("TrianglesIndexArray.doInit()")

      val program = StateSet.getProgram(this.stateSet, parentMergedStateSet)
      if (program eq null) throw new Exception("No program specified for drawable")

      this.addAttribute( new IndexBufferAttribute(elementsData.glBuffer) )

      this.addVertexAttributeValue(program.vertexCoordinatesAttribute.name, 3, gl.const.FLOAT) {
        m_vertexData
      }

      m_texCoordData.foreach { tx =>
        this.addVertexAttributeValue(program.vertexTextureCoordinatesAttribute.name, 2, gl.const.FLOAT) {
          tx
        }
      }

      m_vertexColorData.foreach { color =>
        this.addVertexAttributeValue(program.vertexColorAttribute.name, 3, gl.const.FLOAT) {
          color
        }
      }

      init = (_) => {}
      logger.debug("TrianglesIndexArray.doInit() exit")
    }

    private[gsg] def draw(parentMergedStateSet: StateSet, transformation: MatrixRead4F): Unit = {
      //logger.debug("TrianglesArray.draw(...)")
      init(parentMergedStateSet)
      api_renderTrianglesIndexArray(this, parentMergedStateSet, transformation)
      //logger.debug("TrianglesArray.draw(...) exit")
    }


  }


  }