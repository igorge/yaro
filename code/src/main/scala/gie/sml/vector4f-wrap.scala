package gie.sml


class VectorRead4FWrap(val buffer: Array[Float]) extends VectorRead4F
  with VectorRead4FImplViaIndexAccess
{

  @inline final def apply(index: Int): Float ={
    buffer(index)
  }

}

class VectorWrite4FWrap(val buffer: Array[Float]) extends Vector4FMutable
  with VectorRead4FImplViaIndexAccess
  with VectorWrite4FImplViaIndexAccess
{

  @inline final def apply(index: Int): Float ={
    buffer(index)
  }

  @inline final def update(index: Int, value: Float): Unit={
     buffer(index) = value
  }

}