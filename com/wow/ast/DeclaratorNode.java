package com.wow.ast;

public class DeclaratorNode extends ASTNode {

	public String id;
	public String type;

	public DeclaratorNode(IdentifierNode node1) {
		children.add(node1);
		id = node1.id;
		type = "--";
	}

	public DeclaratorNode(IdentifierNode node1, ExpressionNode node2) {
		children.add(node1);
		children.add(node2);
		id = node1.id;
		type = node2.type;
	}

	public String toString() {
		
		if (children.size() == 1)
			return children.get(0).toString();
		
		return children.get(0) + " = " + children.get(1).toString();
	}

}