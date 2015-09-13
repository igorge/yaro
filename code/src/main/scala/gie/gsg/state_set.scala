package gie.gsg

import gie.gsg.state_attribute.StateAttributeComponent
import gie.search.binarySearch

import scala.collection.Searching.{SearchResult, Found, InsertionPoint}
import scala.collection.generic
import scala.collection.mutable.ArrayBuffer
import scala.math.Ordering

trait StateSetComponent {
  this: StateAttributeComponent=>

  // Growable += simply appends at the end of array buffer with ordering enforcement
  // use insert() for set-like element insertion
  //
  class StateSet extends generic.Growable[StateAttribute] with collection.IndexedSeq[StateAttribute] {
    private val m_attributes = new ArrayBuffer[StateAttribute]()

    def clear() = m_attributes.clear()
    def +=(sa: StateAttribute): this.type={
      if(m_attributes.isEmpty || m_attributes(m_attributes.size-1).index<sa.index){
        m_attributes += sa
      } else {
        throw new IndexOutOfBoundsException(s"Cant insert StateAttribute with index == '${sa.index}' because it will violate ordering.")
      }

      this
    }

    def attributes: collection.IndexedSeq[StateAttribute] = m_attributes

    private def impl_DEBUG_isSorted(): Boolean ={
      attributes.foldLeft( (true, Int.MinValue) ){ (left, right)=>
        left match {
          case (true, value) => (value < right.index, right.index)
          case failed@(false, _) => failed
        }
      }._1
    }

    @inline def length: Int = m_attributes.size
    @inline def apply(idx: Int) = m_attributes(idx)

    def remove(key: Int): Unit= {
      m_attributes.indexWhere(_.index == key) match {
        case -1 =>
        case index => m_attributes.remove(index)
      }
    }

    def insert(s: StateAttribute): Unit ={
      binarySearch(s.index, m_attributes){ (key, attr)=> implicitly[Ordering[Int]].compare(key, attr.index) } match {
        case Found(index) => throw new Exception(s"StateSet already has attribute at index '${s.index}.")
        case InsertionPoint(index) => m_attributes.insert(index, s)
      }
    }


    def mergeCopyWithParent(ss: StateSet): StateSet ={ //this overrides ss

      assert( impl_DEBUG_isSorted() )
      assert( ss.impl_DEBUG_isSorted() )

      val newSS = new StateSet()

      gie.sorted_merge.merge(this, ss, newSS){
        (l:StateAttribute,r:StateAttribute)=>implicitly[Ordering[Int]].compare(l.index,r.index) }{
        (l,r)=>l }


      assert(newSS.impl_DEBUG_isSorted())

      newSS
    }

  }

}

