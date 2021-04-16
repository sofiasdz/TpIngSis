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
import org.junit.Assert;
import org.junit.Test;
import token.Token;


import java.util.List;


public class SyntacticAnalyzerTest {

       @Test
       public void test01_GivenStringDeclarationTokensShouldReturnValidTree(){
           final String testName = "LexerTest_test01_StringDeclaration";
           List<Token> goldenFile = JSONFileWriter.fileJSONToTokenList(testName);
            String line = "let variableName : string = \"olive\";";
          PSSyntacticAnalyzer psSyntacticAnalyzer= new PSSyntacticAnalyzer();
           ASTNode tree= psSyntacticAnalyzer.analize(goldenFile);
           ASTNode expectedTree;
          ASTNode rightChild=new ASTNodeDeclaration(new ASTNodeVariableType(PrintScriptTokenFactory.stringType(4,15)),
                 new ASTNodeIdentifier(PrintScriptTokenFactory.identifier("variableName",4,15)),PrintScriptTokenFactory.let(0,2));
           ASTNode leftChild=new ASTNodeLiteral(PrintScriptTokenFactory.string(28,34,"olive"));
            expectedTree=new ASTNodeAssignation(leftChild,rightChild,PrintScriptTokenFactory.assignation(26,26));
         Assert.assertEquals(tree,expectedTree);
       }

}
