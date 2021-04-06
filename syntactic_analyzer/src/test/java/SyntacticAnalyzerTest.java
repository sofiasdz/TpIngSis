import ASTNode.ASTNode;
import ASTNode.Childless.ASTNodeVariableType;
import ASTNode.NotChildless.ASTNodeAssignation;
import ASTNode.NotChildless.ASTNodeDeclaration;
import org.junit.Assert;
import org.junit.Test;
import syntactic_analyzer.PSSyntacticAnalyzer;
import token.Token;
import org.junit.Assert;
import org.junit.Test;
import token.Token;
//import JSONWriter.JSONFileWriter;

import java.util.List;


public class SyntacticAnalyzerTest {

        @Test
        public void test01_GivenStringDeclarationTokensShouldReturnValidTree(){
            PSSyntacticAnalyzer psSyntacticAnalyzer= new PSSyntacticAnalyzer();
            ASTNode tree= psSyntacticAnalyzer.analize();
            ASTNode expectedTree;
            ASTNode rightChild=new ASTNodeDeclaration(new ASTNodeVariableType())
         /*   PSLexer psLexer = new PSLexer();
            List<Token> tokenList = psLexer.identifyTokens(List.of(line));
            List<Token> goldenFile = JSONFileWriter.fileJSONToTokenList(testName);
            if(goldenFile.isEmpty()) JSONFileWriter.tokenListToJSON(tokenList,testName);
            else Assert.assertEquals(goldenFile,tokenList);*/
        }
}
