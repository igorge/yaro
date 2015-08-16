package gie.gl

trait RichUniformTrait { this: Context =>

  type UniformMapType = scala.collection.Map[String, UniformTrait]

  trait UniformTrait {
    var location: GLUniformLocation
    @inline def get = location
  }

  class Uniform extends UniformTrait {
    var location: GLUniformLocation = uniformLocation_null
  }

  object Uniform {
    def apply(): Uniform = new Uniform()
    def apply(name: String, m: scala.collection.mutable.Map[String, UniformTrait]) = {
      val uniform = new Uniform()
      m.put(name, uniform)
      uniform
    }

    def updateLocations(program: GLProgram, uniforms: UniformMapType): Unit ={
      uniforms.foreach{ case (name, uniform)=>
        val newLocation = getUniformLocation(program, name)

        if (uniformLocation_null_?(newLocation))
          throw new UniformNotFound(s"Not found uniform '${name}', while resolving for '${uniform}'. Returned erroneous location: '${newLocation}'.")

        uniform.location = newLocation
      }
    }
  }

}