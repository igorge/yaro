package gie.gl

trait Constants {
  val NO_ERROR: Int
  val TRIANGLES: Int
  val COLOR_BUFFER_BIT: Int
  val DEPTH_BUFFER_BIT: Int
  val MAX_VERTEX_ATTRIBS: Int
  val CURRENT_PROGRAM: Int
  val VERTEX_SHADER: Int
  val FRAGMENT_SHADER: Int
  val SHADER_TYPE: Int
  val DELETE_STATUS: Int
  val COMPILE_STATUS: Int
  val LINK_STATUS: Int
  val TEXTURE_2D: Int
  val TEXTURE_CUBE_MAP: Int
  val TEXTURE0: Int
  val TEXTURE1: Int
  val TEXTURE2: Int
  val TEXTURE3: Int
  val TEXTURE4: Int
  val TEXTURE5: Int
  val TEXTURE6: Int
  val TEXTURE7: Int
  val ARRAY_BUFFER: Int
  val ELEMENT_ARRAY_BUFFER: Int
  val STATIC_DRAW: Int
  val DYNAMIC_DRAW: Int
  val STREAM_DRAW: Int
  val RGB: Int
  val RGBA: Int
  val BYTE: Int
  val UNSIGNED_BYTE: Int
  val UNSIGNED_SHORT: Int
  val FLOAT: Int
  val INT: Int
  val TEXTURE_MAG_FILTER: Int
  val TEXTURE_MIN_FILTER: Int
  val NEAREST: Int
  val BLEND: Int
  val DEPTH_TEST: Int
  val CULL_FACE: Int
  val ONE: Int
  val SRC_ALPHA: Int
  val ONE_MINUS_SRC_ALPHA: Int

  val NEVER: Int
  val LESS: Int
  val EQUAL: Int
  val LEQUAL: Int
  val GREATER: Int
  val NOTEQUAL: Int
  val GEQUAL: Int
  val ALWAYS: Int
}

trait Context {

  type GLShader
  type GLProgram
  type GLBuffer
  type GLUniformLocation
  type GLVertexAttributeLocation = Int
  type GLTexture

  case class AsUnsignedShort[T <: AnyRef](val data: T)

  trait BufferDataDispatch[T <: AnyRef] {
    def apply(target: Int, data: T, usage: Int): Unit
    def size(data: T): Int
    def componentType: Int
  }

  object BufferDataDispatch {

    implicit object AsUnsignedShort_Int_BufferDataDispatch extends BufferDataDispatch[AsUnsignedShort[Array[Int]]]{
      type SeqT = AsUnsignedShort[Array[Int]]

      @inline def apply(target: Int, data: SeqT, usage: Int): Unit = impl_glBufferDataUnsignedShort(target, data.data, usage)
      @inline def size(data: SeqT): Int = data.data.size
      @inline def componentType: Int = const.UNSIGNED_SHORT
    }

    implicit object AInt_BufferDataDispatch extends BufferDataDispatch[Array[Int]]{
      type SeqT = Array[Int]

      @inline def apply(target: Int, data: SeqT, usage: Int): Unit = impl_glBufferDataInt(target, data, usage)
      @inline def size(data: SeqT): Int = data.size
      @inline def componentType: Int = const.INT
    }

    implicit object SInt_BufferDataDispatch extends BufferDataDispatch[Seq[Int]]{
      type SeqT = Seq[Int]

      @inline def apply(target: Int, data: SeqT, usage: Int): Unit = impl_glBufferDataInt(target, data, usage)
      @inline def size(data: SeqT): Int = data.size
      @inline def componentType: Int = const.INT
    }

    implicit object AFloat_BufferDataDispatch extends BufferDataDispatch[Array[Float]]{
      type SeqT = Array[Float]

      @inline def apply(target: Int, data: SeqT, usage: Int): Unit = impl_glBufferDataFloat(target, data, usage)
      @inline def size(data: SeqT): Int = data.size
      @inline def componentType: Int = const.FLOAT
    }

