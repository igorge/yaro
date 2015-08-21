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
