package com.wow.ast;

public class DoubleNode extends ASTNode {

	private double num;
	public String type;

	public DoubleNode(double num) {
		this.num = num;
		type = "double";
	}

	public String toString() {
		return Double.toString(num);
	}

}