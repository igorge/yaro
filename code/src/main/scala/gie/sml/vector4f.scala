package gie.sml

import scala.annotation.switch

trait VectorRead4F {
  def v0: Float
  def v1: Float
  def v2: Float
  def v3: Float
  def apply(index: Int): Float
}

trait VectorWrite4F extends VectorRead4F {
  def v0_=(v: Float): Unit
  def v1_=(v: Float): Unit
  def v2_=(v: Float): Unit
  def v3_=(v: Float): Unit
  def update(index: Int, value: Float): Unit
}

trait VectorRead4FImplViaIndexAccess { this: VectorRead4F=>
  @inline final def v0: Float = this.apply(0)
  @inline final def v1: Float = this.apply(1)
  @inline final def v2: Float = this.apply(2)
  @inline final def v3: Float = this.apply(3)
}

trait VectorWrite4FImplViaIndexAccess { this: VectorWrite4F=>
  @inline final def v0_=(v: Float): Unit = this.update(0, v)
  @inline final def v1_=(v: Float): Unit = this.update(1, v)
  @inline final def v2_=(v: Float): Unit = this.update(2, v)
  @inline final def v3_=(v: Float): Unit = this.update(3, v)
}

trait VectorRead4FImplIndexAccessViaMembers { this: VectorRead4F =>
  @inline final def apply(index: Int): Float = (index: @switch) match {
    case 0 => v0
    case 1 => v1
    case 2 => v2
    case 3 => v3
  }
}

trait VectorWrite4FImplIndexAccessViaMembers { this: VectorWrite4F =>
  @inline final def update(index: Int, value: Float): Unit = (index: @switch) match {
    case 0 => v0 = value
    case 1 => v1 = value
    case 2 => v2 = value
    case 3 => v3 = value
  }
}


trait Vector4FMutable extends VectorWrite4F
