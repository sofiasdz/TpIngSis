package ASTNode.NotChildless;

import ASTNode.ASTNode;
import ASTNode.Childless.ASTNodeIdentifier;
import ASTNode.Childless.ASTNodeVariableType;
import ASTNode.TokenGroup.TokenGroup;
import token.Token;
import token.TokenType;

import java.util.List;

public class ASTNodeDeclaration extends ASTNodeNotChildless {
    public static TokenGroup tokenGroup = new TokenGroup(List.of(TokenType.LET));

    public ASTNodeDeclaration(ASTNode leftChild, ASTNode rightChild, Token token) {
        super(leftChild, rightChild, token);
    }
}
