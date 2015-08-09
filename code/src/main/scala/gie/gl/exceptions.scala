package gie.gl

class Exception(msg: String) extends scala.Exception( msg )
class ContextInitializationException(msg: String) extends Exception(msg)
class GlGetErrorException(errorCode: Int) extends Exception(s"GlGetErrorException(${errorCode})")
class ShaderException(msg: String) extends Exception(msg)
class ProgramException(msg: String) extends Exception(msg)
class UniformNotFound(uniform: String) extends ProgramException(s"Uniform '${uniform}' not found")


