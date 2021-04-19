package ASTNode.Childless;

import ASTNode.TokenGroup.TokenGroup;
import token.Token;
import token.TokenType;

import java.util.List;

public class ASTNodeVariableType extends ASTNodeChildless{

    public ASTNodeVariableType(Token token) {
        super(token);
    }

    @Override
    public String toString() {
        return "VarType: "+ token;
    }
}
