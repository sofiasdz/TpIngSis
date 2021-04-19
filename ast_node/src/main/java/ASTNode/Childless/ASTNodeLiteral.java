package ASTNode.Childless;

import ASTNode.TokenGroup.TokenGroup;
import token.Token;
import token.TokenType;

import java.util.List;

public class ASTNodeLiteral extends ASTNodeChildless {

    public ASTNodeLiteral(Token token) {
        super(token);
    }

    @Override
    public String toString() {
        return "Literal: "+ token;
    }
}
