package com.wow.ast;

public class AssignmentNode extends ASTNode{

	public String id;

	public AssignmentNode (IdentifierNode identifier, ExpressionNode expr) {
		children.add(identifier);
		children.add(expr);
		this.id=identifier.id;
		if (SymbolTable.symbolTable.get(identifier.id) != null) {
			if (!SymbolTable.symbolTable.get(identifier.id).equals(expr.type) && !(SymbolTable.symbolTable.get(identifier.id).equals("decimal")&&expr.type.equals("int")))
				System.err.println("Error: cannot assign type " + expr.type  + " to type " + SymbolTable.symbolTable.get(identifier.id));
		}
		else 
			System.err.println("Symbol variable "+identifier+"is not defined");

	}

	public AssignmentNode (IdentifierNode identifier, InitializationNode init) {
		children.add(identifier);
		children.add(init);
		this.id=identifier.id;
		if (SymbolTable.symbolTable.get(identifier.id) != null) {
			if (!SymbolTable.symbolTable.get(identifier.id).equals(init.type)) {						
				System.err.println("Error: cannot assign type " + SymbolTable.symbolTable.get(identifier.id) + " to type " + init.type);
			}
		}
		else
			System.err.println("Error: Symbol variable "+identifier+" is not defined");
	}

	public String toString() {

		if (children.size() == 2) 
			return (""+children.get(0)+" = "+children.get(1));

		return (""+children.get(0)+" "+children.get(1)+" "+children.get(2));
	}

}