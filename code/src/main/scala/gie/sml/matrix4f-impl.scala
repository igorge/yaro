package gie.sml

class Matrix4F (
  var m_m00: Float, var m_m01: Float, var m_m02: Float, var m_m03: Float,
  var m_m10: Float, var m_m11: Float, var m_m12: Float, var m_m13: Float,
  var m_m20: Float, var m_m21: Float, var m_m22: Float, var m_m23: Float,
  var m_m30: Float, var m_m31: Float, var m_m32: Float, var m_m33: Float)

  extends MatrixWrite4F
{

  //first line
  @inline final def m00: Float = m_m00
  @inline final def m01: Float = m_m01
  @inline final def m02: Float = m_m02
  @inline final def m03: Float = m_m03

  //second line
  @inline final def m10: Float = m_m10
  @inline final def m11: Float = m_m11
  @inline final def m12: Float = m_m12
  @inline final def m13: Float = m_m13

  //third line
  @inline final def m20: Float = m_m20
  @inline final def m21: Float = m_m21
  @inline final def m22: Float = m_m22
  @inline final def m23: Float = m_m23

  //forth line
  @inline final def m30: Float = m_m30
  @inline final def m31: Float = m_m31
  @inline final def m32: Float = m_m32
  @inline final def m33: Float = m_m33

  //first line
  @inline final def m00_=(x: Float): Unit = m_m00 = x
  @inline final def m01_=(x: Float): Unit = m_m01 = x
  @inline final def m02_=(x: Float): Unit = m_m02 = x
  @inline final def m03_=(x: Float): Unit = m_m03 = x

  //second line
  @inline final def m10_=(x: Float): Unit = m_m10 = x
  @inline final def m11_=(x: Float): Unit = m_m11 = x
  @inline final def m12_=(x: Float): Unit = m_m12 = x
  @inline final def m13_=(x: Float): Unit = m_m13 = x

  //third line
  @inline final def m20_=(x: Float): Unit = m_m20 = x
  @inline final def m21_=(x: Float): Unit = m_m21 = x
  @inline final def m22_=(x: Float): Unit = m_m22 = x
  @inline final def m23_=(x: Float): Unit = m_m23 = x

  //forth line
  @inline final def m30_=(x: Float): Unit = m_m30 = x
  @inline final def m31_=(x: Float): Unit = m_m31 = x
  @inline final def m32_=(x: Float): Unit = m_m32 = x
  @inline final def m33_=(x: Float): Unit = m_m33 = x
  
}

object Matrix4F {
  
  @inline def apply(
    m00: Float, m01: Float, m02: Float, m03: Float,
    m10: Float, m11: Float, m12: Float, m13: Float,
    m20: Float, m21: Float, m22: Float, m23: Float,
    m30: Float, m31: Float, m32: Float, m33: Float): Matrix4F= 
  {
    new Matrix4F(
      m00, m01, m02, m03,
      m10, m11, m12, m13,
      m20, m21, m22, m23,
      m30, m31, m32, m33)
  }

  @inline def identity() = this.apply(
    1,  0,  0,  0,
    0,  1,  0,  0,
    0,  0,  1,  0,
    0,  0,  0,  1
  )

  @inline def zero() = this.apply(
    0,  0,  0,  0,
    0,  0,  0,  0,
    0,  0,  0,  0,
    0,  0,  0,  0
  )

}