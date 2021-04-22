import ASTNode.ASTNode;
import ASTNode.NotChildless.ASTNodeAssignation;
import JSONWriter.JSONFileWriter;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import syntactic_analyzer.PS11SyntacticAnalyzer;
import syntactic_analyzer.PSSyntacticAnalyzer;
import token.Token;

public class SyntacticAnalyzerTest11 {
  static String folder = "print1-1/";

  @Test
  public void test000_GivenStringAndVariableNameShouldReturnDeclarationNode() {
    final String testName = folder + "LexerTest_test01_StringDeclaration";
    List<Token> gF = JSONFileWriter.fileJSONToTokenList(testName);
    PS11SyntacticAnalyzer psSyntacticAnalyzer = new PS11SyntacticAnalyzer();
    List<ASTNode> tree =
        psSyntacticAnalyzer.analyze(List.of(gF.get(0), gF.get(1), gF.get(2), gF.get(3), gF.get(6)));
    Assert.assertEquals("declaration", tree.get(0).getNodeType());
  }

  @Test
  public void test001_GivenStringVariableAndValueShouldReturnAssignationNode() {
    final String testName = folder + "LexerTest_test01_StringDeclaration";
    List<Token> goldenFile = JSONFileWriter.fileJSONToTokenList(testName);
    PSSyntacticAnalyzer psSyntacticAnalyzer = new PSSyntacticAnalyzer();
    List<ASTNode> tree = psSyntacticAnalyzer.analyze(goldenFile);
    Assert.assertEquals("assignation", tree.get(0).getNodeType());
  }

  @Test
  public void test002_GivenMultipleVariablesAndValuesShouldReturnMultipleAssignationNodes() {
    final String testName = folder + "LexerTest_test02_MultipleLines";
    List<Token> goldenFile = JSONFileWriter.fileJSONToTokenList(testName);
    PSSyntacticAnalyzer psSyntacticAnalyzer = new PSSyntacticAnalyzer();
    List<ASTNode> tree = psSyntacticAnalyzer.analyze(goldenFile);
    Assert.assertEquals("assignation", tree.get(0).getNodeType());
    Assert.assertEquals("assignation", tree.get(1).getNodeType());
  }

  @Test
  public void test003_GivenIntegerDeclarationAndValueShouldReturnAssignationNode() {
    final String testName = folder + "LexerTest_test03_IntegerDeclaration";
    List<Token> goldenFile = JSONFileWriter.fileJSONToTokenList(testName);
    PSSyntacticAnalyzer psSyntacticAnalyzer = new PSSyntacticAnalyzer();
    List<ASTNode> tree = psSyntacticAnalyzer.analyze(goldenFile);
    Assert.assertEquals("assignation", tree.get(0).getNodeType());
  }

  @Test
  public void test004_GivenFloatDeclarationAndValueShouldReturnAssignationNode() {
    final String testName = folder + "LexerTest_test04_FloatDeclaration";
    List<Token> goldenFile = JSONFileWriter.fileJSONToTokenList(testName);
    PSSyntacticAnalyzer psSyntacticAnalyzer = new PSSyntacticAnalyzer();
    List<ASTNode> tree = psSyntacticAnalyzer.analyze(goldenFile);
    Assert.assertEquals("assignation", tree.get(0).getNodeType());
  }

  @Test
  public void test005_GivenDeclarationAndLaterValueShouldReturnDeclarationAndAssignationNode() {
    final String testName = folder + "LexerTest_test05_DeclarationAndLaterAssignation";
    List<Token> goldenFile = JSONFileWriter.fileJSONToTokenList(testName);
    PSSyntacticAnalyzer psSyntacticAnalyzer = new PSSyntacticAnalyzer();
    List<ASTNode> tree = psSyntacticAnalyzer.analyze(goldenFile);
    Assert.assertEquals("declaration", tree.get(0).getNodeType());
    Assert.assertEquals("assignation", tree.get(1).getNodeType());
  }

  @Test
  public void test006_GivenAnOperationAndDeclarationsShouldReturnAssignationNodes() {
    final String testName = folder + "LexerTest_test06_OperationWithVariables";
    List<Token> goldenFile = JSONFileWriter.fileJSONToTokenList(testName);
    PSSyntacticAnalyzer psSyntacticAnalyzer = new PSSyntacticAnalyzer();
    List<ASTNode> tree = psSyntacticAnalyzer.analyze(goldenFile);
    Assert.assertEquals("declaration", tree.get(0).getNodeType());
    Assert.assertEquals("assignation", tree.get(1).getNodeType());
    Assert.assertEquals("declaration", tree.get(2).getNodeType());
    Assert.assertEquals("assignation", tree.get(3).getNodeType());
    Assert.assertEquals("assignation", tree.get(4).getNodeType());
  }

  @Test
  public void test007_GivenOperationsAndDeclarationsShouldReturnAssignationNodes() {
    final String testName = folder + "LexerTest_test07_UsingOperationsToAssignValues";
    List<Token> goldenFile = JSONFileWriter.fileJSONToTokenList(testName);
    PSSyntacticAnalyzer psSyntacticAnalyzer = new PSSyntacticAnalyzer();
    List<ASTNode> tree = psSyntacticAnalyzer.analyze(goldenFile);
    for (int i = 0; i < tree.size(); i++) {
      Assert.assertEquals("assignation", tree.get(i).getNodeType());
    }
  }

  @Test
  public void test008_GivenPrintShouldReturnPrintNodes() {
    final String testName = folder + "LexerTest_test08_printLn";
    List<Token> goldenFile = JSONFileWriter.fileJSONToTokenList(testName);
    PSSyntacticAnalyzer psSyntacticAnalyzer = new PSSyntacticAnalyzer();
    List<ASTNode> tree = psSyntacticAnalyzer.analyze(goldenFile);
    Assert.assertEquals("print", tree.get(0).getNodeType());
    Assert.assertEquals("print", tree.get(3).getNodeType());
  }

  @Test
  public void test009_GivenStringConcatShouldReturnValidString() {
    final String testName = folder + "LexerTest_test09_StringConcat";
    List<Token> goldenFile = JSONFileWriter.fileJSONToTokenList(testName);
    PSSyntacticAnalyzer psSyntacticAnalyzer = new PSSyntacticAnalyzer();
    List<ASTNode> tree = psSyntacticAnalyzer.analyze(goldenFile);
    Assert.assertEquals("assignation", tree.get(3).getNodeType());
    Assert.assertEquals(
        "operation", ((ASTNodeAssignation) tree.get(3)).getRightChild().getNodeType());
  }

  @Test
  public void test016_GivenConstVariableShouldReturnDeclarationNode() {
    final String testName = folder + "LexerTest_test16_const";
    List<Token> gF = JSONFileWriter.fileJSONToTokenList(testName);
    PS11SyntacticAnalyzer psSyntacticAnalyzer = new PS11SyntacticAnalyzer();
    List<ASTNode> tree =
        psSyntacticAnalyzer.analyze(List.of(gF.get(0), gF.get(1), gF.get(2), gF.get(3), gF.get(6)));
    Assert.assertEquals("declaration", tree.get(0).getNodeType());
  }
}
