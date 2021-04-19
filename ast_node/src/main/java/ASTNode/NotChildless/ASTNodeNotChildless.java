package ASTNode.NotChildless;

import ASTNode.ASTNode;
import ASTNode.NodeType;
import token.Token;

public  abstract class ASTNodeNotChildless extends ASTNode {
    ASTNode leftChild;
    ASTNode rightChild;

    public ASTNode getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(ASTNode leftChild) {
        this.leftChild = leftChild;
    }

    public ASTNode getRightChild() {
        return rightChild;
    }

    public void setRightChild(ASTNode rightChild) {
        this.rightChild = rightChild;
    }

    public ASTNodeNotChildless(ASTNode leftChild, ASTNode rightChild, Token token) {
        this.leftChild = leftChild;
        this.token=token;
        super.nodeType= NodeType.NOTCHILDLESS;
        this.rightChild = rightChild;
    }
}
