package gie.gl


trait RichContextCommon {
  gl: Context with ContextUnbind =>

  @inline final def createVertexShader(): GLShader =  createShader(const.VERTEX_SHADER)
  @inline final def createFragmentShader(): GLShader =  createShader(const.FRAGMENT_SHADER)
  @inline final def compilationStatus(shader: GLShader): Boolean = getShaderbv(shader, const.COMPILE_STATUS)

  @inline final def get_maxVertexAttribs() = getInteger(const.MAX_VERTEX_ATTRIBS)

  @inline final def createBuffer(target: Int, data: Seq[Float], usage: Int): GLBuffer={
    val buffer = gl.createBuffer()
    gl.bindBuffer(target, buffer)
    gl.bufferData(target, data, usage)
    gl.bindNullBuffer(target)

    buffer
  }

  @inline final def createBuffer(target: Int, data: Array[Float], usage: Int): GLBuffer={
    val buffer = gl.createBuffer()
    gl.bindBuffer(target, buffer)
    gl.bufferData(target, data, usage)
    gl.bindNullBuffer(target)

    buffer
  }

}

trait RichContext
  extends RichUniformTrait
  with    RichVertexAttributeTrait
  with    RichProgramTrait
  with    RichShaderTrait
  with    RichContextCommon {
  this: Context with ContextUnbind =>

  object uniform {
    @inline final def update(location: UniformTrait, v:Int) = uniform1i(location.get, v)
  }


}