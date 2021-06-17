package ASTNode.NotChildless;

import ASTNode.ASTNode;
import ASTNode.NodeType;
import java.util.Objects;
import token.Token;

public abstract class ASTNodeSingleChild extends ASTNode {
  ASTNode child;

  public ASTNode getChild() {
    return child;
  }

  public void setChild(ASTNode child) {
    this.child = child;
  }

  public ASTNodeSingleChild(ASTNode child, Token token) {
    this.child = child;
    this.token = token;
    super.nodeType = NodeType.SINGLECHILD;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof ASTNodeSingleChild))
      return false;
    if (!super.equals(o))
      return false;
    ASTNodeSingleChild that = (ASTNodeSingleChild) o;
    return Objects.equals(child, that.child) && Objects.equals(getNodeType(), that.getNodeType())
        && Objects.equals(token, that.token);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), child, getNodeType());
  }
}
