package gie.sml

trait MatrixRead4F {

  //first line
  def m00: Float
  def m01: Float
  def m02: Float
  def m03: Float

  //second line
  def m10: Float
  def m11: Float
  def m12: Float
  def m13: Float

  //third line
  def m20: Float
  def m21: Float
  def m22: Float
  def m23: Float

  //forth line
  def m30: Float
  def m31: Float
  def m32: Float
  def m33: Float

  override def equals(other: Any): Boolean = other match {
    case m2:MatrixRead4F =>
      m00 == m2.m00 && m01 == m2.m01 && m02 == m2.m02 && m03 == m2.m03 &&
      m10 == m2.m10 && m11 == m2.m11 && m12 == m2.m12 && m13 == m2.m13 &&
      m20 == m2.m20 && m21 == m2.m21 && m22 == m2.m22 && m23 == m2.m23 &&
      m30 == m2.m30 && m31 == m2.m31 && m32 == m2.m32 && m33 == m2.m33
    case _ => false
  }

  override def toString():String = {
    s"[${m00}, ${m01}, ${m02}, ${m03}], [${m10}, ${m11}, ${m12}, ${m13}], [${m20}, ${m21}, ${m22}, ${m23}], [${m30}, ${m31}, ${m32}, ${m33}]"
  }

}

trait MatrixWrite4F extends MatrixRead4F{

  //first line
  def m00_=(x: Float): Unit
  def m01_=(x: Float): Unit
  def m02_=(x: Float): Unit
  def m03_=(x: Float): Unit

  //second line
  def m10_=(x: Float): Unit
  def m11_=(x: Float): Unit
  def m12_=(x: Float): Unit
  def m13_=(x: Float): Unit

  //third line
  def m20_=(x: Float): Unit
  def m21_=(x: Float): Unit
  def m22_=(x: Float): Unit
  def m23_=(x: Float): Unit

  //forth line
  def m30_=(x: Float): Unit
  def m31_=(x: Float): Unit
  def m32_=(x: Float): Unit
  def m33_=(x: Float): Unit

}
