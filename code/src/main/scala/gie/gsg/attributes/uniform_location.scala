package gie.gsg.state_attribute

import gie.gsg.{RenderContext}


trait UniformLocationComponent {
  this: RenderContext with StateAttributeComponent with GlProgramComponent =>

  trait UniformValueAttribute {
    val uniformLocation: gl.UniformTrait
    val uniformProgram: GlProgramHolder

    @inline final def name:String = uniformLocation.name
    @inline final def location:gl.GLUniformLocation = uniformLocation.location

    def ===(y: UniformValueAttribute): Boolean
    def applyUniform(): Unit
  }

  object UniformValueAttribute {
    @inline def orderingCmp(l: UniformValueAttribute, r: UniformValueAttribute) = implicitly[Ordering[String]].compare(l.name, r.name)
  }

  abstract class ConstUniformValueAttribute[T](val uniformProgram: GlProgramHolder, val uniformLocation: gl.UniformTrait, v: => T) extends UniformValueAttribute {
    protected lazy val m_value = v

    def ===(y: UniformValueAttribute): Boolean = {
      assume(name==y.name) //TODO: remove

      if (name != y.name || !y.isInstanceOf[ConstUniformValueAttribute[_]]) {
        false
      } else {
        val yTyped = y.asInstanceOf[ConstUniformValueAttribute[_]]
        uniformProgram == yTyped.uniformProgram && m_value == yTyped.m_value
      }
    }

    def applyUniform(): Unit

    override def toString() = s"ConstUniformValueAttribute(${uniformLocation.name} @ ${uniformLocation.location}, ${m_value})"

  }


}