package gie.yaro

import slogging.LazyLogging

import scala.concurrent.Future
import scala.async.Async.{async, await}
import scodec.bits.{ByteVector, BitVector}
import gie.yaro.rsm.file.{codec => rsmCodec, RsmFileData}


trait RsmLoaderComponent { this: TextureManagerComponent with RoStoreComponent with RoResourceComponent with ExecutionContextComponent =>

  class RsmLoaderImpl() extends LazyLogging {
    def load(path: String): Future[Unit] = async {
      val rsmData = rsmCodec.decode( BitVector( await( roResource.openRsm(path) ) ) ).require.value

      rsmData.textureNames.foreach{ texName=>
        textureManager.get(texName, rsmData.header.alpha)
      }
    }

    private def impl_processNode(rsmData: RsmFileData)={

    }
  }

  lazy val rsmLoader = new RsmLoaderImpl()
}