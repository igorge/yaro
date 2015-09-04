package gie.yaro

import gie.ByteOps
import gie.scodec.BmpCodecs.Bmp256Decoder
import gie.Bmp._

import scodec.bits.BitVector

import scala.async.Async._
import scala.concurrent.{ExecutionContext, Future}

object loadTexture  {

  def apply(dataRaw: Future[IndexedSeq[Byte]])(implicit executor: ExecutionContext): Future[(Int,Int,Array[Byte])] ={
    async {
      val data = BitVector(await( dataRaw ))

      val bmp = await(Future{Bmp256Decoder.decode(data).require.value})

      println(s"w: ${bmp.bitmapInfoHeader.width}, h: ${bmp.bitmapInfoHeader.height}")

      assume(bmp.bitmapInfoHeader.width>0)
      assume(bmp.bitmapInfoHeader.height>0)

      val buffer = new Array[Byte](bmp.bitmapInfoHeader.width * bmp.bitmapInfoHeader.height toInt)

      var targetIdx: Int = 0
      for( scanLine <- bmp.imageData.view.reverse; i <- scanLine ){
        val color = bmp.palette( ByteOps.ub2i(i) )
        buffer(targetIdx + 0) = color.red.asInstanceOf[Byte]
        buffer(targetIdx + 1) = color.green.asInstanceOf[Byte]
        buffer(targetIdx + 2) = color.blue.asInstanceOf[Byte]
        buffer(targetIdx + 3) = -1 //alpha

        targetIdx+=4
      }

      (bmp.bitmapInfoHeader.width.toInt, bmp.bitmapInfoHeader.height.toInt, buffer)
    }

  }

}