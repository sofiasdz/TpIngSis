package syntactic_analyzer;

import ASTNode.ASTNode;
import java.util.List;
import token.Token;

public interface SyntacticAnalyzer {
  List<ASTNode> analyze(List<Token> tokens);
}
