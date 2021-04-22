package ASTNode.NotChildless;

import ASTNode.ASTNode;
import token.Token;

public class ASTNodeAssignation extends ASTNodeNotChildless {

  public ASTNodeAssignation(ASTNode leftChild, ASTNode rightChild, Token token) {
    super(leftChild, rightChild, token);
  }

  @Override
  public String toString() {
    return "Assignation: ("
        + leftChild.toString()
        + ") "
        + token
        + " ("
        + rightChild.toString()
        + ") ";
  }

  @Override
  public String getNodeType() {
    return "assignation";
  }
}
