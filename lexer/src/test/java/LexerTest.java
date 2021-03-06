import JSONWriter.JSONFileWriter;
import java.util.List;
import lexer.PSLexer;
import org.junit.Assert;
import org.junit.Test;
import token.Token;

public class LexerTest {

  private void goldenFileAsserter(String testName, List<String> lines) {
    PSLexer psLexer = new PSLexer();
    List<Token> tokenList = psLexer.identifyTokens(lines);
    List<Token> goldenFile = JSONFileWriter.fileJSONToTokenList("print1-0/" + testName);
    if (goldenFile.isEmpty()) JSONFileWriter.tokenListToJSON(tokenList, "print1-0/" + testName);
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
  public void test10_MultipleNumberOperations() {
    final String testname = "LexerTest_test10_MultipleNumberOperations";
    String line1 = "let cuenta: number = 5*5-8/4+2;";
    String line2 = "printLn(cuenta);";
    goldenFileAsserter(testname, List.of(line1, line2));
  }

  @Test
  public void test11_NegativeNumber() {
    final String testname = "LexerTest_test11_NegativeNumber";
    String line1 = "let a: number = -5;";
    String line2 = "printLn(a);";
    goldenFileAsserter(testname, List.of(line1, line2));
  }

  @Test
  public void testtck_1() {
    final String testname = "Lexer_tck_arithmeticOperations";
    String line1 = "let numberResult: number = 5 * 5 - 8;";
    String line2 = "println(numberResult);";
    goldenFileAsserter(testname, List.of(line1, line2));
  }

  @Test
  public void testtck_2() {
    final String testname = "Lexer_tck_printComplexOperation";
    String line1 = "let pi: number;";
    String line2 = "pi = 3.14;";
    String line3 = "println(pi / 2);";
    goldenFileAsserter(testname, List.of(line1, line2, line3));
  }

  @Test(expected = RuntimeException.class)
  public void test12_PrintWithoutSemicolon() {
    final String testname = "LexerTest_test12_PrintWithoutSemicolon";
    String line3 = "println(\"Hola\")";
    goldenFileAsserter(testname, List.of(line3));
  }
}
