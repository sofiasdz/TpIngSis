package ASTNode;

import java.util.Objects;
import token.Token;

public abstract class ASTNode {
  public Token token;
  public NodeType nodeType;

  public abstract String getNodeType();

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    ASTNode astNode = (ASTNode) o;
    return Objects.equals(token, astNode.token) && nodeType == astNode.nodeType;
  }

  @Override
  public int hashCode() {
    return Objects.hash(token, nodeType);
  }

  public Token getToken() {
    return token;
  }

  public void setToken(Token token) {
    this.token = token;
  }
}
