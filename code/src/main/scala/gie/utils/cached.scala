package gie.utils

import scala.concurrent.{ExecutionContext, Future}

private class CachedNoParentException extends Exception()

trait CachedLike[K,V]{
  def get(key: K)(f: =>V)(implicit executor: ExecutionContext): V
  def clear(): Unit
}

class Cached[K,V]() extends CachedLike[K,V]{
  private val m_cache = new collection.mutable.HashMap[K, V]
  private val m_monitor = m_cache

  def get(key: K)(f: =>V)(implicit executor: ExecutionContext): V = m_monitor.synchronized{
    m_cache.getOrElseUpdate(key, f)
  }

  def clear(): Unit =m_monitor.synchronized{
    m_cache.clear()
  }

}