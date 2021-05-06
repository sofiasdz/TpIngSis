import ASTNode.ASTNode;
import Parser.DebugResult;
import Parser.PS10Parser;
import Parser.PS11Parser;
import token.Token;

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
    System.out.println("5 - DEBUG PRINTSCRIPT 1.0 CODE");
    System.out.println("6 - DEBUG PRINTSCRIPT 1.1 CODE");
    System.out.println();
    int option = Scanner.getInt("ENTER YOUR OPTION: ");
    switch (option) {
      case 1:
        validatePS();
        break;
      case 2:
        executePS();
        break;
      case 3:
        validatePS11();
        break;
      case 4:
        executePS11();
        break;
      case 5:
        debugPS();
        break;
      case 6:
        debugPS11();
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

  private static void validatePS(){
    PS10Parser parser = new PS10Parser();
    String path = Scanner.getString("ENTER THE FILE'S PATH: ");
    System.out.println(parser.validate(path));
  }

  private static void executePS(){
    PS10Parser parser = new PS10Parser();
    String path = Scanner.getString("ENTER THE FILE'S PATH: ");
    parser.execute(path).forEach(System.out::println);
  }

  private static void validatePS11(){
    PS11Parser parser = new PS11Parser();
    String path = Scanner.getString("ENTER THE FILE'S PATH: ");
    System.out.println(parser.validate(path));
  }

  private static void executePS11(){
    PS11Parser parser = new PS11Parser();
    String path = Scanner.getString("ENTER THE FILE'S PATH: ");
    parser.execute(path).forEach(System.out::println);
  }

  private static void debugPS() {
    String path = Scanner.getString("ENTER THE FILE'S PATH: ");
    PS10Parser parser = new PS10Parser();
    debug(parser.debug(path));
  }

  private static void debug(DebugResult debug) {
    System.out.println();
    System.out.println("THE TOKENS ARE: ");
    System.out.println();
    for (Token t : debug.tokens) System.out.println(t.getType() + " " + t.getValue());
    System.out.println();
    System.out.println("THE NODES ARE: ");
    for (ASTNode n : debug.nodes) System.out.println(n.toString());
    System.out.println();
    System.out.println("THE RESULT OF YOUR PROGRAM IS: ");
    for (String s : debug.prints) System.out.println(s);
  }

  private static void debugPS11() {
    String path = Scanner.getString("ENTER THE FILE'S PATH: ");
    PS11Parser parser = new PS11Parser();
    debug(parser.debug(path));
  }
}
