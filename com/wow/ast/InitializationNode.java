package com.wow.ast;

public class InitializationNode extends ASTNode{

	public String type = "";

	public InitializationNode (InitializationNode node) {
		children.add(node);
		type = node.type;
	}

	public String toString() {
		if (children.size() == 1) {
			if (children.get(0) instanceof InitializationNode)
				return ("("+children.get(0)+")");
			else if (children.get(0) instanceof TypeNode) 
				return ("new " + children.get(0)+"()");
			else
				return (children.get(0).toString());
		}
		else { 
			return ("new "+children.get(0)+"("+children.get(1)+")");
		}
	}

}