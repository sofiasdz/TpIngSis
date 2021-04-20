import ASTNode.ASTNode;
import lexer.PSLexer;
import org.junit.Assert;
import org.junit.Test;
import syntactic_analyzer.PSSyntacticAnalyzer;

import java.util.List;

public class InterpreterTest {

    @Test
    public void test03_GivenAnIntegerDeclarationShouldReturnAValidTokenList() {
        final String testName = "LexerTest_test03_IntegerDeclaration";
        String line1 = "let x : number = 12 ;";
        String line2 = "printLn(x) ;";
        String line3 = "printLn(\"holi\") ;";
        PSLexer lexer = new PSLexer();
        PSSyntacticAnalyzer syntactic = new PSSyntacticAnalyzer();
        List<ASTNode> tree = syntactic.analyze(lexer.identifyTokens(List.of(line1,line2,line3)));
        PSInterpreter interpreter = new PSInterpreter();
        List<String> prints = interpreter.analyze(tree);
        Assert.assertEquals("12.0",prints.get(0));
        Assert.assertEquals("holi",prints.get(1));
    }
}
