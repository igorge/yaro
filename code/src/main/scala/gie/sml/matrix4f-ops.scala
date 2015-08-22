package gie.sml

import scala.annotation.switch


trait Matrix4FThisReadOps { this: MatrixRead4F =>

  @inline final def copy()= Matrix4F(
    m00, m01, m02, m03,
    m10, m11, m12, m13,
    m20, m21, m22, m23,
    m30, m31, m32, m33)

  @inline final def row0: Vector4F = Vector4F(m00, m01, m02, m03)
  @inline final def row1: Vector4F = Vector4F(m10, m11, m12, m13)
  @inline final def row2: Vector4F = Vector4F(m20, m21, m22, m23)
  @inline final def row3: Vector4F = Vector4F(m30, m31, m32, m33)

  @inline final def row(index: Int): Vector4F = (index: @switch) match {
    case 0 => row0
    case 1 => row1
    case 2 => row2
    case 3 => row3
  }

  @inline final def +(m2: MatrixRead4F): Matrix4F = {
    Matrix4F(
      m00+m2.m00, m01+m2.m01, m02+m2.m02, m03+m2.m03,
      m10+m2.m10, m11+m2.m11, m12+m2.m12, m13+m2.m13,
      m20+m2.m20, m21+m2.m21, m22+m2.m22, m23+m2.m23,
      m30+m2.m30, m31+m2.m31, m32+m2.m32, m33+m2.m33)
  }

  @inline final def -(m2: MatrixRead4F): Matrix4F = {
    Matrix4F(
      m00-m2.m00, m01-m2.m01, m02-m2.m02, m03-m2.m03,
      m10-m2.m10, m11-m2.m11, m12-m2.m12, m13-m2.m13,
      m20-m2.m20, m21-m2.m21, m22-m2.m22, m23-m2.m23,
      m30-m2.m30, m31-m2.m31, m32-m2.m32, m33-m2.m33)
  }

  @inline final def *(m2: MatrixRead4F): Matrix4F = {
    Matrix4F(
      /*00*/ m00 * m2.m00  +  m01 * m2.m10  +  m02 * m2.m20  +  m03 * m2.m30,
      /*01*/ m00 * m2.m01  +  m01 * m2.m11  +  m02 * m2.m21  +  m03 * m2.m31,
      /*02*/ m00 * m2.m02  +  m01 * m2.m12  +  m02 * m2.m22  +  m03 * m2.m32,
      /*03*/ m00 * m2.m03  +  m01 * m2.m13  +  m02 * m2.m23  +  m03 * m2.m33,

      /*10*/ m10 * m2.m00  +  m11 * m2.m10  +  m12 * m2.m20  +  m13 * m2.m30,
      /*11*/ m10 * m2.m01  +  m11 * m2.m11  +  m12 * m2.m21  +  m13 * m2.m31,
      /*12*/ m10 * m2.m02  +  m11 * m2.m12  +  m12 * m2.m22  +  m13 * m2.m32,
      /*13*/ m10 * m2.m03  +  m11 * m2.m13  +  m12 * m2.m23  +  m13 * m2.m33,

      /*20*/ m20 * m2.m00  +  m21 * m2.m10  +  m22 * m2.m20  +  m23 * m2.m30,
      /*21*/ m20 * m2.m01  +  m21 * m2.m11  +  m22 * m2.m21  +  m23 * m2.m31,
      /*22*/ m20 * m2.m02  +  m21 * m2.m12  +  m22 * m2.m22  +  m23 * m2.m32,
      /*23*/ m20 * m2.m03  +  m21 * m2.m13  +  m22 * m2.m23  +  m23 * m2.m33,

      /*30*/ m30 * m2.m00  +  m31 * m2.m10  +  m32 * m2.m20  +  m33 * m2.m30,
      /*31*/ m30 * m2.m01  +  m31 * m2.m11  +  m32 * m2.m21  +  m33 * m2.m31,
      /*32*/ m30 * m2.m02  +  m31 * m2.m12  +  m32 * m2.m22  +  m33 * m2.m32,
      /*33*/ m30 * m2.m03  +  m31 * m2.m13  +  m32 * m2.m23  +  m33 * m2.m33 )
  }
  
}


object ImplicitOps {

