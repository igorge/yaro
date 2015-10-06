package gie.yaro

import gie.ByteOps
import gie.scodec.BmpCodecs.Bmp256Decoder
import gie.Bmp._
import gie.utils.Cached

import scodec.bits.BitVector
import slogging.LazyLogging

import scala.async.Async._
import scala.concurrent.{ExecutionContext, Future}
import scala.language.postfixOps

object TextureDataLoader {

  def load(dataRaw: Future[IndexedSeq[Byte]], alpha: Int)(implicit executor: ExecutionContext): Future[(Int,Int,Array[Byte])] ={
    async {
      val data = BitVector(await( dataRaw ))

      val bmp = await(Future{Bmp256Decoder.decode(data).require.value})

      //println(s"w: ${bmp.bitmapInfoHeader.width}, h: ${bmp.bitmapInfoHeader.height}")

      assume(bmp.bitmapInfoHeader.width>0)
      assume(bmp.bitmapInfoHeader.height>0)

      val buffer = new Array[Byte](bmp.bitmapInfoHeader.width * bmp.bitmapInfoHeader.height toInt)

      var targetIdx: Int = 0
      for( scanLine <- bmp.imageData.view.reverse; i <- scanLine ){
        val idx = ByteOps.ub2i(i)
        val color = bmp.palette( idx )
        buffer(targetIdx + 0) = color.red.asInstanceOf[Byte]
        buffer(targetIdx + 1) = color.green.asInstanceOf[Byte]
        buffer(targetIdx + 2) = color.blue.asInstanceOf[Byte]
        if(idx==alpha) {
          buffer(targetIdx + 3) = 0
        } else {
          buffer(targetIdx + 3) = -1
        }

        //XXX: HACK: alpha
        if( (ByteOps.ub2i(buffer(targetIdx + 0)) == 255) &&
            (ByteOps.ub2i(buffer(targetIdx + 1)) == 0) &&
            (ByteOps.ub2i(buffer(targetIdx + 2)) == 255) )
        {
          buffer(targetIdx + 3) = 0
        }

        //XXX: HACK

        targetIdx+=4
      }

      assume(targetIdx == buffer.size )

      (bmp.bitmapInfoHeader.width.toInt, bmp.bitmapInfoHeader.height.toInt, buffer)
    }

  }

}


trait TextureManagerComponent { this: RendererContextComponent with RoStoreComponent with RoResourceComponent with ExecutionContextComponent  =>
  
  case class TextureData(w: Int,h: Int,data: Array[Byte])

  class TextureDataLoader(urlResolver: String=>Future[IndexedSeq[Byte]]) extends LazyLogging {

    import TextureDataLoader._

    private case class CachedTextureData(width: Int, height: Int, alpha: Int, data:Array[Byte])


    private val m_cache = new Cached[String, Future[CachedTextureData]]()

    private def impl_loadTexture(key: String, alpha: Int): Future[CachedTextureData] ={
      logger.debug(s"Texture cache miss: ${key}, alpha: ${alpha}")

      load(urlResolver(key), alpha) map {
        case (w,h, data) => CachedTextureData(w, h, alpha, data)
      }
    }

    def get(key: String, alpha: Int): Future[TextureData] = async {
      val texData = await( m_cache.get(key)(impl_loadTexture(key, alpha) ) )
      assert(texData.alpha==alpha, s"texData.alpha==alpha is false: ${texData.alpha}!=${alpha}")
      TextureData(texData.width, texData.height, texData.data)
    }

  }

  class TextureLoader extends LazyLogging {
    import renderer.gl

    private val m_cache = new Cached[String, Future[gl.GLTexture]]()

    private def impl_loadTex(path: String, alpha: Int) = async {

      val TextureData(w,h,data) = await(textureDataLoader.get(path, alpha))
      gl.withBoundTexture(gl.const.TEXTURE_2D, gl.genTextures()){ texture=>
        gl.texImage2D(gl.const.TEXTURE_2D, 0, gl.const.RGBA, w, h, 0, gl.const.RGBA, gl.const.UNSIGNED_BYTE, data)
        gl.texParameter(gl.const.TEXTURE_2D, gl.const.TEXTURE_MAG_FILTER, gl.const.NEAREST)
        gl.texParameter(gl.const.TEXTURE_2D, gl.const.TEXTURE_MIN_FILTER, gl.const.NEAREST)

        texture
      }
    }

    def get(alpha: Int)(path: String): Future[gl.GLTexture] = m_cache.get(path){
      logger.debug(s"GLTexture cache miss: ${path}")
      impl_loadTex(path, alpha)
    }

  }


  lazy val textureDataLoader = new TextureDataLoader( roResource.openTexture _ )
  lazy val textureManager = new TextureLoader()
}