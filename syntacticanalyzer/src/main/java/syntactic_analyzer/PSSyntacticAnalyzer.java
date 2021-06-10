package syntactic_analyzer;

import ASTNode.ASTNode;
import ASTNode.Factory.ASTNodeFactory;
import ASTNode.TokenGroup.TokenGroup;
import java.util.*;
import token.Token;
import token.TokenType;

public class PSSyntacticAnalyzer implements SyntacticAnalyzer {

  public PSSyntacticAnalyzer() {}

  @Override
  public List<ASTNode> analyze(List<Token> tokens) {
    List<ASTNode> nodes = new ArrayList<>();
    List<Token> tokenList = new ArrayList<>();
    for (int i = 0; i < tokens.size(); i++) {
      tokenList.add(tokens.get(i));
      if (tokens.get(i).getType().equals(TokenType.SEMICOLON)) {
        nodes.add(tokensToNode(tokenList));
        tokenList = new ArrayList<>();
      }
    }
    return nodes;
  }

  ASTNode tokensToNode(List<Token> tokens) {
    if (tokens.get(tokens.size() - 1).getType().equals(TokenType.SEMICOLON)) {
      tokens.remove(tokens.size() - 1);
    } else {
      throw new RuntimeException(
          "Error at line " + tokens.get(0).getStartingLine() + ": missing ;");
    }
    if (tokens.get(0).getType().equals(TokenType.LET)) {
      Optional<ASTNode> identifier = ASTNodeIdentifier(tokens.get(1));
      Optional<ASTNode> type = ASTNodeIdentifier(tokens.get(3));
      if (identifier.isEmpty() || type.isEmpty())
        throw new RuntimeException(
            "Error at line " + tokens.get(0).getStartingLine() + ": Invalid variable declaration");
      Optional<ASTNode> declaration =
          ASTNodeIdentifier(tokens.get(2), type.get(), identifier.get());
      if (declaration.isEmpty())
        throw new RuntimeException(
            "Error at line " + tokens.get(0).getStartingLine() + ": Invalid variable declaration");
      if (tokens.size() < 5) return declaration.get();
      Token assignationToken = tokens.get(4);
      tokens.subList(0, 5).clear();
      ASTNode result = operationResolver(tokens);
      return (ASTNodeIdentifier(assignationToken, declaration.get(), result).get());
    } else if (tokens.get(0).getType().equals(TokenType.IDENTIFIER)) {
      Optional<ASTNode> identifier = ASTNodeIdentifier(tokens.get(0));
      if (identifier.isEmpty())
        throw new RuntimeException(
            "Error at line " + tokens.get(0).getStartingLine() + ": Invalid variable declaration");
      Token assignationToken = tokens.get(1);
      tokens.subList(0, 2).clear();
      ASTNode result = operationResolver(tokens);
      return (ASTNodeIdentifier(assignationToken, identifier.get(), result).get());
    } else if (tokens.get(0).getType().equals(TokenType.PRINTLN)) {
      Optional<ASTNode> print = Optional.empty();
      if (tokens.get(1).getType().equals(TokenType.OPENING_PARENTHESIS)) {
        List<Token> parenthesis = new ArrayList<>();
        for (int i = 2; i < tokens.size(); i++) {
          if (tokens.get(i).getType().equals(TokenType.CLOSING_PARENTHESIS)) break;
          parenthesis.add(tokens.get(i));
        }
        ASTNode printValue = operationResolver(parenthesis);
        print = printNodeBuilder(tokens.get(0), printValue);
      }
      if (print.isEmpty())
        throw new RuntimeException(
            "Error at line " + tokens.get(0).getStartingLine() + ": Invalid print declaration");
      return print.get();
    } else {
      throw new RuntimeException(
          "Error at line " + tokens.get(0).getStartingLine() + ": Invalid line start");
    }
  }

