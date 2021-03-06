package gie.gsg

import gie.gsg.state_attribute.{GlProgramAttributeComponent, ShaderVariableComponent, UniformValueAttributeComponent, StateAttributeComponent}
import gie.search.binarySearch
import slogging.LoggerHolder

import scala.collection.Searching.{SearchResult, Found, InsertionPoint}
import scala.collection.generic
import scala.collection.mutable.ArrayBuffer
import scala.math.Ordering

trait StateSetComponent {
  this: ShaderVariableComponent with StateAttributeComponent with UniformValueAttributeComponent with GlProgramAttributeComponent with ProgramHolderComponent with LoggerHolder =>

  object StateSet {
    @inline
    def getProgram(xSS: StateSet, ySS: StateSet): GlProgramHolder={
      if(xSS eq null) {
        if(ySS eq null) null else ySS.getProgram()
      } else {
        xSS.getProgram(ySS)
      }
    }

    def getProgramFrom(ss: StateSet): GlProgramHolder={
      if (ss eq null) null else ss.getProgram()
    }
  }

  // Growable += simply appends at the end of array buffer with ordering enforcement
  // use insert() for set-like element insertion
  //
  class StateSet {
    private val m_parent: StateSet = null // not used or assigned for now

    private[gsg] val m_attributes = new ArrayBuffer[StateAttribute]()
    private var m_variables:ArrayBuffer[ShaderVariableAttribute] = null

    private def impl_allocVariables() = {
      m_variables = new ArrayBuffer[ShaderVariableAttribute]()
      m_variables
    }

    @inline
    private [gsg] def variables_! = if(m_variables eq null) impl_allocVariables() else m_variables
    @inline
    private [gsg] def variables = m_variables


    @inline
    def getProgram(): GlProgramHolder ={
      if(m_attributes.size==0 || m_attributes(0).index!=GlProgramAttribute.index) {
        StateSet.getProgramFrom(m_parent)
      } else {
        m_attributes(0).asInstanceOf[GlProgramAttribute].programHolder
      }
    }

    @inline
    def getProgram(otherSS: StateSet): GlProgramHolder={
      val x = this.getProgram()
      if(x ne null) x else {
        if (otherSS eq null) null else otherSS.getProgram()
      }
    }

    def clear(){
      m_attributes.clear()
      Option(m_variables) foreach (_.clear())
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

    def addVariableValue(u: ShaderVariableAttribute): this.type={
      if(m_variables eq null){
        impl_allocVariables() += u
      } else {
        binarySearch(u.name, m_variables){ (key, uniform)=> implicitly[Ordering[String]].compare(key, uniform.name) } match {
          case Found(index) => throw new Exception(s"StateSet already has uniform/attribute with name '${u.name}.")
          case InsertionPoint(index) => m_variables.insert(index, u)
        }
      }
      this
    }

    def mergeCopyWithParent(ss: StateSet): StateSet = { //this overrides ss

      //logger.debug(s"mergeCopyWithParent(${ss})")

      val newSS = new StateSet()

      //logger.debug(s"mergeCopyWithParent_attrs")
      impl_mergeCopyWithParent_attributes(newSS, ss)
      //logger.debug(s"mergeCopyWithParent_uniforms")
      impl_mergeCopyWithParent_uniforms(newSS, ss)

      newSS
    }

    private def impl_mergeCopyWithParent_uniforms(newSS: StateSet, parentSS: StateSet): Unit={

      //logger.debug(s"impl_mergeCopyWithParent_uniforms")

      if(m_variables eq null){
        if(parentSS.m_variables eq null){
          //do nothing
        } else {
          newSS.variables_! ++= parentSS.m_variables
        }
      } else {
        if(parentSS.m_variables eq null) {
          newSS.variables_! ++= m_variables
        } else {
          //logger.debug(s"impl_mergeCopyWithParent_uniforms: merging ${this.m_variables} with parent ${parentSS.m_variables}")
          gie.sorted_merge.merge(this.m_variables, parentSS.m_variables, newSS.variables_!){ ShaderVariableAttribute.orderingCmp }{ (l,r)=>l }
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

