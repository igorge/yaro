package gie.gl

trait Nullable[T]{
  def isNull(x: T): Boolean
  def nullValue: T
}