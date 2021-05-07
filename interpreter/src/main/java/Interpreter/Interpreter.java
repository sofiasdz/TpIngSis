package Interpreter;

import ASTNode.ASTNode;

import java.util.List;

public interface Interpreter {

    public List<String> analyze(List<ASTNode> nodes);
}
