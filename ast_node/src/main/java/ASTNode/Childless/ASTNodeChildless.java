package ASTNode.Childless;

import ASTNode.ASTNode;
import ASTNode.TokenGroup.TokenGroup;

public class ASTNodeChildless extends ASTNode {
   TokenGroup tokenGroup;

   public boolean verifyTokenBelongsToGroup(){
       return true;
   }

    public ASTNodeChildless(TokenGroup tokenGroup) {
        this.tokenGroup = tokenGroup;
    }
}
