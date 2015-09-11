package gie.gsg.state_attribute

trait StateAttribute {
  val index: Int

  def ===(y: StateAttribute): Boolean
}

object StateAttribute {
  val attributesCount = 3
}

