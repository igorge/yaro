package gie.gl

final class BoxedVar[T](var x:T){
  @inline def apply() = x

  @inline def update(newX: T) = {
    x = newX
    x
  }
}

object BoxedVar {
  @inline final def apply[T](x: T): BoxedVar[T] = new BoxedVar[T](x)
}
