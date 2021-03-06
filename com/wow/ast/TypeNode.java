package com.wow.ast;

public class TypeNode extends ASTNode {

	public String type;

	public TypeNode (String type) {
		this.type = type;
	}

	public String toString() {

		if(type.equals("WoWNodes"))
			return "String[]";
		else if(type.equals("WoWNode"))
			return "String";
		return type;
	}

}