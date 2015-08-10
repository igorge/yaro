package gie.gl

//import _root_.android.opengl.GLES20._
//
//import api.gl._

trait ShaderResourceTrait {
  this: Context =>

  def newShader(shaderType: Int)(implicit resourceContext: ResourceContext) = new ShaderResource(resourceContext, shaderType)

  class ShaderResource(resourceContext: ResourceContext, shaderType: Int) {

    type SelfGLResourceType = Shader

    private var m_handle = BoxedVar(createShader(shaderType))

    @inline final def handle = m_handle()
//
//    {
//      // ctor
//      val breakSelfClosure_handle = m_handle
//
//      rCtx.registerResourceReference(this) {
//        if (breakSelfClosure_handle() != 0) {
//          deleteShader(breakSelfClosure_handle())
//          breakSelfClosure_handle() = 0
//        }
//      }
//    }
//
    def free() {
      if ( !implicitly[Nullable[SelfGLResourceType]].isNull(m_handle()) ) deleteShader(m_handle())
      m_handle() = 0
    }
//
//
//    def source(src: String) = {
//      assume(m_handle() != 0)
//
//      shaderSource(m_handle(), src)
//      this
//    }
//
//    def compile() = {
//      assume(m_handle() != 0)
//
//      compileShader(m_handle())
//      if (getShaderiv(m_handle(), GL_COMPILE_STATUS) == GL_FALSE) {
//        throw new ShaderException(s"Shader compilation have failed with message: ${getShaderInfoLog(m_handle())}")
//      }
//      this
//    }

  }


}
//class VertexShader()(implicit rCtx: ResourceContext) extends Shader(GL_VERTEX_SHADER)
//class FragmentShader()(implicit rCtx: ResourceContext) extends Shader(GL_FRAGMENT_SHADER)

