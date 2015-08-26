package gie.yaro.rsm.file

import gie.yaro.RoStore
import scodec.bits.{ByteVector, BitVector}
import scodec.{codecs, Codec}

import codecs._
import implicits._
import slogging.LazyLogging

import scala.concurrent.ExecutionContext
import scala.scalajs.js
import scala.scalajs.js.JSConverters.array2JSRichGenTrav
import scala.scalajs.js.typedarray.{ArrayBuffer, Uint8Array}
import scala.async.Async.{async, await}


case class Header(
  version: Tuple2[Int,Int],
  animationlenghth: Long,
  shaderType: Long,
  alpha: Int,
  reserved: Array[Byte] )
{
  def havePositionalAnimation_?(): Boolean = version._1>1 || (version._1==1 && version._2>=5)
}

object codec extends LazyLogging {

  private val iconv = gie.Require.require("iconv-lite")
  private val bufferCtor = gie.Require.require("buffer").asInstanceOf[js.Dynamic].Buffer

  private def fromEucKr(data: Array[Byte]): String = {
    val buffer = bufferCtor.apply(data.toJSArray)
    iconv.asInstanceOf[js.Dynamic].decode(buffer, "euc-kr").asInstanceOf[String]
  }


  val rsmMagic = {
    "rsm_magic" | (constant('G') ~> constant('R') ~> constant('S') ~> constant('M'))
  }

  val version = {
    ("major_version" | uint8 ) ::
    ("minor_version" | uint8 )
  }.as[Tuple2[Int,Int]]

  val animationLength = ("animation_length" | uint32L)
  val shaderType = ("shader_type" | uint32L)
  val alphaValue = ("alpha" | uint8 )
  val reserved = ("reserved" | bytes(16) )  xmap (_.toArray, ByteVector(_:Array[Byte]))

  def fixedString(size: Int) = ("fixed_string" | bytes(size) ) xmap( impl_fixedString_bytesToString _, impl_fixedString_stringToBytes(_: String, size) )

  def impl_fixedString_bytesToString(data: ByteVector): String= {
    assume(data.size!=0)
    fromEucKr( data.takeWhile(_!=0).toArray )
  }

  def impl_fixedString_stringToBytes(data: String, size: Int): ByteVector ={
    assume(data.size>0)
    assume(data.size<=size)

    ???
  }

  val header ={
    rsmMagic ~> version :: animationLength :: shaderType :: alphaValue :: reserved
  }.as[Header]

  val textureNames ={
    ("textures_names" | vectorOfN(int32L, fixedString(40)))

  }

  val file = {
    header :: textureNames
  }



  def test()(implicit executor: ExecutionContext): Unit = {
    logger.debug("codec.test()")

    val iconv = gie.Require.require("iconv-lite")

    println(s">>> ${iconv}")

    async {

      //gie.codeset.TableMap.parse( await(RoStore.open("ro-data-unpacked/KOREAN.TXT")) )

       //gie.codeset.TableMap.parse( await(RoStore.open("ro-data-unpacked/test.txt")) )

      val r = file.decode( BitVector( await( RoStore.open("ro-data-unpacked/model/글래지하수로/하수구_라이온1.rsm") ) ) )

      println(s"result >> ${r}")

    }.onComplete( _.get )

  }

}




import scodec.bits._

