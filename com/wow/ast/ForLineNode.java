package com.wow.ast;

public class ForLineNode extends ASTNode {

  public ForLineNode(LoopInitUpdateNode node1, LoopConditionLineNode node2, LoopInitUpdateNode node3, EntireLineNode node4) {
		children.add(node1);
		children.add(node2);
		children.add(node3);
		children.add(node4);
		if (!(node2.type.equals("") || node2.type.equals("boolean")))
			System.err.println("Error: The condition must evaluate to a boolean value");
	}

	public String toString() {
		return ("for (" + children.get(0) + "; " + children.get(1) + "; " + children.get(2) + ") " + children.get(3) +"");
	}
}