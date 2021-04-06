package ASTNode.NotChildless;

import ASTNode.ASTNode;
import token.Token;

public class ASTNodeOperation extends ASTNodeNotChildless {

    public ASTNodeOperation(ASTNode leftChild, ASTNode rightChild, Token token) {
        super(leftChild, rightChild, token);
    }
}
