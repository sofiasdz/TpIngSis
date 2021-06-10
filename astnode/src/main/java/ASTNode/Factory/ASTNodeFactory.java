package ASTNode.Factory;

import ASTNode.ASTNode;
import ASTNode.Childless.ASTNodeIdentifier;
import ASTNode.Childless.ASTNodeLiteral;
import ASTNode.SingleChild.ASTNodePrint;
import ASTNode.Childless.ASTNodeVariableType;
import ASTNode.NotChildless.ASTNodeAssignation;
import ASTNode.NotChildless.ASTNodeDeclaration;
import ASTNode.NotChildless.ASTNodeOperation;
import ASTNode.TokenGroup.TokenGroup;
import java.util.List;
import token.Token;
import token.TokenType;

public class ASTNodeFactory {

  public static ASTNodeIdentifier identifier(Token token) {
    TokenGroup tokenGroup = new TokenGroup(List.of(TokenType.IDENTIFIER));
    if (tokenGroup.belongs(token)) return new ASTNodeIdentifier(token);
    throw new IllegalArgumentException();
  }

  public static ASTNodeVariableType variableType(Token token) {
    TokenGroup tokenGroup = new TokenGroup(List.of(TokenType.NUMBER_TYPE, TokenType.STRING_TYPE));
    if (tokenGroup.belongs(token)) return new ASTNodeVariableType(token);
    throw new IllegalArgumentException();
  }

  public static ASTNodeLiteral literal(Token token) {
    TokenGroup tokenGroup =
        new TokenGroup(List.of(TokenType.STRING, TokenType.INTEGER, TokenType.FLOATING_POINT));
    if (tokenGroup.belongs(token)) return new ASTNodeLiteral(token);
    throw new IllegalArgumentException();
  }

  public static ASTNodeAssignation assignation(Token token, ASTNode left, ASTNode right) {
    TokenGroup tGroup = new TokenGroup(List.of(TokenType.ASSIGNATION));
    TokenGroup lValid =
        new TokenGroup(List.of(TokenType.LET, TokenType.COLON, TokenType.IDENTIFIER));
    TokenGroup rValid =
        new TokenGroup(
            List.of(
                TokenType.INTEGER,
                TokenType.STRING,
                TokenType.FLOATING_POINT,
                TokenType.ADDITION,
                TokenType.SUBSTRACTION,
                TokenType.MULTIPLICATION,
                TokenType.DIVISION,
                TokenType.IDENTIFIER));
    if (tGroup.belongs(token) && lValid.belongs(left.token) && rValid.belongs(right.token))
      return new ASTNodeAssignation(left, right, token);
    throw new IllegalArgumentException();
  }

  public static ASTNodeDeclaration declaration(Token token, ASTNode left, ASTNode right) {
    TokenGroup tGroup = new TokenGroup(List.of(TokenType.LET, TokenType.COLON));
    TokenGroup lValid = new TokenGroup(List.of(TokenType.STRING_TYPE, TokenType.NUMBER_TYPE));
    TokenGroup rValid = new TokenGroup(List.of(TokenType.IDENTIFIER));
    if (tGroup.belongs(token) && lValid.belongs(left.token) && rValid.belongs(right.token))
      return new ASTNodeDeclaration(left, right, token);
    throw new IllegalArgumentException();
  }

  public static ASTNodeOperation operation(Token token, ASTNode left, ASTNode right) {
    TokenGroup tGroup =
        new TokenGroup(
            List.of(
                TokenType.ADDITION,
                TokenType.DIVISION,
                TokenType.SUBSTRACTION,
                TokenType.MULTIPLICATION));
    TokenGroup cValid =
        new TokenGroup(
            List.of(
                TokenType.IDENTIFIER,
                TokenType.FLOATING_POINT,
                TokenType.INTEGER,
                TokenType.STRING,
                TokenType.ADDITION,
                TokenType.DIVISION,
                TokenType.SUBSTRACTION,
                TokenType.MULTIPLICATION));
    if (tGroup.belongs(token) && cValid.belongs(left.token) && cValid.belongs(right.token))
      return new ASTNodeOperation(left, right, token);
    throw new IllegalArgumentException();
  }

  public static ASTNodePrint print(Token token, ASTNode child) {
    TokenGroup tokenGroup = new TokenGroup(List.of(TokenType.PRINTLN));
    TokenGroup cValid =
            new TokenGroup(
                    List.of(
                            TokenType.IDENTIFIER,
                            TokenType.FLOATING_POINT,
                            TokenType.INTEGER,
                            TokenType.STRING,
                            TokenType.ADDITION,
                            TokenType.DIVISION,
                            TokenType.SUBSTRACTION,
                            TokenType.MULTIPLICATION));
    if (tokenGroup.belongs(token) && cValid.belongs(child.getToken()))
      return new ASTNodePrint(token,child);
    throw new IllegalArgumentException();
  }
}
