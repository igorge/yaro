package gie.jsutils

import scala.scalajs.js

//function handleBMP(data, cb) {
//var bmp = new Bitmap(data)
//try {
//bmp.init()
//} catch(e) {
//cb(e)
//return
//}
//var bmpData = bmp.getData()
//var nshape = [ bmpData.getHeight(), bmpData.getWidth(), 4 ]
//var ndata = new Uint8Array(nshape[0] * nshape[1] * nshape[2])
//var result = ndarray(ndata, nshape)
//pack(bmpData, result)
//cb(null, result.transpose(1,0))
//}


trait NodeBitmapPalette extends js.Object {
  val rgbBlue: Int = js.native
  val rgbGreen: Int = js.native
  val rgbRed: Int = js.native
  val rgbReserved: Int = js.native
}


trait NodeBitmap extends js.Object {

  def getWidth(): Int = js.native
  def getHeight(): Int = js.native
  def getData(): js.Any = js.native

  val colorPalette: js.Array[NodeBitmapPalette] = js.native


}



object NodeBitmapClass extends (js.Any=>NodeBitmap) {
  val clazz = jsRequire("node-bitmap")
  def apply(buffer: js.Any) = {
    val b = js.Dynamic.newInstance(clazz)(buffer)
    b.init()
    b.asInstanceOf[NodeBitmap]
  }
}