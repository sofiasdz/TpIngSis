package ASTNode.Factory;


import ASTNode.ASTNode;
import ASTNode.Childless.ASTNodeChildless;
import ASTNode.Childless.ASTNodeIdentifier;
import ASTNode.Childless.ASTNodeLiteral;
import ASTNode.Childless.ASTNodeVariableType;
import ASTNode.NotChildless.ASTNodeAssignation;
import ASTNode.NotChildless.ASTNodeDeclaration;
import ASTNode.NotChildless.ASTNodeOperation;
import ASTNode.TokenGroup.TokenGroup;
import token.Token;
import token.TokenType;

import java.util.List;

public class ASTNodeFactory {

    static ASTNodeIdentifier identifier(Token token){
        TokenGroup tokenGroup = new TokenGroup(List.of(TokenType.IDENTIFIER));
        if(tokenGroup.belongs(token)) return new ASTNodeIdentifier(token);
        throw new IllegalArgumentException();
    }

    static ASTNodeVariableType variableType(Token token){
        TokenGroup tokenGroup = new TokenGroup(List.of(TokenType.NUMBER_TYPE,TokenType.STRING_TYPE));
        if(tokenGroup.belongs(token)) return new ASTNodeVariableType(token);
        throw new IllegalArgumentException();
    }

    static ASTNodeLiteral literal(Token token){
        TokenGroup tokenGroup = new TokenGroup(List.of(TokenType.STRING,TokenType.INTEGER,TokenType.FLOATING_POINT));
        if(tokenGroup.belongs(token)) return new ASTNodeLiteral(token);
        throw new IllegalArgumentException();
    }

    static ASTNodeAssignation assignation(Token token, ASTNode left, ASTNode right){
        TokenGroup tGroup = new TokenGroup(List.of(TokenType.ASSIGNATION));
        TokenGroup lValid = new TokenGroup(List.of(TokenType.LET,TokenType.COLON));
        TokenGroup rValid = new TokenGroup(List.of(TokenType.INTEGER,TokenType.STRING, TokenType.FLOATING_POINT,TokenType.ADDITION,TokenType.SUBSTRACTION,TokenType.MULTIPLICATION,TokenType.DIVISION));
        if(tGroup.belongs(token) && lValid.belongs(left.token) && rValid.belongs(right.token)) return new ASTNodeAssignation(left,right,token);
        throw new IllegalArgumentException();
    }

    static ASTNodeDeclaration declaration(Token token, ASTNode left, ASTNode right){
        TokenGroup tGroup = new TokenGroup(List.of(TokenType.LET,TokenType.COLON));
        TokenGroup lValid = new TokenGroup(List.of(TokenType.STRING_TYPE,TokenType.NUMBER_TYPE));
        TokenGroup rValid = new TokenGroup(List.of(TokenType.IDENTIFIER));
        if(tGroup.belongs(token) && lValid.belongs(left.token) && rValid.belongs(right.token)) return new ASTNodeDeclaration(left,right,token);
        throw new IllegalArgumentException();
    }

    static ASTNodeOperation operation(Token token, ASTNode left, ASTNode right){
        TokenGroup tGroup = new TokenGroup(List.of(TokenType.LET,TokenType.COLON));
        TokenGroup cValid = new TokenGroup(List.of(TokenType.IDENTIFIER,TokenType.FLOATING_POINT,TokenType.INTEGER,TokenType.STRING));
        if(tGroup.belongs(token) && cValid.belongs(left.token) && cValid.belongs(right.token)) return new ASTNodeOperation(left,right,token);
        throw new IllegalArgumentException();
    }

}
