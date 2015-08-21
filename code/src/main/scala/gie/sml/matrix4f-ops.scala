package gie.sml

import scala.annotation.switch


trait Matrix4FThisReadOps { this: MatrixRead4F =>

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

trait Matrix4FThisWriteOps extends  Matrix4FThisReadOps { this: MatrixWrite4F =>
  @inline final def row0_=(vec: VectorRead4F): Unit ={
    m00 = vec.v0
    m01 = vec.v1
    m02 = vec.v2
    m03 = vec.v3
  }
  @inline final def row1_=(vec: VectorRead4F): Unit ={
    m10 = vec.v0
    m11 = vec.v1
    m12 = vec.v2
    m13 = vec.v3
  }
  @inline final def row2_=(vec: VectorRead4F): Unit ={
    m20 = vec.v0
    m21 = vec.v1
    m22 = vec.v2
    m23 = vec.v3
  }
  @inline final def row3_=(vec: VectorRead4F): Unit ={
    m30 = vec.v0
    m31 = vec.v1
    m32 = vec.v2
    m33 = vec.v3
  }


  @inline final def updateRow(index: Int, vec: VectorRead4F): Unit = (index: @switch) match {
    case 0 => row0 = vec
    case 1 => row1 = vec
    case 2 => row2 = vec
    case 3 => row3 = vec
  }

  @inline final def mul_!(m2: MatrixRead4F): Unit= {

    val tmp00= m00 * m2.m00  +  m01 * m2.m10  +  m02 * m2.m20  +  m03 * m2.m30
    val tmp01= m00 * m2.m01  +  m01 * m2.m11  +  m02 * m2.m21  +  m03 * m2.m31
    val tmp02= m00 * m2.m02  +  m01 * m2.m12  +  m02 * m2.m22  +  m03 * m2.m32
    val tmp03= m00 * m2.m03  +  m01 * m2.m13  +  m02 * m2.m23  +  m03 * m2.m33

    val tmp10= m10 * m2.m00  +  m11 * m2.m10  +  m12 * m2.m20  +  m13 * m2.m30
    val tmp11= m10 * m2.m01  +  m11 * m2.m11  +  m12 * m2.m21  +  m13 * m2.m31
    val tmp12= m10 * m2.m02  +  m11 * m2.m12  +  m12 * m2.m22  +  m13 * m2.m32
    val tmp13= m10 * m2.m03  +  m11 * m2.m13  +  m12 * m2.m23  +  m13 * m2.m33

    val tmp20= m20 * m2.m00  +  m21 * m2.m10  +  m22 * m2.m20  +  m23 * m2.m30
    val tmp21= m20 * m2.m01  +  m21 * m2.m11  +  m22 * m2.m21  +  m23 * m2.m31
    val tmp22= m20 * m2.m02  +  m21 * m2.m12  +  m22 * m2.m22  +  m23 * m2.m32
    val tmp23= m20 * m2.m03  +  m21 * m2.m13  +  m22 * m2.m23  +  m23 * m2.m33

    val tmp30= m30 * m2.m00  +  m31 * m2.m10  +  m32 * m2.m20  +  m33 * m2.m30
    val tmp31= m30 * m2.m01  +  m31 * m2.m11  +  m32 * m2.m21  +  m33 * m2.m31
    val tmp32= m30 * m2.m02  +  m31 * m2.m12  +  m32 * m2.m22  +  m33 * m2.m32
    val tmp33= m30 * m2.m03  +  m31 * m2.m13  +  m32 * m2.m23  +  m33 * m2.m33

    m00 = tmp00
    m01 = tmp01
    m02 = tmp02
    m03 = tmp03

    m10 = tmp10
    m11 = tmp11
    m12 = tmp12
    m13 = tmp13

    m20 = tmp20
    m21 = tmp21
    m22 = tmp22
    m23 = tmp23

    m30 = tmp30
    m31 = tmp31
    m32 = tmp32
    m33 = tmp33
  }


}


trait Matrix4FOps {

  @inline final def mul(): Unit ={

  }


}