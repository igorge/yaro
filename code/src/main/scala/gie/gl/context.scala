package gie.gl

trait Constants {
  val NO_ERROR: Int
  val MAX_VERTEX_ATTRIBS: Int
  val CURRENT_PROGRAM: Int
}

trait Context {

  type Shader
  type Program
  type Buffer
  type UniformLocation

  def checkGlError(): Unit
  val const:Constants

  def impl_glClear(mask: Int): Unit
  def impl_glClearColor(red: Float, green: Float, blue: Float, alpha: Float): Unit
  def impl_glViewport(x: Int, y: Int, width: Int, height: Int): Unit
  def impl_glEnable(cap: Int): Unit
  def impl_glDisable(cap: Int): Unit
  def impl_glBlendFunc(sfactor: Int, dfactor: Int): Unit
  def impl_glGetIntegerv(pname: Int): Int
  def impl_glCreateShader(shaderType: Int): Shader
  def impl_glDeleteShader(shader: Shader): Unit
  def impl_glShaderSource(shader: Shader, src: String): Unit
  def impl_glCompileShader(shader: Shader): Unit
  def impl_glGetShaderiv(shader: Shader, pname: Int): Int
  def impl_glCreateProgram(): Program
  def impl_glDeleteProgram(program: Program): Unit
  def impl_glGetProgramiv(program: Program, pname: Int): Int
  def impl_glAttachShader(program: Program, shader: Shader): Unit
  def impl_glBindAttribLocation(program: Program, index: Int, name: String): Unit
  def impl_glLinkProgram(program: Program): Unit
  def impl_glUseProgram(program: Program): Unit
  def impl_glBindBuffer(target: Int, buffer: Buffer): Unit
  def impl_glCreateBuffer(): Buffer
  def impl_glDeleteBuffer(buffer: Buffer): Unit
  def impl_glVertexAttribPointer(indx: Int, size: Int, componentType: Int, normalized: Boolean, stride: Int, offset: Int): Unit
  def impl_glEnableVertexAttribArray(index: Int): Unit
  def impl_glDisableVertexAttribArray(index: Int): Unit
  def impl_glGetUniformLocation(program: Program, name: String): UniformLocation
  def impl_glUniform1f(location: UniformLocation, x: Float): Unit
  def impl_glUniform4fv(location: UniformLocation, v: Array[Float]): Unit
  def impl_glUniform1i(location: UniformLocation, v: Int): Unit
  def impl_glUniformMatrix4fv(location: UniformLocation, transpose: Boolean, v: Array[Float]): Unit


  @inline final def get_maxVertexAttribs() = getInteger(const.MAX_VERTEX_ATTRIBS)

  @inline final def get_currentProgram() = getInteger(const.CURRENT_PROGRAM)


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

  @inline final def createShader(shaderType: Int): Shader = {
    val r = impl_glCreateShader(shaderType)
    checkGlError()
    r
  }

  @inline final def deleteShader(shader: Shader): Unit={
    impl_glDeleteShader(shader)
    checkGlError()
  }

  @inline final def shaderSource(shader: Shader, src: String): Unit={
    impl_glShaderSource(shader, src)
    checkGlError()
  }

  @inline final def compileShader(shader: Shader): Unit={
    impl_glCompileShader(shader)
    checkGlError()
  }

  @inline final def getShaderiv(shader: Shader, pname: Int): Int = {
    val r = impl_glGetShaderiv(shader, pname)
    checkGlError()
    r
  }

  @inline final def createProgram(): Program = {
    val r = impl_glCreateProgram()
    checkGlError()
    r
  }

  @inline final def getProgramiv(program: Program, pname: Int): Int = {
    val r = impl_glGetProgramiv(program, pname)
    checkGlError()
    r
  }

  @inline final def deleteProgram(program: Program): Unit={
    impl_glDeleteProgram(program)
    checkGlError()
  }

  @inline final def attachShader(program: Program, shader: Shader): Unit={
    impl_glAttachShader(program, shader)
    checkGlError()
  }

  @inline final def bindAttributeLocation(program: Program, index: Int, name: String): Unit={
    impl_glBindAttribLocation(program, index, name)
    checkGlError()
  }

  @inline final def linkProgram(program: Program): Unit={
    impl_glLinkProgram(program)
    checkGlError()
  }

  @inline final def useProgram(program: Program): Unit={
    impl_glUseProgram(program)
    checkGlError()
  }

  @inline final def bindBuffer(target: Int, buffer: Buffer): Unit ={
    impl_glBindBuffer(target, buffer)
    checkGlError()
  }

  @inline final def createBuffer(): Buffer={
    val r=impl_glCreateBuffer()
    checkGlError()
    r
  }

  @inline final def deleteBuffer(buffer: Buffer): Unit={
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

  @inline final def getUniformLocation(program: Program, name: String): UniformLocation={
    val r = impl_glGetUniformLocation(program, name)
    checkGlError()
    r
  }

  @inline final def uniform1f(location: UniformLocation, x: Float): Unit={
    impl_glUniform1f(location, x)
    checkGlError()
  }

  @inline final def uniform4f(location: UniformLocation, v: Array[Float]): Unit={
    impl_glUniform4fv(location, v)
    checkGlError()
  }

  @inline final def uniform1i(location: UniformLocation, v: Int): Unit={
    impl_glUniform1i(location, v)
    checkGlError()
  }

  @inline final def uniformMatrix4fv(location: UniformLocation, transpose: Boolean, v: Array[Float]): Unit={
    impl_glUniformMatrix4fv(location, transpose, v)
  }
}
