package ASTNode.Childless;

import ASTNode.ASTNode;
import ASTNode.NodeType;
import ASTNode.TokenGroup.TokenGroup;
import java.util.Objects;
import token.Token;

public abstract class ASTNodeChildless extends ASTNode {

  public ASTNodeChildless(Token token) {
    super.token = token;
    super.nodeType = NodeType.CHILDLESS;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ASTNodeChildless)) return false;
    if (!super.equals(o)) return false;
    ASTNodeChildless that = (ASTNodeChildless) o;
    return Objects.equals(getNodeType(), that.getNodeType()) && Objects.equals(token, that.token);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), getNodeType());
  }
}
