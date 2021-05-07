import ASTNode.ASTNode;
import ASTNode.Childless.ASTNodeChildless;
import ASTNode.NotChildless.ASTNodeNotChildless;
import Interpreter.PSInterpreter;
import java.util.List;
import lexer.PSLexer;
import org.junit.Assert;
import org.junit.Test;
import syntactic_analyzer.PSSyntacticAnalyzer;

public class InterpreterTest {

  private List<String> analyze(List<String> lines) {
    PSLexer lex = new PSLexer();
    PSSyntacticAnalyzer syn = new PSSyntacticAnalyzer();
    PSInterpreter interpreter = new PSInterpreter();
    return interpreter.analyze(syn.analyze(lex.identifyTokens(lines)));
  }

  private List<ASTNode> getASTNode(List<String> lines) {
    PSLexer lex = new PSLexer();
    PSSyntacticAnalyzer syn = new PSSyntacticAnalyzer();
    return syn.analyze(lex.identifyTokens(lines));
  }

  private List<String> analyzeFromNode(List<ASTNode> list) {
    PSInterpreter interpreter = new PSInterpreter();
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
    Assert.assertEquals("\"hello world\"", prints.get(0));
  }

  @Test
  public void test03_GivenMultipleStringDeclarationOperationAndPrintShouldReturnAValidPrintList() {
    String line1 = "let x : string = \"hello\" ;";
    String line2 = "let y : string = \" world\" ;";
    String line3 = "let z : string ;";
    String line4 = "z = x + y ;";
    String line5 = "printLn(z) ;";
    List<String> prints = analyze((List.of(line1, line2, line3, line4, line5)));
    Assert.assertEquals("\"hello world\"", prints.get(0));
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
    Assert.assertEquals("\"hello\"", prints.get(1));
  }

  @Test
  public void test10_AddingStringWithNumber() {
    String line1 = "let x : string = \"hello \";";
    String line2 = "let y : number = 5;";
    String line3 = "let z : string = x + y;";
    String line4 = "printLn(z);";
    List<String> prints = analyze((List.of(line1, line2, line3, line4)));
    Assert.assertEquals("\"hello 5.0\"", prints.get(0));
  }

  @Test
  public void test10_AddingMultipleStrings() {
    String line1 = "let x : string = \"hello \";";
    String line2 = "let y : string = \"world \";";
    String line3 = "let z : string = x + y + \"que tal?\";";
    String line4 = "printLn(z);";
    List<String> prints = analyze((List.of(line1, line2, line3, line4)));
    Assert.assertEquals("\"hello world que tal?\"", prints.get(0));
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
  public void test16_PrintingAComplexOperation(){
    String line1 = "let pi: number;";
    String line2 = "pi = 3.14;";
    String line3 = "pi = pi / 2;";
    String line4 = "println(pi);";
    List<String> prints = analyze(List.of(line1,line2,line3, line4));
  }
}
