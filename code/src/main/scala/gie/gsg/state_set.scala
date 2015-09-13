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

    private def impl_DEBUG_isSorted(): Boolean ={
      attributes.foldLeft( (true, Int.MinValue) ){ (left, right)=>
        left match {
          case (true, value) => (value < right.index, right.index)
          case failed@(false, _) => failed
        }
      }._1
    }

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


    def mergeCopyWithParent(ss: StateSet): StateSet ={ //this overrides ss

      assert( impl_DEBUG_isSorted() )
      assert( ss.impl_DEBUG_isSorted() )

      val mySize = size
      val ssSize = ss.size
      val newSS = new StateSet()

      var i = 0
      var ss_i = 0

      while (i!=mySize && ss_i!=ssSize){
        if (m_attributes(i).index==ss.m_attributes(ss_i).index) {
          newSS.m_attributes += m_attributes(i)
          i+=1
          ss_i+=1
        } else if (m_attributes(i).index < ss.m_attributes(ss_i).index) {
          newSS.m_attributes += m_attributes(i)
          i+=1
        } else { // (m_attributes(i).index > ss.m_attributes(ss_i).index)
          newSS.m_attributes += ss.m_attributes(ss_i)
          ss_i+=1
        }

        // copy tail
        if(i!=mySize){
          assume(ss_i==ssSize)
          while(i!=mySize){
            newSS.m_attributes += m_attributes(i)
            i+=1
          }
        } else { //i==mySize
          assume(i==mySize)
          assume(ss_i!=ssSize)
          while(ss_i!=ssSize){
            newSS.m_attributes += ss.m_attributes(ss_i)
            ss_i+=1
          }
        }

      }

      assert(newSS.impl_DEBUG_isSorted())

      newSS
    }

  }

}