    implicit object SFloat_BufferDataDispatch extends BufferDataDispatch[Seq[Float]]{
      type SeqT = Seq[Float]
      @inline def apply(target: Int, data: SeqT, usage: Int): Unit = impl_glBufferDataFloat(target, data, usage)
      @inline def size(data: SeqT): Int = data.size
      @inline def componentType: Int = const.FLOAT
    }

  }

  def uniformLocation_null: GLUniformLocation
  def uniformLocation_null_?(x: GLUniformLocation): Boolean

  def program_null: GLProgram
  def program_null_?(x: GLProgram): Boolean

  def buffer_null: GLBuffer
  def buffer_null_?(x: GLBuffer): Boolean

  def texture_null: GLTexture
  def texture_null_?(x: GLTexture): Boolean

  @inline final def vertexAttributeLocation_null: GLVertexAttributeLocation = -1
  @inline final def vertexAttributeLocation_null_?(x: GLVertexAttributeLocation): Boolean = x == -1

  val const:Constants

  @inline final def checkGlError(): Unit={
    val code = getError()
    if ( code != const.NO_ERROR ) throw new GlGetErrorException(code)
  }

  def optCheckGlError(): Unit = checkGlError()

  def impl_glGetError(): Int
  def impl_glClear(mask: Int): Unit
  def impl_glClearColor(red: Float, green: Float, blue: Float, alpha: Float): Unit
  def impl_glViewport(x: Int, y: Int, width: Int, height: Int): Unit
  def impl_glEnable(cap: Int): Unit
  def impl_glDisable(cap: Int): Unit
  def impl_glBlendFunc(sfactor: Int, dfactor: Int): Unit
  def impl_glDepthFunc(func: Int): Unit
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
  def impl_glBufferDataFloat(target: Int, data: Array[Float], usage: Int): Unit
  def impl_glBufferDataFloat(target: Int, data: Seq[Float], usage: Int): Unit
  def impl_glBufferDataInt(target: Int, data: Array[Int], usage: Int): Unit
  def impl_glBufferDataInt(target: Int, data: Seq[Int], usage: Int): Unit
  def impl_glBufferDataUnsignedShort(target: Int, data: Array[Int], usage: Int): Unit
  def impl_glBufferDataUnsignedShort(target: Int, data: Seq[Int], usage: Int): Unit
  def impl_glVertexAttribPointer(indx: Int, size: Int, componentType: Int, normalized: Boolean, stride: Int, offset: Int): Unit
  def impl_glEnableVertexAttribArray(index: Int): Unit
  def impl_glDisableVertexAttribArray(index: Int): Unit
  def impl_glGetUniformLocation(program: GLProgram, name: String): GLUniformLocation
  def impl_glGetAttribLocation(program: GLProgram, name: String): Int
  def impl_glUniform1f(location: GLUniformLocation, x: Float): Unit
  def impl_glUniform4fv(location: GLUniformLocation, v: Array[Float]): Unit
  def impl_glUniform1i(location: GLUniformLocation, v: Int): Unit
  def impl_glUniformMatrix4fv(location: GLUniformLocation, transpose: Boolean, v: Array[Float]): Unit
  def impl_glDrawArrays(mode: Int, first: Int, count: Int): Unit
  def impl_glDrawElements(mode: Int, count: Int, `type`: Int, offset: Int): Unit
  def impl_glGenTexture(): GLTexture
  def impl_glDeleteTextures(texture: GLTexture): Unit
  def impl_glIsTexture(texture: GLTexture): Boolean
  def impl_glActiveTexture(texture: Int): Unit
  def impl_glBindTexture(target: Int, texture: GLTexture): Unit
  def impl_glTexParameterf(target: Int, pname: Int, param: Float): Unit
  def impl_glTexParameteri(target: Int, pname: Int, param: Int): Unit
  def impl_glTexImage2D(target: Int, level: Int, internalformat: Int, width: Int, height: Int, border: Int, format: Int, `type`: Int, pixels: Array[Byte]): Unit


