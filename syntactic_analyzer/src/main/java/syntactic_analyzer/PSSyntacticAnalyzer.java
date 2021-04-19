package syntactic_analyzer;

import ASTNode.ASTNode;
import ASTNode.Childless.ASTNodeIdentifier;
import ASTNode.Factory.ASTNodeFactory;
import ASTNode.NotChildless.ASTNodeAssignation;
import ASTNode.TokenGroup.TokenGroup;
import token.Token;
import token.TokenType;
import ASTNode.NodeType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

public class PSSyntacticAnalyzer implements SyntacticAnalyzer{


    public PSSyntacticAnalyzer() {

    }

    @Override
    public List<ASTNode> analyze(List<Token> tokens){
        List<ASTNode> nodes = new ArrayList<>();
        List<Token> tokenList = new ArrayList<>();
        for (int i = 0; i < tokens.size() ; i++) {
            tokenList.add(tokens.get(i));
            if(tokens.get(i).getType().equals(TokenType.SEMICOLON)){
                nodes.add(tokensToNode(tokenList));
                tokenList = new ArrayList<>();
            }
        }
        return nodes;
    }

    public ASTNode tokensToNode(List<Token> tokens) {
        Stack<ASTNode> nodeStack = new Stack<>();
        List<Token> tokenList = new ArrayList<>();
        for (int i = tokens.size()-1; i >=0 ; i--) {
            Optional<ASTNode> optionalASTNode = ASTNodeIdentifier(tokens.get(i));
            if(optionalASTNode.isEmpty()) tokenList.add(tokens.get(i));
            else nodeStack.push(optionalASTNode.get());
            //Si el optional es un nodo, lo stackeo. Voy de atrás para adelante para no tener problema con los nodos
        }
        for (int i = tokenList.size()-1; i >= 0 ; i--) {
            if(tokenList.get(i).getType().equals(TokenType.LET) | tokenList.get(i).getType().equals(TokenType.SEMICOLON)) tokenList.remove(i);
        }

        while(nodeStack.size()>1){
            ASTNode right = nodeStack.pop();
            ASTNode left = nodeStack.pop();
            Optional<ASTNode> optionalASTNode = ASTNodeIdentifier(tokenList.get(tokenList.size()-1),left,right);
            if(optionalASTNode.isPresent()) {
                nodeStack.push(optionalASTNode.get());
                tokenList.remove(tokenList.size()-1);
            }
            else {
                optionalASTNode = ASTNodeIdentifier(tokenList.get(tokenList.size()-1),right,left);
                if(optionalASTNode.isPresent()) {
                    nodeStack.push(optionalASTNode.get());
                    tokenList.remove(tokenList.size()-1);
                }
                else {
                    nodeStack.push(right);
                    nodeStack.push(left);
                    nodeStack = operationCheck(nodeStack,tokenList);
                }
            }
        }
        return nodeStack.pop();
    }

    Stack<ASTNode> operationCheck(Stack<ASTNode> nodeStack, List<Token> tokenList){
        if(nodeStack.peek().token.getType().equals(TokenType.ASSIGNATION)){
            TokenGroup group = new TokenGroup(List.of(TokenType.ADDITION,TokenType.SUBSTRACTION,TokenType.DIVISION,TokenType.MULTIPLICATION));
            for (int i = 0; i < tokenList.size() ; i++) {
                if(group.belongs(tokenList.get(i))){
                    ASTNodeAssignation node1 = (ASTNodeAssignation)nodeStack.pop();
                    ASTNode node2 = nodeStack.pop();
                    Optional<ASTNode> operation = ASTNodeIdentifier(tokenList.get(i),node1.getRightChild(),node2);
                    node1.setRightChild(operation.get());
                    nodeStack.push(node1);
                }
            }

        }
        return nodeStack;
    }


   Optional<ASTNode> ASTNodeIdentifier(Token token){
        if(token.getType().equals(TokenType.IDENTIFIER)){
          return Optional.of(ASTNodeFactory.identifier(token));
        }
        else if(token.getType().equals(TokenType.NUMBER_TYPE)| token.getType().equals(TokenType.STRING_TYPE)){
            return Optional.of(ASTNodeFactory.variableType(token));
       }
        else{
            try {
                return Optional.of(ASTNodeFactory.literal(token));
            }
            catch (IllegalArgumentException e) {
                return Optional.empty();
            }
        }

   }

   Optional<ASTNode> ASTNodeIdentifier(Token token,ASTNode left,ASTNode right){
        try{  return Optional.of(ASTNodeFactory.assignation(token,left,right));
        }
        catch (IllegalArgumentException e){
            try{
                return Optional.of(ASTNodeFactory.declaration(token,left,right));
            }
            catch(IllegalArgumentException i){
                try {
                    return Optional.of(ASTNodeFactory.operation(token, left, right));
                }
                catch(IllegalArgumentException h){
                    return Optional.empty();
                }
            }

        }
   }


}
