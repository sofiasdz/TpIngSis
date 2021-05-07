package ASTNode.MultiChilds;

import ASTNode.ASTNode;
import ASTNode.NodeType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import token.Token;

public abstract class ASTNodeMultiChilds extends ASTNode {
  ASTNode leftChild;
  List<ASTNode> rightChild;

  public ASTNode getLeftChild() {
    return leftChild;
  }

  public void setLeftChild(ASTNode leftChild) {
    this.leftChild = leftChild;
  }

  public List<ASTNode> getRightChild() {
    return rightChild;
  }

  public ASTNodeMultiChilds(ASTNode leftChild, List<ASTNode> rightChild, Token token) {
    this.leftChild = leftChild;
    this.token = token;
    super.nodeType = NodeType.MULTICHILDS;
    this.rightChild = rightChild;
  }

  public ASTNodeMultiChilds(ASTNode leftChild, ASTNode rightChild, Token token) {
    this.leftChild = leftChild;
    this.token = token;
    super.nodeType = NodeType.MULTICHILDS;
    this.rightChild = new ArrayList<>();
    this.rightChild.add(rightChild);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ASTNodeMultiChilds)) return false;
    if (!super.equals(o)) return false;
    ASTNodeMultiChilds that = (ASTNodeMultiChilds) o;
    return Objects.equals(leftChild, that.leftChild)
        && Objects.equals(rightChild, that.rightChild)
        && Objects.equals(getNodeType(), that.getNodeType())
        && Objects.equals(token, that.token);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), leftChild, rightChild, getNodeType());
  }
}
