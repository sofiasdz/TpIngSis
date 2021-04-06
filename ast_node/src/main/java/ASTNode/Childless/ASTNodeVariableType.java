package ASTNode.Childless;

import ASTNode.TokenGroup.TokenGroup;
import token.Token;
import token.TokenType;

import java.util.List;

public class ASTNodeVariableType extends ASTNodeChildless{
    public static TokenGroup tokenGroup = new TokenGroup(List.of(TokenType.STRING_TYPE,TokenType.NUMBER_TYPE));

    public ASTNodeVariableType(Token token) {
        super(token);
    }
}
