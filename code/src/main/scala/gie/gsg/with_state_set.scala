package gie.gsg

import gie.gsg.state_attribute.{VertexAttributeAttributeComponent, UniformValueAttributeComponent, StateAttributeComponent}

trait WithStateSetComponent {
  this: RenderContext with StateSetComponent with StateAttributeComponent with UniformValueAttributeComponent with VertexAttributeAttributeComponent=>

  trait WithStateSet {
    def stateSet: StateSet
    def stateSet_! : StateSet
    def addAttribute(attr: StateAttribute): this.type={
      stateSet_!.addAttribute(attr)
      this
    }
    def addUniformValue(name: String, v: Int): this.type={
      stateSet_!.addVariableValue(new ConstIntUniformValueAttribute(name, v) )
      this
    }
    def addVertexAttributeValue(attr: VertexAttributeAttribute): this.type={
      stateSet_!.addVariableValue(attr)
      this
    }
    def addVertexAttributeValue(vertexAttr: String, componentSize: Int, componentType: Int, stride: Int = 0, offset: Int = 0)(buffer: => gl.GLBuffer): this.type={
      addVertexAttributeValue( new VertexAttributeAttribute(vertexAttr, buffer, componentSize, componentType, stride, offset) )
    }
  }

  trait WithStateSetImpl {
    this: WithStateSet =>

    private var m_stateSet:StateSet = null

    def stateSet: StateSet = m_stateSet
    def stateSet_! : StateSet = {
      if(m_stateSet eq null) { m_stateSet = new StateSet() }

      m_stateSet
    }
  }

}