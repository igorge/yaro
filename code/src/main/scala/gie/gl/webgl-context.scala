package gie.gl

import org.scalajs.dom
import scalajs.js


object WebGLContext extends Constants {
  final val NO_ERROR = dom.raw.WebGLRenderingContext.NO_ERROR
  final val MAX_VERTEX_ATTRIBS: Int = dom.raw.WebGLRenderingContext.MAX_VERTEX_ATTRIBS
  final val CURRENT_PROGRAM: Int = dom.raw.WebGLRenderingContext.CURRENT_PROGRAM
}

class WebGLContext(val real: dom.raw.WebGLRenderingContext) extends Context {

  final type Shader = dom.raw.WebGLShader
  final type Program = dom.raw.WebGLProgram
  final type Buffer = dom.raw.WebGLBuffer
  final type UniformLocation = dom.raw.WebGLUniformLocation

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
  @inline final def impl_glCreateShader(shaderType: Int): Shader ={
    real.createShader( shaderType )
  }

  @inline final def impl_glDeleteShader(shader: Shader): Unit ={
    real.deleteShader(shader)
  }

  @inline final def impl_glShaderSource(shader: Shader, src: String): Unit ={
    real.shaderSource(shader, src)
  }

  @inline final def impl_glCompileShader(shader: Shader): Unit ={
    real.compileShader(shader)
  }

  @inline final def impl_glGetShaderiv(shader: Shader, pname: Int): Int ={
    real.getShaderParameter(shader, pname).asInstanceOf[Int]
  }

  @inline final def impl_glCreateProgram(): Program ={
    real.createProgram()
  }

  @inline final def impl_glDeleteProgram(program: Program): Unit ={
    real.deleteProgram(program)
  }

  @inline final def impl_glGetProgramiv(program: Program, pname: Int): Int ={
    real.getProgramParameter(program, pname).asInstanceOf[Int]
  }

  @inline final def impl_glAttachShader(program: Program, shader: Shader): Unit ={
    real.attachShader(program, shader)
  }

  @inline final def impl_glBindAttribLocation(program: Program, index: Int, name: String): Unit ={
    real.bindAttribLocation(program, index, name)
  }

  @inline final def impl_glLinkProgram(program: Program): Unit ={
    real.linkProgram(program)
  }

  @inline final def impl_glUseProgram(program: Program): Unit ={
    real.useProgram(program)
  }

  @inline final def impl_glCreateBuffer(): Buffer={
    real.createBuffer()
  }

  @inline final def impl_glDeleteBuffer(buffer: Buffer): Unit={
    real.deleteBuffer(buffer)
  }

  @inline final def impl_glBindBuffer(target: Int, buffer: Buffer): Unit={
    real.bindBuffer(target, buffer)
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

  @inline final def impl_glGetUniformLocation(program: Program, name: String): UniformLocation ={
    real.getUniformLocation(program, name)
  }

  @inline final def impl_glUniform1f(location: UniformLocation, x: Float): Unit={
    real.uniform1f(location, x)
  }

  @inline final def impl_glUniform4fv(location: UniformLocation, v: Array[Float]): Unit={
    real.uniform4fv(location, js.Array[Double](v(0),v(1),v(2),v(3))) // XXX: slow?
  }

  @inline final def impl_glUniform1i(location: UniformLocation, x: Int): Unit={
    real.uniform1i(location, x)
  }

  @inline final def impl_glUniformMatrix4fv(location: UniformLocation, transpose: Boolean, v: Array[Float]): Unit={
    import js.JSConverters._
    real.uniformMatrix4fv(location, transpose, js.Array[Double]( // XXX: slow?
      v(0), v(1), v(2), v(3),
      v(4), v(5), v(6), v(7),
      v(8), v(9), v(10),v(11),
      v(12),v(13),v(14),v(15)
    ))
  }


}