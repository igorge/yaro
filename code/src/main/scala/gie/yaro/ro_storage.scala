package gie.yaro

import gie.jsutils.XMLHttpRequestFuture
import gie.jsutils.XMLHttpRequestFuture.statusCodes._
import org.scalajs.dom
import slogging.LoggerHolder

import scala.scalajs.js.typedarray.{ArrayBuffer, Int8Array}

trait RoResourceComponent { this: RoStoreComponent with LoggerHolder with ExecutionContextComponent =>

  class RoStoreByTypeImpl {

    val dataDir = "ro-data-unpacked"
    val textureDir = "texture"
    val rsmDir = "model"

    def openTexture(path: String) = {

      roStore.open(s"${dataDir}/${textureDir}/${path}").recoverWith{
        case e@_ if { logger.debug(s"Exception while loading texture: ${e}"); false } => ???
        case e:gie.jsutils.ConnectionException if e.status==SC_NOT_FOUND =>
          val newPath = path.toLowerCase()
          logger.debug(s"Trying different name: '${path}' => '${newPath}'")
          roStore.open(s"${dataDir}/${textureDir}/${newPath}")
      }
    }
    def openRsm(path: String) = roStore.open(s"${dataDir}/${rsmDir}/${path}")
  }

  lazy val roResource = new RoStoreByTypeImpl()

}

trait RoStoreComponent { this: LoggerHolder with ExecutionContextComponent =>

  val roStorePrefix = s"${dom.location.origin}/"

  class RoStore {


    def open(path: String)= {
      val url = s"${roStorePrefix}${path.replace('\\','/')}"

      logger.debug(s"GET ###> ${url}")

      val xhr = new dom.XMLHttpRequest()

      val f = new XMLHttpRequestFuture(xhr, Some(XMLHttpRequestFuture.responseType.arrayBuffer))

      xhr.open("GET", url)
      xhr.send()

      f.map{ r=>
        val buffer = new Int8Array ( r.asInstanceOf[ArrayBuffer] )
        new IndexedSeq[Byte] {
          def length = buffer.length
          def apply(idx: Int) = buffer(idx)
        }
      }
    }

  }

  lazy val roStore = new RoStore()

}