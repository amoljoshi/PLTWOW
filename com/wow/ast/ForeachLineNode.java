package com.wow.ast;

public class ForeachLineNode extends ASTNode {

  public ForeachLineNode(String from, String to, EntireLineNode entireline) {
  		// // if(!to.equals("WoWNodes")){
  		// // 	System.err.println("Error: Type " + to + " is not iterable!");
  		// // 	return;
  		// // }
  		// if(!from.equals("WoWNode")){
  		// 	System.err.println("Error: Type " + from + " can't be used to iterate through WowNodes");
  		// 	return;	
  		// }
		children.add(new StringNode(from));
		children.add(new StringNode(to));
		children.add(entireline);
	}
	public String toString() {
		if(children.size() == 3)
			return "for (" + "String " + children.get(0) + ":" + children.get(1) + ") " + children.get(2) + "";
		return "";
	}
}