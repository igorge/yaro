package gie.yaro

import gie.sml.Vector3F
import slogging.LazyLogging

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.Future
import scala.async.Async.{async, await}
import scodec.bits.{ByteVector, BitVector}
import gie.yaro.rsm.file.{codec => rsmCodec, Vector3I, Face, Node, RsmFileData}
import gie.ImplicitPipe._

import scala.language.existentials

class ModelNode {

}


trait RsmLoaderComponent { this: RendererContextComponent with TextureManagerComponent with RoStoreComponent with RoResourceComponent with ExecutionContextComponent =>

  class RsmLoaderImpl() extends LazyLogging {

    import renderer.gl

    def load(path: String): Future[renderer.Node] = async {
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


      val nodes = await{ Future.sequence( rsmData.nodes.map( impl_processNode(_, rsmData, texturesData) ) ) }

      nodes.foldLeft( new renderer.Group() )( _ += _)

    }

    private def impl_processNode(node: Node, rsmData: RsmFileData, textures:IndexedSeq[Tuple1[gl.GLTexture]]): Future[renderer.Node] = async {
      import node._

      import gl.BufferDataDispatch._

      val rootNode = new renderer.Group()

      logger.debug(s"processing node: ${node.name}")

      def impl_getTextFromIdx(idx: Int) = {
        assume(idx<texturesIds.size, s"Texture index '${idx}' should be lower than ${texturesIds.size}")
        assume(texturesIds(idx)<textures.size, s"Indirect texture index '${idx}' should be lower than ${textures.size}")

        textures( texturesIds(idx) )
      }

      def impl_processNodeByTexId(texId: Int) = {
        logger.debug(s"processing node: '${node.name}', texture id: ${texId}")
        val vertices = new ArrayBuffer[Float]()
        val textureUVs = new ArrayBuffer[Float]()

        def addVertex(vertexIdx: Int, texVertexIdx: Int): Unit ={
          val newBaseIdx = vertices.size / 3
          vertices ++= node.vertices( vertexIdx ).toArray
          textureUVs ++= node.texCoords( texVertexIdx ) |> (uv=>Array(uv.u, -1f * uv.v))
        }

        node.faces.view.filter( _.textureId == texId ).foreach{face=>
          addVertex(face.vertexIndex.x, face.texVertexIndex.x)
          addVertex(face.vertexIndex.y, face.texVertexIndex.y)
          addVertex(face.vertexIndex.z, face.texVertexIndex.z)
        }

        assume(vertices.size % 3 == 0)
        assume(textureUVs.size % 2 == 0)

        val drawable = new renderer.TrianglesArray( renderer.staticArrayBuffer(vertices.toSeq),  Some(renderer.staticArrayBuffer(textureUVs.toSeq)))

//        case class BoundingBox(min: Vector3F, max: Vector3F)
//
//        val bb  = vertices.view.toSeq.sliding(3).foldLeft[Option[BoundingBox]](None){ (bb, p)=>
//
//        logger.debug(s"${p.toArray.toSeq}")
//
//          Some( bb.map( bbb=> BoundingBox(Vector3F( bbb.min.v0 min p(0), bbb.min.v1 min p(1), bbb.min.v2 min p(2)  ),
//          Vector3F( bbb.max.v0 max p(0), bbb.max.v1 max p(1), bbb.max.v2 max p(2)  )
//          )  ).getOrElse(BoundingBox( Vector3F(p),Vector3F(p)) ) )
//        }
//
//        logger.debug(s"BB: ${bb}")

         drawable.addAttribute(new renderer.Texture2D(impl_getTextFromIdx(texId)._1, 0))

        drawable
      }

      //for each texture generate face, vertices and texture uv arrays
      (0 until texturesIds.size) map impl_processNodeByTexId foreach{ v=>
        rootNode += new renderer.Geode(v)
      }


      rootNode
    }
  }

  lazy val rsmLoader = new RsmLoaderImpl()
}