package com.wow.ast;

public class DeclarationNode extends ASTNode {

	public String id;

	public DeclarationNode(TypeNode node1, DeclaratorListNode node2) {
		children.add(node1);
		children.add(node2);
		id = node2.id;
		if (!(node2.type.equals(node1.type)) && !node2.type.equals("--")){
			System.err.println("Error: Identifier "+id+" is defined for type "+node1.type+", but is being assigned to "+node2.type+".");
		}

		String[] ids = id.split(",");

		for(int i=0;i<ids.length;i++)
		{
			if (SymbolTable.symbolTable.get(ids[i]) != null)
				System.err.println("Error: Identifier "+id+" is already defined");
			else
				SymbolTable.symbolTable.put(ids[i], (node1.toString()));
		}
	}

	public DeclarationNode() {

	}

	public String toString() {

		return children.get(0).toString() + " " + children.get(1).toString();
		
	}

}
