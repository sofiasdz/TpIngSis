import ASTNode.ASTNode;
import ASTNode.Childless.ASTNodeChildless;
import ASTNode.NotChildless.ASTNodeNotChildless;
import Interpreter.PS11Interpreter;
import java.util.List;
import lexer.PS11Lexer;
import org.junit.Assert;
import org.junit.Test;
import syntactic_analyzer.PS11SyntacticAnalyzer;

public class Interpreter11Test {

  private List<String> analyze(List<String> lines) {
    PS11Lexer lex = new PS11Lexer();
    PS11SyntacticAnalyzer syn = new PS11SyntacticAnalyzer();
    PS11Interpreter interpreter = new PS11Interpreter();
    return interpreter.analyze(syn.analyze(lex.identifyTokens(lines)));
  }

  private List<ASTNode> getASTNode(List<String> lines) {
    PS11Lexer lex = new PS11Lexer();
    PS11SyntacticAnalyzer syn = new PS11SyntacticAnalyzer();
    return syn.analyze(lex.identifyTokens(lines));
  }

  private List<String> analyzeFromNode(List<ASTNode> list) {
    PS11Interpreter interpreter = new PS11Interpreter();
    return interpreter.analyze(list);
  }

  @Test
  public void test01_GivenAnIntegerDeclarationOperationAndPrintShouldReturnAValidPrintList() {
    String line1 = "let x : number = 12 + 2 ;";
    String line2 = "printLn(x) ;";
    List<String> prints = analyze((List.of(line1, line2)));
    Assert.assertEquals("14", prints.get(0));
  }

  @Test
  public void test02_GivenAStringDeclarationOperationAndPrintShouldReturnAValidPrintList() {
    String line1 = "let x : string = \"hello\" + \" world\" ;";
    String line2 = "printLn(x) ;";
    List<String> prints = analyze((List.of(line1, line2)));
    Assert.assertEquals("hello world", prints.get(0));
  }

  @Test
  public void test03_GivenMultipleStringDeclarationOperationAndPrintShouldReturnAValidPrintList() {
    String line1 = "let x : string = \"hello\" ;";
    String line2 = "let y : string = \" world\" ;";
    String line3 = "let z : string ;";
    String line4 = "z = x + y ;";
    String line5 = "printLn(z) ;";
    List<String> prints = analyze((List.of(line1, line2, line3, line4, line5)));
    Assert.assertEquals("hello world", prints.get(0));
  }

  @Test
  public void test04_GivenMultipleNumberDeclarationOperationAndPrintShouldReturnAValidPrintList() {
    String line1 = "let x : number = 4 ;";
    String line2 = "let y : number = 2 ;";
    String line3 = "let suma : number = x + y ;";
    String line4 = "let mult : number = x * y ;";
    String line5 = "let rest : number = x - y ;";
    String line6 = "let divi : number = x / y ;";
    String line7 = "printLn(suma) ;";
    String line8 = "printLn(mult) ;";
    String line9 = "printLn(rest) ;";
    String line10 = "printLn(divi) ;";
    List<String> prints =
        analyze((List.of(line1, line2, line3, line4, line5, line6, line7, line8, line9, line10)));
    Assert.assertEquals("6", prints.get(0));
    Assert.assertEquals("8", prints.get(1));
    Assert.assertEquals("2", prints.get(2));
    Assert.assertEquals("2", prints.get(3));
  }

  @Test(expected = IllegalArgumentException.class)
  public void test05_DeclaringSameVariableTwiceShouldThrowException() {
    String line1 = "let x : string = \"hello\" ;";
    String line2 = "let x : string = \" world\" ;";
    List<String> prints = analyze((List.of(line1, line2)));
  }

  @Test
  public void test06_AssigningValueToANonDeclaredVariableShouldThrowException() {
    String line1 = "let x : number = 4 ;";
    String line2 = "let y : number = 2 ;";
    String line3 = "y = y + 2 ;";
    String line4 = "printLn(y);";
    List<String> prints = analyze((List.of(line1, line2, line3, line4)));
    Assert.assertEquals("4", prints.get(0));
  }

