package syntactic_analyzer;

import ASTNode.ASTNode;
import token.Token;

import java.util.List;

public interface SyntacticAnalyzer {
    ASTNode analyze(List<Token> tokens);
}
