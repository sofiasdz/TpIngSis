import JSONWriter.JSONFileWriter;
import java.util.List;
import lexer.PS11Lexer;
import org.junit.Assert;
import org.junit.Test;
import token.Token;

public class Lexer11Test {
  private void goldenFileAsserter(String testName, List<String> lines) {
    PS11Lexer psLexer = new PS11Lexer();
    List<Token> tokenList = psLexer.identifyTokens(lines);
    List<Token> goldenFile = JSONFileWriter.fileJSONToTokenList("print1-1/" + testName);
    if (goldenFile.isEmpty()) JSONFileWriter.tokenListToJSON(tokenList, "print1-1/" + testName);
    else Assert.assertEquals(goldenFile, tokenList);
  }

  @Test
  public void test01_GivenAStringDeclarationShouldReturnAValidTokensList() {
    final String testName = "LexerTest_test01_StringDeclaration";
    String line = "let variableName : string = \"olive\";";
    goldenFileAsserter(testName, List.of(line));
  }

  @Test
  public void test02_GivenMultipleLinesShouldReturnAValidTokensList() {
    final String testName = "LexerTest_test02_MultipleLines";
    String line1 = "let name : string = \"Khalil\" ;";
    String line2 = "let lastName : string = \"Stessens\" ;";
    goldenFileAsserter(testName, List.of(line1, line2));
  }

  @Test
  public void test03_GivenAnIntegerDeclarationShouldReturnAValidTokenList() {
    final String testName = "LexerTest_test03_IntegerDeclaration";
    String line1 = "let x : number = 12 ;";
    goldenFileAsserter(testName, List.of(line1));
  }

  @Test
  public void test04_GivenAFloatDeclarationShouldReturnAValidTokenList() {
    final String testName = "LexerTest_test04_FloatDeclaration";
    String line1 = "let x : number = 12.04;";
    goldenFileAsserter(testName, List.of(line1));
  }

  @Test
  public void test05_GivenAVariableDeclarationAndLaterDefiningAValueShouldReturnAValidTokenList() {
    final String testName = "LexerTest_test05_DeclarationAndLaterAssignation";
    String line1 = "let x : number;";
    String line2 = "x = 24;";
    goldenFileAsserter(testName, List.of(line1, line2));
  }

  @Test
  public void test06_GivenAVariableOperationShouldReturnAValidTokenList() {
    final String testName = "LexerTest_test06_OperationWithVariables";
    String line1 = "let x : number;";
    String line2 = "x = 24;";
    String line3 = "let y :number ;";
    String line4 = "y = 16 ;";
    String line5 = "let z : number = x + y ;";
    goldenFileAsserter(testName, List.of(line1, line2, line3, line4, line5));
  }

  @Test
  public void test07_GivenMultipleOperationsShouldReturnAValidTokenList() {
    final String testName = "LexerTest_test07_UsingOperationsToAssignValues";
    String line1 = "let suma : number = 22 + 2;";
    String line2 = "let resta : number = 26 - 2 ;";
    String line3 = "let division : number = 48 / 2 ;";
    String line4 = "let multiplicacion : number = 12 * 2;";
    String line5 = "let concatenacion : string = \"holi\" + \"mundo\" ;";
    goldenFileAsserter(testName, List.of(line1, line2, line3, line4, line5));
  }

  @Test
  public void test08_GivenPrintLnTokenShouldReturnAValidToken() {
    final String testname = "LexerTest_test08_printLn";
    String line1 = "printLn(123) ;";
    String line2 = "printLn(\"holi\") ;";
    String line3 = "let x : number = 32 ;";
    String line4 = "printLn(x);";
    goldenFileAsserter(testname, List.of(line1, line2, line3, line4));
  }

  @Test
  public void test09_GivenTwoStringsConcatenationShouldReturnAValidToken() {
    final String testname = "LexerTest_test09_StringConcat";
    String line1 = "let variableName : string = \"Khali\" ;";
    String line2 = "let variableLastName : string = \"Stessens\" ;";
    String line3 = "let fullName : string ;";
    String line4 = "fullName = variableName + variableLastName ;";
    String line5 = "printLn(fullName);";
    goldenFileAsserter(testname, List.of(line1, line2, line3, line4, line5));
  }

  @Test
  public void test010_GivenABooleanShouldReturnAValidToken() {
    final String testname = "LexerTest_test10_boolean";
    String line1 = "let test: boolean = true;";
    goldenFileAsserter(testname, List.of(line1));
  }

  @Test
  public void test011_GivenABooleanShouldReturnAValidToken() {
    final String testname = "LexerTest_test11_boolean";
    String line1 = "let test: boolean = false;";
    goldenFileAsserter(testname, List.of(line1));
  }

  @Test
  public void test012_GivenAGreaterThanShouldReturnAValidToken() {
    final String testname = "LexerTest_test12_greater";
    String line1 = "let x: number = 2;";
    String line2 = "let y: number = 4;";
    String line3 = "let test: boolean = x>y;";
    goldenFileAsserter(testname, List.of(line1, line2, line3));
  }

