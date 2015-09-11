package gie.gsg.state_attribute

class GlProgram() extends StateAttribute {
  val index = 0

  def ===(y: StateAttribute): Boolean=if (index!=y.index) false else {
    false
  }
}

