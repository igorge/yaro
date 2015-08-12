package gie.gl

trait RichShaderTrait {
  this: Context with RichContextCommon =>

  trait ShaderApiTrait {

    def get:GLShader

    def source(s: String) = {
      shaderSource(this.get, s)
      this
    }
    def compile() = {
      compileShader(this.get)
      if( !compilationStatus(this.get) ){
        throw new ShaderCompilationException(s"${getShaderInfoLog(this.get)}")
      }
      this
    }

    def free(): Unit ={
      deleteShader(this.get)
    }

  }


  @inline final def shaderOps(shader: GLShader): ShaderApiTrait = new Object with ShaderApiTrait {
    def get = shader
  }


}