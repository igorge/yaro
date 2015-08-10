package gie.gl

trait WebGlResourceContext extends ResourceContext {

  def registerResource(freeFun: =>Any): Unit = {/*do nothing*/}
  def gcTick(): Unit = {/*do nothing*/}
  def gcAll(): Unit = {/*do nothing*/}

}