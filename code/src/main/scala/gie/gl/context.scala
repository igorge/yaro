package gie.gl

trait Constants {
  val NO_ERROR: Int
  val MAX_VERTEX_ATTRIBS: Int
  val CURRENT_PROGRAM: Int
  val VERTEX_SHADER: Int
  val FRAGMENT_SHADER: Int
  val SHADER_TYPE: Int
  val DELETE_STATUS: Int
  val COMPILE_STATUS: Int
  val LINK_STATUS: Int
}

trait Context {

  type GLShader
  type GLProgram
  type GLBuffer
  type GLUniformLocation
  type GLVertexAttributeLocation = Int

  def uniformLocation_null: GLUniformLocation
  def uniformLocation_null_?(x: GLUniformLocation): Boolean

  def program_null: GLProgram
  def program_null_?(x: GLProgram): Boolean

  @inline final def vertexAttributeLocation_null: GLVertexAttributeLocation = -1
  @inline final def vertexAttributeLocation_null_?(x: GLVertexAttributeLocation): Boolean = x == -1
  
  def checkGlError(): Unit
  val const:Constants

  def impl_glClear(mask: Int): Unit
  def impl_glClearColor(red: Float, green: Float, blue: Float, alpha: Float): Unit
  def impl_glViewport(x: Int, y: Int, width: Int, height: Int): Unit
  def impl_glEnable(cap: Int): Unit
  def impl_glDisable(cap: Int): Unit
  def impl_glBlendFunc(sfactor: Int, dfactor: Int): Unit
  def impl_glGetIntegerv(pname: Int): Int
  def impl_glCreateShader(shaderType: Int): GLShader
  def impl_glDeleteShader(shader: GLShader): Unit
  def impl_glShaderSource(shader: GLShader, src: String): Unit
  def impl_glCompileShader(shader: GLShader): Unit
  def impl_glGetShaderiv(shader: GLShader, pname: Int): Int
  def impl_glGetShaderbv(shader: GLShader, pname: Int): Boolean
  def impl_getShaderInfoLog(shader: GLShader): String
  def impl_glCreateProgram(): GLProgram
  def impl_glDeleteProgram(program: GLProgram): Unit
  def impl_getProgramInfoLog(program: GLProgram): String
  def impl_glGetProgramiv(program: GLProgram, pname: Int): Int
  def impl_glGetProgrambv(program: GLProgram, pname: Int): Boolean
  def impl_glAttachShader(program: GLProgram, shader: GLShader): Unit
  def impl_glBindAttribLocation(program: GLProgram, index: Int, name: String): Unit
  def impl_glLinkProgram(program: GLProgram): Unit
  def impl_glUseProgram(program: GLProgram): Unit
  def impl_glBindBuffer(target: Int, buffer: GLBuffer): Unit
  def impl_glCreateBuffer(): GLBuffer
  def impl_glDeleteBuffer(buffer: GLBuffer): Unit
  def impl_glVertexAttribPointer(indx: Int, size: Int, componentType: Int, normalized: Boolean, stride: Int, offset: Int): Unit
  def impl_glEnableVertexAttribArray(index: Int): Unit
  def impl_glDisableVertexAttribArray(index: Int): Unit
  def impl_glGetUniformLocation(program: GLProgram, name: String): GLUniformLocation
  def impl_glUniform1f(location: GLUniformLocation, x: Float): Unit
  def impl_glUniform4fv(location: GLUniformLocation, v: Array[Float]): Unit
  def impl_glUniform1i(location: GLUniformLocation, v: Int): Unit
  def impl_glUniformMatrix4fv(location: GLUniformLocation, transpose: Boolean, v: Array[Float]): Unit
  def impl_glDrawArrays(mode: Int, first: Int, count: Int): Unit


  @inline final def get_maxVertexAttribs() = getInteger(const.MAX_VERTEX_ATTRIBS)

  def currentProgram(): GLProgram

  @inline final def clearColor(red: Float, green: Float, blue: Float, alpha: Float): Unit={
    impl_glClearColor(red, green, blue, alpha)
    checkGlError()
  }

  @inline final def clear(mask: Int): Unit={
    impl_glClear(mask)
    checkGlError()
  }

  @inline final def viewport(x: Int, y: Int, width: Int, height: Int) {
    impl_glViewport(x, y, width, height)
    checkGlError()
  }

  @inline final def enable(cap: Int) {
    impl_glEnable(cap)
    checkGlError()
  }

  @inline final def disable(cap: Int) {
    impl_glDisable(cap)
    checkGlError()
  }

  @inline final def blendFunc(sfactor: Int, dfactor: Int): Unit = {
    impl_glBlendFunc(sfactor, dfactor)
    checkGlError()
  }

