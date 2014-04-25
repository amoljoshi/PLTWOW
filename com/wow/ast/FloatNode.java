package com.wow.ast;

public class FloatNode extends ASTNode {

	public float num;
	public String type;

	public FloatNode(float num) {
		this.num = num;
		type = "float";
	}

	public String toString() {
		return Float.toString(num);
	}

}