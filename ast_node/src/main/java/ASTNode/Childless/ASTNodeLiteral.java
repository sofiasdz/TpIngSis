package ASTNode.Childless;

import ASTNode.TokenGroup.TokenGroup;
import token.Token;
import token.TokenType;

import java.util.List;

public class ASTNodeLiteral extends ASTNodeChildless {
    public static TokenGroup tokenGroup = new TokenGroup(List.of(TokenType.FLOATING_POINT,TokenType.INTEGER,TokenType.STRING));

    public ASTNodeLiteral(Token token) {
        super(token);
    }
}
