package ASTNode.NotChildless;

import ASTNode.ASTNode;
import token.Token;

public class ASTNodeBooleanOperation extends ASTNodeNotChildless {

  public ASTNodeBooleanOperation(ASTNode leftChild, ASTNode rightChild, Token token) {
    super(leftChild, rightChild, token);
  }

  @Override
  public String toString() {
    return "Operation: ("
        + leftChild.toString()
        + ") "
        + token
        + " ("
        + rightChild.toString()
        + ") ";
  }

  @Override
  public String getNodeType() {
    return "booleanOperation";
  }
}
