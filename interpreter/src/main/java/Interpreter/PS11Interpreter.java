package Interpreter;

import ASTNode.ASTNode;
import ASTNode.Childless.ASTNodeLiteral;
import ASTNode.Childless.ASTNodePrint;
import ASTNode.MultiChilds.ASTNodeIf;
import ASTNode.MultiChilds.ASTNodeIfElse;
import ASTNode.NotChildless.ASTNodeAssignation;
import ASTNode.NotChildless.ASTNodeBooleanOperation;
import ASTNode.NotChildless.ASTNodeDeclaration;
import ASTNode.NotChildless.ASTNodeOperation;
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
        nodeExecution((ASTNodePrint) node);
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
    if (booleanValueGetter(left)) {
      analyzeBranch(node.getRightChild());
    }
  }

  private void nodeExecution(ASTNodeIfElse node) {
    ASTNodeIf left = (ASTNodeIf) node.getLeftChild();
    if (booleanValueGetter(left.getLeftChild())) {
      analyzeBranch(left.getRightChild());
    } else {
      analyzeBranch(node.getRightChild());
    }
  }

  private void nodeExecution(ASTNodePrint node) {
    String val = node.token.getValue();
    if (val.charAt(0) == '"') prints.add(val.substring(1, val.length() - 1));
    else if (isNumber(val)) prints.add(val);
    else if (!identifierExists(val))
      throw new RuntimeException(
          "Error at line: "
              + node.token.getStartingLine()
              + ": Variable "
              + val
              + " was not declared!");
    else if (numberVariables.containsKey(val))
    {
      String value =(numberVariables.get(val)).toString();
      if(value.charAt(value.length()-1)=='0' && value.charAt(value.length()-2)=='.') value=value.substring(0,value.length()-2);
      prints.add(value);
    }
    else if (stringVariables.containsKey(val)) prints.add(stringVariables.get(val));
    else if (numberConst.containsKey(val))
    {
      String value =(numberConst.get(val)).toString();
      if(value.charAt(value.length()-1)=='0' && value.charAt(value.length()-2)=='.') value=value.substring(0,value.length()-2);
      prints.add(value);
    }
    else if (stringConst.containsKey(val)) prints.add(stringConst.get(val));
    else if (booleanVariables.containsKey(val)) prints.add((booleanVariables.get(val)).toString());
    else prints.add(booleanConst.get(val).toString());
  }

  private boolean isNumber(String string) {
    try {
      Integer.parseInt(string);
      return true;
    } catch (NumberFormatException e) {
      try {
        Double.parseDouble(string);
      } catch (NumberFormatException f) {
        return false;
      }
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
    return switch (rightChild.getNodeType()) {
      case "literal" -> booleanLiteralParse(rightChild);
      case "identifier" -> booleanVariables.containsKey(rightChild.token.getValue())
          ? booleanVariables.get(rightChild.token.getValue())
          : booleanConst.get(rightChild.token.getValue());
      default -> booleanOperation((ASTNodeBooleanOperation) rightChild);
    };
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
    return switch (node.token.getType()) {
      case GREATER -> (left > right);
      case SMALLER -> (left < right);
      case EQUAL_OR_G -> (left >= right);
      default -> (left <= right);
    };
  }

  private Double numberValueGetter(ASTNode rightChild) {
    return switch (rightChild.getNodeType()) {
      case "literal" -> numberLiteralValidator((ASTNodeLiteral) rightChild);
      case "identifier" -> numberVariables.containsKey(rightChild.token.getValue())
          ? numberVariables.get(rightChild.token.getValue())
          : numberConst.get(rightChild.token.getValue());
      default -> numberOperation((ASTNodeOperation) rightChild);
    };
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
    return switch (node.token.getType()) {
      case ADDITION -> (leftVal + rightVal);
      case SUBSTRACTION -> (leftVal - rightVal);
      case MULTIPLICATION -> (leftVal * rightVal);
      default -> (leftVal / rightVal);
    };
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
    return switch (rightChild.getNodeType()) {
      case "literal" -> stringLiteralValidator((ASTNodeLiteral) rightChild);
      case "identifier" -> stringVariables.containsKey(rightChild.token.getValue())
          ? stringVariables.get(rightChild.token.getValue())
          : stringConst.get(rightChild.token.getValue());
      default -> stringOperation((ASTNodeOperation) rightChild);
    };
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
    if (numberVariables.containsKey(identifier)){
      String value = numberVariables.get(identifier).toString();
      if(value.charAt(value.length()-1)=='0' && value.charAt(value.length()-2)=='.'){
        value = value.substring(0,value.length()-2);
      }
      return ("\"" + value + "\"");
    }
    if (booleanVariables.containsKey(identifier))
      return ("\"" + booleanVariables.get(identifier).toString() + "\"");
    if (stringConst.containsKey(identifier)) return stringConst.get(identifier);
    if (numberConst.containsKey(identifier)){
      String value = numberConst.get(identifier).toString();
      if(value.charAt(value.length()-1)=='0' && value.charAt(value.length()-2)=='.'){
        value = value.substring(0,value.length()-2);
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
