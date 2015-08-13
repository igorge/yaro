package gie.gl

import scala.annotation.switch

trait RichContextCommon {
  this: Context =>

  @inline final def createVertexShader(): GLShader =  createShader(const.VERTEX_SHADER)
  @inline final def createFragmentShader(): GLShader =  createShader(const.FRAGMENT_SHADER)
  @inline final def compilationStatus(shader: GLShader): Boolean = getShaderbv(shader, const.COMPILE_STATUS)

  @inline final def get_maxVertexAttribs() = getInteger(const.MAX_VERTEX_ATTRIBS)
}

trait RichContext
  extends RichUniformTrait
  with    RichVertexAttributeTrait
  with    RichProgramTrait
  with    RichShaderTrait
  with    RichContextCommon {
  this: Context =>


}