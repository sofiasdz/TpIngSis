package syntactic_analyzer;

import ASTNode.ASTNode;
import ASTNode.Childless.ASTNodeIdentifier;
import ASTNode.Childless.ASTNodeLiteral;
import ASTNode.Factory.ASTNodeFactory;
import ASTNode.Factory.ASTNodeFactory11;
import ASTNode.MultiChilds.ASTNodeIf;
import ASTNode.NotChildless.ASTNodeBooleanOperation;
import ASTNode.NotChildless.ASTNodeOperation;
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
      if(tokens.get(i).getType().equals(TokenType.IF)){
        for (int j = i+1; j < tokens.size() ; j++) {
          if(!tokens.get(j).getType().equals(TokenType.OPENING_BRACKETS)){
            tokenList.add(tokens.get(j));
          } else {
            ArrayList<Token> branch = new ArrayList<>();
            for (int k = j+1; k < tokens.size(); k++) {
              if(!tokens.get(k).getType().equals(TokenType.CLOSING_BRACKETS)){
                branch.add(tokens.get(k));
              } else {
                nodes.add(ifNodeBuilder(tokenList,branch));
                tokenList = new ArrayList<>();
                i=k;
                k=tokens.size();
                j=tokens.size();
              }
            }
          }
        }
      }
      if (tokens.get(i).getType().equals(TokenType.SEMICOLON)) {
        nodes.add(tokensToNodeRefactored(tokenList));
        tokenList = new ArrayList<>();
      }
    }
    return nodes;
  }

  private ASTNode ifNodeBuilder(List<Token> ifList, List<Token> branch){
    try{
      for (int i = ifList.size()-1; i > 0; i--) {
        if(ifList.get(i).getType().equals(TokenType.OPENING_PARENTHESIS) || ifList.get(i).getType().equals(TokenType.CLOSING_PARENTHESIS)){
          ifList.remove(i);
        }
      }
      ASTNode leftChild;
      if(ifList.size() == 2) {
        leftChild = ASTNodeIdentifier(ifList.get(1)).get();
      } else {
        leftChild = booleanOperationNodeBuilder(ifList);
      }
      List<ASTNode> branchCode = this.analyze(branch);
      return ASTNodeIdentifier(ifList.get(0),leftChild,branchCode).get();
    } catch (NoSuchElementException e){
      throw new RuntimeException("Error at line "+ifList.get(0).getStartingLine()+": Invalid if declaration");
    }
  }

  private ASTNodeBooleanOperation booleanOperationNodeBuilder(List<Token> list){
    try {
      Token opToken = list.get(2);
      ASTNode left = ASTNodeIdentifier(list.get(1)).get();
      ASTNode right = ASTNodeIdentifier(list.get(3)).get();
      return new ASTNodeBooleanOperation(left, right, opToken);
    } catch (NoSuchElementException e){
      throw new RuntimeException("Error at line "+list.get(0).getStartingLine()+": Invalid boolean operation");
    }
  }

  private ASTNode tokensToNodeRefactored(List<Token> tokens) {
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
      Optional<ASTNode> print = ASTNodeIdentifier(tokens.get(0));
      if (print.isEmpty())
        throw new RuntimeException(
                "Error at line " + tokens.get(0).getStartingLine() + ": Invalid print declaration");
      return print.get();
    }
    else {
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
    return switch (token.getType()) {
      case ADDITION, SUBSTRACTION -> 2;
      case DIVISION, MULTIPLICATION -> 3;
      default -> 0;
    };
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
          try{
            return Optional.of(ASTNodeFactory11.booleanOperation(token,left,right));
          } catch (IllegalArgumentException j){
            return Optional.empty();
          }
        }
      }
    }
  }

  Optional<ASTNode> ASTNodeIdentifier(Token token, ASTNode left, List<ASTNode> right){
    try{
      return Optional.of(ASTNodeFactory11.ifNode(token,left,right));
    } catch (IllegalArgumentException e){
      try{
        return Optional.of(ASTNodeFactory11.ifElseNode(token,left,right));
      } catch (IllegalArgumentException f){
        return Optional.empty();
      }
    }
  }
}
