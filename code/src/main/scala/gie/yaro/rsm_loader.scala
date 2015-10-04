package gie.yaro

import slogging.LazyLogging

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.Future
import scala.async.Async.{async, await}
import scodec.bits.{ByteVector, BitVector}
import gie.yaro.rsm.file.{codec => rsmCodec, Vector3I, Face, Node, RsmFileData}

import scala.language.existentials

class ModelNode {

}


trait RsmLoaderComponent { this: RendererContextComponent with TextureManagerComponent with RoStoreComponent with RoResourceComponent with ExecutionContextComponent =>

  class RsmLoaderImpl() extends LazyLogging {

    import renderer.gl

    def load(path: String): Future[Unit] = async {
      val rsmData = rsmCodec.decode( BitVector( await( roResource.openRsm(path) ) ) ).require.value

      val textureLoader = textureManager.get(rsmData.header.alpha)_

      val texturesData = await {
        Future.fold(rsmData.textureNames.view.zipWithIndex.map { case (texName, idx) =>
          textureLoader(texName).map((_, idx))
        })(new Array[Tuple1[gl.GLTexture]](rsmData.textureNames.size)) { case (buffer, (texData, idx)) =>
          assume(buffer(idx) eq null)
          buffer(idx) = Tuple1(texData)
          buffer
        }
      }

      await{
        Future.sequence( rsmData.nodes.map( impl_processNode(_, rsmData, texturesData) ) )
      }



    }

    private def impl_processNode(node: Node, rsmData: RsmFileData, textures:IndexedSeq[Tuple1[gl.GLTexture]]): Future[Unit] = async {
      import node._

      logger.debug(s"processing node: ${node.name}")

      def impl_getTextFromIdx(idx: Int) = {
        assume(idx<texturesIds.size, s"Texture index '${idx}' should be lower than ${texturesIds.size}")
        assume(texturesIds(idx)<textures.size, s"Indirect texture index '${idx}' should be lower than ${textures.size}")

        textures( texturesIds(idx) )
      }

      def impl_processNodeByTexId(texId: Int) = {
        logger.debug(s"processing node: '${node.name}', texture id: ${texId}, named: ${impl_getTextFromIdx(texId)}")
        node.faces.view.filter( _.textureId == texId ).unzip{face=>(face.vertexIndex.toArray, face.texVertexIndex.toArray)}
        match {
          case(vIdx, texIdx) => (vIdx.flatten, texIdx.flatten)
        }
      }

      //for each texture generate face, vertices and texture uv arrays
      (0 until texturesIds.size) map impl_processNodeByTexId foreach{
        v=>
          logger.debug(v._1.toArray.toSeq.toString)
      }


      Unit

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