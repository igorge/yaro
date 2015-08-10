package gie.gl

trait ResourceHandle[T]{
  def isNull(x: T): Boolean
  def nullValue: T
  def create(): T
  def free(x: T): Unit
}