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

	public ExpressionNode (LibraryFunctionsNode n) {
		children.add(n);
		type = n.type;
	}

	public ExpressionNode (DoubleNode n) {
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

	public ExpressionNode (ExpressionNode node1, String operator, ExpressionNode n3) {
		//implementation remaining
		children.add(node1);
		op = operator;
		children.add(n3);
		if(n3.type.startsWith("LibraryFunctions")){
				String splits[] = n3.type.split("-");
				String functionName = splits[1];
				if(functionName.equals("getAllNodes")){
					if(!node1.type.equals("WoWNodes")){
						System.err.println("Error: Cannot perform the operation " + node1.type + " " + op + " " + functionName);
					}
				}					
				else if(functionName.equals("getTime")){
					if(!node1.type.equals("double")){
						System.err.println("Error: Cannot perform the operation " + node1.type + " " + op + " " + functionName);
					}
				}					
				else if(functionName.equals("getNodeWaitingTime")){
					if(!node1.type.equals("double")){
						System.err.println("Error: Cannot perform the operation " + node1.type + " " + op + " " + functionName);
					}
				}					
				else if(functionName.equals("getLastNode")){
					if(!node1.type.equals("WoWNode")){
						System.err.println("Error: Cannot perform the operation " + node1.type + " " + op + " " + functionName);						
					}
				}					
				else if(functionName.equals("getTotalWaitingTime")){
					if(!node1.type.equals("double")){
						System.err.println("Error: Cannot perform the operation " + node1.type + " " + op + " " + functionName);						
					}
				}					
				else if(functionName.equals("getAllFirstNodes")){
					if(!node1.type.equals("WoWNodes")){
						System.err.println("Error: Cannot perform the operation " + node1.type + " " + op + " " + functionName);						
					}
				}					
				else if(functionName.equals("getNext()")){
					if(!node1.type.equals("WoWNodes")){
						System.err.println("Error: Cannot perform the operation " + node1.type + " " + op + " " + functionName);						
					}
				}					
				else if(functionName.equals("getTotalTime")){
					if(!node1.type.equals("double")){
						System.err.println("Error: Cannot perform the operation " + node1.type + " " + op + " " + functionName);						
					}
				}					
				else if(functionName.equals("getPrevious")){
					if(!node1.type.equals("WoWNodes")){
						System.err.println("Error: Cannot perform the operation " + node1.type + " " + op + " " + functionName);
					}
				}
				return;
		}
		if (!node1.type.equals(n3.type) && (node1.type.equals("int") && n3.type.equals("double") || node1.type.equals("double") && n3.type.equals("int")
									  || node1.type.equals("string") && n3.type.equals("int") || node1.type.equals("int") && n3.type.equals("string")
									  || node1.type.equals("string") && n3.type.equals("double") || node1.type.equals("double") && n3.type.equals("string")))
			System.err.println("Error: Cannot perform the operation " + node1.type + " " + op + " " + n3.type);
		else {
			if (op.equals("+")) {
				if (node1.type.equals("int") && n3.type.equals("int"))
					type = "int";
				else if((node1.type.equals("double") && n3.type.equals("int")))
					type = "double";
				else if((node1.type.equals("int") && n3.type.equals("double")))
					type = "double";
				else if ((node1.type.equals("double")||node1.type.equals("int"))&&n3.type.equals("string"))
					type = "string";
				else if ((n3.type.equals("double")||n3.type.equals("int"))&&node1.type.equals("string"))
					type = "string";
				else
					System.err.println("Error: Cannot perform the operation "+node1.type+" "+op+" "+n3.type);
			}

			if (op.equals("-")||op.equals("*")||op.equals("/")||op.equals("^")||op.equals("%")) {
				if (node1.type.equals("int") && n3.type.equals("int"))
					type = "int";
				else if((node1.type.equals("double") && n3.type.equals("int")))
					type = "double";
				else if((node1.type.equals("int") && n3.type.equals("double")))
					type = "double";
				else if((node1.type.equals("double") && n3.type.equals("double"))) type = "double";
				else
					System.err.println("Error: Cannot perform the operation "+node1.type+" "+op+" "+n3.type);
			}

			if (op.equals("<")||op.equals(">")||op.equals("<=")||op.equals(">=")) {
				if (node1.type.equals("boolean")||node1.type.equals("string")||n3.type.equals("boolean")||n3.type.equals("string")) {
					System.out.println("Error: Cannot perform the operation "+node1.type+" "+op+" "+n3.type);
				}
				else
					type = "boolean";
			}

			if (op.equals("!=")||op.equals("==")) {
				if (node1.type.equals("boolean")&& n3.type.equals("boolean"))
					type = "boolean";
				else if ((node1.type.equals("double")||node1.type.equals("int"))&&(n3.type.equals("double")||n3.type.equals("int")))
					type = "boolean";
				else
					System.err.println("Error: Cannot perform the operation "+node1.type+" "+op+" "+n3.type);

			}

			if (op.equals ("and") || op.equals("or")) {
				if (node1.type.equals("boolean") && n3.type.equals("boolean"))
					type = "boolean";
			else 
				System.err.println("Error: Cannot perform the operation "+node1.type+" "+op+" "+n3.type);

			}
		}
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