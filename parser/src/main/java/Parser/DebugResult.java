package Parser;

import ASTNode.ASTNode;
import java.util.List;
import token.Token;

public class DebugResult {
  public List<Token> tokens;
  public List<ASTNode> nodes;
  public List<String> prints;

  public DebugResult(List<Token> tokens, List<ASTNode> nodes, List<String> prints) {
    this.tokens = tokens;
    this.nodes = nodes;
    this.prints = prints;
  }
}
