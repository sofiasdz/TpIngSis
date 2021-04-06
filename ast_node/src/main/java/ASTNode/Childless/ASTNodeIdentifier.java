package ASTNode.Childless;

import ASTNode.TokenGroup.TokenGroup;
import token.Token;
import token.TokenType;

import java.util.List;

public class ASTNodeIdentifier extends ASTNodeChildless {
    public static TokenGroup tokenGroup = new TokenGroup(List.of(TokenType.IDENTIFIER));

    public ASTNodeIdentifier(Token token) {
        super(token);
    }
}
