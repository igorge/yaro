package gie.yaro.rsm.file

import gie.yaro.RoStore
import scodec.bits.{ByteVector, BitVector}
import scodec.{codecs, Codec}

import codecs._
import implicits._
import slogging.LazyLogging

import scala.concurrent.ExecutionContext
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

  val header ={
    rsmMagic ~> version :: animationLength :: shaderType :: alphaValue :: reserved
  }.as[Header]



  def test()(implicit executor: ExecutionContext): Unit = {
    logger.debug("codec.test()")

    async {

      val r = header.decode( BitVector( await( RoStore.open("ro-data-unpacked/model/글래지하수로/하수구_라이온1.rsm") ) ) )

      println(s"result >> ${r}")

    }.onComplete( _.get )

  }

}




import scodec.bits._

