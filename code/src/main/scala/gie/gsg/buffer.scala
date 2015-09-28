package gie.gsg

import slogging.LoggerHolder

trait BufferComponent {
  this: RenderContext with LoggerHolder =>

  trait GLBufferLike {
    val usage: Int
    def glBuffer: gl.GLBuffer
    def size: Int
    def componentType: Int
    def bufferTarget: Int
  }


  class GLStaticBuffer[ SeqT >: Null <: AnyRef](val bufferTarget: Int, private var data:SeqT)(implicit bufferDataDispatch: gl.BufferDataDispatch[SeqT]) extends GLBufferLike {
    val usage = gl.const.STATIC_DRAW
    private val m_size = bufferDataDispatch.size(data)
    def size = m_size
    def componentType = bufferDataDispatch.componentType
    private lazy val m_buffer = {
      val b= gl.createBuffer(bufferTarget, data, usage)
      data = null
      b
    }
    def glBuffer = m_buffer
  }

  @inline def staticArrayBuffer[ SeqT >: Null <: AnyRef](data:SeqT)(implicit bufferDataDispatch: gl.BufferDataDispatch[SeqT]) = new GLStaticBuffer(gl.const.ARRAY_BUFFER, data)
  @inline def staticElementArrayBuffer[ SeqT >: Null <: AnyRef](data:SeqT)(implicit bufferDataDispatch: gl.BufferDataDispatch[SeqT]) = new GLStaticBuffer(gl.const.ELEMENT_ARRAY_BUFFER, data)


}
