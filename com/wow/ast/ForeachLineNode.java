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
  		// System.out.println("WowNode of " + from);
  		// SymbolTable.symbolTable.put(from, "WoWNode");
    // String id = SymbolTable.symbolTable.get(from);
    // if(id == null){
    //   System.err.println("Symbol variable "+from+" is not defined");
    // }
    System.err.println("from defined now");
    if (SymbolTable.symbolTable.get(from) != null)
      System.err.println("Error: Identifier "+ from +" is already defined");
    else
      SymbolTable.symbolTable.put(from, "WoWNode");    
		children.add(new StringNode(from));
		children.add(new StringNode(to));
		children.add(entireline);
	}
  public ForeachLineNode(DeclarationNode from, IdentifierNode to, EntireLineNode entireline) {
    // System.err.println("from defined now");
    if(SymbolTable.symbolTable.get(to.id) == null){
      System.err.println("Error: Identifier " + to.id + " is not defined");
    }
    if("WoWNodes".equals(SymbolTable.symbolTable.get(to.id))){
      System.err.println("Error: Type " + SymbolTable.symbolTable.get(to.id) + " is not iterable");
    }
    children.add(from);
    children.add(to);
    children.add(entireline);
  }
	public String toString() {
		if(children.size() == 3)
			return "for (" + children.get(0) + ":" + children.get(1) + ") " + children.get(2) + "";
		return "";
	}
}