  @Test
  public void test013_GivenASmallerThanShouldReturnAValidToken() {
    final String testname = "LexerTest_test13_smaller";
    String line1 = "let x: number = 2;";
    String line2 = "let y: number = 4;";
    String line3 = "let test: boolean = x<y;";
    goldenFileAsserter(testname, List.of(line1, line2, line3));
  }

  @Test
  public void test014_GivenASmallerOrEqualThanShouldReturnAValidToken() {
    final String testname = "LexerTest_test14_smallerOrEqual";
    String line1 = "let x: number = 2;";
    String line2 = "let y: number = 4;";
    String line3 = "let test: boolean = x<=y;";
    goldenFileAsserter(testname, List.of(line1, line2, line3));
  }

  @Test
  public void test015_GivenAGreaterOrEqualThanShouldReturnAValidToken() {
    final String testname = "LexerTest_test15_greaterOrEqual";
    String line1 = "let x: number = 2;";
    String line2 = "let y: number = 4;";
    String line3 = "let test: boolean = x>=y;";
    goldenFileAsserter(testname, List.of(line1, line2, line3));
  }

  @Test
  public void test016_GivenAConstShouldReturnAValidToken() {
    final String testname = "LexerTest_test16_const";
    String line1 = "const gravity: number = 10 ;";
    goldenFileAsserter(testname, List.of(line1));
  }

  @Test
  public void test017_GivenAnIfStatementShouldReturnAValidToken() {
    final String testname = "LexerTest_test17_if";
    String line1 = "if(true){ let x: number = 1;}";
    goldenFileAsserter(testname, List.of(line1));
  }

  @Test
  public void test018_GivenAnIfElseStatementReturnAValidToken() {
    final String testname = "LexerTest_test18_ifElse";
    String line1 = "if(true){ let x: number = 1;}";
    String line2 = "else { let x: number = 2;}";
    goldenFileAsserter(testname, List.of(line1, line2));
  }

  @Test
  public void test19_MultipleNumberOperations() {
    final String testname = "LexerTest_test19_MultipleNumberOperations";
    String line1 = "let cuenta: number = 5*5-8/4+2;";
    String line2 = "printLn(cuenta);";
    goldenFileAsserter(testname, List.of(line1, line2));
  }

  @Test
  public void test20_NegativeNumber() {
    final String testname = "LexerTest_test20_NegativeNumber";
    String line1 = "let a: number = -5;";
    String line2 = "printLn(a);";
    goldenFileAsserter(testname, List.of(line1, line2));
  }

  @Test
  public void test21_IfElseStatements() {
    final String testname = "LexerTest_test21_IfElseStatements";
    String line1 = "let a : number = 20;";
    String line2 = "if(true) {";
    String line3 = "a = a * 2;";
    String line4 = "} else {";
    String line5 = "a = a / 2;";
    String line6 = "}";
    String line7 = "printLn(a);";
    goldenFileAsserter(testname, List.of(line1, line2, line3, line4, line5, line6, line7));
  }

  @Test
  public void test22_IfStatement() {
    final String testname = "LexerTest_test22_IfStatements";
    String line1 = "let a : number = 20;";
    String line2 = "if(true) {";
    String line3 = "a = a * 2;";
    String line4 = "printLn(a);";
    String line5 = "}";
    String line6 = "if( a > 10 ) {";
    String line7 = "a = 5;";
    String line8 = "}";
    String line9 = "printLn(a);";
    goldenFileAsserter(
        testname, List.of(line1, line2, line3, line4, line5, line6, line7, line8, line9));
  }

  @Test
  public void test23_BooleanVariables() {
    final String testname = "LexerTest_test23_BooleanVariables";
    String line1 = "let a : boolean = true;";
    String line2 = "a = 23 > 10;";
    String line3 = "printLn(a);";
    String line4 = "a = 2 >= 4;";
    String line5 = "printLn(a);";
    goldenFileAsserter(testname, List.of(line1, line2, line3, line4, line5));
  }

  @Test
  public void test24_IfElseStatement() {
    final String testname = "LexerTest_test24_IfElseStatements";
    String line1 = "let a : number = 20;";
    String line2 = "if(false) {";
    String line3 = "a = a * 2;";
    String line4 = "}";
    String line5 = "else {";
    String line6 = "a = 5;";
    String line7 = "}";
    String line8 = "printLn(a);";
    goldenFileAsserter(testname, List.of(line1, line2, line3, line4, line5, line6, line7, line8));
  }

  @Test
  public void test25_NumericalOperators() {
    final String testname = "LexerTest_test25_NumericalOperators";
    String line1 = "let a : number = 2 * 4 + 4 * 3 / 2 * 4 + 3 / 6 * 2 ;";
    goldenFileAsserter(testname, List.of(line1));
  }
}
