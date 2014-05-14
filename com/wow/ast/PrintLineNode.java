package com.wow.ast;
public class PrintLineNode extends ASTNode{
	public PrintLineNode(ExpressionNode expr){
		if(expr.type == null)
			System.err.println("Undefined variable passed to print statement");
		// if(!expr.type.startsWith("LibraryFunctions"))
			children.add(expr);
		// else{
			// System.err.println("Error: Can't print " + expr.type);
		// }
	}
	public String toString(){
		if(children.get(0) != null){
			return "System.out.println(" + children.get(0) + ")";
		}
		return "System.out.println()";
	}
}