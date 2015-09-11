package gie.gsg.state_attribute

class Texture() extends StateAttribute{
  val index = 2

  def ===(y: StateAttribute): Boolean=if (index!=y.index) false else {
    false
  }
}
