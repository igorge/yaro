package gie.gsg

import gie.gsg.state_attribute.StateAttributeComponent

import scala.collection.mutable.ArrayBuffer

trait StateSetComponent {
  this: StateAttributeComponent=>

  class StateSet {
    val attributes = new ArrayBuffer[StateAttribute]()

    def size: Int = 0

    var paren: StateSet = null

  }

}

