package gie.gl

trait RichVertexAttributeTrait {
  self: Context with RichContextCommon =>

  type AttributeMapType = scala.collection.Map[String, VertexAttributeApiTrait]

  trait VertexAttributeApiTrait {
    var location: GLVertexAttributeLocation

    def get = location

    @inline final def vertexAttribPointer(size: Int, componentType: Int, normalized: Boolean, stride: Int, offset: Int): this.type={
      self.vertexAttribPointer(this.get, size, componentType, normalized, stride, offset )
      this
    }

    @inline final def enable(): this.type= {
      enableVertexAttribArray(this.get)
      this
    }

    @inline final def disable(): this.type= {
      disableVertexAttribArray(this.get)
      this
    }

    @inline final def bindBuffer(buffer: GLBuffer): this.type={
      self.bindBuffer(const.ARRAY_BUFFER, buffer)
      this
    }


  }

  class VertexAttribute(val name: String) extends VertexAttributeApiTrait {
    var location: GLVertexAttributeLocation = vertexAttributeLocation_null
  }

  object VertexAttribute {
    def apply(name: String): VertexAttribute = new VertexAttribute(name)
    def apply(name: String, m: scala.collection.mutable.Map[String, VertexAttributeApiTrait]) = {
      val uniform = new VertexAttribute(name)
      m.put(name, uniform)
      uniform
    }

    @inline def maxVertexAttribs: Int =  get_maxVertexAttribs()

    def updateLocations(program: GLProgram, attributes: AttributeMapType, startFromIndex: Int=0): Unit ={

      val maxAttribs = maxVertexAttribs
      var current = startFromIndex

      attributes.foreach{ case (name, attribute)=>
        if(current >= maxAttribs) throw new VertexAttributeBindingException(s"Out of vertex attribute indices while binding '${name}', need index: ${current}, max avaliable count is ${maxAttribs}")
        attribute.location = current
        bindAttributeLocation(program, attribute.location, name)
        current += 1
      }
    }

  }


}