package gie.yaro.rsm.file

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
  version: Tuple2[Int,Int],
  animationlenghth: Long,
  shaderType: Long,
  alpha: Int,
  reserved: Array[Byte] )
{
  def havePositionalAnimation_?(): Boolean = version._1>1 || (version._1==1 && version._2>=5)
}

object codec extends LazyLogging {
  import FixedVectorCodec._
  import gie.yaro.FixedString._
  import gie.yaro.emptyStringToOpt

  implicit val iconv = makeIconv()

  val rsmMagic = {
    "rsm_magic" | (constant('G') :: constant('R') :: constant('S') :: constant('M')).as[Unit]
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
    rsmMagic  :~>: version :: animationLength :: shaderType :: alphaValue :: reserved
  }.as[Header]

  val textureNames ={
    ("textures_names" | vectorOfN(int32L, fixedString(40)))

  }

  val mainNodeName ={
    ("main_node_name" | fixedString(40))
  }

  val textureId = ("texture_id" | int32L)

  val texColor = { int32L.withContext("color") :: floatL.withContext("u") :: floatL.withContext("v") }.as[TexColor]

  val quaternion = {
    ("qx" | floatL) ::
    ("qy" | floatL) ::
    ("qz" | floatL) ::
    ("qw" | floatL)
  }.as[(Float,Float,Float,Float)]

  val face = {
    ("vertex_idx" | uint16L :: uint16L :: uint16L).as[(Int,Int,Int)] ::
    ("texture_vertex_idx" | uint16L :: uint16L :: uint16L).as[(Int,Int,Int)] ::
    ("texture_id" | uint16L) ::
    ("m_reserved_padding" | uint16L) ::
    ("two_side" | int32L) ::
    ("smooth_group" | int32L)
  }

  val rotationalKeyFrame = {
    ("frame" | int32L) ::
    quaternion
  }

  case class TexColor(color: Int, u: Float, v: Float)
  case class Node(
    name: String,
    parentNodeName: String,
    texturesIds: Vector[Int],
                   axisOrigin:Vector[Float],
                   translation:Vector[Float],
                   rotAngle: Float,
                   rotAxis:Vector[Float],
                   scale: Float,
                   vertices: Vector[(Float, Float, Float)],
                   texCoords: Vector[TexColor])

  val node ={
    ("node_name" | fixedString(40)) ::
    ("parent_node_name" | emptyStringToOpt(fixedString(40))) ::
    ("textures_id" | vectorOfN( int32L.withContext("number_of_bound_textures"), textureId)) ::
    ("axis_origin" | fixedVectorCodec(3*4, floatL) ) :: //3x4 matrix that identifies the axis and origin of this node
    ("translation" | fixedVectorCodec(3, floatL) ) ::
    ("rot_angle" | floatL ) :: // angle of rotation around the axis in radians
    ("rot_axis" | fixedVectorCodec(3, floatL) ) ::
    ("scale" | fixedVectorCodec(3, floatL) ) ::
    ("vertices" | vectorOfN( int32L.withContext("num_of_vertices"), (floatL :: floatL :: floatL).as[(Float, Float, Float)])) ::
    ("texture_coordinates" | vectorOfN( int32L.withContext("num_of_tex_coords"), texColor)) ::
    ("faces" | vectorOfN( int32L.withContext("num_of_faces"), face)) ::
      /*:: positional animation if version >=1.5*/
    ("rotational_animation" | vectorOfN( int32L.withContext("num_of_key_frames"), rotationalKeyFrame))
  }

  val nodes ={
    ("nodes" | vectorOfN(int32L, node))
  }

  val file = {
    header ::
    textureNames ::
    mainNodeName ::
    nodes
  }



  def test()(implicit executor: ExecutionContext): Unit = {
    logger.debug("codec.test()")

    async {

      val r = file.decode( BitVector( await( RoStore.open("ro-data-unpacked/model/글래지하수로/하수구_라이온1.rsm") ) ) )
      println(s"result >> ${r}")

    }.onComplete( _.get )

  }

}




import scodec.bits._

