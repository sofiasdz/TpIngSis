package ASTNode.Childless;

import token.Token;

public class ASTNodeIdentifier extends ASTNodeChildless {

    public ASTNodeIdentifier(Token token) {
        super(token);
    }

    @Override
    public String toString() {
        return "Identifier: "+ token;
    }
}
