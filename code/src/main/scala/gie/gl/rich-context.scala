package gie.gl

trait RichContext
  extends RichUniformTrait
  with    RichVertexAttributeTrait
  with    RichProgramTrait
{ this: Context =>

  final class ShaderOps(val shader: GLShader) {
    def get = shader

    def source(s: String) = {
      shaderSource(shader, s)
      this
    }
    def compile() = {
      compileShader(shader)
      if( !compilationStatus(shader) ){
        throw new ShaderCompilationException(s"${getShaderInfoLog(shader)}")
      }
      this
    }

    def free(): Unit ={
      deleteShader(shader)
    }

  }

  final class ProgramOps(val program: GLProgram) {
    def get = program

    def attach(shader: GLShader): this.type = {
      attachShader(program, shader)
      this
    }

    def use():this.type ={
      useProgram(program)
      this
    }

    def free(): Unit ={
      deleteProgram(program)
    }
  }

  @inline final def shaderOps(shader: GLShader) = new ShaderOps(shader)

  @inline final def createVertexShader(): GLShader =  createShader(const.VERTEX_SHADER)
  @inline final def createFragmentShader(): GLShader =  createShader(const.FRAGMENT_SHADER)
  @inline final def compilationStatus(shader: GLShader): Boolean = getShaderbv(shader, const.COMPILE_STATUS)

}