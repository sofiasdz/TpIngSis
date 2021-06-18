package Interpreter;

import ASTNode.ASTNode;
import ASTNode.Childless.ASTNodeLiteral;
import ASTNode.MultiChilds.ASTNodeIf;
import ASTNode.MultiChilds.ASTNodeIfElse;
import ASTNode.NodeType;
import ASTNode.NotChildless.*;
import ASTNode.TokenGroup.TokenGroup;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import token.TokenType;

public class PS11Interpreter implements Interpreter {

  HashMap<String, String> stringVariables;
  HashMap<String, Double> numberVariables;
  HashMap<String, Boolean> booleanVariables;
  HashMap<String, String> stringConst;
  HashMap<String, Double> numberConst;
  HashMap<String, Boolean> booleanConst;
  ArrayList<String> uninitializedConst;
  List<String> prints;

  public PS11Interpreter() {}

  private void initialize() {
    stringVariables = new HashMap<>();
    numberVariables = new HashMap<>();
    booleanVariables = new HashMap<>();
    numberConst = new HashMap<>();
    stringConst = new HashMap<>();
    booleanConst = new HashMap<>();
    uninitializedConst = new ArrayList<>();
    prints = new ArrayList<>();
  }

  public List<String> analyze(List<ASTNode> nodes) {
    initialize();
    for (int i = 0; i < nodes.size(); i++) {
      nodeExecution(nodes.get(i));
    }
    return prints;
  }

  private void analyzeBranch(List<ASTNode> nodes) {
    for (int i = 0; i < nodes.size(); i++) {
      nodeExecution(nodes.get(i));
    }
  }

  private void nodeExecution(ASTNode node) {
    switch (node.getNodeType()) {
      case "declaration":
        nodeExecution((ASTNodeDeclaration) node);
        break;
      case "assignation":
        nodeExecution((ASTNodeAssignation) node);
        break;
      case "print":
        nodeExecution((ASTNodePrintln) node);
        break;
      case "if":
        nodeExecution((ASTNodeIf) node);
        break;
      case "ifElse":
        nodeExecution((ASTNodeIfElse) node);
        break;
      default:
        break;
    }
  }

  private void nodeExecution(ASTNodeIf node) {

    ASTNode left = node.getLeftChild();
    if (!isBoolean(left))
      throw new RuntimeException(
          "Error at line " + node.getToken().getStartingLine() + ": Invalid argument in if!");
    if (booleanValueGetter(left)) {
      analyzeBranch(node.getRightChild());
    }
  }

  private boolean isBoolean(ASTNode node) {
    if (node.getToken().getType().equals(TokenType.IDENTIFIER)) {
      return (booleanConst.containsKey(node.getToken().getValue())
          || booleanVariables.containsKey(node.getToken().getValue()));
    }
    TokenGroup tg =
        new TokenGroup(
            List.of(
                TokenType.BOOLEAN_TYPE,
                TokenType.TRUE,
                TokenType.FALSE,
                TokenType.GREATER,
                TokenType.SMALLER,
                TokenType.EQUAL_OR_G,
                TokenType.EQUAL_OR_S));
    return tg.belongs(node.getToken());
  }

  private void nodeExecution(ASTNodeIfElse node) {
    ASTNodeIf left = (ASTNodeIf) node.getLeftChild();
    if (booleanValueGetter(left.getLeftChild())) {
      analyzeBranch(left.getRightChild());
    } else {
      analyzeBranch(node.getRightChild());
    }
  }

  private void nodeExecution(ASTNodePrintln node) {
    TokenGroup tg =
        new TokenGroup(
            List.of(
                TokenType.STRING,
                TokenType.INTEGER,
                TokenType.FLOATING_POINT,
                TokenType.TRUE,
                TokenType.BOOLEAN_TYPE,
                TokenType.FALSE,
                TokenType.IDENTIFIER));
    String value = "";
    if (tg.belongs(node.getChild().getToken())) {
      if (printIsString(node.getChild())) {
        value = stringValueGetter(node.getChild());
      } else if (printIsBoolean(node.getChild())) {
        value = booleanValueGetter(node.getChild()).toString();
      } else value = numberVariableToString(node.getChild().getToken().getValue());
    } else {
      if (printIsString(node.getChild())) {
        value = stringOperation((ASTNodeOperation) node.getChild());
      } else if (printIsBoolean(node.getChild())) {
        value = booleanOperation((ASTNodeBooleanOperation) node.getChild()).toString();
      } else value = numberOperation((ASTNodeOperation) node.getChild()).toString();
    }
    if (value.isEmpty())
      throw new RuntimeException(
          "Error at line " + node.getToken().getStartingLine() + ": Error at print statement!");
    if (value.charAt(0) == '"' && value.charAt(value.length() - 1) == '"')
      value = value.substring(1, value.length() - 1);
    prints.add(value);
  }

