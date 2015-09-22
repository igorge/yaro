package gie.gl

import slogging.LoggerHolder


trait ContextUnbind {
  gl: Context =>

  @inline def bindNullBuffer(target: Int): Unit={
    gl.bindBuffer(target, gl.buffer_null)
  }

  @inline def bindNullTexture(target: Int): Unit={
    gl.bindTexture(target, gl.texture_null)
  }

  @inline def useNullProgram(): Unit ={
    gl.useProgram(gl.program_null)
  }

  @inline final def withBoundTexture[T](target: Int, texture: GLTexture)(fun: GLTexture=>T): T = {
    gl.bindTexture(target, texture)
    val r = fun(texture)
    bindNullTexture(target)
    r
  }

}

trait RichContextCommon extends ContextUnbind {
  gl: Context =>

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
  with    RichVertexAttributeComponent
  with    RichProgramTrait
  with    RichShaderTrait
  with    RichContextCommon
  with    LoggerHolder {
  this: Context =>

  object uniform {
    @inline final def update(location: UniformTrait, v:Int) = uniform1i(location.get, v)
  }


}