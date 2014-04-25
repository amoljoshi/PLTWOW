package com.wow.ast;

public class TypeNode extends ASTNode {

	private String type;

	public TypeNode (String type) {
		this.type = type;
	}

	public String toString() {
		return type;
	}

}