  private String numberVariableToString(String identifier) {
    String value =
        numberVariables.containsKey(identifier)
            ? numberVariables.get(identifier).toString()
            : numberConst.get(identifier).toString();
    if (value.charAt(value.length() - 1) == '0' && value.charAt(value.length() - 2) == '.') {
      value = value.substring(0, value.length() - 2);
    }
    return value;
  }

  private boolean printIsString(ASTNode node) {
    TokenGroup tg = new TokenGroup(List.of(TokenType.STRING, TokenType.STRING_TYPE));
    if (node.getTypeEnum().equals(NodeType.CHILDLESS)) {
      if (node.getNodeType().equals("identifier"))
        return (stringVariables.containsKey(node.getToken().getValue())
            || stringConst.containsKey(node.getToken().getValue()));
      return tg.belongs(node.getToken());
    } else {
      ASTNodeNotChildless notChildless = (ASTNodeNotChildless) node;

      if (printIsString(notChildless.getRightChild())) return true;
      if (printIsString(notChildless.getLeftChild())) return true;
    }
    return false;
  }

  private boolean printIsBoolean(ASTNode node) {
    TokenGroup tg =
        new TokenGroup(List.of(TokenType.TRUE, TokenType.FALSE, TokenType.BOOLEAN_TYPE));
    if (node.getTypeEnum().equals(NodeType.CHILDLESS)) {
      if (node.getNodeType().equals("identifier"))
        return (booleanVariables.containsKey(node.getToken().getValue())
            || booleanConst.containsKey(node.getToken().getValue()));
      return tg.belongs(node.getToken());
    } else {
      ASTNodeNotChildless notChildless = (ASTNodeNotChildless) node;

      if (printIsBoolean(notChildless.getRightChild())) return true;
      if (printIsBoolean(notChildless.getLeftChild())) return true;
    }
    return false;
  }

  private void nodeExecution(ASTNodeDeclaration node) {
    String identifier = node.getRightChild().token.getValue();
    if (identifierExists(identifier))
      throw new IllegalArgumentException(
          "Error at line: "
              + node.token.getStartingLine()
              + ": Variable "
              + identifier
              + " already declared!");
    if (node.getToken().equals(TokenType.CONST)) {
      if (node.getLeftChild().token.getType().equals(TokenType.BOOLEAN_TYPE)) {
        booleanConst.put(identifier, false);
      } else if (node.getLeftChild().token.getType().equals(TokenType.STRING_TYPE)) {
        stringConst.put(identifier, "");
      } else {
        numberConst.put(identifier, 0.0);
      }
      uninitializedConst.add(identifier);
    } else {
      if (node.getLeftChild().token.getType().equals(TokenType.BOOLEAN_TYPE)) {
        booleanVariables.put(identifier, false);
      } else if (node.getLeftChild().token.getType().equals(TokenType.STRING_TYPE)) {
        stringVariables.put(identifier, "");
      } else {
        numberVariables.put(identifier, 0.0);
      }
    }
  }

