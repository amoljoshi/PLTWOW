package com.wow.ast;

public class ForeachLineNode extends ASTNode {

  public ForeachLineNode(String from, String to, EntireLineNode entireline) {
		children.add(new StringNode(from));
		children.add(new StringNode(to));
		children.add(entireline);
	}
	public String toString() {
		return "for (" + "String " + children.get(0) + ":" + children.get(1) + ") " + children.get(2) + "";
	}
}