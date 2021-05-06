package ASTNode.MultiChilds;

import ASTNode.ASTNode;
import java.util.List;
import token.Token;

public class ASTNodeIfElse extends ASTNodeMultiChilds {

  public ASTNodeIfElse(ASTNode leftChild, ASTNode rightChild, Token token) {
    super(leftChild, rightChild, token);
  }

  public ASTNodeIfElse(ASTNode leftChild, List<ASTNode> rightChild, Token token) {
    super(leftChild, rightChild, token);
  }

  @Override
  public String toString() {
    return "IfElseNode: ("
        + leftChild.toString()
        + ") "
        + token
        + " ("
        + rightChild.toString()
        + ") ";
  }

  @Override
  public String getNodeType() {
    return "ifElse";
  }
}
