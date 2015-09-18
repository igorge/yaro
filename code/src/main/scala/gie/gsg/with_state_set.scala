package gie.gsg

import gie.gsg.state_attribute.StateAttributeComponent

trait WithStateSetComponent {
  this: StateSetComponent with StateAttributeComponent =>

  trait WithStateSet {
    def stateSet: StateSet
    def stateSet_! : StateSet
    def addAttribute(attr: StateAttribute): this.type={
      stateSet_!.insert(attr)
      this
    }
  }

  trait WithStateSetImpl {
    this: WithStateSet =>

    private var m_stateSet:StateSet = null

    def stateSet: StateSet = m_stateSet
    def stateSet_! : StateSet = {
      if(m_stateSet eq null) { m_stateSet = new StateSet() }

      m_stateSet
    }
  }

}