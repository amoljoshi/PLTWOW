package com.wow.ast;

public class MultiLineNode extends ASTNode {

    public MultiLineNode(LineBlockNode n) {
        children.add(n);
    }
    public String toString() {
        return "{" + children.get(0).toString() + "}";
    }
}