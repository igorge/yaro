package gie.yaro

import scodec.bits.ByteVector
import scodec.codecs._

import scala.scalajs.js
import scala.scalajs.js.JSConverters.array2JSRichGenTrav


case class IconvHolder(iconv: js.Dynamic, bufferCtor: js.Dynamic)

object FixedString {

  def makeIconv() = IconvHolder(gie.Require.require("iconv-lite"), gie.Require.require("buffer").Buffer)

  private def fromEucKr(data: Array[Byte])(implicit ich:IconvHolder ): String = {
    val buffer = ich.bufferCtor.apply(data.toJSArray)
    ich.iconv.decode(buffer, "euc-kr").asInstanceOf[String]
  }

  def fixedString(size: Int)(implicit ich:IconvHolder) = ("fixed_string" | bytes(size) ) xmap( impl_fixedString_bytesToString _, impl_fixedString_stringToBytes(_: String, size) )

  def impl_fixedString_bytesToString(data: ByteVector)(implicit ich:IconvHolder): String= {
    assume(data.size!=0)
    fromEucKr( data.takeWhile(_!=0).toArray )
  }

  def impl_fixedString_stringToBytes(data: String, size: Int): ByteVector ={
    assume(data.size>0)
    assume(data.size<=size)

    ???
  }


}