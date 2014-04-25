package com.wow.ast;

public class IfLineNode extends ASTNode{

	public IfLineNode(ExpressionNode expr, EntireLineNode node1, EntireLineNode node2) {
		children.add(expr);
		children.add(node1);
		children.add(node2);
		if (!expr.type.equals("boolean"))
			System.err.println("Error: The condition of an if statement must be of type boolean");
	}

	public IfLineNode(ExpressionNode expr, EntireLineNode node) {
		children.add(expr);
		children.add(node);
		if (!expr.type.equals("boolean"))
			System.err.println("Error: The condition of an if statement must be of type boolean");
	}

	public String toString() {
		if (children.size() == 2)
			return ("if ("+children.get(0)+") "+children.get(1));
		else 
			return ("if ("+children.get(0)+") "+children.get(1)+" else "+children.get(2));
	}

}