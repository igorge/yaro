package gie.yaro.rsm.file

import gie.jsutils._
import gie.scodec.FixedVectorCodec
import gie.yaro.RoResourceComponent
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


case class RsmFileData( header: Header,
                        textureNames: Vector[String],
                        mainNodeName: String,
                        nodes: Vector[Node])


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
case class Vector3I(x: Int, y: Int, z:Int){
  def toArray:Array[Int] = Array(x,y,z)
}
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


object codec extends Codec[RsmFileData] with LazyLogging {
  import FixedVectorCodec._
  import gie.yaro.FixedString._
  import gie.yaro.emptyStringToOpt

  private implicit val bufferCtor = BufferConstructor()
  private implicit val iconvLite = IconvLite()

  private
  val rsmMagic = {
    constant('G') :: constant('R') :: constant('S') :: constant('M')
  }.withContext("magic").as[Unit]

  private
  val version = {
      ("major_version" | constant(1) ) ::
      ("minor_version" | constant(4) )
  }.withContext("version").as[Unit]

  private
  val animationLength = ("animation_length" | uint32L)
  private
  val shaderType = ("shader_type" | uint32L)
  private
  val alphaValue = ("alpha" | uint8 )
  private
  val reserved = ("reserved" | fixedVectorCodec(16, byte) )

  private
  val header ={
    rsmMagic  :~>: version :: animationLength :: shaderType :: alphaValue :: reserved
  }.withContext("header").as[Header]

  private
  val textureNames ={
    ("textures_names" | vectorOfN(int32L, fixedString(40)))
  }

  private
  val mainNodeName ={
    ("main_node_name" | fixedString(40))
  }

  private
  val textureId = ("texture_id" | int32L)

  private
  val texColor = {
    int32L.withContext("color") ::
    floatL.withContext("u") ::
    floatL.withContext("v")
  }.withContext("texture_color").as[TexColor]

  private
  val quaternion = {
    ("qx" | floatL) ::
    ("qy" | floatL) ::
    ("qz" | floatL) ::
    ("qw" | floatL)
  }.withContext("quaternion").as[Quaternion]


  private
  val face = {
    ("vertex_idx" | uint16L :: uint16L :: uint16L).as[Vector3I] ::
    ("texture_vertex_idx" | uint16L :: uint16L :: uint16L).as[Vector3I] ::
    ("texture_id" | uint16L) ::
    ("m_reserved_padding" | uint16L) ::
    ("two_side" | int32L) ::
    ("smooth_group" | int32L)
  }.withContext("face").as[Face]

  private
  val rotationalKeyFrame = {
    ("frame" | int32L) ::
    quaternion
  }.withContext("rotational_key_frame").as[RotationalKeyFrame]

  private
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

  private
  val nodes ={
    ("nodes" | vectorOfN(int32L, node))
  }

  private
  val file = {
    header ::
    textureNames ::
    mainNodeName ::
    nodes
  }.as[RsmFileData].withContext("rsm_file")


  def sizeBound: scodec.SizeBound = file.sizeBound
  def decode(bits: scodec.bits.BitVector): scodec.Attempt[scodec.DecodeResult[gie.yaro.rsm.file.RsmFileData]] = file.decode(bits)
  def encode(value: gie.yaro.rsm.file.RsmFileData): scodec.Attempt[scodec.bits.BitVector] = ???



//  def test(roServices: RoResourceComponent)(implicit executor: ExecutionContext): Unit = {
//    import roServices._
//    logger.debug("codec.test()")
//
//    import scala.scalajs.js.JSConverters._
//    import gie.scodec.BmpCodecs.Bmp256Decoder
//
//    async {
//      val r = this.decode( BitVector( await( roResource.openRsm("글래지하수로/하수구_라이온1.rsm") ) ) )
//
//      println(r)
//
//    }.onComplete( _.get )
//
//  }

}
