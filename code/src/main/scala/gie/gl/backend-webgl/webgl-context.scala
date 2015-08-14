package gie.gl

import org.scalajs.dom
import scala.scalajs.js.typedarray.Float32Array
import scalajs.js


object WebGLContext extends Constants {
  final val NO_ERROR = dom.raw.WebGLRenderingContext.NO_ERROR
  final val TRIANGLES: Int = dom.raw.WebGLRenderingContext.TRIANGLES
  final val MAX_VERTEX_ATTRIBS: Int = dom.raw.WebGLRenderingContext.MAX_VERTEX_ATTRIBS
  final val CURRENT_PROGRAM: Int = dom.raw.WebGLRenderingContext.CURRENT_PROGRAM
  final val VERTEX_SHADER: Int = dom.raw.WebGLRenderingContext.VERTEX_SHADER
  final val FRAGMENT_SHADER: Int = dom.raw.WebGLRenderingContext.FRAGMENT_SHADER
  final val SHADER_TYPE: Int = dom.raw.WebGLRenderingContext.SHADER_TYPE
  final val DELETE_STATUS: Int = dom.raw.WebGLRenderingContext.DELETE_STATUS
  final val COMPILE_STATUS: Int = dom.raw.WebGLRenderingContext.COMPILE_STATUS
  final val LINK_STATUS: Int = dom.raw.WebGLRenderingContext.LINK_STATUS
  final val TEXTURE_2D: Int = dom.raw.WebGLRenderingContext.TEXTURE_2D
  final val TEXTURE_CUBE_MAP: Int = dom.raw.WebGLRenderingContext.TEXTURE_CUBE_MAP
  final val TEXTURE0: Int = dom.raw.WebGLRenderingContext.TEXTURE0
  final val TEXTURE1: Int = dom.raw.WebGLRenderingContext.TEXTURE1
  final val TEXTURE2: Int = dom.raw.WebGLRenderingContext.TEXTURE2
  final val TEXTURE3: Int = dom.raw.WebGLRenderingContext.TEXTURE3
  final val TEXTURE4: Int = dom.raw.WebGLRenderingContext.TEXTURE4
  final val TEXTURE5: Int = dom.raw.WebGLRenderingContext.TEXTURE5
  final val TEXTURE6: Int = dom.raw.WebGLRenderingContext.TEXTURE6
  final val TEXTURE7: Int = dom.raw.WebGLRenderingContext.TEXTURE7
  final val ARRAY_BUFFER: Int = dom.raw.WebGLRenderingContext.ARRAY_BUFFER
  final val ELEMENT_ARRAY_BUFFER: Int = dom.raw.WebGLRenderingContext.ELEMENT_ARRAY_BUFFER
  final val STATIC_DRAW: Int = dom.raw.WebGLRenderingContext.STATIC_DRAW
  final val DYNAMIC_DRAW: Int = dom.raw.WebGLRenderingContext.DYNAMIC_DRAW
  final val STREAM_DRAW: Int = dom.raw.WebGLRenderingContext.STREAM_DRAW
  final val FLOAT: Int = dom.raw.WebGLRenderingContext.FLOAT

}

class WebGLContext(val real: dom.raw.WebGLRenderingContext) extends Context {

  final type GLShader = dom.raw.WebGLShader
  final type GLProgram = dom.raw.WebGLProgram
  final type GLBuffer = dom.raw.WebGLBuffer
  final type GLUniformLocation = dom.raw.WebGLUniformLocation
  final type GLTexture = dom.raw.WebGLTexture

  @inline final def uniformLocation_null: GLUniformLocation = null
  @inline final def uniformLocation_null_?(x: GLUniformLocation): Boolean = x eq null

  @inline final def program_null: GLProgram = null
  @inline final def program_null_?(x: GLProgram): Boolean = x eq null



  // ctor
  //
  if(real eq null) throw new ContextInitializationException("dom.raw.WebGLRenderingContext eq null")
  //
  //

  final val const = WebGLContext

  def checkGlError(): Unit = {
    val code = real.getError()
    if ( code != const.NO_ERROR ) throw new GlGetErrorException(code)
  }


  @inline final def currentProgram(): GLProgram = real.getParameter(const.CURRENT_PROGRAM).asInstanceOf[GLProgram]

  @inline final def impl_glClear(mask: Int): Unit = {
    real.clear(mask)
  }
  @inline final def impl_glClearColor(red: Float, green: Float, blue: Float, alpha: Float): Unit = {
    real.clearColor(red, green, blue, alpha)
  }
  @inline final def  impl_glViewport(x: Int, y: Int, width: Int, height: Int): Unit ={
    real.viewport(x, y, width, height)
  }
  @inline final def impl_glEnable(cap: Int): Unit ={
    real.enable(cap)
  }
  @inline final def impl_glDisable(cap: Int): Unit ={
    real.disable(cap)
  }
  @inline final def impl_glBlendFunc(sfactor: Int, dfactor: Int): Unit ={
    real.blendFunc(sfactor, dfactor)
  }
  @inline final def impl_glGetIntegerv(pname: Int): Int ={
    real.getParameter(pname).asInstanceOf[Int]
  }
  @inline final def impl_glCreateShader(shaderType: Int): GLShader ={
    real.createShader( shaderType )
  }

