package ASTNode.NotChildless;

import ASTNode.ASTNode;
import ASTNode.Childless.ASTNodeIdentifier;
import ASTNode.Childless.ASTNodeVariableType;
import token.Token;

public class ASTNodeDeclaration extends ASTNodeNotChildless {

    public ASTNodeDeclaration(ASTNode leftChild, ASTNode rightChild, Token token) {
        super(leftChild, rightChild, token);
    }
}
