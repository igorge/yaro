package gie.gl

import gie.sml._


trait SML_Matrix4F { this: Context=>

    @inline final def uniformMatrix4fv(location: GLUniformLocation, transpose: Boolean, m: MatrixRead4F): Unit={

      val v = if(transpose) Array(
        m.m00, m.m01, m.m02, m.m03,
        m.m10, m.m11, m.m12, m.m13,
        m.m20, m.m21, m.m22, m.m23,
        m.m30, m.m31, m.m32, m.m33
      ) else  Array( //column major
        m.m00, m.m10, m.m20, m.m30,
        m.m01, m.m11, m.m21, m.m31,
        m.m02, m.m12, m.m22, m.m32,
        m.m03, m.m13, m.m23, m.m33
      )

      this.uniformMatrix4fv(location, false, v)
    }


}

trait SML_Matrix4FRich extends SML_Matrix4F {
  this: Context with RichContext =>


  @inline final def setUniformMatrix(location: UniformTrait, m: MatrixRead4F, transpose: Boolean = false): Unit={
    uniformMatrix4fv(location.get, transpose, m)
  }

  object uniformMatrix {
    @inline final def update(location: UniformTrait, m: MatrixRead4F) = setUniformMatrix(location, m)
  }


}
