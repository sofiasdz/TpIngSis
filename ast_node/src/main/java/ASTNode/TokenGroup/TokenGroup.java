package ASTNode.TokenGroup;

import token.Token;
import token.TokenType;

import java.util.List;

public class TokenGroup {
    List<TokenType> tokenTypes;

    public TokenGroup(List<TokenType> tokenTypes) {
        this.tokenTypes = tokenTypes;
    }

    public List<TokenType> getTokens() {
        return tokenTypes;
    }

    public  boolean belongs(Token token){
        for (TokenType t : tokenTypes) {
            if(t.equals(token.getType())) return true;
        }
        return false;
    }
}
