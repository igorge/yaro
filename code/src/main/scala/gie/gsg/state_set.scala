package gie.gsg

import gie.gsg.state_attribute.StateAttributeComponent
import gie.search.binarySearch

import scala.collection.Searching.{SearchResult, Found, InsertionPoint}
import scala.collection.mutable.ArrayBuffer
import scala.math.Ordering

trait StateSetComponent {
  this: StateAttributeComponent=>

  class StateSet {
    private val m_attributes = new ArrayBuffer[StateAttribute]()

    def attributes: collection.IndexedSeq[StateAttribute] = m_attributes

    @inline def size: Int = m_attributes.size
    @inline def apply(idx: Int) = m_attributes(idx)

    def remove(key: Int): Unit= {
      m_attributes.indexWhere(_.index == key) match {
        case -1 =>
        case index => m_attributes.remove(index)
      }
    }

    def add(s: StateAttribute): Unit ={
      binarySearch(s.index, m_attributes){ (key, attr)=> implicitly[Ordering[Int]].compare(key, attr.index) } match {
        case Found(index) => throw new Exception(s"StateSet already has attribute at index '${s.index}.")
        case InsertionPoint(index) => m_attributes.insert(index, s)
      }
    }
  }

}

