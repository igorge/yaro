package gie.yaro

import gie.gl.{RichContext, ContextUnbind, Context}
import gie.gsg

import scala.reflect.ClassTag


trait RendererContextComponent {
  val renderer: gsg.Renderer[T] forSome {type T <: Context with ContextUnbind with RichContext}
}

