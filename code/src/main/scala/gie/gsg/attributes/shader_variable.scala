package gie.gsg.state_attribute

import gie.gsg.{StateSetComponent, RenderContext}


trait ShaderVariableComponent {
  this: RenderContext with StateSetComponent =>

  trait ShaderVariableAttribute {

    def name:String

    def ===(y: ShaderVariableAttribute): Boolean
    def apply(from: ShaderVariableAttribute): Unit
    def unapply(): Unit

    protected lazy val program = rendererActiveProgram // program is applied first (because of its idx 0), so it will be valid on this methods
  }

  object ShaderVariableAttribute {
    @inline def orderingCmp(l: ShaderVariableAttribute, r: ShaderVariableAttribute) = implicitly[Ordering[String]].compare(l.name, r.name)
  }


}