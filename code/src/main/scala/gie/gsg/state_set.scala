package gie.gsg

import gie.gsg.state_attribute.{UniformLocationComponent, StateAttributeComponent}
import gie.search.binarySearch

import scala.collection.Searching.{SearchResult, Found, InsertionPoint}
import scala.collection.generic
import scala.collection.mutable.ArrayBuffer
import scala.math.Ordering

trait StateSetComponent {
  this: StateAttributeComponent with UniformLocationComponent =>

  // Growable += simply appends at the end of array buffer with ordering enforcement
  // use insert() for set-like element insertion
  //
  class StateSet {
    private[gsg] val m_attributes = new ArrayBuffer[StateAttribute]()
    private var m_uniforms:ArrayBuffer[UniformValueAttribute] = null

    private def impl_allocUniforms() = {
      m_uniforms = new ArrayBuffer[UniformValueAttribute]()
      m_uniforms
    }

    @inline
    private [gsg] def uniforms_! = if(m_uniforms eq null) impl_allocUniforms() else m_uniforms
    @inline
    private [gsg] def uniforms = m_uniforms



    def clear(){
      m_attributes.clear()
      Option(m_uniforms) foreach (_.clear())
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

    def removeAttribute(key: Int): this.type={
      m_attributes.indexWhere(_.index == key) match {
        case -1 =>
        case index => m_attributes.remove(index)
      }
      this
    }

    def addAttribute(s: StateAttribute): this.type={
      binarySearch(s.index, m_attributes){ (key, attr)=> implicitly[Ordering[Int]].compare(key, attr.index) } match {
        case Found(index) => throw new Exception(s"StateSet already has attribute at index '${s.index}.")
        case InsertionPoint(index) => m_attributes.insert(index, s)
      }
      this
    }

    def addUniformValue(u: UniformValueAttribute): this.type={
      if(m_uniforms eq null){
        impl_allocUniforms() += u
      } else {
        binarySearch(u.name, m_uniforms){ (key, uniform)=> implicitly[Ordering[String]].compare(key, uniform.name) } match {
          case Found(index) => throw new Exception(s"StateSet already has uniform with name '${u.name}.")
          case InsertionPoint(index) => m_uniforms.insert(index, u)
        }
      }
      this
    }

    def mergeCopyWithParent(ss: StateSet): StateSet = { //this overrides ss

      val newSS = new StateSet()

      impl_mergeCopyWithParent_attributes(newSS, ss)
      impl_mergeCopyWithParent_uniforms(newSS, ss)

      newSS
    }

    private def impl_mergeCopyWithParent_uniforms(newSS: StateSet, parentSS: StateSet): Unit={

      if(m_uniforms eq null){
        if(parentSS.m_uniforms eq null){
          //do nothing
        } else {
          newSS.m_uniforms ++= parentSS.m_uniforms
        }
      } else {
        if(parentSS.m_uniforms eq null) {
          newSS.m_uniforms ++= m_uniforms
        } else {
          gie.sorted_merge.merge(this.m_uniforms, parentSS.m_uniforms, newSS.m_uniforms){ UniformValueAttribute.orderingCmp }{ (l,r)=>l }
        }
      }

    }

    private def impl_mergeCopyWithParent_attributes(newSS: StateSet, ss: StateSet): Unit={

      assert( impl_DEBUG_isSorted() ) //TODO: remove
      assert( ss.impl_DEBUG_isSorted() )

      gie.sorted_merge.merge(this.m_attributes, ss.m_attributes, newSS.m_attributes){
        (l, r)=>implicitly[Ordering[Int]].compare(l.index,r.index) }{ (l,r)=>l }


      assert(newSS.impl_DEBUG_isSorted())
    }

  }

}

