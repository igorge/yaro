package gie.sml.ut

import utest._
import gie.sml._
import gie.sml.ImplicitOps._

import scala.util.Random

object Matrix4FOps extends TestSuite {

  private def genM1() = Matrix4F(
    1,  2,  3,  4,
    5,  6,  7,  8,
    9,  10, 11, 12,
    13, 14, 15, 16)

  private def genM2() = Matrix4F(
    16, 15, 14, 13,
    12, 11, 10, 9,
    8,  7,  6,  5,
    4,  3,  2,  1)

  private def genRandomM() = Matrix4F(
    Random.nextFloat(), Random.nextFloat(), Random.nextFloat(), Random.nextFloat(),
    Random.nextFloat(), Random.nextFloat(), Random.nextFloat(), Random.nextFloat(),
    Random.nextFloat(),  Random.nextFloat(),  Random.nextFloat(),  Random.nextFloat(),
    Random.nextFloat(),  Random.nextFloat(),  Random.nextFloat(),  Random.nextFloat())

  def tests = TestSuite {

    'Compare{
      val m1 = Matrix4F(
        1,  2,  3,  4,
        5,  6,  7,  8,
        9,  10, 11, 12,
        13, 14, 15, 16)

      val m2 = Matrix4F(
        1,  2,  3,  4,
        5,  6,  7,  8,
        9,  10, 11, 12,
        13, 14, 15, 16)

      val m3 = Matrix4F(
        1,  2,  3,  4,
        5,  6,  7,  8,
        9,  10, 11, 12 +1,
        13, 14, 15, 16)

      assert( m1==m2, m2!=m3)

    }

