package ASTNode.Childless;

import token.Token;

public class ASTNodeLiteral extends ASTNodeChildless {

    public ASTNodeLiteral(Token token) {
        super(token);
    }

    @Override
    public String toString() {
        return "Literal: "+ token;
    }
}
