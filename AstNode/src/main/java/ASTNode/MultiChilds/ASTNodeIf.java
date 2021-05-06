package ASTNode.MultiChilds;

import ASTNode.ASTNode;
import ASTNode.NotChildless.ASTNodeNotChildless;
import token.Token;

import java.util.List;

public class ASTNodeIf extends ASTNodeMultiChilds {

  public ASTNodeIf(ASTNode leftChild, ASTNode rightChild, Token token) {
    super(leftChild,rightChild,token);
  }

  public ASTNodeIf(ASTNode leftChild, List<ASTNode> rightChild, Token token) {
    super(leftChild,rightChild,token);
  }

  @Override
  public String toString() {
    return "IfNode: ("
        + leftChild.toString()
        + ") "
        + token
        + " ("
        + rightChild.toString()
        + ") ";
  }

  @Override
  public String getNodeType() {
    return "if";
  }
}
