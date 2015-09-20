package gie.gsg.state_attribute

import gie.gsg.RenderContext


trait ShaderVariableComponent {
  this: RenderContext =>

  trait ShaderVariableAttribute {

    def name:String

    def ===(y: ShaderVariableAttribute): Boolean
    def apply(): Unit
    def unapply(): Unit
  }

  object ShaderVariableAttribute {
    @inline def orderingCmp(l: ShaderVariableAttribute, r: ShaderVariableAttribute) = implicitly[Ordering[String]].compare(l.name, r.name)
  }


}