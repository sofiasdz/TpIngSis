package ASTNode.Childless;

import ASTNode.ASTNode;
import ASTNode.TokenGroup.TokenGroup;
import token.Token;

public class ASTNodeChildless extends ASTNode {
   TokenGroup tokenGroup;

   public boolean verifyTokenBelongsToGroup(){
       return true;
   }

    public ASTNodeChildless(Token token) {
       super.token=token;
    }
}
