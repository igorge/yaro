package gie.gsg.state_attribute

import gie.gsg.{ProgramHolderComponent, RenderContext}


trait UniformValueAttributeComponent {
  this: RenderContext with ProgramHolderComponent with StateAttributeComponent with GlProgramAttributeComponent with ShaderVariableComponent =>

  trait UniformValueAttribute extends ShaderVariableAttribute {
    final lazy val uniformLocation = program.resolveUniform(new gl.Uniform(name))

    def apply(from: ShaderVariableAttribute): Unit
    def unapply(): Unit={/* do nothing*/}

  }

  abstract class ConstUniformValueAttribute[T](val name: String, v: => T) extends UniformValueAttribute {
    protected lazy val m_value = v

    def ===(y: ShaderVariableAttribute): Boolean = {
      assume(name==y.name) //TODO: remove

      if (name != y.name || !y.isInstanceOf[ConstUniformValueAttribute[_]]) {
        false
      } else {
        val yTyped = y.asInstanceOf[ConstUniformValueAttribute[_]]
        program == yTyped.program && m_value == yTyped.m_value
      }
    }

//    def apply(from: ShaderVariableAttribute): Unit ={
//      gl.uniform(uniformLocation) = m_value
//    }

    override def toString() = s"ConstUniformValueAttribute(${uniformLocation.name} @ ${uniformLocation.location}, ${m_value})"
  }


  class ConstIntUniformValueAttribute(name: String, v: Int) extends ConstUniformValueAttribute[Int](name, v){
    def apply(from: ShaderVariableAttribute): Unit ={
      gl.uniform(uniformLocation) = m_value
    }
  }


}