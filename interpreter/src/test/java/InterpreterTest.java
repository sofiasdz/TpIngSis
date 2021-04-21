import ASTNode.ASTNode;
import lexer.PSLexer;
import org.junit.Assert;
import org.junit.Test;
import syntactic_analyzer.PSSyntacticAnalyzer;

import java.util.List;

public class InterpreterTest {

    private List<String> analyze(List<String> lines){
        PSLexer lex = new PSLexer();
        PSSyntacticAnalyzer syn = new PSSyntacticAnalyzer();
        PSInterpreter interpreter = new PSInterpreter();
        return interpreter.analyze(syn.analyze(lex.identifyTokens(lines)));
    }

    @Test
    public void test01_GivenAnIntegerDeclarationOperationAndPrintShouldReturnAValidPrintList() {
        String line1 = "let x : number = 12 + 2 ;";
        String line2 = "printLn(x) ;";
        List<String> prints = analyze((List.of(line1,line2)));
        Assert.assertEquals("14.0",prints.get(0));
    }

    @Test
    public void test02_GivenAStringDeclarationOperationAndPrintShouldReturnAValidPrintList() {
        String line1 = "let x : string = \"hello\" + \" world\" ;";
        String line2 = "printLn(x) ;";
        List<String> prints = analyze((List.of(line1,line2)));
        Assert.assertEquals("\"hello world\"",prints.get(0));
    }

    @Test
    public void test03_GivenMultipleStringDeclarationOperationAndPrintShouldReturnAValidPrintList() {
        String line1 = "let x : string = \"hello\" ;";
        String line2 = "let y : string = \" world\" ;";
        String line3 = "let z : string ;";
        String line4 = "z = x + y ;";
        String line5 = "printLn(z) ;";
        List<String> prints = analyze((List.of(line1,line2,line3,line4,line5)));
        Assert.assertEquals("\"hello world\"",prints.get(0));
    }

    @Test
    public void test04_GivenMultipleNumberDeclarationOperationAndPrintShouldReturnAValidPrintList() {
        String line1 = "let x : number = 4 ;";
        String line2 = "let y : number = 2 ;";
        String line3 = "let suma : number = x + y ;";
        String line4 = "let mult : number = x * y ;";
        String line5 = "let rest : number = x - y ;";
        String line6 = "let divi : number = x / y ;";
        String line7 = "printLn(suma) ;";
        String line8 = "printLn(mult) ;";
        String line9 = "printLn(rest) ;";
        String line10 = "printLn(divi) ;";
        List<String> prints = analyze((List.of(line1,line2,line3,line4,line5,line6,line7,line8,line9,line10)));
        Assert.assertEquals("6.0",prints.get(0));
        Assert.assertEquals("8.0",prints.get(1));
        Assert.assertEquals("2.0",prints.get(2));
        Assert.assertEquals("2.0",prints.get(3));
    }

    @Test (expected = IllegalArgumentException.class)
    public void test05_DeclaringSameVariableTwiceShouldThrowException() {
        String line1 = "let x : string = \"hello\" ;";
        String line2 = "let x : string = \" world\" ;";
        List<String> prints = analyze((List.of(line1,line2)));
    }

    @Test
    public void test06_AssigningValueToANonDeclaredVariableShouldThrowException() {
        String line1 = "let x : number = 4 ;";
        String line2 = "let y : number = 2 ;";
        String line3 = "y = y + 2 ;";
        String line4 = "printLn(y);";
        List<String> prints = analyze((List.of(line1,line2,line3,line4)));
        Assert.assertEquals("4.0",prints.get(0));
    }

    @Test (expected = RuntimeException.class)
    public void test07_AssigningNumberToStringShouldThrowException() {
        String line1 = "let x : string;";
        String line2 = "x = 324 ;";
        List<String> prints = analyze((List.of(line1, line2)));
    }

    @Test (expected = RuntimeException.class)
    public void test08_AssigningStringToNumberShouldThrowException() {
        String line1 = "let x : number;";
        String line2 = "x = \"324\" ;";
        List<String> prints = analyze((List.of(line1, line2)));
    }

}
