import JSONWriter.JSONFileWriter;
import lexer.PS11Lexer;
import lexer.PSLexer;
import org.junit.Assert;
import org.junit.Test;
import token.Token;
import java.util.List;

public class Lexer11Test {
  private void goldenFileAsserter(String testName, List<String> lines) {
    PS11Lexer psLexer = new PS11Lexer();
    List<Token> tokenList = psLexer.identifyTokens(lines);
    List<Token> goldenFile = JSONFileWriter.fileJSONToTokenList("print1-1/" + testName);
    if (goldenFile.isEmpty())
      JSONFileWriter.tokenListToJSON(tokenList, "print1-1/" + testName);
    else
      Assert.assertEquals(goldenFile, tokenList);
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
}
