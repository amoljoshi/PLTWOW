package com.wow.ast;

public class LoopInitUpdateLineNode extends ASTNode{

  public LoopInitUpdateLineNode(LineNode n) {
		children.add(n);
	}

	public LoopInitUpdateLineNode() {

	}

	public String toString() {
		
		if (children.size()==1)
			return (""+children.get(0));
		
		return "";
	}

}