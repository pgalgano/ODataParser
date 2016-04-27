import org.scalatest._

/**
  * Created by petegalgano on 4/26/16.
  */
class FilterParserSpec  extends FlatSpec{

  def validateParser(filter:String) = {assert(FilterParser.parse(filter).toString() == filter)}

  "The parser" should "be able to handle fields with . notation" in {
    validateParser("x.y eq 'a'")
  }
  "The parser" should "be able to handle numerical values" in {
    validateParser("x.y eq 3")
    validateParser("x.y eq 3.2")
  }
  "The parser" should "be able to handle single and double quotes for strings as the value" in {
    validateParser("x.b eq 'single quotes'")
    validateParser("x.y eq \"double quotes\"")
  }
  "The parser" should "be able to handle true or false constants for the value" in {
    validateParser("x.y eq true")
    validateParser("x.y eq false")
  }
  "The parser" should "be able to handle mulitple nested queries" in {
    validateParser("(x ne 'y' and (a eq 'b' and c eq 'd')) or (b eq 'b' and (c eq 'b' or d eq 'd')) and e eq 'e'");
  }
  "The parser" should "be able to handle chained logic operations" in {
    validateParser("x.b ne 'y' and b eq 5 and c eq 'b' or d eq 'd' and e eq 'e'");
  }

}
