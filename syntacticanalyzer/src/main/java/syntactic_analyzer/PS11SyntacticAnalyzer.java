package syntactic_analyzer;

import ASTNode.ASTNode;
import ASTNode.Factory.ASTNodeFactory;
import ASTNode.Factory.ASTNodeFactory11;
import ASTNode.NotChildless.ASTNodeBooleanOperation;
import ASTNode.TokenGroup.TokenGroup;
import java.util.*;
import token.Token;
import token.TokenType;

public class PS11SyntacticAnalyzer implements SyntacticAnalyzer {

  public PS11SyntacticAnalyzer() {}

  @Override
  public List<ASTNode> analyze(List<Token> tokens) {
    List<ASTNode> nodes = new ArrayList<>();
    List<Token> tokenList = new ArrayList<>();
    for (int i = 0; i < tokens.size(); i++) {
      tokenList.add(tokens.get(i));
      if (tokens.get(i).getType().equals(TokenType.IF)) {
        for (int j = i + 1; j < tokens.size(); j++) {
          if (!tokens.get(j).getType().equals(TokenType.OPENING_BRACKETS)) {
            tokenList.add(tokens.get(j));
          } else {
            i = bracketResolver(tokens, tokenList, nodes, j);
            j = tokens.size();
            tokenList = new ArrayList<>();
          }
        }
      } else if (tokens.get(i).getType().equals(TokenType.ELSE)) {
        for (int j = i + 1; j < tokens.size(); j++) {
          if (!tokens.get(j).getType().equals(TokenType.OPENING_BRACKETS)) {
            throw new RuntimeException(
                "Error at line "
                    + tokens.get(j).getStartingLine()
                    + ": a brackets block must be created after an else");
          } else {
            i = bracketResolver(tokens, tokenList, nodes, j);
            j = tokens.size();
            tokenList = new ArrayList<>();
          }
        }
      } else if (tokens.get(i).getType().equals(TokenType.SEMICOLON)) {
        nodes.add(tokensToNode(tokenList));
        tokenList = new ArrayList<>();
      }
    }
    if (!tokenList.isEmpty())
      throw new RuntimeException(
          "Error at line " + tokenList.get(0).getStartingLine() + ": missing ;");
    return nodes;
  }

  private int bracketResolver(
      List<Token> tokens, List<Token> tokenList, List<ASTNode> nodes, int j) {
    ArrayList<Token> branch = new ArrayList<>();
    for (int k = j + 1; k < tokens.size(); k++) {
      if (!tokens.get(k).getType().equals(TokenType.CLOSING_BRACKETS)) {
        branch.add(tokens.get(k));
      } else {
        if (tokenList.get(0).getType().equals(TokenType.IF)) {
          nodes.add(ifNodeBuilder(tokenList, branch));
        } else {
          if (nodes.isEmpty())
            throw new RuntimeException(
                "Error at line "
                    + tokenList.get(0).getStartingLine()
                    + ": code can't start with an else");
          nodes.set(
              nodes.size() - 1, elseNodeBuilder(tokenList, branch, nodes.get(nodes.size() - 1)));
        }
        return k;
      }
    }
    throw new RuntimeException(
        "Error at line " + tokenList.get(0).getStartingLine() + ": Invalid brackets block");
  }

  private ASTNode ifNodeBuilder(List<Token> ifList, List<Token> branch) {
    try {
      for (int i = ifList.size() - 1; i > 0; i--) {
        if (ifList.get(i).getType().equals(TokenType.OPENING_PARENTHESIS)
            || ifList.get(i).getType().equals(TokenType.CLOSING_PARENTHESIS)) {
          ifList.remove(i);
        }
      }
      ASTNode leftChild;
      if (ifList.size() == 2) {
        leftChild = ASTNodeIdentifier(ifList.get(1)).get();
      } else {
        leftChild = booleanOperationNodeBuilder(ifList);
      }
      List<ASTNode> branchCode = this.analyze(branch);
      return ASTNodeIdentifier(ifList.get(0), leftChild, branchCode).get();
    } catch (NoSuchElementException e) {
      throw new RuntimeException(
          "Error at line " + ifList.get(0).getStartingLine() + ": Invalid if declaration");
    }
  }

