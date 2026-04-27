package lab1

import scala.util.parsing.combinator._

sealed trait RegexAST
case class Literal(char: Char) extends RegexAST
case class Concat(left: RegexAST, right: RegexAST) extends RegexAST
case class Union(left: RegexAST, right: RegexAST) extends RegexAST
case class Star(inner: RegexAST) extends RegexAST
case class Plus(inner: RegexAST) extends RegexAST
case class Optional(inner: RegexAST) extends RegexAST

object RegexParser extends RegexParsers {
  
  override val skipWhitespace = false

  def parseRegex(input: String): Either[String, RegexAST] =
    parseAll(regex, input) match {
      case Success(result, _) => Right(result)
      case Failure(msg, _) => Left(msg)
      case Error(msg, _) => Left(msg)
    }

  def regex: Parser[RegexAST] = union

  def union: Parser[RegexAST] = 
    concat ~ rep("|" ~> concat) ^^ {
      case first ~ rest => rest.foldLeft(first)(Union.apply)
    }

  def concat: Parser[RegexAST] = 
    rep1(modifier) ^^ { list =>
      list.tail.foldLeft(list.head)(Concat.apply)
    }

  def modifier: Parser[RegexAST] = 
    base ~ rep("*" | "+" | "?") ^^ {
      case b ~ mods => mods.foldLeft(b) {
        case (acc, "*") => Star(acc)
        case (acc, "+") => Plus(acc)
        case (acc, "?") => Optional(acc)
        case (acc, _)   => acc // unreachable
      }
    }

  def base: Parser[RegexAST] = 
    group | escape | literal

  def group: Parser[RegexAST] = 
    "(" ~> regex <~ ")"

  def escape: Parser[RegexAST] = 
    "\\" ~> elem("any char", _ => true) ^^ { c => Literal(c) }

  def literal: Parser[RegexAST] = 
    elem("literal", c => c != '(' && c != ')' && c != '*' && c != '+' && c != '?' && c != '|' && c != '\\') ^^ { c => Literal(c) }
}
