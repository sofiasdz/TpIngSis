package ASTNode.SingleChild;

import ASTNode.ASTNode;
import token.Token;

public class ASTNodePrint extends ASTNodeSingleChild {

  public ASTNodePrint(Token token, ASTNode child) {
    super(child,token);
  }

  @Override
  public String toString() {
    return "PrintLn ( " + child.toString() + " )";
  }

  @Override
  public String getNodeType() {
    return "print";
  }


}
