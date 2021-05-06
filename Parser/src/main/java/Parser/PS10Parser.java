package Parser;

import ASTNode.ASTNode;
import Interpreter.PSInterpreter;
import lexer.PSLexer;
import syntactic_analyzer.PSSyntacticAnalyzer;
import token.Token;

import java.util.List;

public class PS10Parser implements Parser {
    static PSLexer lexer = new PSLexer();
    static PSSyntacticAnalyzer syntactic = new PSSyntacticAnalyzer();
    static PSInterpreter interpreter = new PSInterpreter();

    public String validate(String path) {
        List<String> list = FileToString.fileReader(path);
        return validate(list);
    }

    public String validate(List<String> list) {
        try {
            List<Token> tokens = lexer.identifyTokens(list);
            List<ASTNode> nodes = syntactic.analyze(tokens);
            interpreter.analyze(nodes);
            return "No errors encountered";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public List<String> execute(String path) {
        List<String> list = FileToString.fileReader(path);
        return execute(list);
    }

    public List<String> execute(List<String> list) {
        List<Token> tokens = lexer.identifyTokens(list);
        List<ASTNode> nodes = syntactic.analyze(tokens);
        return interpreter.analyze(nodes);
    }

    public DebugResult debug(String path){
        List<String> list = FileToString.fileReader(path);
        return debug(list);
    }

    public DebugResult debug(List<String> list){
        List<Token> tokens = lexer.identifyTokens(list);
        List<ASTNode> nodes = syntactic.analyze(tokens);
        List<String> prints = interpreter.analyze(nodes);
        return new DebugResult(tokens,nodes,prints);
    }

}
