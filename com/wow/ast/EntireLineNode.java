package com.wow.ast;

public class EntireLineNode extends ASTNode{

	public EntireLineNode(LineNode n) {
		children.add(n);
	}

	public EntireLineNode(IfLineNode n) {
		children.add(n);
	}

	public EntireLineNode(ForLineNode n) {
		children.add(n);
	}

	public EntireLineNode(ForeachLineNode n) {
		children.add(n);
	}

	public EntireLineNode(WhileLineNode n) {
		children.add(n);
	}
	public EntireLineNode(MultiLineNode n) {
		children.add(n);
	}

	public String toString() {

		if (children.get(0) instanceof LineNode)
			return ""+children.get(0)+";";

		return ""+children.get(0);
	}
}