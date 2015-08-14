package gie.geom

object square {
  def apply(w:Float, h: Float, unit: Float, z: Float = 0f) = impl_genBox(w,h,unit,z)

  private def impl_genBox(w:Float, h: Float, unit: Float, z: Float = 0f) = {
    assume(w>0)
    assume(h>0)

    val x = w/2*unit
    val y = h/2*unit

    val vertexCoord = Array(
      -x, -y, z,
      -x,  y, z,
      x,  y, z,

      -x, -y, z,
      x,  y, z,
      x, -y, z
    )
    val texCoord = Array(
      0f,  0f,
      0f,  1f,
      1f,  1f,

      0f,  0f,
      1f,  1f,
      1f,  0f
    )

    (vertexCoord, texCoord)
  }

}