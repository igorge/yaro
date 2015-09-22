package gie.gsg.state_attribute

import gie.gl.Context
import gie.gsg.{RenderContext}
import gie.sml.MatrixRead4F
import slogging.LoggerHolder

trait GlProgramAttributeComponent {
  this: ShaderVariableComponent with StateAttributeVisitorComponent with RenderContext with StateAttributeComponent with UniformValueAttributeComponent with LoggerHolder =>


  abstract class GlProgramHolder { programHolder=>
    val program: gl.GLProgram
    def applied(): Unit

    def projectionMatrix: MatrixRead4F = ???
    def projectionMatrix_=(m:MatrixRead4F): m.type

    def resolveAttribute(attribute: gl.VertexAttributeApiTrait): attribute.type

    def transformationMatrix: MatrixRead4F = ???
    def transformationMatrix_=(m:MatrixRead4F): m.type
    def vertexCoordinatesAttribute: gl.VertexAttribute = ???
    def vertexTextureCoordinatesAttribute: gl.VertexAttribute = ???
    def vertexColorAttribute: gl.VertexAttribute = ???

    @inline def constUniformValue(ul: gl.UniformTrait)(v: Int): ConstUniformValueAttribute[Int] ={
      new ConstUniformValueAttribute[Int](programHolder, ul, v){
        def apply(from: ShaderVariableAttribute): Unit ={
          gl.uniform(uniformLocation) = m_value
        }
      }
    }


  }

  class GlProgramAttribute(programHolderCtor: => GlProgramHolder) extends StateAttribute {

    lazy val programHolder: GlProgramHolder = programHolderCtor

    private[gsg] def m_program = programHolder.program

    final val index = 0

    def ===(y: StateAttribute): Boolean = if (index != y.index) false else { m_program == y.asInstanceOf[GlProgramAttribute].m_program }

    def accept(visitor: StateAttributeVisitor): Unit= visitor.visit(this)

    def applied(): Unit={
      programHolder.applied()
    }

  }

}