  implicit class Matrix4FThisWriteOps(val m: MatrixWrite4F) extends AnyVal {

    @inline final def mul_!(m2: MatrixRead4F): Unit = {

      val tmp00 = m.m00 * m2.m00 + m.m01 * m2.m10 + m.m02 * m2.m20 + m.m03 * m2.m30
      val tmp01 = m.m00 * m2.m01 + m.m01 * m2.m11 + m.m02 * m2.m21 + m.m03 * m2.m31
      val tmp02 = m.m00 * m2.m02 + m.m01 * m2.m12 + m.m02 * m2.m22 + m.m03 * m2.m32
      val tmp03 = m.m00 * m2.m03 + m.m01 * m2.m13 + m.m02 * m2.m23 + m.m03 * m2.m33

      val tmp10 = m.m10 * m2.m00 + m.m11 * m2.m10 + m.m12 * m2.m20 + m.m13 * m2.m30
      val tmp11 = m.m10 * m2.m01 + m.m11 * m2.m11 + m.m12 * m2.m21 + m.m13 * m2.m31
      val tmp12 = m.m10 * m2.m02 + m.m11 * m2.m12 + m.m12 * m2.m22 + m.m13 * m2.m32
      val tmp13 = m.m10 * m2.m03 + m.m11 * m2.m13 + m.m12 * m2.m23 + m.m13 * m2.m33

      val tmp20 = m.m20 * m2.m00 + m.m21 * m2.m10 + m.m22 * m2.m20 + m.m23 * m2.m30
      val tmp21 = m.m20 * m2.m01 + m.m21 * m2.m11 + m.m22 * m2.m21 + m.m23 * m2.m31
      val tmp22 = m.m20 * m2.m02 + m.m21 * m2.m12 + m.m22 * m2.m22 + m.m23 * m2.m32
      val tmp23 = m.m20 * m2.m03 + m.m21 * m2.m13 + m.m22 * m2.m23 + m.m23 * m2.m33

      val tmp30 = m.m30 * m2.m00 + m.m31 * m2.m10 + m.m32 * m2.m20 + m.m33 * m2.m30
      val tmp31 = m.m30 * m2.m01 + m.m31 * m2.m11 + m.m32 * m2.m21 + m.m33 * m2.m31
      val tmp32 = m.m30 * m2.m02 + m.m31 * m2.m12 + m.m32 * m2.m22 + m.m33 * m2.m32
      val tmp33 = m.m30 * m2.m03 + m.m31 * m2.m13 + m.m32 * m2.m23 + m.m33 * m2.m33

      m.m00 = tmp00
      m.m01 = tmp01
      m.m02 = tmp02
      m.m03 = tmp03

      m.m10 = tmp10
      m.m11 = tmp11
      m.m12 = tmp12
      m.m13 = tmp13

      m.m20 = tmp20
      m.m21 = tmp21
      m.m22 = tmp22
      m.m23 = tmp23

      m.m30 = tmp30
      m.m31 = tmp31
      m.m32 = tmp32
      m.m33 = tmp33
    }

    @inline final def rotX_!(angle: Float): Unit = {
      val c: Float = Math.cos(angle).asInstanceOf[Float]
      val s: Float = Math.sin(angle).asInstanceOf[Float]

      m.m00 = 1.0f
      m.m01 = 0.0f
      m.m02 = 0.0f
      m.m03 = 0.0f

      m.m10 = 0.0f
      m.m11 = c
      m.m12 = -s
      m.m13 = 0.0f

      m.m20 = 0.0f
      m.m21 = s
      m.m22 = c
      m.m23 = 0.0f

      m.m30 = 0.0f
      m.m31 = 0.0f
      m.m32 = 0.0f
      m.m33 = 1.0f
    }

    def rotY_!(angle: Float): Unit = {
      val c: Float = Math.cos(angle).asInstanceOf[Float]
      val s: Float = Math.sin(angle).asInstanceOf[Float]

      m.m00 = c
      m.m01 = 0.0f
      m.m02 = s
      m.m03 = 0.0f

      m.m10 = 0.0f
      m.m11 = 1.0f
      m.m12 = 0.0f
      m.m13 = 0.0f

      m.m20 = -s
      m.m21 = 0.0f
      m.m22 = c
      m.m23 = 0.0f

      m.m30 = 0.0f
      m.m31 = 0.0f
      m.m32 = 0.0f
      m.m33 = 1.0f
    }

    @inline final def rotZ_!(angle: Float): Unit = {
      val c: Float = Math.cos(angle).asInstanceOf[Float]
      val s: Float = Math.sin(angle).asInstanceOf[Float]

      m.m00 = c
      m.m01 = -s
      m.m02 = 0.0f
      m.m03 = 0.0f

      m.m10 = s
      m.m11 = c
      m.m12 = 0.0f
      m.m13 = 0.0f

      m.m20 = 0.0f
      m.m21 = 0.0f
      m.m22 = 1.0f
      m.m23 = 0.0f

      m.m30 = 0.0f
      m.m31 = 0.0f
      m.m32 = 0.0f
      m.m33 = 1.0f
    }
  }

}


