import ASTNode.ASTNode;
import lexer.PSLexer;
import syntactic_analyzer.PSSyntacticAnalyzer;
import token.Token;

import java.util.List;

public class CLI {

    public static void main(String[] args) {
        System.out.println("******************************");
        System.out.println();
        System.out.println("WELCOME TO PRINTSCRIPT'S CLI!");
        System.out.println("WHAT DO YOU WANT TO DO TODAY?");
        System.out.println();
        System.out.println("1 - VALIDATE PRINTSCRIPT 1.0 CODE");
        System.out.println("2 - EXECUTE PRINTSCRIPT 1.0 CODE");
        System.out.println("3 - VALIDATE PRINTSCRIPT 1.1 CODE");
        System.out.println("4 - EXECUTE PRINTSCRIPT 1.1 CODE");
        System.out.println();
        int option = Scanner.getInt("ENTER YOUR OPTION: ");
        switch (option) {
            case 1 -> validatePrintScript1Code();
            case 2 -> executePrintScript1Code();
            default -> System.out.println("I HAVE NO IDEA WHAT THAT MEANS.");
        }
        System.out.println();
        System.out.println("HAVE A NICE DAY!");
        System.out.println();
        System.out.println("******************************");
    }

    private static void validatePrintScript1Code() {
        try{
            String path = Scanner.getString("ENTER THE FILE'S PATH: ");
            List<String> list = FileToString.fileReader(path);
            PSLexer lexer = new PSLexer();
            List<Token> tokens = lexer.identifyTokens(list);
            PSSyntacticAnalyzer syntactic = new PSSyntacticAnalyzer();
            List<ASTNode> nodes = syntactic.analyze(tokens);
            PSInterpreter interpreter = new PSInterpreter();
            interpreter.analyze(nodes);
            System.out.println("YOUR CODE IS VALID!");
        }
        catch (Exception e){
            System.out.println("YOUR CODE IS INVALID!");
        }
    }

    private static void executePrintScript1Code(){
        String path = Scanner.getString("ENTER THE FILE'S PATH: ");
        List<String> list = FileToString.fileReader(path);
        System.out.println("YOUR CODE IS: ");
        System.out.println();
        for (String s : list) System.out.println(s);
        System.out.println();
        System.out.println("THE TOKENS ARE: ");
        System.out.println();
        PSLexer lexer = new PSLexer();
        List<Token> tokens = lexer.identifyTokens(list);
        for (Token t : tokens) System.out.println(t.getType() + " " + t.getValue());
        System.out.println();
        System.out.println("THE NODES ARE: ");
        PSSyntacticAnalyzer syntactic = new PSSyntacticAnalyzer();
        List<ASTNode> nodes = syntactic.analyze(tokens);
        for (ASTNode n : nodes) System.out.println(n.toString());
        System.out.println();
        System.out.println("THE RESULT OF YOUR PROGRAM IS: ");
        PSInterpreter interpreter = new PSInterpreter();
        List<String> prints = interpreter.analyze(nodes);
        for (String s : prints) System.out.println(s);
    }
}