  @Test(expected = RuntimeException.class)
  public void test07_AssigningNumberToStringShouldThrowException() {
    String line1 = "let x : string;";
    String line2 = "x = 324 ;";
    List<String> prints = analyze((List.of(line1, line2)));
  }

  @Test(expected = RuntimeException.class)
  public void test08_AssigningStringToNumberShouldThrowException() {
    String line1 = "let x : number;";
    String line2 = "x = \"324\" ;";
    List<String> prints = analyze((List.of(line1, line2)));
  }

  @Test
  public void test09_AssigningAnotherVariablesValueToANewOne() {
    String line1 = "let x : number = 3;";
    String line2 = "let y : number = x;";
    String line3 = "printLn(y);";
    String line4 = "let z : string = \"hello\";";
    String line5 = "let w : string = z;";
    String line6 = "printLn(w);";
    List<String> prints = analyze((List.of(line1, line2, line3, line4, line5, line6)));
    Assert.assertEquals("3", prints.get(0));
    Assert.assertEquals("hello", prints.get(1));
  }

  @Test
  public void test10_AddingStringWithNumber() {
    String line1 = "let x : string = \"hello \";";
    String line2 = "let y : number = 5;";
    String line3 = "let z : string = x + y;";
    String line4 = "printLn(z);";
    List<String> prints = analyze((List.of(line1, line2, line3, line4)));
    Assert.assertEquals("hello 5", prints.get(0));
  }

  @Test
  public void test10_AddingMultipleStrings() {
    String line1 = "let x : string = \"hello \";";
    String line2 = "let y : string = \"world \";";
    String line3 = "let z : string = x + y + \"que tal?\";";
    String line4 = "printLn(z);";
    List<String> prints = analyze((List.of(line1, line2, line3, line4)));
    Assert.assertEquals("hello world que tal?", prints.get(0));
  }

  @Test(expected = RuntimeException.class)
  public void test11_SubstractingStringsShouldThrowException() {
    String line1 = "let x : string = \"hello \";";
    String line2 = "let y : string = \"world \";";
    String line3 = "let z : string = x - y;";
    String line4 = "let w : string = \"hola\" - \"que tul\";";
    List<String> prints = analyze((List.of(line1, line2, line3, line4)));
  }

  @Test(expected = RuntimeException.class)
  public void test12_AssigningNumberToStringShouldThrowException() {
    String line1 = "let x : number = 20;";
    String line2 = "x=4;"; // cambiar a y
    List<ASTNode> nodes = getASTNode((List.of(line1, line2)));
    ASTNodeNotChildless node = (ASTNodeNotChildless) nodes.get(1);
    ASTNodeChildless leftChild = (ASTNodeChildless) node.getLeftChild();
    leftChild.token.setValue("y");
    node.setLeftChild(leftChild);
    nodes.set(1, node);
    List<String> prints = analyzeFromNode(nodes);
  }

  @Test(expected = RuntimeException.class)
  public void test13_AssigningNumberToStringShouldThrowException() {
    String line1 = "let x : number = 20;";
    String line2 = "printLn(x);";
    List<ASTNode> nodes = getASTNode((List.of(line1, line2)));
    List<String> prints = analyzeFromNode(List.of(nodes.get(1)));
  }

  @Test(expected = RuntimeException.class)
  public void test14_AssigningVariableTwiceShouldThrowException() {
    String line1 = "let x : number = 20;";
    String line2 = "let y: number = 3;";
    List<ASTNode> nodes = getASTNode((List.of(line1, line2)));
    List<String> prints = analyzeFromNode(List.of(nodes.get(1), nodes.get(1)));
  }

  @Test(expected = RuntimeException.class)
  public void test15_DeclaringVariableTwiceShouldThrowException() {
    String line1 = "let x:number;";
    List<ASTNode> nodes = getASTNode((List.of(line1)));
    List<String> prints = analyzeFromNode(List.of(nodes.get(0), nodes.get(0)));
  }

