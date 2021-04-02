import lexer.PSLexer;
import org.junit.Assert;
import org.junit.Test;
import token.Token;

import java.util.ArrayList;
import java.util.List;

public class LexerTest {

    @Test
    public void test01_GivenAStringDeclarationShouldReturnATokensListOfSize7(){
        String line = "let variableName : string = \"olive\" ;";
        PSLexer psLexer = new PSLexer();
        List<Token> tokenList = psLexer.identifyTokens(List.of(line));
        Assert.assertEquals(6,tokenList.size());
    }

    @Test
    public void test02() {
        String line1 = "let name : string = \"Khali\" ;";
        String line2 = "let lastName : string = \"Stessens\" ;";
        List<String> list= new ArrayList();
        list.add(line1);
        list.add(line2);
        PSLexer psLexer= new PSLexer();
        List<Token> tokenList = psLexer.identifyTokens(list);
        System.out.println("finished");

    }
    @Test
    public void test03() {
        String line1 = "let x : number = 12;";
        List<String> list= new ArrayList();
        list.add(line1);
        PSLexer psLexer= new PSLexer();
        List<Token> tokenList = psLexer.identifyTokens(list);
        System.out.println("finished");

    }
    @Test
    public void test04() {
        String line1 = "let x : number = 12.04;";
        List<String> list= new ArrayList();
        list.add(line1);
        PSLexer psLexer= new PSLexer();
        List<Token> tokenList = psLexer.identifyTokens(list);
        System.out.println("finished");

    }
}
