package gie.gsg

import gie.gsg.state_attribute.{UniformValueAttributeComponent, StateAttributeComponent, StateAttributeVisitorComponent, ShaderVariableComponent}
import gie.sml.MatrixRead4F
import slogging.LoggerHolder


trait ProgramHolderComponent {
  this: ShaderVariableComponent with RenderContext with StateAttributeComponent with UniformValueAttributeComponent with LoggerHolder =>

  abstract class GlProgramHolder { programHolder=>
    val program: gl.GLProgram
    def applied(): Unit

    def projectionMatrix: MatrixRead4F = ???
    def projectionMatrix_=(m:MatrixRead4F): m.type

    def resolveAttribute(attribute: gl.VertexAttributeApiTrait): attribute.type

    def transformationMatrix: MatrixRead4F = ???
    def transformationMatrix_=(m:MatrixRead4F): m.type
    def vertexCoordinatesAttribute: gl.VertexAttribute
    def vertexTextureCoordinatesAttribute: gl.VertexAttribute
    def vertexColorAttribute: gl.VertexAttribute

    @inline def constUniformValue(ul: gl.UniformTrait)(v: Int): ConstUniformValueAttribute[Int] ={
      new ConstUniformValueAttribute[Int](programHolder, ul, v){
        def apply(from: ShaderVariableAttribute): Unit ={
          gl.uniform(uniformLocation) = m_value
        }
      }
    }


  }


}