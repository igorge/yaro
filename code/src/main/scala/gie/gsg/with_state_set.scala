package gie.gsg


trait WithStateSetComponent {
  this: StateSetComponent =>

  trait WithStateSet {
    def stateSet: StateSet
    def stateSet_! : StateSet
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