  private void nodeExecution(ASTNodeAssignation node) {
    if (node.getLeftChild().getNodeType().equals("declaration")) {
      ASTNodeDeclaration dNode = (ASTNodeDeclaration) node.getLeftChild();
      String identifier = dNode.getRightChild().token.getValue();
      if (identifierExists(identifier))
        throw new IllegalArgumentException(
            "Error at line: "
                + node.token.getStartingLine()
                + ": Variable "
                + identifier
                + " already declared!");
      if (dNode.getToken().getType().equals(TokenType.CONST)) {
        if (dNode.getLeftChild().token.getType().equals(TokenType.BOOLEAN_TYPE)) {
          Boolean value = booleanValueGetter(node.getRightChild());
          booleanConst.put(identifier, value);
        } else if (dNode.getLeftChild().token.getType().equals(TokenType.STRING_TYPE)) {
          String value = stringValueGetter(node.getRightChild());
          stringConst.put(identifier, value);
        } else {
          Double value = numberValueGetter(node.getRightChild());
          numberConst.put(identifier, value);
        }
      } else {
        if (dNode.getLeftChild().token.getType().equals(TokenType.BOOLEAN_TYPE)) {
          Boolean value = booleanValueGetter(node.getRightChild());
          booleanVariables.put(identifier, value);
        } else if (dNode.getLeftChild().token.getType().equals(TokenType.STRING_TYPE)) {
          String value = stringValueGetter(node.getRightChild());
          stringVariables.put(identifier, value);
        } else {
          Double value = numberValueGetter(node.getRightChild());
          numberVariables.put(identifier, value);
        }
      }
    } else {
      String identifier = node.getLeftChild().token.getValue();
      if (!identifierExists(identifier))
        throw new IllegalArgumentException(
            "Error at line: " + node.token.getStartingLine() + ": Variable was not declared!");
      if (constWasDeclared(identifier)) {
        if (uninitializedConst.contains(identifier)) {
          if (booleanConst.containsKey(identifier)) {
            Boolean value = booleanValueGetter(node.getRightChild());
            booleanConst.put(identifier, value);
          } else if (stringConst.containsKey(identifier)) {
            String value = stringValueGetter(node.getRightChild());
            stringConst.put(identifier, value);
          } else {
            Double value = numberValueGetter(node.getRightChild());
            numberConst.put(identifier, value);
          }
          uninitializedConst.remove(identifier);
        } else
          throw new IllegalArgumentException(
              "Error at line: "
                  + node.token.getStartingLine()
                  + ": you can't modify a const's value");
      } else {
        if (booleanVariables.containsKey(identifier)) {
          Boolean value = booleanValueGetter(node.getRightChild());
          booleanVariables.put(identifier, value);
        } else if (stringVariables.containsKey(identifier)) {
          String value = stringValueGetter(node.getRightChild());
          stringVariables.put(identifier, value);
        } else {
          Double value = numberValueGetter(node.getRightChild());
          numberVariables.put(identifier, value);
        }
      }
    }
  }

  private Boolean booleanValueGetter(ASTNode rightChild) {
    switch (rightChild.getNodeType()) {
      case "literal":
        return booleanLiteralParse(rightChild);
      case "identifier":
        return booleanVariables.containsKey(rightChild.token.getValue())
                ? booleanVariables.get(rightChild.token.getValue())
                : booleanConst.get(rightChild.token.getValue());
      default:
        return booleanOperation((ASTNodeBooleanOperation) rightChild);
    }
  }

  private Boolean booleanLiteralParse(ASTNode node) {
    String value = node.getToken().getValue();
    if (value.equals("true")) return true;
    if (value.equals("false")) return false;
    throw new RuntimeException(
        "Error at line "
            + node.getToken().getStartingLine()
            + ": you are trying to assign a non-boolean to a boolean");
  }

  private Boolean booleanOperation(ASTNodeBooleanOperation node) {
    Double left = fetcher(node.getLeftChild());
    Double right = fetcher(node.getRightChild());
    switch (node.token.getType()) {
      case GREATER:
        return (left > right);
      case SMALLER:
        return (left < right);
      case EQUAL_OR_G:
        return (left >= right);
      default:
        return (left <= right);
    }
  }

  private Double numberValueGetter(ASTNode rightChild) {
    switch (rightChild.getNodeType()) {
      case "literal":
        return numberLiteralValidator((ASTNodeLiteral) rightChild);
      case "identifier":
        return numberVariables.containsKey(rightChild.token.getValue())
                ? numberVariables.get(rightChild.token.getValue())
                : numberConst.get(rightChild.token.getValue());
      default:
        return numberOperation((ASTNodeOperation) rightChild);
    }
  }

  private Double numberLiteralValidator(ASTNodeLiteral node) {
    if (!node.token.getType().equals(TokenType.INTEGER)
        && !node.token.getType().equals(TokenType.FLOATING_POINT))
      throw new RuntimeException(
          "Error at line: "
              + node.token.getStartingLine()
              + ": You are trying to assign a non-number to a number variable!");
    String literal = node.token.getValue();
    try {
      return Double.parseDouble(literal);
    } catch (NumberFormatException e) {
      throw new RuntimeException(
          "Error at line: "
              + node.token.getStartingLine()
              + ": You are trying to assign a non-number to a number variable!");
    }
  }

  private Double numberOperation(ASTNodeOperation node) {
    Double leftVal = fetcher(node.getLeftChild());
    Double rightVal = fetcher(node.getRightChild());
    switch (node.token.getType()) {
      case ADDITION:
        return (leftVal + rightVal);
      case SUBSTRACTION:
        return (leftVal - rightVal);
      case MULTIPLICATION:
        return (leftVal * rightVal);
      default:
        return (leftVal / rightVal);
    }
  }

