package syntactic_analyzer;

import ASTNode.ASTNode;
import ASTNode.Factory.ASTNodeFactory;
import ASTNode.NotChildless.ASTNodeAssignation;
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
        nodes.add(tokensToNodeRefactored(tokenList));
        tokenList = new ArrayList<>();
      }
    }
    return nodes;
  }

  public ASTNode tokensToNode(List<Token> tokens) {
    Stack<ASTNode> nodeStack = new Stack<>();
    List<Token> tokenList = new ArrayList<>();
    for (int i = tokens.size() - 1; i >= 0; i--) {
      Optional<ASTNode> optionalASTNode = ASTNodeIdentifier(tokens.get(i));
      if (optionalASTNode.isEmpty()) tokenList.add(tokens.get(i));
      else nodeStack.push(optionalASTNode.get());
      // Si el optional es un nodo, lo stackeo. Voy de atrás para adelante para no tener problema
      // con los nodos
    }
    for (int i = tokenList.size() - 1; i >= 0; i--) {
      if (tokenList.get(i).getType().equals(TokenType.LET)
          | tokenList.get(i).getType().equals(TokenType.SEMICOLON)) tokenList.remove(i);
    }

    while (nodeStack.size() > 1) {
      ASTNode left = nodeStack.pop();
      ASTNode right = nodeStack.pop();
      Optional<ASTNode> optionalASTNode =
          ASTNodeIdentifier(tokenList.get(tokenList.size() - 1), left, right);
      if (optionalASTNode.isPresent()) {
        nodeStack.push(optionalASTNode.get());
        tokenList.remove(tokenList.size() - 1);
      } else {
        optionalASTNode = ASTNodeIdentifier(tokenList.get(tokenList.size() - 1), right, left);
        if (optionalASTNode.isPresent()) {
          nodeStack.push(optionalASTNode.get());
          tokenList.remove(tokenList.size() - 1);
        } else {
          nodeStack.push(right);
          nodeStack.push(left);
          nodeStack = operationCheck(nodeStack, tokenList);
        }
      }
    }
    return nodeStack.pop();
  }

  public ASTNode tokensToNodeRefactored(List<Token> tokens) {
    if (tokens.get(tokens.size() - 1).getType().equals(TokenType.SEMICOLON)) {
      tokens.remove(tokens.size() - 1);
    } else {
      throw new RuntimeException("Line missing a semicolon ;");
    }

    List<Token> tokenList = new ArrayList<>();

    if (tokens.get(0).getType().equals(TokenType.LET)) {
      Optional<ASTNode> identifier = ASTNodeIdentifier(tokens.get(1));
      Optional<ASTNode> type = ASTNodeIdentifier(tokens.get(3));
      if (identifier.isEmpty() || type.isEmpty())
        throw new RuntimeException("Invalid variable declaration");
      Optional<ASTNode> declaration =
          ASTNodeIdentifier(tokens.get(2), type.get(), identifier.get());
      if (declaration.isEmpty()) throw new RuntimeException("Invalid variable declaration");
      if (tokens.size() < 5) return declaration.get();
      Token assignationToken = tokens.get(4);
      tokens.subList(0, 5).clear();
      ASTNode result = operationResolver(tokens);
      return (ASTNodeIdentifier(assignationToken, declaration.get(), result).get());
    } else if (tokens.get(0).getType().equals(TokenType.IDENTIFIER)) {
      Optional<ASTNode> identifier = ASTNodeIdentifier(tokens.get(0));
      if (identifier.isEmpty()) throw new RuntimeException("Invalid variable declaration");
      Token assignationToken = tokens.get(1);
      tokens.subList(0, 2).clear();
      ASTNode result = operationResolver(tokens);
      return (ASTNodeIdentifier(assignationToken, identifier.get(), result).get());
    } else if (tokens.get(0).getType().equals(TokenType.PRINTLN)) {
      Optional<ASTNode> print = ASTNodeIdentifier(tokens.get(0));
      if (print.isEmpty()) throw new RuntimeException("Invalid printLn declaration");
      return print.get();
    } else {
      throw new RuntimeException("Invalid line start!");
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

    // Shunting Yard
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
      case ADDITION -> 2;
      case SUBSTRACTION -> 2;
      case DIVISION -> 3;
      case MULTIPLICATION -> 3;
      default -> 0;
    };
  }

  Stack<ASTNode> operationCheck(Stack<ASTNode> nodeStack, List<Token> tokenList) {
    if (nodeStack.peek().token.getType().equals(TokenType.ASSIGNATION)) {
      TokenGroup group =
          new TokenGroup(
              List.of(
                  TokenType.ADDITION,
                  TokenType.SUBSTRACTION,
                  TokenType.DIVISION,
                  TokenType.MULTIPLICATION));
      for (int i = 0; i < tokenList.size(); i++) {
        if (group.belongs(tokenList.get(i))) {
          ASTNodeAssignation node1 = (ASTNodeAssignation) nodeStack.pop();
          ASTNode node2 = nodeStack.pop();
          Optional<ASTNode> operation =
              ASTNodeIdentifier(tokenList.get(i), node1.getRightChild(), node2);
          node1.setRightChild(operation.get());
          nodeStack.push(node1);
        }
      }
    }
    return nodeStack;
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
        try {
          return Optional.of(ASTNodeFactory.print(token));
        } catch (IllegalArgumentException j) {
          return Optional.empty();
        }
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
}
