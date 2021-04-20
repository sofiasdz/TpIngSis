package ASTNode.Childless;

import token.Token;

public class ASTNodeVariableType extends ASTNodeChildless{

    public ASTNodeVariableType(Token token) {
        super(token);
    }

    @Override
    public String toString() {
        return "VarType: "+ token;
    }

    @Override
    public String getNodeType() {
        return "variableType";
    }
}
