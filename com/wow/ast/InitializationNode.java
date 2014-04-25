package com.wow.ast;

public class InitializationNode extends ASTNode{

	public String type = "";

	// for parentheses
	public InitializationNode (InitializationNode node) {
		children.add(node);
		type = node.type;
	}

	
	public InitializationNode (TypeNode name, StringNode filename) {
		children.add(name);
		StringNode temp = new StringNode("\"../" + filename.toString().substring(1, filename.toString().length()));
		children.add(temp);
		type = name.type;
	}


		public InitializationNode (TypeNode name) {
		children.add(name);
		type = name.type;
	}

	
	public InitializationNode (TypeNode name, IdentifierNode id) {
		children.add(name);
		children.add(id);
		type = name.type;
	}



	public String toString() {
		if (children.size() == 1) {
			if (children.get(0) instanceof InitializationNode)
				//initialiaztion is put into parentheses
				return ("("+children.get(0)+")");
			else if (children.get(0) instanceof TypeNode) 
				return ("new " + children.get(0)+"()");
			else
				return (children.get(0).toString());
		}
		else{ // if (children.size() == 2) {
			//filename or an identifier
			return ("new "+children.get(0)+"("+children.get(1)+")");
		}
	}

}