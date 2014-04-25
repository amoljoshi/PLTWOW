package com.wow.ast;

public class DeclaratorListNode extends ASTNode {

	public String id;
	public String type;

	public DeclaratorListNode(DeclaratorListNode node1, DeclaratorNode node2) {
		children.add(node1);
		children.add(node2);
		if (!node1.type.equals(node2.type) && !node2.type.equals("--") && !node1.type.equals("--"))
			System.err.println("Error: Inconsistent types");
		type = node2.type;
		id = node1.id+","+node2.id;
	}

	public DeclaratorListNode(DeclaratorNode node1) {
		children.add(node1);
		id = noed1.id;
		type = node1.type;
	}

	public String toString() {

		if(children.size() == 1) 
			return children.get(0).toString();

		return children.get(0) + ", " +  children.get(1);

	}	
}