  private ASTNode elseNodeBuilder(List<Token> elseList, List<Token> branch, ASTNode ifNode) {
    try {
      if (!ifNode.getNodeType().equals("if"))
        throw new RuntimeException(
            "Error at line " + elseList.get(0).getStartingLine() + ": else must go after an if");
      List<ASTNode> branchCode = this.analyze(branch);
      return ASTNodeIdentifier(elseList.get(0), ifNode, branchCode).get();
    } catch (NoSuchElementException e) {
      throw new RuntimeException(
          "Error at line " + elseList.get(0).getStartingLine() + ": Invalid else declaration");
    }
  }

  private ASTNodeBooleanOperation booleanOperationNodeBuilder(List<Token> list) {
    try {
      Token opToken = list.get(2);
      ASTNode left = ASTNodeIdentifier(list.get(1)).get();
      ASTNode right = ASTNodeIdentifier(list.get(3)).get();
      return new ASTNodeBooleanOperation(left, right, opToken);
    } catch (NoSuchElementException e) {
      throw new RuntimeException(
          "Error at line " + list.get(0).getStartingLine() + ": Invalid boolean operation");
    }
  }

  private ASTNode tokensToNode(List<Token> tokens) {
    tokens.remove(tokens.size() - 1);
    if (tokens.get(0).getType().equals(TokenType.LET)
        || tokens.get(0).getType().equals(TokenType.CONST)) {
      Optional<ASTNode> identifier = ASTNodeIdentifier(tokens.get(1));
      Optional<ASTNode> type = ASTNodeIdentifier(tokens.get(3));
      if (identifier.isEmpty() || type.isEmpty())
        throw new RuntimeException(
            "Error at line " + tokens.get(0).getStartingLine() + ": Invalid variable declaration");
      Optional<ASTNode> declaration =
          ASTNodeIdentifier(tokens.get(0), type.get(), identifier.get());
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
                TokenType.IDENTIFIER,
                TokenType.BOOLEAN_TYPE,
                TokenType.TRUE,
                TokenType.FALSE));
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
                TokenType.MULTIPLICATION,
                TokenType.GREATER,
                TokenType.EQUAL_OR_S,
                TokenType.SMALLER,
                TokenType.EQUAL_OR_G));
    TokenGroup operands =
        new TokenGroup(
            List.of(
                TokenType.STRING,
                TokenType.INTEGER,
                TokenType.FLOATING_POINT,
                TokenType.IDENTIFIER,
                TokenType.BOOLEAN_TYPE,
                TokenType.TRUE,
                TokenType.FALSE));
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
    switch (token.getType()) {
      case ADDITION:
      case SUBSTRACTION:
        return 2;
      case DIVISION:
      case MULTIPLICATION:
        return 3;
      default:
        return 0;
    }
  }

  Optional<ASTNode> ASTNodeIdentifier(Token token) {
    if (token.getType().equals(TokenType.IDENTIFIER)) {
      return Optional.of(ASTNodeFactory11.identifier(token));
    } else if (token.getType().equals(TokenType.NUMBER_TYPE)
        | token.getType().equals(TokenType.STRING_TYPE)
        | token.getType().equals(TokenType.BOOLEAN_TYPE)) {
      return Optional.of(ASTNodeFactory11.variableType(token));
    } else {
      try {
        return Optional.of(ASTNodeFactory11.literal(token));
      } catch (IllegalArgumentException e) {
        try {
          return Optional.of(ASTNodeFactory11.print(token));
        } catch (IllegalArgumentException j) {
          return Optional.empty();
        }
      }
    }
  }

  Optional<ASTNode> ASTNodeIdentifier(Token token, ASTNode left, ASTNode right) {
    try {
      return Optional.of(ASTNodeFactory11.assignation(token, left, right));
    } catch (IllegalArgumentException e) {
      try {
        return Optional.of(ASTNodeFactory11.declaration(token, left, right));
      } catch (IllegalArgumentException i) {
        try {
          return Optional.of(ASTNodeFactory11.operation(token, left, right));
        } catch (IllegalArgumentException h) {
          try {
            return Optional.of(ASTNodeFactory11.booleanOperation(token, left, right));
          } catch (IllegalArgumentException j) {
            return Optional.empty();
          }
        }
      }
    }
  }

  Optional<ASTNode> ASTNodeIdentifier(Token token, ASTNode left, List<ASTNode> right) {
    try {
      return Optional.of(ASTNodeFactory11.ifNode(token, left, right));
    } catch (IllegalArgumentException e) {
      try {
        return Optional.of(ASTNodeFactory11.ifElseNode(token, left, right));
      } catch (IllegalArgumentException f) {
        return Optional.empty();
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
