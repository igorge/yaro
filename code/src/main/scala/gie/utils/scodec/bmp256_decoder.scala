package gie


package Bmp {
  case class BITMAPFILEHEADER(//`type: Int, //fixed 'BM' marker
                              size: Long,
                              reserved1: Int,
                              reserved2: Int,
                              offBits: Long)

  case class BITMAPINFOHEADER(size: Long,
                              width: Long,
                              height: Long,
                              planes: Int,
                              bitCount: Int,
                              compression: Long,
                              sizeImage: Long,
                              xPelsPerMeter: Long,
                              yPelsPerMeter: Long,
                              clrUsed: Long,
                              clrImportant: Long)

  case class RGBQUAD(blue: Int, green: Int, red: Int, reserved: Int)

  case class Bmp256(bitmapFileHeader: BITMAPFILEHEADER,
                    bitmapInfoHeader:BITMAPINFOHEADER,
                    palette: Vector[RGBQUAD],
                    imageData: Vector[Vector[Byte]] )


}


package scodec {


  package object BmpCodecs {

    import _root_.gie.ByteOps._
    import _root_.gie.Bmp._
    import _root_.scodec._
    import _root_.scodec.codecs._
    //import _root_.scodec.codecs.implicits._
    import _root_.scodec.bits.BitVector

    implicit val codec_BITMAPFILEHEADER: Codec[BITMAPFILEHEADER] = {
      val `type`      = { constant('B') :: constant('M') }.withContext("type").as[Unit]
      val size        = ("size" | uint32L)
      val reserved1   = ("reserved1" | uint16L)
      val reserved2   = ("reserved2" | uint16L)
      val offBits     = ("offBits" | uint32L)

      `type` :~>: size :: reserved1 :: reserved2 :: offBits
    }.withContext("BITMAPFILEHEADER").as[BITMAPFILEHEADER]

    implicit val codec_BITMAPINFOHEADER: Codec[BITMAPINFOHEADER] = {
      val size         = ("size" | uint32L)
      val width        = ("width" | uint32L)
      val height       = ("height" | uint32L)
      val planes       = ("planes" | uint16L)
      val bitCount     = ("bitCoun" | uint16L)
      val compression  = ("compression" | uint32L)
      val sizeImage    = ("sizeImage" | uint32L)
      val xPelsPerMeter= ("xPelsPerMeter" | uint32L)
      val yPelsPerMeter= ("xPelsPerMeter" | uint32L)
      val clrUsed      = ("clrUsed" | uint32L)
      val clrImportant = ("clrImportant" | uint32L)

      size :: width :: height :: planes :: bitCount :: compression :: sizeImage :: xPelsPerMeter :: yPelsPerMeter :: clrUsed :: clrImportant
    }.withContext("BITMAPINFOHEADER").as[BITMAPINFOHEADER]

    implicit val codec_RGBQUAD: Codec[RGBQUAD] = {
      ("blue" | uint8L) :: ("green" | uint8L) :: ("red" | uint8L) :: ("reserved" | uint8L)
    }.withContext("RGBQUAD").as[RGBQUAD]


    object Bmp256Decoder extends Decoder[Bmp256] {
      import FixedVectorCodec._

      val fileHeaderCodec = implicitly[Codec[BITMAPFILEHEADER]]

      def decode(bits: BitVector): Attempt[DecodeResult[Bmp256]] = {

        assume(fileHeaderCodec.sizeBound.lowerBound == fileHeaderCodec.sizeBound.upperBound.get)

        val headersCodec = { fileHeaderCodec :: implicitly[Codec[BITMAPINFOHEADER]] }.map(_.tupled)

        val DecodeResult((fileHeader, imageHeader), _) = headersCodec.decode(bits).require

        val DecodeResult(palette, _) =  {
          if(imageHeader.bitCount!=8) throw new IllegalArgumentException(s"Support only 8-bit palette images, found ${imageHeader.bitCount} bit image.")
          if(imageHeader.compression!=0)  throw new IllegalArgumentException(s"Support only uncompressed images, found '${imageHeader.compression}' compression.")

          val paletteSize = {
            val maxPalette = 256
            if (imageHeader.clrUsed>maxPalette) throw new IllegalArgumentException(s"Illegal palette size: ${imageHeader.clrUsed}")
            if (imageHeader.clrUsed==0) maxPalette else imageHeader.clrUsed
          }.toInt

          val skipBits = fileHeaderCodec.sizeBound.lowerBound  + imageHeader.size * uint8L.sizeBound.lowerBound
          val contBits = bits.drop(skipBits)

          val palette = fixedVectorCodec(paletteSize, implicitly[Codec[RGBQUAD]]).decode(contBits)
          palette.require
        }

        val DecodeResult(imageData, rem) = {
          val contBits = bits.drop( fileHeader.offBits * 8 )

          val padding = {
            val modulo4 = imageHeader.width % 4
            if(modulo4 == 0 ) 0  else 4-modulo4
          }.toInt

          val scanlineCodec = "scanline" | fixedVectorCodec[Byte](imageHeader.width.toInt,  uint8.xmap(_.toByte, ub2i)) <~ fixedVectorCodec(padding, uint8).unit(Vector.fill(padding)(0))
          val imageCodec = "image-data" | fixedVectorCodec(imageHeader.height.toInt, scanlineCodec)

          imageCodec.decode(contBits).require
        }

        Attempt.successful( DecodeResult(Bmp256(fileHeader, imageHeader, palette, imageData), rem))
      }


    }

  }

} // package scodec