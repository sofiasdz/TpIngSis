import ASTNode.ASTNode;
import ASTNode.Childless.ASTNodeLiteral;
import ASTNode.Childless.ASTNodePrint;
import ASTNode.NotChildless.ASTNodeAssignation;
import ASTNode.NotChildless.ASTNodeDeclaration;
import ASTNode.NotChildless.ASTNodeOperation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import token.TokenType;

public class PS11Interpreter implements Interpreter {

  HashMap<String, String> stringVariables;
  HashMap<String, Double> numberVariables;
  List<String> prints;

  public PS11Interpreter() {}

  private void initialize() {
    stringVariables = new HashMap<>();
    numberVariables = new HashMap<>();
    prints = new ArrayList<>();
  }

  public List<String> analyze(List<ASTNode> nodes) {
    initialize();
    for (int i = 0; i < nodes.size(); i++) {
      nodeExecution(nodes.get(i));
    }
    return prints;
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
      default:
        break;
    }
  }

  private void nodeExecution(ASTNodePrint node) {
    String val = node.token.getValue();
    if (val.charAt(0) == '"')
      prints.add(val.substring(1, val.length() - 1));
    else if (isNumber(val))
      prints.add(val);
    else if (numberVariables.containsKey(val))
      prints.add((numberVariables.get(val)).toString());
    else if (stringVariables.containsKey(val))
      prints.add(stringVariables.get(val));
    else
      throw new RuntimeException("Error at line: " + node.token.getStartingLine() + ": Variable "
          + val + " was not declared!");
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
    if (stringVariables.containsKey(identifier) || numberVariables.containsKey(identifier))
      throw new IllegalArgumentException("Error at line: " + node.token.getStartingLine()
          + ": Variable " + identifier + " already declared!");
    if (node.getLeftChild().token.getType().equals(TokenType.STRING_TYPE)) {
      stringVariables.put(identifier, "");
    } else {
      numberVariables.put(identifier, 0.0);
    }
  }

  private void nodeExecution(ASTNodeAssignation node) {
    if (node.getLeftChild().getNodeType().equals("declaration")) {
      ASTNodeDeclaration dNode = (ASTNodeDeclaration) node.getLeftChild();
      String identifier = dNode.getRightChild().token.getValue();
      if (stringVariables.containsKey(identifier) || numberVariables.containsKey(identifier))
        throw new IllegalArgumentException("Error at line: " + node.token.getStartingLine()
            + ": Variable " + identifier + " already declared!");
      if (dNode.getLeftChild().token.getType().equals(TokenType.STRING_TYPE)) {
        String value = stringValueGetter(node.getRightChild());
        stringVariables.put(identifier, value);
      } else {
        Double value = numberValueGetter(node.getRightChild());
        numberVariables.put(identifier, value);
      }
    } else {
      String identifier = node.getLeftChild().token.getValue();
      if (!(stringVariables.containsKey(identifier) || numberVariables.containsKey(identifier)))
        throw new IllegalArgumentException("Error at line: " + node.token.getStartingLine()
            + ": Variable was not declared!");
      if (stringVariables.containsKey(identifier)) {
        String value = stringValueGetter(node.getRightChild());
        stringVariables.put(identifier, value);
      } else {
        Double value = numberValueGetter(node.getRightChild());
        numberVariables.put(identifier, value);
      }
    }
  }

  private Double numberValueGetter(ASTNode rightChild) {
        return switch (rightChild.getNodeType()) {
            case "literal" -> numberLiteralValidator((ASTNodeLiteral) rightChild);
            case "identifier" -> numberVariables.get(rightChild.token.getValue());
            default -> numberOperation((ASTNodeOperation) rightChild);
        };
    }

  private Double numberLiteralValidator(ASTNodeLiteral node) {
    if (node.token.getType().equals(TokenType.STRING))
      throw new RuntimeException("Error at line: " + node.token.getStartingLine()
          + ": You are trying to assign a string to a non-string variable!");
    String literal = node.token.getValue();
    try {
      return Double.parseDouble(literal);
    } catch (NumberFormatException e) {
      throw new RuntimeException("Error at line: " + node.token.getStartingLine()
          + ": You are trying to assign a string to a non-string variable!");
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
      }
      throw new RuntimeException("Error at line: " + node.token.getStartingLine()
          + ": Variable not declared!");
    }
    if (node.getNodeType().equals("literal"))
      return Double.parseDouble(node.token.getValue());
    return numberOperation((ASTNodeOperation) node);
  }

  private String stringValueGetter(ASTNode rightChild) {
        return switch (rightChild.getNodeType()) {
            case "literal" -> stringLiteralValidator((ASTNodeLiteral) rightChild);
            case "identifier" -> stringVariables.get(rightChild.token.getValue());
            case "operation" -> stringOperation((ASTNodeOperation) rightChild);
            default -> throw new RuntimeException(
                    "on token: "
                            + rightChild.token.getValue()
                            + " line: "
                            + rightChild.token.getStartingLine()
                            + " Strings can only use + operator!");
        };
    }

  private String stringOperation(ASTNodeOperation node) {
    if (node.token.getType().equals(TokenType.ADDITION)) {
      String value;
      ASTNode currentChild = node.getLeftChild();
      if (currentChild.getNodeType().equals("identifier")) {
        value =
            stringVariables.containsKey(currentChild.token.getValue()) ? stringVariables
                .get(currentChild.token.getValue()) : "\""
                + numberVariables.get(currentChild.token.getValue()).toString() + "\"";
      } else if (currentChild.getNodeType().equals("literal"))
        value = node.getLeftChild().token.getValue();
      else
        value = stringOperation((ASTNodeOperation) currentChild);
      value = value.substring(0, value.length() - 1);

      currentChild = node.getRightChild();
      if (currentChild.getNodeType().equals("identifier")) {
        value +=
            stringVariables.containsKey(currentChild.token.getValue()) ? stringVariables.get(
                currentChild.token.getValue()).substring(1) : numberVariables.get(
                currentChild.token.getValue()).toString()
                + "\"";
      } else if (currentChild.getNodeType().equals("literal"))
        value += currentChild.token.getValue().substring(1);
      else
        value += stringOperation((ASTNodeOperation) currentChild).substring(1);

      return value;
    } else
      throw new RuntimeException("Error at line " + node.token.getStartingLine()
          + ": Strings can only use + operator!");
  }

  private String stringLiteralValidator(ASTNodeLiteral node) {
    if (!node.token.getType().equals(TokenType.STRING))
      throw new RuntimeException("Error at line: " + node.token.getStartingLine()
          + ": You are trying to assign a non-string to a string variable!");
    String literal = node.token.getValue();
    if (literal.charAt(0) == '"' && literal.charAt(literal.length() - 1) == '"')
      return literal;
    throw new RuntimeException("Error at line: " + node.token.getStartingLine()
        + ": You are trying to assign a non-string to a string variable!");
  }
}
