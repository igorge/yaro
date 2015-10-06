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
    def resolveUniform(uniform: gl.UniformTrait): uniform.type = {
      if(uniform.isDefined) throw new Exception(s"uniform '${uniform.name}' is already defined")
      uniform.location = gl.getUniformLocation(program, uniform.name)
      uniform
    }

    def transformationMatrix: MatrixRead4F = ???
    def transformationMatrix_=(m:MatrixRead4F): m.type
    def vertexCoordinatesAttribute: gl.VertexAttribute
    def vertexTextureCoordinatesAttribute: gl.VertexAttribute
    def vertexColorAttribute: gl.VertexAttribute
  }


}