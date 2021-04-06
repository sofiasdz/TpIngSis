package ASTNode.NotChildless;

import ASTNode.ASTNode;
import token.Token;

public class ASTNodeAssignation extends ASTNodeNotChildless{
    public ASTNodeAssignation(ASTNode leftChild, ASTNode rightChild, Token token) {
        super(leftChild, rightChild, token);
    }
}
