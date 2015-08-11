package gie.gl

class Exception(msg: String) extends scala.Exception( msg )
class ContextInitializationException(msg: String) extends Exception(msg)
class GlGetErrorException(errorCode: Int) extends Exception(s"GlGetErrorException(${errorCode})")
class ShaderException(msg: String) extends Exception(msg)
class ShaderCompilationException(msg: String) extends ShaderException(msg)
class ProgramException(msg: String) extends Exception(msg)
class VertexAttributeBindingException(msg: String) extends ProgramException(msg)
class UniformNotFound(uniform: String) extends ProgramException(s"Uniform '${uniform}' not found")


