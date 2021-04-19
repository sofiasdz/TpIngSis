package ASTNode.NotChildless;

import ASTNode.ASTNode;
import ASTNode.TokenGroup.TokenGroup;
import token.Token;
import token.TokenType;

import java.util.List;

public class ASTNodeOperation extends ASTNodeNotChildless {

    public ASTNodeOperation(ASTNode leftChild, ASTNode rightChild, Token token) {
        super(leftChild, rightChild, token);
    }

    @Override
    public String toString() {
        return "Operation: ("+leftChild.toString() + ") " + token + " (" + rightChild.toString()+") ";
    }
}