  @inline final def getInteger(pname: Int): Int = {
    val r = impl_glGetIntegerv(pname)
    checkGlError()
    r
  }

  @inline final def createShader(shaderType: Int): GLShader = {
    val r = impl_glCreateShader(shaderType)
    checkGlError()
    r
  }

  @inline final def deleteShader(shader: GLShader): Unit={
    impl_glDeleteShader(shader)
    checkGlError()
  }

  @inline final def shaderSource(shader: GLShader, src: String): Unit={
    impl_glShaderSource(shader, src)
    checkGlError()
  }

  @inline final def compileShader(shader: GLShader): Unit={
    impl_glCompileShader(shader)
    checkGlError()
  }

  @inline final def getShaderiv(shader: GLShader, pname: Int): Int = {
    val r = impl_glGetShaderiv(shader, pname)
    checkGlError()
    r
  }

  @inline final def getShaderbv(shader: GLShader, pname: Int): Boolean = {
    val r = impl_glGetShaderbv(shader, pname)
    checkGlError()
    r
  }


  @inline final def getShaderInfoLog(shader: GLShader): String ={
    val r = impl_getShaderInfoLog(shader)
    checkGlError()
    r
  }

  @inline final def getProgramInfoLog(program: GLProgram): String ={
    val r = impl_getProgramInfoLog(program)
    checkGlError()
    r
  }



  @inline final def createProgram(): GLProgram = {
    val r = impl_glCreateProgram()
    checkGlError()
    r
  }

  @inline final def getProgramiv(program: GLProgram, pname: Int): Int = {
    val r = impl_glGetProgramiv(program, pname)
    checkGlError()
    r
  }

  @inline final def getProgrambv(program: GLProgram, pname: Int): Boolean = {
    val r = impl_glGetProgrambv(program, pname)
    checkGlError()
    r
  }

  @inline final def deleteProgram(program: GLProgram): Unit={
    impl_glDeleteProgram(program)
    checkGlError()
  }

  @inline final def attachShader(program: GLProgram, shader: GLShader): Unit={
    impl_glAttachShader(program, shader)
    checkGlError()
  }

  @inline final def bindAttributeLocation(program: GLProgram, index: Int, name: String): Unit={
    impl_glBindAttribLocation(program, index, name)
    checkGlError()
  }

  @inline final def linkProgram(program: GLProgram): Unit={
    impl_glLinkProgram(program)
    checkGlError()
  }

  @inline final def useProgram(program: GLProgram): Unit={
    impl_glUseProgram(program)
    checkGlError()
  }

  @inline final def bindBuffer(target: Int, buffer: GLBuffer): Unit ={
    impl_glBindBuffer(target, buffer)
    checkGlError()
  }

  @inline final def createBuffer(): GLBuffer={
    val r=impl_glCreateBuffer()
    checkGlError()
    r
  }

  @inline final def deleteBuffer(buffer: GLBuffer): Unit={
    impl_glDeleteBuffer(buffer)
    checkGlError()
  }

  @inline final def vertexAttribPointer(indx: Int, size: Int, componentType: Int, normalized: Boolean, stride: Int, offset: Int): Unit={
    impl_glVertexAttribPointer(indx, size, componentType, normalized, stride, offset)
    checkGlError()
  }

  @inline final def enableVertexAttribArray(index: Int): Unit={
    impl_glEnableVertexAttribArray(index)
    checkGlError()
  }

  @inline final def disableVertexAttribArray(index: Int): Unit={
    impl_glDisableVertexAttribArray(index)
    checkGlError()
  }

  @inline final def getUniformLocation(program: GLProgram, name: String): GLUniformLocation={
    val r = impl_glGetUniformLocation(program, name)
    checkGlError()
    r
  }

  @inline final def uniform1f(location: GLUniformLocation, x: Float): Unit={
    impl_glUniform1f(location, x)
    checkGlError()
  }

  @inline final def uniform4f(location: GLUniformLocation, v: Array[Float]): Unit={
    impl_glUniform4fv(location, v)
    checkGlError()
  }

  @inline final def uniform1i(location: GLUniformLocation, v: Int): Unit={
    impl_glUniform1i(location, v)
    checkGlError()
  }

  @inline final def uniformMatrix4fv(location: GLUniformLocation, transpose: Boolean, v: Array[Float]): Unit={
    impl_glUniformMatrix4fv(location, transpose, v)
  }

  @inline final def drawArrays(mode: Int, first: Int, count: Int): Unit={
    impl_glDrawArrays(mode, first, count)
    checkGlError()
  }
}
