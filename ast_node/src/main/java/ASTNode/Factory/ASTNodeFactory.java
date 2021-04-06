package ASTNode.Factory;


import ASTNode.Childless.ASTNodeChildless;
import ASTNode.Childless.ASTNodeIdentifier;
import ASTNode.Childless.ASTNodeLiteral;
import ASTNode.Childless.ASTNodeVariableType;
import token.Token;

public class ASTNodeFactory {

    static ASTNodeIdentifier astNodeIdentifier(Token token){

        return new ASTNodeIdentifier(token);
    }

    static ASTNodeVariableType astNodeVariableType(Token token){
        return new ASTNodeVariableType(token);
    }

    static ASTNodeLiteral astNodeLiteral(Token token){
        return new ASTNodeLiteral(token);
    }





}
