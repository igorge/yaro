//package gie.gl
//
//import simplex3d.math.float._
//import simplex3d.math.float.functions._
//
//
//trait Simplex3D { this: Context=>
//
//  @inline final def uniformMatrix4fv(location: GLUniformLocation, transpose: Boolean, m: ReadMat4): Unit={
//
//    val v = if(transpose) Array(
//      m.m00, m.m01, m.m02, m.m03,
//      m.m10, m.m11, m.m12, m.m13,
//      m.m20, m.m21, m.m22, m.m23,
//      m.m30, m.m31, m.m32, m.m33
//    ) else  Array( //column major
//      m.m00, m.m10, m.m20, m.m30,
//      m.m01, m.m11, m.m21, m.m31,
//      m.m02, m.m12, m.m22, m.m32,
//      m.m03, m.m13, m.m23, m.m33
//    )
//
//    this.uniformMatrix4fv(location, false, v)
//  }
//
//
//}