  ASTNode operationResolver(List<Token> tokens) {
    TokenGroup literals =
        new TokenGroup(
            List.of(
                TokenType.STRING,
                TokenType.FLOATING_POINT,
                TokenType.INTEGER,
                TokenType.IDENTIFIER));
    if (tokens.size() == 1 && literals.belongs(tokens.get(0)))
      return ASTNodeIdentifier(tokens.get(0)).get();
    Stack<Token> operatorStack = new Stack<>();
    Queue<Token> outputQueue = new ArrayDeque<>();
    TokenGroup operators =
        new TokenGroup(
            List.of(
                TokenType.SUBSTRACTION,
                TokenType.ADDITION,
                TokenType.DIVISION,
                TokenType.MULTIPLICATION));
    TokenGroup operands =
        new TokenGroup(
            List.of(
                TokenType.STRING,
                TokenType.INTEGER,
                TokenType.FLOATING_POINT,
                TokenType.IDENTIFIER));
    for (int i = 0; i < tokens.size(); i++) {
      if (operands.belongs(tokens.get(i))) {
        outputQueue.add(tokens.get(i));
      } else if (operators.belongs(tokens.get(i))) {
        if (operatorStack.isEmpty()) {
          operatorStack.push(tokens.get(i));
        } else {
          if (operatorPrecedence(operatorStack.peek()) < operatorPrecedence(tokens.get(i))) {
            operatorStack.push(tokens.get(i));
          } else {
            while (!operatorStack.isEmpty()
                && operatorPrecedence(operatorStack.peek()) >= operatorPrecedence(tokens.get(i))) {
              outputQueue.add(operatorStack.pop());
            }
            operatorStack.push(tokens.get(i));
          }
        }
      }
    }
    while (!operatorStack.isEmpty()) outputQueue.add(operatorStack.pop());
    Stack<ASTNode> nodeStack = new Stack<>();
    while (!outputQueue.isEmpty()) {
      if (operands.belongs(outputQueue.peek())) {
        nodeStack.push(ASTNodeIdentifier(outputQueue.poll()).get());
      } else {
        ASTNode right = nodeStack.pop();
        ASTNode left = nodeStack.pop();
        nodeStack.push(ASTNodeIdentifier(outputQueue.poll(), left, right).get());
      }
    }
    return nodeStack.pop();
  }

  int operatorPrecedence(Token token) {
    return switch (token.getType()) {
      case ADDITION, SUBSTRACTION -> 2;
      case DIVISION, MULTIPLICATION -> 3;
      default -> 0;
    };
  }

  Optional<ASTNode> ASTNodeIdentifier(Token token) {
    if (token.getType().equals(TokenType.IDENTIFIER)) {
      return Optional.of(ASTNodeFactory.identifier(token));
    } else if (token.getType().equals(TokenType.NUMBER_TYPE)
        | token.getType().equals(TokenType.STRING_TYPE)) {
      return Optional.of(ASTNodeFactory.variableType(token));
    } else {
      try {
        return Optional.of(ASTNodeFactory.literal(token));
      } catch (IllegalArgumentException e) {
        return Optional.empty();
      }
    }
  }

  Optional<ASTNode> ASTNodeIdentifier(Token token, ASTNode left, ASTNode right) {
    try {
      return Optional.of(ASTNodeFactory.assignation(token, left, right));
    } catch (IllegalArgumentException e) {
      try {
        return Optional.of(ASTNodeFactory.declaration(token, left, right));
      } catch (IllegalArgumentException i) {
        try {
          return Optional.of(ASTNodeFactory.operation(token, left, right));
        } catch (IllegalArgumentException h) {
          return Optional.empty();
        }
      }
    }
  }

  Optional<ASTNode> printNodeBuilder(Token token, ASTNode child) {
    try {
      return Optional.of(ASTNodeFactory.print(token, child));
    } catch (IllegalArgumentException e) {
      return Optional.empty();
    }
  }
}
