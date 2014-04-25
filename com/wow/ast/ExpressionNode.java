package com.wow.ast;

public class ExpressionNode extends ASTNode {

	public String type = "";
	public String id = "";
	public String op = "";

	public ExpressionNode (ExpressionNode n) {
		children.add(n);
		type = n.type;
	}

	public ExpressionNode (IntegerNode n) {
		children.add(n);
		type = n.type;
	}

	public ExpressionNode (DecimalNode n) {
		children.add(n);
		type = n.type;
	}

	public ExpressionNode(IdentifierNode n) {
		children.add(n);
		id = n.id;
		type = SymbolTable.symbolTable.get(n.id);
	}

	public ExpressionNode(BooleanNode n) {
		children.add(n);
		type = n.type;
	}

	public ExpressionNode(StringNode n) {
		children.add(n);
		type = n.type;
	}

	public ExpressionNode (ExpressionNode n1, String operator, ExpressionNode n3) {
		//implementation remaining

	}


	public String toString() {
		if(children.size() == 0) 
			return "";
		else if (children.size() == 1) {
			if (children.get(0) instanceof ExpressionNode) 
				return ("("+children.get(0)+")");
			else
				return ""+children.get(0);
		}
		else 
			return (""+children.get(0) + op + children.get(1));
	}

}