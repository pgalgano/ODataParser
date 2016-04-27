import scala.util.parsing.combinator._

sealed abstract class Expr

case class ValueListNode(value:List[Expr]) extends Expr {override def toString  = {"(" + value.mkString + ")"}}
case class LogicNode(lhs:Expr,operation:String,rhs:Expr) extends Expr {override def toString = {lhs.toString + " " + operation + " " + rhs.toString}}
case class SimpleOperation(field:String,operation:String,value:String) extends Expr {override def toString = {field + " " + operation + " " + value}}

object FilterParser extends JavaTokenParsers with PackratParsers{

  def quotedString = stringLiteral | """'[^']*'""".r
  def trueOrFalse = "true"|"false"
  def value =  decimalNumber | trueOrFalse |quotedString
  def operation = "eq" | "ne" | "gt" | "lt" | "le"

  lazy val identifier:PackratParser[String] = ident ~ "." ~ identifier ^^ {case a~dot~b => a+dot+b} | ident

  lazy val simpleOperation = identifier ~ operation ~ value ^^ {case f~op~v => SimpleOperation(f,op,v)}

  lazy val exprList:PackratParser[Expr]  = "(" ~ rep(anyExpr | simpleOperation | exprList) ~ ")" ^^ {case op~l~cp => ValueListNode(l) }|
    simpleOperation

  lazy val anyExpr:PackratParser[Expr] = "("~>anyExpr ~ ("and"|"or") ~ anyExpr<~")" ^^ {case lhs~op~rhs => LogicNode(lhs,op,rhs)} |
    anyExpr ~ ("and"|"or") ~ anyExpr ^^ {case lhs~op~rhs => LogicNode(lhs,op,rhs)} |
    anyExpr ~ ("and"|"or") ~ exprList ^^ {case lhs~op~rhs => LogicNode(lhs,op,rhs)} |
    exprList ~ ("and"|"or") ~ anyExpr ^^ {case lhs~op~rhs => LogicNode(lhs,op,rhs)} |
    exprList

  def createTree(s:String) = {
    parseAll(anyExpr,s);
  }

  def parse(s:String): Expr = {
    createTree(s) match {
      case Success(tree, _) => tree
      case e: NoSuccess => throw new Exception("Error**********>" + e)
    }
  }

}


