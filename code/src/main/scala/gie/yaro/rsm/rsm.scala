package gie.yaro.rsm.file

import gie.yaro.RoStore
import scodec.{codecs, Codec}


import codecs._
import implicits._
import slogging.LazyLogging

case class HeaderID()

case class Header() {

}

object codec extends LazyLogging {

  val rsmMagic = {
    "rsm_magic" | (uint8 :: uint8 :: uint8 :: uint8)
  }
  val version ={
    ("major_version" | uint8 ) ::
    ("minor_version" | uint8 )
  }

  val header ={
    rsmMagic ::
    version
  }



  def test(): Unit = {
    logger.debug("codec.test()")

    RoStore.open("ro-data-unpacked/model/글래지하수로/하수구_라이온1.rsm")

    //header.dec
  }

}




import scodec.bits._

