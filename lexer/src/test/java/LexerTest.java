import lexer.PSLexer;
import org.junit.Assert;
import org.junit.Test;
import token.Token;

import java.util.List;

import JSONWriter.JSONFileWriter;

public class LexerTest {

    @Test
    public void test01_GivenAStringDeclarationShouldReturnAValidTokensList(){
        final String testName = "LexerTest_test01_StringDeclaration";
        String line = "let variableName : string = \"olive\" ;";
        PSLexer psLexer = new PSLexer();
        List<Token> tokenList = psLexer.identifyTokens(List.of(line));
        List<Token> goldenFile = JSONFileWriter.fileJSONToTokenList(testName);
        if(goldenFile.isEmpty()) JSONFileWriter.tokenListToJSON(tokenList,testName);
        else Assert.assertEquals(goldenFile,tokenList);
    }

    @Test
    public void test02_GivenMultipleLinesShouldReturnAValidTokensList() {
        final String testName = "LexerTest_test02_MultipleLines";
        String line1 = "let name : string = \"Khalil\" ;";
        String line2 = "let lastName : string = \"Stessens\" ;";
        PSLexer psLexer= new PSLexer();
        List<Token> tokenList = psLexer.identifyTokens(List.of(line1,line2));
        List<Token> goldenFile = JSONFileWriter.fileJSONToTokenList(testName);
        if(goldenFile.isEmpty()) JSONFileWriter.tokenListToJSON(tokenList,testName);
        else Assert.assertEquals(goldenFile,tokenList);
    }

    @Test
    public void test03_GivenAnIntegerDeclarationShouldReturnAValidTokenList() {
        final String testName = "LexerTest_test03_IntegerDeclaration";
        String line1 = "let x : number = 12;";
        PSLexer psLexer= new PSLexer();
        List<Token> tokenList = psLexer.identifyTokens(List.of(line1));
        List<Token> goldenFile = JSONFileWriter.fileJSONToTokenList(testName);
        if(goldenFile.isEmpty()) JSONFileWriter.tokenListToJSON(tokenList,testName);
        else Assert.assertEquals(goldenFile,tokenList);
    }

    @Test
    public void test04_GivenAFloatDeclarationShouldReturnAValidTokenList() {
        final String testName = "LexerTest_test04_FloatDeclaration";
        String line1 = "let x : number = 12.04;";
        PSLexer psLexer= new PSLexer();
        List<Token> tokenList = psLexer.identifyTokens(List.of(line1));
        List<Token> goldenFile = JSONFileWriter.fileJSONToTokenList(testName);
        if(goldenFile.isEmpty()) JSONFileWriter.tokenListToJSON(tokenList,testName);
        else Assert.assertEquals(goldenFile,tokenList);
    }
}
