package com.wow.ast;

public class IdentifierNode extends ASTNode {

    public String id;
    
    public IdentifierNode(String identifier) {
        this.id = identifier;
    }
    
    public String toString() {
        return this.id;
    }

}