package com.wow.ast;

public class WoWNodeNode extends ASTNode {

	public String name;
	public String type;

	public WoWNodeNode(String name) {
		this.name = name;
		type = "WoWNode";
	}

	public String toString() {
		return name;
	}

}