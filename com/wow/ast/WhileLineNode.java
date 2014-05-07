package com.wow.ast;

public class WhileLineNode extends ASTNode{
	public WhileLineNode(ExpressionNode expr, EntireLineNode line){
		children.add(expr);
		children.add(line);
	}
	public String toString(){
		return "while (" + children.get(0) + ")" + children.get(1);	
	}
}