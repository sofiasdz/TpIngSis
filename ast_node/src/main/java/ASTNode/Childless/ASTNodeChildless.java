package ASTNode.Childless;

import ASTNode.ASTNode;
import ASTNode.TokenGroup.TokenGroup;
import token.Token;

public  abstract class ASTNodeChildless extends ASTNode {
   TokenGroup tokenGroup;



    public ASTNodeChildless(Token token) {
       super.token=token;
    }
}
