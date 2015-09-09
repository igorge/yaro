package gie.gsg

trait WithStateSet {
  def stateSet: StateSet
}

trait WithStateSetImpl { this: WithStateSet =>
  def stateSet: StateSet = null
}