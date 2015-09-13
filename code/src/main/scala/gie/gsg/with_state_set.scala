package gie.gsg


trait WithStateSetComponent {
  this: StateSetComponent =>

  trait WithStateSet {
    def stateSet: StateSet
  }

  trait WithStateSetImpl {
    this: WithStateSet =>
    def stateSet: StateSet = null
  }

}