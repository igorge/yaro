package gie.gsg.state_attribute

import gie.gsg.{RenderContext}


trait UniformValueAttributeComponent {
  this: RenderContext with StateAttributeComponent with GlProgramAttributeComponent with ShaderVariableComponent =>

  trait UniformValueAttribute extends ShaderVariableAttribute {
    val uniformLocation: gl.UniformTrait
    val uniformProgram: GlProgramHolder

    @inline final def name:String = uniformLocation.name
    @inline final def location:gl.GLUniformLocation = uniformLocation.location

    def apply(from: ShaderVariableAttribute): Unit
    def unapply(): Unit={/* do nothing*/}

  }

  abstract class ConstUniformValueAttribute[T](val uniformProgram: GlProgramHolder, val uniformLocation: gl.UniformTrait, v: => T) extends UniformValueAttribute {
    protected lazy val m_value = v

    def ===(y: ShaderVariableAttribute): Boolean = {
      assume(name==y.name) //TODO: remove

      if (name != y.name || !y.isInstanceOf[ConstUniformValueAttribute[_]]) {
        false
      } else {
        val yTyped = y.asInstanceOf[ConstUniformValueAttribute[_]]
        uniformProgram == yTyped.uniformProgram && m_value == yTyped.m_value
      }
    }

//    def apply(from: ShaderVariableAttribute): Unit

    override def toString() = s"ConstUniformValueAttribute(${uniformLocation.name} @ ${uniformLocation.location}, ${m_value})"

  }


}