  @Test
  public void test12_BooleanShouldReturnTrue() {
    String line1 = "let test: boolean = true;";
    String line2 = "printLn(test);";
    List<String> prints = analyze((List.of(line1, line2)));
    Assert.assertEquals("true", prints.get(0));
  }

  @Test
  public void test13_BooleanShouldReturnFalse() {
    String line1 = "let test: boolean = false;";
    String line2 = "printLn(test);";
    List<String> prints = analyze((List.of(line1, line2)));
    Assert.assertEquals("false", prints.get(0));
  }

  @Test
  public void test14_VariableWithGreaterThan() {
    String line1 = "let x : number = 3;";
    String line2 = "let y : number = 6;";
    String line3 = "let test: boolean = x>y;";
    String line4 = "printLn(test);";
    List<String> prints = analyze((List.of(line1, line2, line3, line4)));
    Assert.assertEquals("false", prints.get(0));
  }

  @Test
  public void test15_VariableWithSmallerThan() {
    String line1 = "let x : number = 3;";
    String line2 = "let y : number = 6;";
    String line3 = "let test: boolean = x<y;";
    String line4 = "printLn(test);";
    List<String> prints = analyze((List.of(line1, line2, line3, line4)));
    Assert.assertEquals("true", prints.get(0));
  }

  @Test
  public void test16_VariableWithEqualOrGreaterThan() {
    String line1 = "let x : number = 3;";
    String line2 = "let y : number = 3;";
    String line3 = "let test: boolean = x>=y;";
    String line4 = "printLn(test);";
    List<String> prints = analyze((List.of(line1, line2, line3, line4)));
    Assert.assertEquals("true", prints.get(0));
  }

  @Test
  public void test17_VariableWithEqualOrSmallerThan() {
    String line1 = "let x : number = 3;";
    String line2 = "let y : number = 3;";
    String line3 = "let test: boolean = x<=y;";
    String line4 = "printLn(test);";
    List<String> prints = analyze((List.of(line1, line2, line3, line4)));
    Assert.assertEquals("true", prints.get(0));
  }

  @Test
  public void test18_VariableConst() {
    String line1 = "const gravity: number = 10 ;";
    String line2 = "printLn(gravity);";
    List<String> prints = analyze((List.of(line1, line2)));
    Assert.assertEquals("10", prints.get(0));
  }

  @Test(expected = RuntimeException.class)
  public void test19_ChangingAConstShouldThrowException() {
    String line1 = "const gravity: number = 10 ;";
    String line2 = "gravity = 9;";
    List<String> prints = analyze((List.of(line1, line2)));
  }

  @Test(expected = RuntimeException.class)
  public void test20_AsingningAStringToABooleanShouldThrowException() {
    String line1 = "let test: boolean = \"hola\";";
    List<String> prints = analyze((List.of(line1)));
  }

  @Test(expected = RuntimeException.class)
  public void test21_AsingningANumberToABooleanShouldThrowException() {
    String line1 = "let test: boolean = 9;";
    List<String> prints = analyze((List.of(line1)));
  }

  @Test
  public void test22_VariableConstBoolean() {
    String line1 = "const isOn: boolean = true ;";
    String line2 = "printLn(isOn);";
    List<String> prints = analyze((List.of(line1, line2)));
    Assert.assertEquals("true", prints.get(0));
  }

  @Test
  public void test23_ConditionalStatement1() {
    String line1 = "if(true){ let x: number = 1;}";
    String line2 = "else { let x: number = 2;}";
    String line3 = "printLn(x);";
    List<String> prints = analyze((List.of(line1, line2, line3)));
    Assert.assertEquals("1", prints.get(0));
  }

  @Test
  public void test24_ConditionalStatement2() {
    String line1 = "if(false){ let x: number = 1;}";
    String line2 = "else { let x: number = 2;}";
    String line3 = "printLn(x);";
    List<String> prints = analyze((List.of(line1, line2, line3)));
    Assert.assertEquals("2", prints.get(0));
  }

