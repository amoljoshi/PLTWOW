package com.wow.ast;

public class IntegerNode extends ASTNode {

	public int num;
	public String type;

	public IntegerNode(int num) {
		this.num = num;
		type = "int";
	}

	public String toString() {
		return Integer.toString(num);
	}

}