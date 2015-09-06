package gie.yaro

import slogging.LazyLogging

import scala.concurrent.Future
import scala.async.Async.{async, await}
import scodec.bits.{ByteVector, BitVector}
import gie.yaro.rsm.file.{codec => rsmCodec, Node, RsmFileData}

class ModelNode {

}


trait RsmLoaderComponent { this: TextureManagerComponent with RoStoreComponent with RoResourceComponent with ExecutionContextComponent =>

  class RsmLoaderImpl() extends LazyLogging {


    def load(path: String): Future[Unit] = async {
      val rsmData = rsmCodec.decode( BitVector( await( roResource.openRsm(path) ) ) ).require.value

      val texturesData = await {
        Future.fold(rsmData.textureNames.view.zipWithIndex.map { case (texName, idx) =>
          textureManager.get(texName, rsmData.header.alpha).map((_, idx))
        })(new Array[TextureData](rsmData.textureNames.size)) { case (buffer, (texData, idx)) =>
          assume(buffer(idx) eq null)
          buffer(idx) = texData
          buffer
        }
      }

      await{
        Future.sequence( rsmData.nodes.map( impl_processNode(_, rsmData, texturesData) ) )
      }



    }

    private def impl_processNode(node: Node, rsmData: RsmFileData, textures:Array[TextureData])= async {
      import node._

      logger.debug(s"processing node: ${node.name}")

      def impl_getTextFromIdx(idx: Int) = {
        assume(idx<texturesIds.size, s"Texture index '${idx}' should be lower than ${texturesIds.size}")
        assume(texturesIds(idx)<textures.size, s"Indirect texture index '${idx}' should be lower than ${textures.size}")

        textures( texturesIds(idx) )
      }

      def impl_processNodeByTexId(texId: Int) = {
        node.faces.filter( _.textureId == texId )
      }

      (0 until texturesIds.size) map impl_processNodeByTexId


//      val maxVIdxX = node.faces.map(_.vertexIndex.x).reduce(_ max _)
//      val maxVIdxY = node.faces.map(_.vertexIndex.y).reduce(_ max _)
//      val maxVIdxZ = node.faces.map(_.vertexIndex.z).reduce(_ max _)
//
//      val maxTexIdx= {
//        node.faces.map(_.texVertexIndex.x) ++ node.faces.map(_.texVertexIndex.y) ++ node.faces.map(_.texVertexIndex.z)
//      }.reduce(_ max _)
//
//      logger.debug(s"max: (${maxVIdxX}, ${maxVIdxY}, ${maxVIdxZ}), count: ${node.vertices.size}")
//
//      logger.debug(s"max tex: ${maxTexIdx}, count: ${node.texCoords.size}")

    }
  }

  lazy val rsmLoader = new RsmLoaderImpl()
}