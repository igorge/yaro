package gie.sml

final class Vector3F(private var m_v0: Float, private var m_v1: Float, private var m_v2: Float)
  extends Vector3FMutable
  with VectorRead3FImplIndexAccessViaMembers
  with VectorWrite3FImplIndexAccessViaMembers
{
  @inline final def v0: Float = m_v0
  @inline final def v1: Float = m_v1
  @inline final def v2: Float = m_v2

  @inline final def v0_=(v: Float): Unit = m_v0 = v0
  @inline final def v1_=(v: Float): Unit = m_v1 = v1
  @inline final def v2_=(v: Float): Unit = m_v2 = v2

  override def toString = s"Vector3F[${v0}, ${v1}, ${v2}]"
}

object Vector3F {

  def apply(v0: Float, v1: Float, v2: Float) = {
    new Vector3F(v0, v1, v2)
  }

  def apply(seqv: Seq[Float]): Vector3F ={
    assume(seqv.size==3)
    this.apply(seqv(0), seqv(1), seqv(2))
  }

}