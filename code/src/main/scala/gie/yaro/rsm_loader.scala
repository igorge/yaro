package gie.yaro

import scodec.bits.{ByteVector, BitVector}


trait RsmLoaderComponent { this: TextureManagerComponent with RoStoreComponent with RoResourceComponent =>

  def load(path: String): Unit ={

    //val r = file.decode( BitVector( await( roResource.openRsm(path) ) ) )

  }

}