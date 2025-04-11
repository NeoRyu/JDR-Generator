package jdr.generator.api.scala.tools


object JavaDelimiters {
  abstract class Delimiter

  case class BACKSPACE() extends Delimiter
  case class TAB() extends Delimiter
  case class LINEFEED() extends Delimiter
  case class FORMFEED() extends Delimiter
  case class RETURN() extends Delimiter
  case class DOUBLE_QUOTE() extends Delimiter
  case class SINGLE_QUOTE() extends Delimiter
  case class BACKSLASH() extends Delimiter
  case class SPACING() extends Delimiter


  def delimiter(d: Delimiter): String = d match {
    case BACKSPACE() => "\b"
    case TAB() => "\t"
    case LINEFEED() => "\n"
    case FORMFEED() => "\f"
    case RETURN() => "\r"
    case DOUBLE_QUOTE() => "\""
    case SINGLE_QUOTE() => "\'"
    case BACKSLASH() => "\\"
    case SPACING() => " "
    case _ => d.toString
  }

}
