package gie.codeset

import  _root_.scala.util.parsing.combinator
import scala.collection.immutable.PagedSeq
import gie.utils.ImplicitPipe._


trait TupleLinearization { this:combinator.Parsers =>
  @inline final def lt[T1](notTuple: T1) = (notTuple, Unit)
  @inline final def lt[T1,T2](t: ~[T1,T2]) = (t._1, t._2)
  @inline final def lt[T1,T2,T3](t: ~[~[T1,T2], T3]): Tuple3[T1, T2, T3] = (t._1._1, t._1._2, t._2 )
  @inline final def lt[T1,T2,T3,T4](t: ~[~[~[T1,T2], T3],T4]): Tuple4[T1,T2,T3,T4] = {
    val tmp = lt(t._1)
    (tmp._1, tmp._2, tmp._3, t._2)
  }
  @inline final def lt[T1,T2,T3,T4,T5](t: ~[~[~[~[T1,T2], T3],T4], T5]): Tuple5[T1,T2,T3,T4,T5] = {
    val tmp = lt(t._1)
    (tmp._1, tmp._2, tmp._3, tmp._4, t._2)
  }
  @inline final def lt[T1,T2,T3,T4,T5,T6](t: ~[~[~[~[~[T1,T2],T3],T4],T5],T6]): Tuple6[T1,T2,T3,T4,T5,T6] = {
    val tmp = lt(t._1)
    (tmp._1, tmp._2, tmp._3, tmp._4, tmp._5, t._2)
  }
  @inline final def lt[T1,T2,T3,T4,T5,T6,T7](t: ~[~[~[~[~[~[T1,T2],T3],T4],T5],T6],T7]): Tuple7[T1,T2,T3,T4,T5,T6,T7] = {
    val tmp = lt(t._1)
    (tmp._1, tmp._2, tmp._3, tmp._4, tmp._5, tmp._6, t._2)
  }
  @inline final def lt[T1,T2,T3,T4,T5,T6,T7,T8](t: ~[~[~[~[~[~[~[T1,T2],T3],T4],T5],T6],T7],T8]): Tuple8[T1,T2,T3,T4,T5,T6,T7,T8] = {
    val tmp = lt(t._1)
    (tmp._1, tmp._2, tmp._3, tmp._4, tmp._5, tmp._6, tmp._7, t._2)
  }
  @inline final def lt[T1,T2,T3,T4,T5,T6,T7,T8,T9](t: ~[~[~[~[~[~[~[~[T1,T2],T3],T4],T5],T6],T7],T8],T9]): Tuple9[T1,T2,T3,T4,T5,T6,T7,T8, T9] = {
    val tmp = lt(t._1)
    (tmp._1, tmp._2, tmp._3, tmp._4, tmp._5, tmp._6, tmp._7, tmp._8, t._2)
  }
  @inline final def lt[T1,T2,T3,T4,T5,T6,T7,T8,T9,T10](t: ~[~[~[~[~[~[~[~[~[T1,T2],T3],T4],T5],T6],T7],T8],T9],T10]): Tuple10[T1,T2,T3,T4,T5,T6,T7,T8,T9,T10] = {
    val tmp = lt(t._1)
    (tmp._1, tmp._2, tmp._3, tmp._4, tmp._5, tmp._6, tmp._7, tmp._8, tmp._9, t._2)
  }
  @inline final def lt[T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11](t: ~[~[~[~[~[~[~[~[~[~[T1,T2],T3],T4],T5],T6],T7],T8],T9],T10],T11]): Tuple11[T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11] = {
    val tmp = lt(t._1)
    (tmp._1, tmp._2, tmp._3, tmp._4, tmp._5, tmp._6, tmp._7, tmp._8, tmp._9, tmp._10, t._2)
  }
}

case class MappingDef(from: String, to: Any)

object TableMap {


  class EofWhileParsing() extends Exception

  @inline private final def b2i(b:Byte): Int = 0x000000FF & b
  @inline private final def b2c(b:Byte): Char = (0x00FF & b).asInstanceOf[Char]

  private def readerFromIndexedSeq(ps:scala.collection.IndexedSeq[Byte], initOffset:Int = 0): scala.util.parsing.input.Reader[Char] = new scala.util.parsing.input.Reader[Char] {
    import scala.util.parsing.input._

    override val offset = initOffset

    override lazy val rest: Reader[Char] = if( atEnd ) this else readerFromIndexedSeq(ps.drop(1), initOffset+1)
    override lazy val first: Char = { if(!ps.isEmpty) b2c(ps.head) else throw new EofWhileParsing() }

    def pos: Position = new Position{val lineContents = ""; val column=offset; val line=0;}
    def atEnd = ps.isEmpty
  }

  private class TableParsers extends combinator.Parsers with TupleLinearization {
    type Elem = Char


    val eof: Parser[Unit] = Parser { in => if (in.atEnd) Success(Unit, in.rest) else Failure("err", in) }

    val hexChars = Set('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'A', 'B', 'C', 'D', 'E', 'F')

    val ws = rep(elem(' ') | elem('\t') | eol ) ^^^ (Unit)

    val hexNumber=(ws ~ elem('0') ~ elem('x') ~ (rep( acceptIf{ hexChars.contains(_) }{ err => s"Error parsing hex number: ${err}"} ) ^^(v=>v.foldLeft("")(_ + _))) ) ^^{v=>
      //Integer.parseInt(lt(v)._4, 16)
      lt(v)._4
    }

    val eol = (elem('\r') ~ elem('\n')) | (elem('\n') ~ elem('\r')) | elem('\n') | elem('\r')

    val comment= (ws ~ elem('#') ~ (rep( acceptIf(e=> e!='\n' && e!='\r')(e=>s"expected comment terminator, but found: ${e}") )  ^^ (v=>v.foldLeft("")(_ + _))) ~ (eol | eof)) ^^ ( lt(_)) ^^ (_._3)

    val op = ws ~ elem('+') ^^ (_._2)

    val expression:Parser[Any] =
      (hexNumber ~ op ~ expression) ^^{v=>lt(v)} |
      (hexNumber)

    val mapDef = (hexNumber ~ expression ~ opt(comment)) ^^ {v=> val t=lt(v); MappingDef(t._1, t._2) }


    val tableP =
      rep1((comment ^^^(Unit)) | mapDef)


  }




  def parse(in: Iterable[Byte]): Unit ={

    val inReader = in.iterator |> (PagedSeq.fromIterator _) |> (readerFromIndexedSeq(_))

    println(s"hhhhh>>> ${inReader.first}")

    val p = new TableParsers()

    val result = p.tableP(inReader)

    //println(s"parse result: ${result}")

    println("====================")

    result.get.foreach{
      case Unit=>
      case MappingDef(from, _)=> println(s"0x${from},")
    }

    println("====================")

    def transform(x: Any): String = x match {
      case v:String => s"0x${v}"
      case (x, '+', y) => s"${transform(x)} + ${transform(y)}"
    }

    result.get.foreach{
      case Unit=>
      case MappingDef(_, to)=> println(s"${transform(to)}, ")
    }



    //p.

  }
}