package gie.sml

import scala.annotation.switch


trait VectorRead3F {
  def v0: Float
  def v1: Float
  def v2: Float
  def apply(index: Int): Float
}

trait VectorWrite3F extends VectorRead3F {
  def v0_=(v: Float): Unit
  def v1_=(v: Float): Unit
  def v2_=(v: Float): Unit
  def update(index: Int, value: Float): Unit
}

trait VectorRead3FImplIndexAccess { this: VectorRead3F=>
  @inline final def v0: Float = this.apply(0)
  @inline final def v1: Float = this.apply(1)
  @inline final def v2: Float = this.apply(2)
}

trait VectorWrite3FImplIndexAccess { this: VectorWrite3F=>
  @inline final def v0_=(v: Float): Unit = this.update(0, v)
  @inline final def v1_=(v: Float): Unit = this.update(1, v)
  @inline final def v2_=(v: Float): Unit = this.update(2, v)
}

trait VectorRead3FImplIndexAccessViaMembers { this: VectorRead3F =>
  @inline final def apply(index: Int): Float = (index: @switch) match {
    case 0 => v0
    case 1 => v1
    case 2 => v2
  }
}

trait VectorWrite3FImplIndexAccessViaMembers { this: VectorWrite3F =>
  @inline final def update(index: Int, value: Float): Unit = (index: @switch) match {
    case 0 => v0 = value
    case 1 => v1 = value
    case 2 => v2 = value
  }
}


trait Vector3FMutable extends VectorWrite3F