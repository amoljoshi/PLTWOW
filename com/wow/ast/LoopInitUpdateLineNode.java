package com.wow.ast;

public class LoopInitUpdateLineNode extends ASTNode{

  public LoopInitUpdateNode(LineNode n) {
		children.add(n);
	}

	public LoopInitUpdateNode() {

	}

	public String toString() {
		
		if (children.size()==1)
			return (""+children.get(0));
		
		return "";
	}

}