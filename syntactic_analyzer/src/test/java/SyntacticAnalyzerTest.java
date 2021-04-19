import ASTNode.ASTNode;
import JSONWriter.JSONFileWriter;
import org.junit.Assert;
import org.junit.Test;
import syntactic_analyzer.PSSyntacticAnalyzer;
import token.Token;
import token.TokenType;

import java.util.List;


public class SyntacticAnalyzerTest {


    @Test
    public void test000_GivenStringAndVariableNameShouldReturnDeclarationNode() {
        final String testName = "LexerTest_test01_StringDeclaration";
        List<Token> gF = JSONFileWriter.fileJSONToTokenList(testName);
        PSSyntacticAnalyzer psSyntacticAnalyzer = new PSSyntacticAnalyzer();
        List<ASTNode> tree = psSyntacticAnalyzer.analyze(List.of(gF.get(0),gF.get(1),gF.get(2),gF.get(3),gF.get(6)));
        Assert.assertEquals(TokenType.COLON, tree.get(0).token.getType());
    }

    @Test
    public void test001_GivenStringVariableAndValueShouldReturnAssignationNode() {
        final String testName = "LexerTest_test01_StringDeclaration";
        List<Token> goldenFile = JSONFileWriter.fileJSONToTokenList(testName);
        PSSyntacticAnalyzer psSyntacticAnalyzer = new PSSyntacticAnalyzer();
        List<ASTNode> tree = psSyntacticAnalyzer.analyze(goldenFile);
        Assert.assertEquals(TokenType.ASSIGNATION, tree.get(0).token.getType());
    }

    @Test
    public void test002_GivenMultipleVariablesAndValuesShouldReturnMultipleAssignationNodes() {
        final String testName = "LexerTest_test02_MultipleLines";
        List<Token> goldenFile = JSONFileWriter.fileJSONToTokenList(testName);
        PSSyntacticAnalyzer psSyntacticAnalyzer = new PSSyntacticAnalyzer();
        List<ASTNode> tree = psSyntacticAnalyzer.analyze(goldenFile);
        Assert.assertEquals(TokenType.ASSIGNATION, tree.get(0).token.getType());
        Assert.assertEquals(TokenType.ASSIGNATION, tree.get(1).token.getType());
    }

    @Test
    public void test003_GivenIntegerDeclarationAndValueShouldReturnAssignationNode() {
        final String testName = "LexerTest_test03_IntegerDeclaration";
        List<Token> goldenFile = JSONFileWriter.fileJSONToTokenList(testName);
        PSSyntacticAnalyzer psSyntacticAnalyzer = new PSSyntacticAnalyzer();
        List<ASTNode> tree = psSyntacticAnalyzer.analyze(goldenFile);
        Assert.assertEquals(TokenType.ASSIGNATION, tree.get(0).token.getType());
    }

    @Test
    public void test004_GivenFloatDeclarationAndValueShouldReturnAssignationNode() {
        final String testName = "LexerTest_test04_FloatDeclaration";
        List<Token> goldenFile = JSONFileWriter.fileJSONToTokenList(testName);
        PSSyntacticAnalyzer psSyntacticAnalyzer = new PSSyntacticAnalyzer();
        List<ASTNode> tree = psSyntacticAnalyzer.analyze(goldenFile);
        Assert.assertEquals(TokenType.ASSIGNATION, tree.get(0).token.getType());
    }

    @Test
    public void test005_GivenDeclarationAndLaterValueShouldReturnDeclarationAndAssignationNode() {
        final String testName = "LexerTest_test05_DeclarationAndLaterAssignation";
        List<Token> goldenFile = JSONFileWriter.fileJSONToTokenList(testName);
        PSSyntacticAnalyzer psSyntacticAnalyzer = new PSSyntacticAnalyzer();
        List<ASTNode> tree = psSyntacticAnalyzer.analyze(goldenFile);
        Assert.assertEquals(TokenType.COLON, tree.get(0).token.getType());
        Assert.assertEquals(TokenType.ASSIGNATION, tree.get(1).token.getType());
    }

    @Test
    public void test006_GivenAnOperationAndDeclarationsShouldReturnAssignationNodes() {
        final String testName = "LexerTest_test06_OperationWithVariables";
        List<Token> goldenFile = JSONFileWriter.fileJSONToTokenList(testName);
        PSSyntacticAnalyzer psSyntacticAnalyzer = new PSSyntacticAnalyzer();
        List<ASTNode> tree = psSyntacticAnalyzer.analyze(goldenFile);
        Assert.assertEquals(TokenType.COLON, tree.get(0).token.getType());
        Assert.assertEquals(TokenType.ASSIGNATION, tree.get(1).token.getType());
        Assert.assertEquals(TokenType.COLON, tree.get(2).token.getType());
        Assert.assertEquals(TokenType.ASSIGNATION, tree.get(3).token.getType());
        Assert.assertEquals(TokenType.ASSIGNATION, tree.get(4).token.getType());
    }

    @Test
    public void test007_GivenOperationsAndDeclarationsShouldReturnAssignationNodes() {
        final String testName = "LexerTest_test07_UsingOperationsToAssignValues";
        List<Token> goldenFile = JSONFileWriter.fileJSONToTokenList(testName);
        PSSyntacticAnalyzer psSyntacticAnalyzer = new PSSyntacticAnalyzer();
        List<ASTNode> tree = psSyntacticAnalyzer.analyze(goldenFile);
        for (int i = 0; i <tree.size(); i++) {
            Assert.assertEquals(TokenType.ASSIGNATION, tree.get(i).token.getType());
        }
    }

    @Test
    public void test008_GivenPrintShouldReturnPrintNodes() {
        final String testName = "LexerTest_test08_printLn";
        List<Token> goldenFile = JSONFileWriter.fileJSONToTokenList(testName);
        PSSyntacticAnalyzer psSyntacticAnalyzer = new PSSyntacticAnalyzer();
        List<ASTNode> tree = psSyntacticAnalyzer.analyze(goldenFile);
        Assert.assertEquals(TokenType.PRINTLN, tree.get(0).token.getType());
        Assert.assertEquals(TokenType.PRINTLN, tree.get(3).token.getType());
    }
}
