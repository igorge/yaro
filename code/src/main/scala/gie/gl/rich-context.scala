package gie.gl

trait RichContextCommon {
  this: Context =>

  @inline final def createVertexShader(): GLShader =  createShader(const.VERTEX_SHADER)
  @inline final def createFragmentShader(): GLShader =  createShader(const.FRAGMENT_SHADER)
  @inline final def compilationStatus(shader: GLShader): Boolean = getShaderbv(shader, const.COMPILE_STATUS)

}

trait RichContext
  extends RichUniformTrait
  with    RichVertexAttributeTrait
  with    RichProgramTrait
  with    RichShaderTrait
  with    RichContextCommon {
  this: Context =>


}