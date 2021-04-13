import lexer.PSLexer;
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
        switch (option){
            case 1:
                validatePrintScript1Code();
                break;
            default:
                System.out.println("I HAVE NO IDEA WHAT THAT MEANS.");
                break;
        }
        System.out.println();
        System.out.println("HAVE A NICE DAY!");
        System.out.println();
        System.out.println("******************************");
    }

    private static void validatePrintScript1Code(){
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
    }
}
