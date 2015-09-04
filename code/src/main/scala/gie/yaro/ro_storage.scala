package gie.yaro

import gie.jsutils.XMLHttpRequestFuture
import org.scalajs.dom
import slogging.LoggerHolder

import scala.scalajs.js.typedarray.{ArrayBuffer, Int8Array}

trait RoStoreComponent { this: LoggerHolder with ExecutionContextComponent =>

  val roStorePrefix = s"${dom.location.origin}/"

  class RoStore {

    def openTexture(path: String) = open(path)

    def open(path: String)= {
      val url = s"${roStorePrefix}${path}"

      logger.debug(url)

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