package com.wow.ast;

public class StringNode extends ASTNode {

	public String str;
	public String type;

	public StringNode(String str) {
		this.str = str;
		type = "String";
	}

	public String toString() {
		return str;
	}
}