  private Double fetcher(ASTNode node) {
    if (node.getNodeType().equals("identifier")) {
      if (numberVariables.containsKey(node.token.getValue())) {
        return numberVariables.get(node.token.getValue());
      } else if (numberConst.containsKey(node.token.getValue())) {
        return numberConst.get(node.token.getValue());
      }
      throw new RuntimeException(
          "Error at line: " + node.token.getStartingLine() + ": Variable not declared!");
    }
    if (node.getNodeType().equals("literal")) return Double.parseDouble(node.token.getValue());
    return numberOperation((ASTNodeOperation) node);
  }

  private String stringValueGetter(ASTNode rightChild) {
    switch (rightChild.getNodeType()) {
      case "literal":
        return stringLiteralValidator((ASTNodeLiteral) rightChild);
      case "identifier":
        return stringVariables.containsKey(rightChild.token.getValue())
                ? stringVariables.get(rightChild.token.getValue())
                : stringConst.get(rightChild.token.getValue());
      default:
        return stringOperation((ASTNodeOperation) rightChild);
    }
  }

  private String stringOperation(ASTNodeOperation node) {
    if (node.token.getType().equals(TokenType.ADDITION)) {
      String value;
      ASTNode currentChild = node.getLeftChild();
      if (currentChild.getNodeType().equals("identifier")) {
        value = varAsString(currentChild.token.getValue());
      } else if (currentChild.getNodeType().equals("literal"))
        value = node.getLeftChild().token.getValue();
      else value = stringOperation((ASTNodeOperation) currentChild);
      value = value.substring(0, value.length() - 1);

      currentChild = node.getRightChild();
      if (currentChild.getNodeType().equals("identifier")) {
        value += varAsString(currentChild.token.getValue()).substring(1);
      } else if (currentChild.getNodeType().equals("literal"))
        value += currentChild.token.getValue().substring(1);
      else value += stringOperation((ASTNodeOperation) currentChild).substring(1);

      return value;
    } else
      throw new RuntimeException(
          "Error at line " + node.token.getStartingLine() + ": Strings can only use + operator!");
  }

  private String varAsString(String identifier) {
    if (stringVariables.containsKey(identifier)) return stringVariables.get(identifier);
    if (numberVariables.containsKey(identifier)) {
      String value = numberVariables.get(identifier).toString();
      if (value.charAt(value.length() - 1) == '0' && value.charAt(value.length() - 2) == '.') {
        value = value.substring(0, value.length() - 2);
      }
      return ("\"" + value + "\"");
    }
    if (booleanVariables.containsKey(identifier))
      return ("\"" + booleanVariables.get(identifier).toString() + "\"");
    if (stringConst.containsKey(identifier)) return stringConst.get(identifier);
    if (numberConst.containsKey(identifier)) {
      String value = numberConst.get(identifier).toString();
      if (value.charAt(value.length() - 1) == '0' && value.charAt(value.length() - 2) == '.') {
        value = value.substring(0, value.length() - 2);
      }
      return ("\"" + value + "\"");
    }
    return ("\"" + booleanConst.get(identifier).toString() + "\"");
  }

  private String stringLiteralValidator(ASTNodeLiteral node) {
    if (!node.token.getType().equals(TokenType.STRING))
      throw new RuntimeException(
          "Error at line: "
              + node.token.getStartingLine()
              + ": You are trying to assign a non-string to a string variable!");
    String literal = node.token.getValue();
    if (literal.charAt(0) == '"' && literal.charAt(literal.length() - 1) == '"') return literal;
    throw new RuntimeException(
        "Error at line: "
            + node.token.getStartingLine()
            + ": You are trying to assign a non-string to a string variable!");
  }

  private boolean variableWasDeclared(String identifier) {
    if (booleanVariables.containsKey(identifier)) return true;
    if (stringVariables.containsKey(identifier)) return true;
    return (numberVariables.containsKey(identifier));
  }

  private boolean constWasDeclared(String identifier) {
    if (booleanConst.containsKey(identifier)) return true;
    if (stringConst.containsKey(identifier)) return true;
    return (numberConst.containsKey(identifier));
  }

  private boolean identifierExists(String identifier) {
    return (variableWasDeclared(identifier) || constWasDeclared(identifier));
  }
}
