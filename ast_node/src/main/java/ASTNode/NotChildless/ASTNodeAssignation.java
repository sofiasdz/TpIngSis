package ASTNode.NotChildless;

import ASTNode.ASTNode;
import ASTNode.TokenGroup.TokenGroup;
import token.Token;
import token.TokenType;

import java.util.List;

public class ASTNodeAssignation extends ASTNodeNotChildless{

    public ASTNodeAssignation(ASTNode leftChild, ASTNode rightChild, Token token) {
        super(leftChild, rightChild, token);
    }
}