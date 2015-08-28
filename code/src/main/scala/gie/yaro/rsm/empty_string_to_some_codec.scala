package gie.yaro

import scodec.Codec


object emptyStringToOpt {

  def apply(c: Codec[String]): Codec[Option[String]] = emptyStringToOpt(c)

  def emptyStringToOpt(c: Codec[String]): Codec[Option[String]] = {
    c.xmap(
      str=>if(str.isEmpty) None else Some(str),
      _ match {
        case Some(str) => str
        case None => ""
      }
    )
  }


}