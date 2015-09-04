package gie.yaro.rsm.file

import gie.jsutils._
import gie.scodec.FixedVectorCodec
import gie.yaro.RoStore
import scodec.bits.{ByteVector, BitVector}
import scodec._

import scodec.codecs._
import implicits._
import slogging.LazyLogging

import scala.concurrent.ExecutionContext
import scala.scalajs.js
import scala.scalajs.js.JSConverters.array2JSRichGenTrav
import scala.scalajs.js.typedarray.{ArrayBuffer, Uint8Array}
import scala.async.Async.{async, await}


case class Header(
  //version: Tuple2[Int,Int],
  animationlenghth: Long,
  shaderType: Long,
  alpha: Int,
  reserved: Vector[Byte] )
{
//  def havePositionalAnimation_?(): Boolean = version._1>1 || (version._1==1 && version._2>=5)
}


case class RotationalKeyFrame(frame: Int, rot:Quaternion)
case class Vector3I(x: Int, y: Int, z:Int)
case class Quaternion(qx: Float, qy: Float,qz: Float, qw: Float)

case class TexColor(color: Int, u: Float, v: Float)

case class Face( vertexIndex: Vector3I,
                 texVertexIndex: Vector3I,
                 textureId: Int,
                 reserved_padding: Int,
                 twoSide: Int,
                 smoothGroup: Int )

case class Node( name: String,
                 parentNodeName: Option[String],
                 texturesIds: Vector[Int],
                 axisOrigin:Vector[Float],
                 translation:Vector[Float],
                 rotAngle: Float,
                 rotAxis:Vector[Float],
                 scale: Vector[Float],
                 vertices: Vector[(Float, Float, Float)],
                 texCoords: Vector[TexColor],
                 faces: Vector[Face],
                 rotationalAnimation: Vector[RotationalKeyFrame] )


object codec extends LazyLogging {
  import FixedVectorCodec._
  import gie.yaro.FixedString._
  import gie.yaro.emptyStringToOpt

  implicit val bufferCtor = BufferConstructor()
  implicit val iconvLite = IconvLite()

  val rsmMagic = {
    constant('G') :: constant('R') :: constant('S') :: constant('M')
  }.withContext("magic").as[Unit]

//  val version = {
//    ("major_version" | uint8 ) ::
//    ("minor_version" | uint8 )
//  }.as[Tuple2[Int,Int]]

  val version = {
      ("major_version" | constant(1) ) ::
      ("minor_version" | constant(4) )
  }.withContext("version").as[Unit]

  val animationLength = ("animation_length" | uint32L)
  val shaderType = ("shader_type" | uint32L)
  val alphaValue = ("alpha" | uint8 )
  val reserved = ("reserved" | fixedVectorCodec(16, byte) )

  val header ={
    rsmMagic  :~>: version :: animationLength :: shaderType :: alphaValue :: reserved
  }.withContext("header").as[Header]

  val textureNames ={
    ("textures_names" | vectorOfN(int32L, fixedString(40)))
  }

  val mainNodeName ={
    ("main_node_name" | fixedString(40))
  }

  val textureId = ("texture_id" | int32L)

  val texColor = {
    int32L.withContext("color") ::
    floatL.withContext("u") ::
    floatL.withContext("v")
  }.withContext("texture_color").as[TexColor]

  val quaternion = {
    ("qx" | floatL) ::
    ("qy" | floatL) ::
    ("qz" | floatL) ::
    ("qw" | floatL)
  }.withContext("quaternion").as[Quaternion]


  val face = {
    ("vertex_idx" | uint16L :: uint16L :: uint16L).as[Vector3I] ::
    ("texture_vertex_idx" | uint16L :: uint16L :: uint16L).as[Vector3I] ::
    ("texture_id" | uint16L) ::
    ("m_reserved_padding" | uint16L) ::
    ("two_side" | int32L) ::
    ("smooth_group" | int32L)
  }.withContext("face").as[Face]

  val rotationalKeyFrame = {
    ("frame" | int32L) ::
    quaternion
  }.withContext("rotational_key_frame").as[RotationalKeyFrame]

  val node ={
    ("node_name" | fixedString(40)) ::
    ("parent_node_name" | emptyStringToOpt(fixedString(40))) ::
    ("textures_id" | vectorOfN( int32L.withContext("number_of_bound_textures"), textureId)) ::
    ("axis_origin" | fixedVectorCodec(3*4, floatL) ) :: //3x4 matrix that identifies the axis and origin of this node
    ("translation" | fixedVectorCodec(3, floatL) ) ::
    ("rot_angle" | floatL ) :: // angle of rotation around the axis in radians
    ("rot_axis" | fixedVectorCodec(3, floatL) )  ::
    ("scale" | fixedVectorCodec(3, floatL) ) ::
    ("vertices" | vectorOfN( int32L.withContext("num_of_vertices"), (floatL :: floatL :: floatL).as[(Float, Float, Float)])) ::
    ("texture_coordinates" | vectorOfN( int32L.withContext("num_of_tex_coords"), texColor)) ::
    ("faces" | vectorOfN( int32L.withContext("num_of_faces"), face)) ::
      /*:: positional animation if version >=1.5*/
    ("rotational_animation" | vectorOfN( int32L.withContext("num_of_key_frames"), rotationalKeyFrame))
  }.withContext("node").as[Node]

  val nodes ={
    ("nodes" | vectorOfN(int32L, node))
  }

  val file = {
    header ::
    textureNames ::
    mainNodeName ::
    nodes
  }.withContext("rsm_file")



  def test()(implicit executor: ExecutionContext): Unit = {
    logger.debug("codec.test()")

    import scala.scalajs.js.JSConverters._
    import gie.scodec.BmpCodecs.Bmp256Decoder

    async {

//      val newBuffer = BufferConstructor()

      //val data = BitVector(await( RoStore.open("ro-data-unpacked/texture/내부소품/lion ring.bmp") ))
      val data = BitVector(await( RoStore.open("ro-data-unpacked/texture/내부소품/gedan-side4.bmp") ))


      val bmp = Bmp256Decoder.decode(data)

      println(bmp)



      val r = file.decode( BitVector( await( RoStore.open("ro-data-unpacked/model/글래지하수로/하수구_라이온1.rsm") ) ) )
//      println(s"result >> ${r}")

    }.onComplete( _.get )

  }

}




import scodec.bits._

