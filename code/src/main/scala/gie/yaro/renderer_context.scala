package gie.yaro

import gie.gsg


trait RendererContextComponent {
  val renderer: gsg.Renderer[T] forSome {type T}
}

