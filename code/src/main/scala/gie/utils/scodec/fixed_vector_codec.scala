package gie.scodec

import scodec.bits.BitVector
import scodec.{Decoder, Encoder, Codec}

class FixedVectorCodec[A](count: Int, codec: Codec[A]) extends Codec[Vector[A]] {

  def sizeBound = codec.sizeBound * count

  def encode(vector: Vector[A]) = Encoder.encodeSeq(codec)(vector)

  def decode(buffer: BitVector) = Decoder.decodeCollect[Vector, A](codec, Some(count))(buffer)

  override def toString = s"vector($codec)"

}


object FixedVectorCodec {
  def fixedVectorCodec[A](count: Int, codec: Codec[A]):Codec[Vector[A]] = new FixedVectorCodec(count, codec)
}

