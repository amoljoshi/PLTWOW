package com.wow.ast;

public class BooleanNode extends ASTNode {

	public boolean value;
	public String type;

	public BooleanNode(boolean value) {
		this.value = value;
		type = "boolean";
	}

	public String toString() {
		return new Boolean(value).toString();
	}

}