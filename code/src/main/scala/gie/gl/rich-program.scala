package gie.gl


trait RichProgramTrait {
  this: Context with RichUniformTrait with RichVertexAttributeComponent =>

  trait NameToLocationReadMaps {
    val uniforms: UniformMapType
    val attributes: AttributeMapType
  }

  trait NameToLocationWriteMaps extends NameToLocationReadMaps {
    val uniforms: collection.mutable.Map[String, UniformTrait]
    val attributes: collection.mutable.Map[String, VertexAttributeApiTrait]
  }

  def nameToLocationsMaps(): NameToLocationWriteMaps = new Object with NameToLocationWriteMaps {
    final val uniforms = collection.mutable.Map[String, UniformTrait]()
    final val attributes = collection.mutable.Map[String, VertexAttributeApiTrait]()
  }

  trait ProgramApiTrait {
    val program: GLProgram

    @inline final def get = program

    @inline final def attach(shader: GLShader): this.type = {
      attachShader(program, shader)
      this
    }

    @inline final def link(): this.type = {
      linkProgram(program)
      if (!getProgrambv(program, const.LINK_STATUS)) {
        throw new ProgramException(getProgramInfoLog(program))
      }
      this
    }

    def updateAndLink(nameToLocation:NameToLocationReadMaps, vertexAttributeStartLocation: Int = 0): this.type ={
      assume(nameToLocation ne null)
      assume(vertexAttributeStartLocation>=0)

      VertexAttribute.updateLocations(program, nameToLocation.attributes)
      this.link()
      Uniform.updateLocations(program, nameToLocation.uniforms)

      this
    }

    @inline final def use(): this.type = {
      useProgram(program)
      this
    }

    @inline def free(): Unit ={
      deleteProgram(program)
    }
  }

  class Program(initProgram: GLProgram = program_null) extends ProgramApiTrait {
    val program = if (program_null_?(initProgram)) (createProgram()) else (initProgram)
  }

  object Program {
    def apply() = new Program( )
    def apply(program: GLProgram) = new Program(program)
  }

}