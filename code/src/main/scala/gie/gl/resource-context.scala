package gie.gl

trait ResourceContext {

  def registerResource(freeFun: =>Any): Unit
  def gcTick(): Unit
  def gcAll(): Unit

}