package ASTNode.NotChildless;

import ASTNode.ASTNode;
import token.Token;

public class ASTNodePrint extends ASTNodeNotChildless{

    public ASTNodePrint(ASTNode leftChild, Token token) {
        super(leftChild, leftChild, token);
    }

    @Override
    public String toString() {
        return "PrintLN ( "+leftChild.toString()+" )";
    }
}
