package gie.gl

trait RichContext { this: Context =>

  final class ShaderOps(val shader: Shader) {
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

  @inline final def shaderOps(shader: Shader) = new ShaderOps(shader)

  @inline final def createVertexShader(): Shader =  createShader(const.VERTEX_SHADER)
  @inline final def createFragmentShader(): Shader =  createShader(const.FRAGMENT_SHADER)
  @inline final def compilationStatus(shader: Shader): Boolean = getShaderbv(shader, const.COMPILE_STATUS)

}