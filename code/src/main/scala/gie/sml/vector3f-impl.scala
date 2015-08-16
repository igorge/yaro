package gie.sml

final class Vector3F(private var m_v0: Float, private var m_v1: Float, private var m_v2: Float, private var m_v3: Float)
  extends Vector3FMutable
  with VectorRead3FImplIndexAccessViaMembers
  with VectorWrite3FImplIndexAccessViaMembers
{
  @inline final def v0: Float = m_v0
  @inline final def v1: Float = m_v1
  @inline final def v2: Float = m_v2
  @inline final def v3: Float = m_v3

  @inline final def v0_=(v: Float): Unit = m_v0 = v0
  @inline final def v1_=(v: Float): Unit = m_v1 = v1
  @inline final def v2_=(v: Float): Unit = m_v2 = v2
  @inline final def v3_=(v: Float): Unit = m_v3 = v3
}

object Vector3F {

  def apply(v0: Float, v1: Float, v2: Float, v3: Float) = {
    new Vector3F(v0, v1, v2, v3)
  }

  def apply(vec: VectorRead3F, v3: Float) = {
    new Vector3F(vec.v0, vec.v1, vec.v2, v3)
  }

  def apply(v0: Float, vec: VectorRead3F) = {
    new Vector3F(v0, vec.v0, vec.v1, vec.v2)
  }

}