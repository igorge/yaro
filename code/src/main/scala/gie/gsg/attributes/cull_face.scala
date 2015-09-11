package gie.gsg.state_attribute

class CullFace()  extends StateAttribute {
  val index = 1

  def ===(y: StateAttribute): Boolean=if (index!=y.index) false else {
    false
  }
}