  @Test
  public void test24_ConditionalStatement3() {
    String line1 = "let x : number = 3;";
    String line2 = "let y : number = 6;";
    String line3 = "if(x>6){ let a: number = 1;}";
    String line4 = "else { let a: number = 2;}";
    String line5 = "printLn(a);";
    List<String> prints = analyze((List.of(line1, line2, line3, line4, line5)));
    Assert.assertEquals("2", prints.get(0));
  }

  @Test
  public void test24_ConditionalStatement4() {
    String line1 = "let x : number = 3;";
    String line2 = "let y : number = 6;";
    String line3 = "if(x<6){ let a: number = 1;}";
    String line4 = "else { let a: number = 2;}";
    String line5 = "printLn(a);";
    List<String> prints = analyze((List.of(line1, line2, line3, line4, line5)));
    Assert.assertEquals("1", prints.get(0));
  }

  @Test
  public void test25_MultipleNumberOperation() {
    String line1 = "let cuenta: number = 5*5-8/4+2;";
    String line2 = "printLn(cuenta);";
    List<String> prints = analyze((List.of(line1, line2)));
    Assert.assertEquals("25", prints.get(0));
  }

  @Test
  public void test26_NegativeNumberDeclaration() {
    String line1 = "let a: number = -5;";
    String line2 = "printLn(a);";
    List<String> prints = analyze((List.of(line1, line2)));
    Assert.assertEquals("-5", prints.get(0));
  }

  @Test
  public void test26_NegativeNumberAsAResult() {
    String line1 = "let a: number = 3-5;";
    String line2 = "printLn(a);";
    List<String> prints = analyze((List.of(line1, line2)));
    Assert.assertEquals("-2", prints.get(0));
  }

  @Test
  public void test27_ConcatenatingStringAndBoolean() {
    String line1 = "let x: string = \"true is: \";";
    String line2 = "let y: boolean = true;";
    String line3 = "let z: string = x + y;";
    String line4 = "printLn(z);";
    List<String> prints = analyze((List.of(line1, line2, line3, line4)));
    Assert.assertEquals("true is: true", prints.get(0));
  }

  @Test
  public void test28_ConcatenatingConstVariables() {
    String line1 = "const x: number = 3;";
    String line2 = "const y: boolean = true;";
    String line3 = "const z: string = x + y;";
    String line4 = "printLn(z);";
    List<String> prints = analyze((List.of(line1, line2, line3, line4)));
    Assert.assertEquals("3true", prints.get(0));
  }

  @Test
  public void test29_TCKIf() {
    String line1 = "const booleanResult: boolean = 5 > 3;";
    String line2 = "if(booleanResult){";
    String line3 = "println(\"if statement working correctly\");";
    String line4 = "}";
    String line5 = "println(\"outside of conditional\");";
    List<String> prints = analyze((List.of(line1, line2, line3, line4, line5)));
    Assert.assertEquals("if statement working correctly", prints.get(0));
  }

  @Test
  public void test29_TCKElse() {
    String line1 = "const booleanResult: boolean = 5 <= 3;";
    String line2 = "if(booleanResult){";
    String line3 = "}";
    String line4 = "else {";
    String line5 = "println(\"else statement working correctly\");";
    String line6 = "}";
    String line7 = "println(\"outside of conditional\");";
    List<String> prints = analyze((List.of(line1, line2, line3, line4, line5, line6, line7)));
    Assert.assertEquals("else statement working correctly", prints.get(0));
  }

  @Test(expected = RuntimeException.class)
  public void test30_TCKInvalidIf() {
    String line1 = "let a: number = 21;";
    String line2 = "if(a) {";
    String line3 = "println(\"this should fail, invalid argument in if statement\");";
    String line4 = "}";
    List<String> prints = analyze(List.of(line1, line2, line3, line4));
  }
}
