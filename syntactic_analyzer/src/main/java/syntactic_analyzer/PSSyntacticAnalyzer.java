package syntactic_analyzer;

import ASTNode.ASTNode;
import ASTNode.Childless.ASTNodeIdentifier;
import ASTNode.Factory.ASTNodeFactory;
import token.Token;
import token.TokenType;
import ASTNode.NodeType;

import java.util.ArrayList;
import java.util.List;

public class PSSyntacticAnalyzer implements SyntacticAnalyzer{


    public PSSyntacticAnalyzer() {


    }

    @Override
    public ASTNode analize(List<Token> tokens) {
       /* List<ASTNode> list = new ArrayList();
        if(list.isEmpty()){
            list.add(ASTNodeIdentifier(tokens.get()))
        }*/
        return null;
    }


   ASTNode ASTNodeIdentifier(Token token){
        if(token.getType().equals(TokenType.IDENTIFIER)){
          return ASTNodeFactory.identifier(token);
        }
        else if(token.getType().equals(TokenType.NUMBER_TYPE)| token.getType().equals(TokenType.STRING_TYPE)){
            return ASTNodeFactory.variableType(token);
       }
        else{
            return ASTNodeFactory.literal(token);
        }

   }

   ASTNode ASTNodeIdentifier(Token token,ASTNode left,ASTNode right){
        try{  return ASTNodeFactory.assignation(token,left,right);
        }
        catch (IllegalArgumentException e){
            try{
                return ASTNodeFactory.declaration(token,left,right);
            }
            catch(IllegalArgumentException i){
                try {
                    return ASTNodeFactory.operation(token, left, right);
                }
                catch(IllegalArgumentException h){ throw new IllegalArgumentException();}
            }

        }
   }


}
