package ASTNode.Childless;

import token.Token;

public class ASTNodePrint extends ASTNodeChildless {

    public ASTNodePrint(Token token) {
        super(token);
    }

    @Override
    public String toString() {
        return "PrintLn ( "+ token.getValue()+" )";
    }
}