  def currentProgram(): GLProgram

  @inline final def getError(): Int = impl_glGetError()

  @inline final def clearColor(red: Float, green: Float, blue: Float, alpha: Float): Unit={
    impl_glClearColor(red, green, blue, alpha)
    optCheckGlError()
  }

  @inline final def clear(mask: Int): Unit={
    impl_glClear(mask)
    optCheckGlError()
  }

  @inline final def viewport(x: Int, y: Int, width: Int, height: Int) {
    impl_glViewport(x, y, width, height)
    optCheckGlError()
  }

  @inline final def enable(cap: Int) {
    impl_glEnable(cap)
    optCheckGlError()
  }

  @inline final def disable(cap: Int) {
    impl_glDisable(cap)
    optCheckGlError()
  }

  @inline final def blendFunc(sfactor: Int, dfactor: Int): Unit = {
    impl_glBlendFunc(sfactor, dfactor)
    optCheckGlError()
  }

  @inline final def depthFunc(func: Int): Unit={
    impl_glDepthFunc(func)
    optCheckGlError()
  }


  @inline final def getInteger(pname: Int): Int = {
    val r = impl_glGetIntegerv(pname)
    optCheckGlError()
    r
  }

  @inline final def createShader(shaderType: Int): GLShader = {
    val r = impl_glCreateShader(shaderType)
    optCheckGlError()
    r
  }

  @inline final def deleteShader(shader: GLShader): Unit={
    impl_glDeleteShader(shader)
    optCheckGlError()
  }

  @inline final def shaderSource(shader: GLShader, src: String): Unit={
    impl_glShaderSource(shader, src)
    optCheckGlError()
  }

  @inline final def compileShader(shader: GLShader): Unit={
    impl_glCompileShader(shader)
    optCheckGlError()
  }

  @inline final def getShaderiv(shader: GLShader, pname: Int): Int = {
    val r = impl_glGetShaderiv(shader, pname)
    optCheckGlError()
    r
  }

  @inline final def getShaderbv(shader: GLShader, pname: Int): Boolean = {
    val r = impl_glGetShaderbv(shader, pname)
    optCheckGlError()
    r
  }


  @inline final def getShaderInfoLog(shader: GLShader): String ={
    val r = impl_getShaderInfoLog(shader)
    optCheckGlError()
    r
  }

  @inline final def getProgramInfoLog(program: GLProgram): String ={
    val r = impl_getProgramInfoLog(program)
    optCheckGlError()
    r
  }



  @inline final def createProgram(): GLProgram = {
    val r = impl_glCreateProgram()
    optCheckGlError()
    r
  }

  @inline final def getProgramiv(program: GLProgram, pname: Int): Int = {
    val r = impl_glGetProgramiv(program, pname)
    optCheckGlError()
    r
  }

  @inline final def getProgrambv(program: GLProgram, pname: Int): Boolean = {
    val r = impl_glGetProgrambv(program, pname)
    optCheckGlError()
    r
  }

  @inline final def deleteProgram(program: GLProgram): Unit={
    impl_glDeleteProgram(program)
    optCheckGlError()
  }

  @inline final def attachShader(program: GLProgram, shader: GLShader): Unit={
    impl_glAttachShader(program, shader)
    optCheckGlError()
  }

  @inline final def bindAttributeLocation(program: GLProgram, index: Int, name: String): Unit={
    impl_glBindAttribLocation(program, index, name)
    optCheckGlError()
  }

  @inline final def linkProgram(program: GLProgram): Unit={
    impl_glLinkProgram(program)
    optCheckGlError()
  }

  @inline final def useProgram(program: GLProgram): Unit={
    impl_glUseProgram(program)
    optCheckGlError()
  }

  @inline final def bindBuffer(target: Int, buffer: GLBuffer): Unit ={
    impl_glBindBuffer(target, buffer)
    optCheckGlError()
  }

