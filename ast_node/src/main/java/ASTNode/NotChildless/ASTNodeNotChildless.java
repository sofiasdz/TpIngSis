package ASTNode.NotChildless;

import ASTNode.ASTNode;
import ASTNode.NodeType;
import token.Token;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ASTNodeNotChildless)) return false;
        if (!super.equals(o)) return false;
        ASTNodeNotChildless that = (ASTNodeNotChildless) o;
        return Objects.equals(leftChild, that.leftChild) &&
                Objects.equals(rightChild, that.rightChild) &&
                Objects.equals(getNodeType(), that.getNodeType()) &&
                Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), leftChild, rightChild, getNodeType());
    }
}
