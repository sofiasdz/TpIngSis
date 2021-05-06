package ASTNode.Factory;

import ASTNode.ASTNode;
import ASTNode.Childless.*;
import ASTNode.MultiChilds.*;
import ASTNode.NotChildless.*;
import ASTNode.TokenGroup.TokenGroup;
import java.util.List;
import token.Token;
import token.TokenType;

public class ASTNodeFactory11 {

  public static ASTNodeIdentifier identifier(Token token) {
    TokenGroup tokenGroup = new TokenGroup(List.of(TokenType.IDENTIFIER));
    if (tokenGroup.belongs(token)) return new ASTNodeIdentifier(token);
    throw new IllegalArgumentException();
  }

  public static ASTNodeVariableType variableType(Token token) {
    TokenGroup tokenGroup =
        new TokenGroup(
            List.of(TokenType.NUMBER_TYPE, TokenType.STRING_TYPE, TokenType.BOOLEAN_TYPE));
    if (tokenGroup.belongs(token)) return new ASTNodeVariableType(token);
    throw new IllegalArgumentException();
  }

  public static ASTNodeLiteral literal(Token token) {
    TokenGroup tokenGroup =
        new TokenGroup(
            List.of(
                TokenType.STRING,
                TokenType.INTEGER,
                TokenType.FLOATING_POINT,
                TokenType.TRUE,
                TokenType.FALSE));
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
                TokenType.BOOLEAN_TYPE,
                TokenType.TRUE,
                TokenType.FALSE,
                TokenType.ADDITION,
                TokenType.SUBSTRACTION,
                TokenType.MULTIPLICATION,
                TokenType.DIVISION,
                TokenType.IDENTIFIER,
                TokenType.EQUAL_OR_S,
                TokenType.EQUAL_OR_G,
                TokenType.GREATER,
                TokenType.SMALLER));
    if (tGroup.belongs(token) && lValid.belongs(left.token) && rValid.belongs(right.token))
      return new ASTNodeAssignation(left, right, token);
    throw new IllegalArgumentException();
  }

  public static ASTNodeDeclaration declaration(Token token, ASTNode left, ASTNode right) {
    TokenGroup tGroup = new TokenGroup(List.of(TokenType.LET, TokenType.CONST));
    TokenGroup lValid =
        new TokenGroup(
            List.of(TokenType.STRING_TYPE, TokenType.NUMBER_TYPE, TokenType.BOOLEAN_TYPE));
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
                TokenType.SUBSTRACTION,
                TokenType.MULTIPLICATION,
                TokenType.DIVISION));
    if (tGroup.belongs(token) && cValid.belongs(left.token) && cValid.belongs(right.token))
      return new ASTNodeOperation(left, right, token);
    throw new IllegalArgumentException();
  }

  public static ASTNodePrint print(Token token) {
    TokenGroup tokenGroup = new TokenGroup(List.of(TokenType.PRINTLN));
    if (tokenGroup.belongs(token)) return new ASTNodePrint(token);
    throw new IllegalArgumentException();
  }

  public static ASTNodeBooleanOperation booleanOperation(Token token, ASTNode left, ASTNode right) {
    TokenGroup tGroup =
        new TokenGroup(
            List.of(
                TokenType.GREATER, TokenType.EQUAL_OR_G, TokenType.SMALLER, TokenType.EQUAL_OR_S));
    TokenGroup cValid =
        new TokenGroup(List.of(TokenType.IDENTIFIER, TokenType.FLOATING_POINT, TokenType.INTEGER));
    if (tGroup.belongs(token) && cValid.belongs(left.token) && cValid.belongs(right.token))
      return new ASTNodeBooleanOperation(left, right, token);
    throw new IllegalArgumentException();
  }

  public static ASTNodeIf ifNode(Token token, ASTNode left, ASTNode right) {
    if (isValidIf(token, left)) {
      return new ASTNodeIf(left, right, token);
    }
    throw new IllegalArgumentException();
  }

  public static ASTNodeIf ifNode(Token token, ASTNode left, List<ASTNode> right) {
    if (isValidIf(token, left)) {
      return new ASTNodeIf(left, right, token);
    }
    throw new IllegalArgumentException();
  }

  public static boolean isValidIf(Token token, ASTNode left) {
    TokenGroup tGroup = new TokenGroup(List.of(TokenType.IF));
    TokenGroup lGroup =
        new TokenGroup(
            List.of(
                TokenType.GREATER,
                TokenType.EQUAL_OR_S,
                TokenType.EQUAL_OR_G,
                TokenType.SMALLER,
                TokenType.TRUE,
                TokenType.FALSE,
                TokenType.BOOLEAN_TYPE));
    return (tGroup.belongs(token) && lGroup.belongs(left.token));
  }

  public static ASTNodeIfElse ifElseNode(Token token, ASTNode left, ASTNode right) {
    if (isValidIfElse(token, left)) {
      return new ASTNodeIfElse(left, right, token);
    }
    throw new IllegalArgumentException();
  }

  public static ASTNodeIfElse ifElseNode(Token token, ASTNode left, List<ASTNode> right) {
    if (isValidIfElse(token, left)) {
      return new ASTNodeIfElse(left, right, token);
    }
    throw new IllegalArgumentException();
  }

  public static boolean isValidIfElse(Token token, ASTNode left) {
    TokenGroup tGroup = new TokenGroup(List.of(TokenType.ELSE));
    TokenGroup lGroup = new TokenGroup(List.of(TokenType.IF));
    return (tGroup.belongs(token) && lGroup.belongs(left.token));
  }
}
