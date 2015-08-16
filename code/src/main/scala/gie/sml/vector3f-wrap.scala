package gie.sml

import scala.collection.mutable

class VectorRead3FWrap(val buffer: IndexedSeq[Float]) extends VectorRead3F
  with VectorRead3FImplIndexAccess
{

  @inline final def apply(index: Int): Float ={
    buffer(index)
  }

}

class VectorWrite3FWrap(val buffer: mutable.IndexedSeq[Float]) extends Vector3FMutable
  with VectorRead3FImplIndexAccess
  with VectorWrite3FImplIndexAccess
{

  @inline final def apply(index: Int): Float ={
    buffer(index)
  }

  @inline final def update(index: Int, value: Float): Unit={
     buffer(index) = value
  }

}