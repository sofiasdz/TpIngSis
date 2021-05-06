import ASTNode.ASTNode;
import token.Token;

import java.util.List;

public class DebugResult {
    List<Token> tokens;
    List<ASTNode> nodes;
    List<String> prints;

    public DebugResult(List<Token> tokens, List<ASTNode> nodes, List<String> prints) {
        this.tokens = tokens;
        this.nodes = nodes;
        this.prints = prints;
    }
}
