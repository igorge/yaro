package gie.gl

import slogging.LoggerHolder

trait RichVertexAttributeComponent {
  self: Context with RichContextCommon with LoggerHolder =>

  type AttributeMapType = scala.collection.Map[String, VertexAttributeApiTrait]

  trait VertexAttributeApiTrait {
    val name: String
    var location: GLVertexAttributeLocation = vertexAttributeLocation_null

    def isDefined: Boolean = !vertexAttributeLocation_null_?(location)

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

    @inline final def bindBuffer(target: Int, buffer: GLBuffer): this.type={
      self.bindBuffer(target /*const.ARRAY_BUFFER*/, buffer)
      this
    }


  }

  class VertexAttribute(val name: String) extends VertexAttributeApiTrait {
    def cloneName: VertexAttribute = new VertexAttribute(name)
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