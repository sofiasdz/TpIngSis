package ASTNode.NotChildless;

import ASTNode.ASTNode;
import token.Token;

public class ASTNodeDeclaration extends ASTNodeNotChildless {

  public ASTNodeDeclaration(ASTNode leftChild, ASTNode rightChild, Token token) {
    super(leftChild, rightChild, token);
  }

  @Override
  public String toString() {
    return "Declaration: (" + leftChild.toString() + ") " + token + " (" + rightChild.toString()
        + ") ";
  }

  @Override
  public String getNodeType() {
    return "declaration";
  }
}
