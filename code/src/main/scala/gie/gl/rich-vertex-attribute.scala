package gie.gl

trait RichVertexAttributeTrait {
  this: Context with RichContextCommon =>

  type AttributeMapType = scala.collection.Map[String, VertexAttributeTrait]

  trait VertexAttributeTrait {
    var location: GLVertexAttributeLocation
  }

  class VertexAttribute extends VertexAttributeTrait {
    var location: GLVertexAttributeLocation = vertexAttributeLocation_null
  }

  object VertexAttribute {
    def apply(): VertexAttribute = new VertexAttribute()
    def apply(name: String, m: scala.collection.mutable.Map[String, VertexAttributeTrait]) = {
      val uniform = new VertexAttribute()
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