  @inline final def createBuffer(): GLBuffer={
    val r=impl_glCreateBuffer()
    optCheckGlError()
    r
  }

  @inline final def deleteBuffer(buffer: GLBuffer): Unit={
    impl_glDeleteBuffer(buffer)
    optCheckGlError()
  }

  @inline final def bufferData[T <: AnyRef](target: Int, data: T, usage: Int)(implicit dispatch: BufferDataDispatch[T]): Unit={
    dispatch(target, data, usage)
    optCheckGlError()
  }

  @inline final def vertexAttribPointer(indx: Int, size: Int, componentType: Int, normalized: Boolean, stride: Int, offset: Int): Unit={
    impl_glVertexAttribPointer(indx, size, componentType, normalized, stride, offset)
    optCheckGlError()
  }

  @inline final def enableVertexAttribArray(index: Int): Unit={
    impl_glEnableVertexAttribArray(index)
    optCheckGlError()
  }

  @inline final def disableVertexAttribArray(index: Int): Unit={
    impl_glDisableVertexAttribArray(index)
    optCheckGlError()
  }

  @inline final def getUniformLocation(program: GLProgram, name: String): GLUniformLocation={
    val r = impl_glGetUniformLocation(program, name)
    optCheckGlError()
    r
  }

  @inline final def getAttribLocation(program: GLProgram, name: String): Int={
    val r = impl_glGetAttribLocation(program, name)
    optCheckGlError()
    r
  }
  

  @inline final def uniform1f(location: GLUniformLocation, x: Float): Unit={
    impl_glUniform1f(location, x)
    optCheckGlError()
  }

  @inline final def uniform4f(location: GLUniformLocation, v: Array[Float]): Unit={
    impl_glUniform4fv(location, v)
    optCheckGlError()
  }

  @inline final def uniform1i(location: GLUniformLocation, v: Int): Unit={
    impl_glUniform1i(location, v)
    optCheckGlError()
  }

  @inline final def uniformMatrix4fv(location: GLUniformLocation, transpose: Boolean, v: Array[Float]): Unit={
    impl_glUniformMatrix4fv(location, transpose, v)
    optCheckGlError()
  }

  @inline final def drawArrays(mode: Int, first: Int, count: Int): Unit={
    impl_glDrawArrays(mode, first, count)
    optCheckGlError()
  }

  @inline final def drawElements(mode: Int, count: Int, `type`: Int, offset: Int): Unit={
    impl_glDrawElements(mode, count, `type`, offset)
    optCheckGlError()
  }

  @inline final def IsTexture(texture: GLTexture): Boolean ={
    val r = impl_glIsTexture(texture)
    optCheckGlError()
    r
  }

  @inline final def genTextures(): GLTexture ={
    val r = impl_glGenTexture()
    optCheckGlError()
    r
  }

  @inline final def deleteTextures(texture: GLTexture): Unit={
    impl_glDeleteTextures(texture)
    optCheckGlError()
  }

  @inline final def activateTexture(texture: Int): Unit={
    impl_glActiveTexture(texture)
    optCheckGlError()
  }

  @inline final def bindTexture(target: Int, texture: GLTexture): Unit={
    impl_glBindTexture(target, texture)
    optCheckGlError()
  }

  @inline final def texParameter(target: Int, pname: Int, param: Float): Unit={
    impl_glTexParameterf(target, pname, param)
    optCheckGlError()
  }

  @inline final def texParameter(target: Int, pname: Int, param: Int): Unit={
    impl_glTexParameteri(target, pname, param)
    optCheckGlError()
  }

  @inline final def texImage2D(target: Int, level: Int, internalformat: Int, width: Int, height: Int, border: Int, format: Int, `type`: Int, pixels: Array[Byte]): Unit={
    impl_glTexImage2D(target, level, internalformat, width, height, border, format, `type`, pixels)
    optCheckGlError()
  }


}
