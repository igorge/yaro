package gie.yaro

import gie.jsutils.{BufferConstructor, IconvLite, jsRequire}
import scodec.bits.ByteVector
import scodec.codecs._

import scala.scalajs.js
import scala.scalajs.js.JSConverters.array2JSRichGenTrav


object FixedString {

  private def fromEucKr(data: Array[Byte])(implicit iconvLite: IconvLite, bufferCtor: BufferConstructor ): String = {
    val buffer = bufferCtor(data.toJSArray)
    iconvLite.iconv.decode(buffer, "euc-kr").asInstanceOf[String]
  }

  def fixedString(size: Int)(implicit iconvLite: IconvLite, bufferCtor: BufferConstructor ) = ("fixed_string" | bytes(size) ) xmap( impl_fixedString_bytesToString _, impl_fixedString_stringToBytes(_: String, size) )

  def impl_fixedString_bytesToString(data: ByteVector)(implicit iconvLite: IconvLite, bufferCtor: BufferConstructor ): String= {
    assume(data.size!=0)
    fromEucKr( data.takeWhile(_!=0).toArray )
  }

  def impl_fixedString_stringToBytes(data: String, size: Int): ByteVector ={
    assume(data.size>0)
    assume(data.size<=size)

    ???
  }


}