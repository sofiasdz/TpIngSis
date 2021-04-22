package lexer;

import java.util.List;
import token.Token;

public interface Lexer {
  List<Token> identifyTokens(List<String> text);
}
