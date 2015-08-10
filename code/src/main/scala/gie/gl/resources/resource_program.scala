package gie.gl


trait ProgramResourceTrait { this: Context =>

  def newProgram()(implicit resourceContext: ResourceContext) = new ProgramResource(resourceContext)

  class ProgramResource(resourceContext: ResourceContext) {

    private var m_handle = BoxedVar( createProgram() )

    val breakSelfClosure_handle = m_handle

    resourceContext.registerResource{
      if ( ! implicitly[Nullable[Program]].isNull(breakSelfClosure_handle()) ) {
        deleteProgram(breakSelfClosure_handle())
        breakSelfClosure_handle() = implicitly[Nullable[Program]].nullValue
      }
    }

    //  private var m_uniformHead: Uniform = _
    //  private var m_attribHead: VertexAttribute = _
    //
    //
    //  private[gl] def impl_registerAttribute(attr: VertexAttribute){
    //    attr.nextAttrib = m_attribHead
    //    m_attribHead = attr
    //  }
    //
    //  private[gl] def impl_registerUniform(uniform: Uniform){
    //    uniform.nextUniform = m_uniformHead
    //    m_uniformHead = uniform
    //  }
    //
    @inline final def handle = m_handle()
    //
    //
    //  private def impl_updateUniform(){
    //    var current = m_uniformHead
    //    while(current ne null){
    //      current.updateLocation(this)
    //      current = current.nextUniform
    //    }
    //  }
    //
    //  private def impl_updateAndBindAttrib(){
    //    val locationsCount = getInteger_MaxVertexAttribs()
    //
    //    var currentLocation = 0
    //    var current = m_attribHead
    //
    //    while(current ne null){
    //      if(currentLocation >= locationsCount) throw new ShaderException(s"Out of vertex attribute, need index: ${currentLocation}, max avaliable ${locationsCount}")
    //      current.updateLocation(currentLocation)
    //      current.bind()(this)
    //      current = current.nextAttrib
    //      currentLocation += 1
    //    }
    //  }
    //
//      @inline final def attach(shader: Shader) = {
//        attachShader(handle, shader.handle)
//        this
//      }
    //
    //  @inline final def bindAttributeLocation(index: Int, name: String) = {
    //    glBindAttribLocation(handle, index, name)
    //    this
    //  }
    //
    //  @inline final def link() = {
    //    impl_updateAndBindAttrib()
    //
    //    linkProgram(handle)
    //    if (getProgramiv(handle, GL_LINK_STATUS)!=GL_TRUE){
    //      throw new ProgramException(getProgramInfoLog(handle))
    //    }
    //
    //    this.impl_updateUniform()
    //
    //    this
    //  }
    //
      @inline final def use() = {
        if( currentProgram()!=handle) useProgram(handle)
        this
      }
    //
    //  @inline final def uniformLocation(name: String): Option[Int] = {
    //    val r = getUniformLocation(handle, name)
    //    if (r == INVALID_UNIFORM_LOCATION) None else Some(r)
    //  }
    //
    def free(){
      if( ! implicitly[Nullable[Program]].isNull(m_handle()) ) deleteProgram(m_handle())
      m_handle() = implicitly[Nullable[Program]].nullValue
    }
  }

}