  @inline final def impl_glDeleteShader(shader: GLShader): Unit ={
    real.deleteShader(shader)
  }

  @inline final def impl_glShaderSource(shader: GLShader, src: String): Unit ={
    real.shaderSource(shader, src)
  }

  @inline final def impl_glCompileShader(shader: GLShader): Unit ={
    real.compileShader(shader)
  }

  @inline final def impl_glGetShaderiv(shader: GLShader, pname: Int): Int ={
    real.getShaderParameter(shader, pname).asInstanceOf[Int]
  }

  @inline final def impl_glGetShaderbv(shader: GLShader, pname: Int): Boolean ={
    real.getShaderParameter(shader, pname).asInstanceOf[Boolean]
  }

  @inline final def impl_getShaderInfoLog(shader: GLShader): String ={
    real.getShaderInfoLog(shader)
  }

  @inline final def impl_glCreateProgram(): GLProgram ={
    real.createProgram()
  }

  @inline final def impl_glDeleteProgram(program: GLProgram): Unit ={
    real.deleteProgram(program)
  }

  @inline final def impl_getProgramInfoLog(program: GLProgram): String={
    real.getProgramInfoLog(program)
  }

  @inline final def impl_glGetProgramiv(program: GLProgram, pname: Int): Int ={
    real.getProgramParameter(program, pname).asInstanceOf[Int]
  }

  @inline final def impl_glGetProgrambv(program: GLProgram, pname: Int): Boolean ={
    real.getProgramParameter(program, pname).asInstanceOf[Boolean]
  }

  @inline final def impl_glAttachShader(program: GLProgram, shader: GLShader): Unit ={
    real.attachShader(program, shader)
  }

  @inline final def impl_glBindAttribLocation(program: GLProgram, index: Int, name: String): Unit ={
    real.bindAttribLocation(program, index, name)
  }

  @inline final def impl_glLinkProgram(program: GLProgram): Unit ={
    real.linkProgram(program)
  }

  @inline final def impl_glUseProgram(program: GLProgram): Unit ={
    real.useProgram(program)
  }

  @inline final def impl_glCreateBuffer(): GLBuffer={
    real.createBuffer()
  }

  @inline final def impl_glDeleteBuffer(buffer: GLBuffer): Unit={
    real.deleteBuffer(buffer)
  }

  @inline final def impl_glBindBuffer(target: Int, buffer: GLBuffer): Unit={
    real.bindBuffer(target, buffer)
  }


  @inline final def impl_glBufferData(target: Int, data: Array[Float], usage: Int): Unit={
    import js.JSConverters._
    real.bufferData(target, new Float32Array(data.toJSArray), usage)  // XXX: slow?
  }

  @inline final def impl_glVertexAttribPointer(indx: Int, size: Int, componentType: Int, normalized: Boolean, stride: Int, offset: Int): Unit={
    real.vertexAttribPointer(indx, size, componentType, normalized, stride, offset)
  }

  @inline final def impl_glEnableVertexAttribArray(index: Int): Unit ={
    real.enableVertexAttribArray(index)
  }

  @inline final def impl_glDisableVertexAttribArray(index: Int): Unit={
    real.disableVertexAttribArray(index)
  }

  @inline final def impl_glGetUniformLocation(program: GLProgram, name: String): GLUniformLocation ={
    real.getUniformLocation(program, name)
  }

  @inline final def impl_glUniform1f(location: GLUniformLocation, x: Float): Unit={
    real.uniform1f(location, x)
  }

  @inline final def impl_glUniform4fv(location: GLUniformLocation, v: Array[Float]): Unit={
    real.uniform4fv(location, js.Array[Double](v(0),v(1),v(2),v(3))) // XXX: slow?
  }

  @inline final def impl_glUniform1i(location: GLUniformLocation, x: Int): Unit={
    real.uniform1i(location, x)
  }

  @inline final def impl_glUniformMatrix4fv(location: GLUniformLocation, transpose: Boolean, v: Array[Float]): Unit={
    import js.JSConverters._
    real.uniformMatrix4fv(location, transpose, js.Array[Double]( // XXX: slow?
      v(0), v(1), v(2), v(3),
      v(4), v(5), v(6), v(7),
      v(8), v(9), v(10),v(11),
      v(12),v(13),v(14),v(15)
    ))
  }

  @inline final def impl_glDrawArrays(mode: Int, first: Int, count: Int): Unit = {
    real.drawArrays(mode, first, count)
  }

  @inline final def impl_glGenTexture(): GLTexture={
    real.createTexture()
  }

  @inline final def impl_glDeleteTextures(texture: GLTexture): Unit={
    real.deleteTexture(texture)
  }

  @inline final def impl_glIsTexture(texture: GLTexture): Boolean ={
    real.isTexture()
  }

  @inline final def impl_glActiveTexture(texture: Int): Unit={
    real.activeTexture(texture)
  }

  @inline final def impl_glBindTexture(target: Int, texture: GLTexture): Unit={
    real.bindTexture(target, texture)
  }

  @inline final def impl_glTexParameterf(target: Int, pname: Int, param: Float): Unit={
    real.texParameterf(target, pname, param.asInstanceOf[Int])  // XXX:
  }

  @inline final def impl_glTexParameteri(target: Int, pname: Int, param: Int): Unit={
    real.texParameteri(target, pname, param)
  }

}