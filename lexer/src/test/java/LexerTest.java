import lexer.PSLexer;
import JSONWriter.JSONFileWriter;
import org.junit.Assert;
import org.junit.Test;
import token.Token;

import java.util.List;

public class LexerTest {

    private void goldenFileAsserter(String testName, List<String> lines) {
        PSLexer psLexer = new PSLexer();
        List<Token> tokenList = psLexer.identifyTokens(lines);
        List<Token> goldenFile = JSONFileWriter.fileJSONToTokenList(testName);
        if (goldenFile.isEmpty()) JSONFileWriter.tokenListToJSON(tokenList, testName);
        else Assert.assertEquals(goldenFile, tokenList);
    }

    @Test
    public void test01_GivenAStringDeclarationShouldReturnAValidTokensList(){
        final String testName = "LexerTest_test01_StringDeclaration";
        String line = "let variableName : string = \"olive\";";
        goldenFileAsserter(testName, List.of(line));
    }

    @Test
    public void test02_GivenMultipleLinesShouldReturnAValidTokensList() {
        final String testName = "LexerTest_test02_MultipleLines";
        String line1 = "let name : string = \"Khalil\" ;";
        String line2 = "let lastName : string = \"Stessens\" ;";
        goldenFileAsserter(testName, List.of(line1,line2));
    }

    @Test
    public void test03_GivenAnIntegerDeclarationShouldReturnAValidTokenList() {
        final String testName = "LexerTest_test03_IntegerDeclaration";
        String line1 = "let x : number = 12 ;";
        goldenFileAsserter(testName, List.of(line1));
    }

    @Test
    public void test04_GivenAFloatDeclarationShouldReturnAValidTokenList() {
        final String testName = "LexerTest_test04_FloatDeclaration";
        String line1 = "let x : number = 12.04;";
        goldenFileAsserter(testName, List.of(line1));
    }


    @Test
    public void test05_GivenAVariableDeclarationAndLaterDefiningAValueShouldReturnAValidTokenList() {
        final String testName = "LexerTest_test05_DeclarationAndLaterAssignation";
        String line1 = "let x : number;";
        String line2 = "x = 24;";
        goldenFileAsserter(testName, List.of(line1, line2));
    }

    @Test
    public void test06_GivenAVariableOperationShouldReturnAValidTokenList() {
        final String testName = "LexerTest_test06_OperationWithVariables";
        String line1 = "let x : number;";
        String line2 = "x = 24;";
        String line3 = "let y :number ;";
        String line4 = "y = 16 ;";
        String line5 = "let z : number = x + y ;";
        goldenFileAsserter(testName, List.of(line1, line2, line3, line4, line5));
    }

    @Test
    public void test07_GivenMultipleOperationsShouldReturnAValidTokenList() {
        final String testName = "LexerTest_test07_UsingOperationsToAssignValues";
        String line1 = "let suma : number = 22 + 2;";
        String line2 = "let resta : number = 26 - 2 ;";
        String line3 = "let division : number = 48 / 2 ;";
        String line4 = "let multiplicacion : number = 12 * 2;";
        String line5 = "let concatenacion : string = \"holi\" + \"mundo\" ;";
        goldenFileAsserter(testName, List.of(line1, line2, line3, line4, line5));
    }
}
