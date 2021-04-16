import ASTNode.ASTNode;
import ASTNode.Childless.ASTNodeIdentifier;
import ASTNode.Childless.ASTNodeLiteral;
import ASTNode.Childless.ASTNodeVariableType;
import ASTNode.NotChildless.ASTNodeAssignation;
import ASTNode.NotChildless.ASTNodeDeclaration;
import JSONWriter.JSONFileWriter;
import org.junit.Assert;
import org.junit.Test;
import syntactic_analyzer.PSSyntacticAnalyzer;
import token.Token;
import token.PrintScriptTokenFactory;
import token.TokenType;


import java.util.List;


public class SyntacticAnalyzerTest {

       @Test
       public void test001_GivenStringAndVariableNameShouldReturnDeclarationNode(){
           final String testName = "LexerTest_test01_StringDeclaration";
           List<Token> goldenFile = JSONFileWriter.fileJSONToTokenList(testName);
           String line = "let variableName : string ";
           List<Token> tokens = List.of(goldenFile.get(0),goldenFile.get(1),goldenFile.get(2),goldenFile.get(3));
           PSSyntacticAnalyzer psSyntacticAnalyzer= new PSSyntacticAnalyzer();
           ASTNode tree = psSyntacticAnalyzer.analyze(tokens);
           Assert.assertEquals(TokenType.COLON,tree.token.getType());
       }

    @Test
    public void test002_GivenStringAndVariableNameShouldReturnDeclarationNode(){
        final String testName = "LexerTest_test01_StringDeclaration";
        List<Token> goldenFile = JSONFileWriter.fileJSONToTokenList(testName);
        String line = "let variableName : string = \"olive\" ;";
        PSSyntacticAnalyzer psSyntacticAnalyzer= new PSSyntacticAnalyzer();
        ASTNode tree = psSyntacticAnalyzer.analyze(goldenFile);
        Assert.assertEquals(TokenType.ASSIGNATION,tree.token.getType());
    }
}
