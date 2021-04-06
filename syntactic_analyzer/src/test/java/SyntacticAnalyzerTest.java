import org.junit.Assert;
import org.junit.Test;
import token.Token;
import org.junit.Assert;
import org.junit.Test;
import token.Token;
//import JSONWriter.JSONFileWriter;

import java.util.List;


public class SyntacticAnalyzerTest {

        @Test
        public void test01_GivenStringDeclarationTokensShouldReturnValidTree(){
            final String testName = "LexerTest_test01_StringDeclaration";
            String line = "let variableName : string = \"olive\";";
         /*   PSLexer psLexer = new PSLexer();
            List<Token> tokenList = psLexer.identifyTokens(List.of(line));
            List<Token> goldenFile = JSONFileWriter.fileJSONToTokenList(testName);
            if(goldenFile.isEmpty()) JSONFileWriter.tokenListToJSON(tokenList,testName);
            else Assert.assertEquals(goldenFile,tokenList);*/
        }
}
