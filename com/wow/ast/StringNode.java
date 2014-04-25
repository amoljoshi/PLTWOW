package com.wow.ast;

public class StringNode extends ASTNode {

	public String str;
	public String type;

	public IntegerNode(String str) {
		this.str = str;
		type = "String";
	}

	public String toString() {
		return Integer.toString(str);
	}

}