    'Zero {
      assert(
        Matrix4F.zero() == new Matrix4F(
          0,  0,  0,  0,
          0,  0,  0,  0,
          0,  0,  0,  0,
          0,  0,  0,  0)
      )
    }

    'Identity {
      assert(
        Matrix4F.identity() == new Matrix4F(
            1,  0,  0,  0,
            0,  1,  0,  0,
            0,  0,  1,  0,
            0,  0,  0,  1)
      )
    }


    'Setters1 {
      val m1 = Matrix4F(
        1,  2,  3,  4,
        5,  6,  7,  8,
        9,  10, 11, 12,
        13, 14, 15, 16)

      val m2 = Matrix4F.zero()

      m2.m00 = 1
      m2.m01 = 2
      m2.m02 = 3
      m2.m03 = 4

      m2.m10 = 5
      m2.m11 = 6
      m2.m12 = 7
      m2.m13 = 8

      m2.m20 = 9
      m2.m21 = 10
      m2.m22 = 11
      m2.m23 = 12

      m2.m30 = 13
      m2.m31 = 14
      m2.m32 = 15
      m2.m33 = 16

      assert(m1 == m2)

    }

    'Setters2 {
      val m1 = Matrix4F(
        1,  2,  3,  4,
        5,  6,  7,  8,
        9,  10, 11, 12,
        13, 14, 15, 16)

      val m2 = Matrix4F.zero()

      m2.m00 = m1.m00
      m2.m01 = m1.m01
      m2.m02 = m1.m02
      m2.m03 = m1.m03

      m2.m10 = m1.m10
      m2.m11 = m1.m11
      m2.m12 = m1.m12
      m2.m13 = m1.m13

      m2.m20 = m1.m20
      m2.m21 = m1.m21
      m2.m22 = m1.m22
      m2.m23 = m1.m23

      m2.m30 = m1.m30
      m2.m31 = m1.m31
      m2.m32 = m1.m32
      m2.m33 = m1.m33

      assert(m1 == m2)

    }


    'Copy {
      val m1 = Matrix4F(
        1,  2,  3,  4,
        5,  6,  7,  8,
        9,  10, 11, 12,
        13, 14, 15, 16)

      val m2 = m1.copy()

      assert(m1==m2, m1 ne m2)


    }

    'Add {
      val m1 = Matrix4F(
        1,  2,  3,  4,
        5,  6,  7,  8,
        9,  10, 11, 12,
        13, 14, 15, 16)

      val m2 = Matrix4F(
        16, 15, 14, 13,
        12, 11, 10, 9,
        8,  7,  6,  5,
        4,  3,  2,  1)

      val m3 = Matrix4F(
        17,  17,  17,  17,
        17,  17,  17,  17,
        17,  17,  17,  17,
        17,  17,  17,  17)

      assert(m1+m2 == m3)
    }

    'Sub {
      val m1 = Matrix4F(
        1,  2,  3,  4,
        5,  6,  7,  8,
        9,  10, 11, 12,
        13, 14, 15, 16)

      val m2 = Matrix4F(
        16, 15, 14, 13,
        12, 11, 10, 9,
        8,  7,  6,  5,
        4,  3,  2,  1)

      val m3 = Matrix4F(
        -15, -13, -11, -9,
        -7,  -5,  -3,  -1,
         1,   3,   5,   7,
         9,   11,  13,  15)

      assert(m1-m2 == m3)
    }

    'Transpose {

      val m1 = Matrix4F(
        1,  2,  3,  4,
        5,  6,  7,  8,
        9,  10, 11, 12,
        13, 14, 15, 16)

      val m1Copy = m1.copy()

      val m1Transpose = m1.transpose()

      val m2Ref = Matrix4F(
        1,5,9,13,
        2,6,10,14,
        3,7,11,15,
        4,8,12,16)

      assert(m1Transpose==m2Ref, m1==m1Copy)

    }

    'TransposeSelf {

      val m1 = Matrix4F(
        1,  2,  3,  4,
        5,  6,  7,  8,
        9,  10, 11, 12,
        13, 14, 15, 16)

      val m2Ref = Matrix4F(
        1,5,9,13,
        2,6,10,14,
        3,7,11,15,
        4,8,12,16)

      m1.transpose_!()

      assert(m1==m2Ref)

    }

    'TransposeEq {

      val m1 = Matrix4F(
        1,  2,  3,  4,
        5,  6,  7,  8,
        9,  10, 11, 12,
        13, 14, 15, 16)

      val m1Copy = m1.copy()

      val m1Transpose = m1.transpose()

      val m2Ref = m1.copy()
      m2Ref.transpose_!()

      assert(m1Transpose==m2Ref, m1==m1Copy)

    }

    'Mul {
      val m1 = Matrix4F(
        1,  2,  3,  4,
        5,  6,  7,  8,
        9,  10, 11, 12,
        13, 14, 15, 16)

      val m2 = Matrix4F(
        16, 15, 14, 13,
        12, 11, 10, 9,
        8,  7,  6,  5,
        4,  3,  2,  1)

      val m3 = Matrix4F(
        80,70,60,50,
        240,214,188,162,
        400,358,316,274,
        560,502,444,386
      )

      val m1_times_m2 = m1*m2

      assert(m3 == m1_times_m2)

    }

    'MulInplace {
      val m1 = Matrix4F(
        1,  2,  3,  4,
        5,  6,  7,  8,
        9,  10, 11, 12,
        13, 14, 15, 16)

      val m2 = Matrix4F(
        16, 15, 14, 13,
        12, 11, 10, 9,
        8,  7,  6,  5,
        4,  3,  2,  1)

      val m3 = Matrix4F(
        80,70,60,50,
        240,214,188,162,
        400,358,316,274,
        560,502,444,386
      )

      m1.mul_!(m2)

      assert(m3 == m1)

    }


    'MulEqSeldMul {

      val m1 = Matrix4F(
        1,  2,  3,  4,
        5,  6,  7,  8,
        9,  10, 11, 12,
        13, 14, 15, 16)

      val m2 = Matrix4F(
        16, 15, 14, 13,
        12, 11, 10, 9,
        8,  7,  6,  5,
        4,  3,  2,  1)


      val m1_times_m2 = m1*m2

      val m1copy = m1.copy()

      m1copy.mul_!(m2)

      assert(m1copy == m1_times_m2)
    }


    'MulEqSeldMulRandom {

      val m1 = genRandomM()

      val m2 = genRandomM()

      val m1_times_m2 = m1*m2

      val m1copy = m1.copy()

      m1copy.mul_!(m2)

      assert(m1copy == m1_times_m2)
    }

    'RotXSelfvsRotGen {
      val angle = 15.0*math.Pi/180 toFloat
      val m1 = Matrix4F.zero()

      m1.rotX_!(angle)

      val m2 = Matrix4F.rotX(angle)

      assert(m1==m2)
    }

    'RotYSelfvsRotGen {
      val angle = 15.0*math.Pi/180 toFloat
      val m1 = Matrix4F.zero()

      m1.rotY_!(angle)

      val m2 = Matrix4F.rotY(angle)

      assert(m1==m2)
    }


    'RotZSelfvsRotGen {
      val angle = 15.0*math.Pi/180 toFloat
      val m1 = Matrix4F.zero()

      m1.rotZ_!(angle)

      val m2 = Matrix4F.rotZ(angle)

      assert(m1==m2)
    }


  }

}