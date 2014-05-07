package com.wow.ast;

public class LineNode extends ASTNode {
	
	public LineNode (DeclarationNode n) {
		children.add(n);
	}
	
	public LineNode(ExpressionNode n) {
		children.add(n);
	}
	
	public LineNode(AssignmentNode n) {
		children.add(n);
	}
	
	public LineNode(PrintLineNode n) {
		children.add(n);
	}

	public String toString() {	
		return (""+children.get(0));
	}

}