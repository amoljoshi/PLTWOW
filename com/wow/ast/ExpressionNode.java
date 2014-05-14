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
		// System.err.println("In ExpressionNode " + n.type);
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

	public ExpressionNode (ExpressionNode node1, String operator, ExpressionNode node3) {
		//implementation remaining - completed now: Yash, Amol
		children.add(node1);
		op = operator;
		children.add(node3);
		
		if(node1!= null && SymbolTable.symbolTable.get(node1.toString()) == null)
			System.err.println("Error: "+node1.toString()+" is not defined.");

		else if(node3!= null && SymbolTable.symbolTable.get(node3.toString()) == null)
			System.err.println("Error: "+node3.toString()+" is not defined.");

		else if(node3.type.startsWith("LibraryFunctions")){
				String splits[] = node3.type.split("-");
				String functionName = splits[1];
				if(functionName.equals("getAllNodes")){
					if(!node1.type.equals("WoWNodes")){
						System.err.println("Error: Cannot perform the operation " + node1.type + " " + op + " " + functionName);
					}
					else{
						type = "WoWNodes";
					}
				}					
				else if(functionName.equals("getTime")){
					if(!node1.type.equals("double")){
						System.err.println("Error: Cannot perform the operation " + node1.type + " " + op + " " + functionName);
					}
					else{
						type = "double";
					}
				}					
				else if(functionName.equals("getNodeWaitingTime")){
					if(!node1.type.equals("double")){
						System.err.println("Error: Cannot perform the operation " + node1.type + " " + op + " " + functionName);
					}
					else{
						type = "double";
					}				
				}					
				else if(functionName.equals("getLastNode")){
					if(!node1.type.equals("WoWNode")){
						System.err.println("Error: Cannot perform the operation " + node1.type + " " + op + " " + functionName);						
					}
					else{
						type = "WoWNode";
					}					
				}					
				else if(functionName.equals("getTotalWaitingTime")){
					if(!node1.type.equals("double")){
						System.err.println("Error: Cannot perform the operation " + node1.type + " " + op + " " + functionName);						
					}
					else{
						type = "double";
					}					
				}					
				else if(functionName.equals("getAllFirstNodes")){
					if(!node1.type.equals("WoWNodes")){
						System.err.println("Error: Cannot perform the operation " + node1.type + " " + op + " " + functionName);						
					}
					else{
						type = "WoWNodes";
					}					
				}					
				else if(functionName.equals("getNext()")){
					if(!node1.type.equals("WoWNodes")){
						System.err.println("Error: Cannot perform the operation " + node1.type + " " + op + " " + functionName);						
					}
					else{
						type = "WoWNodes";
					}					
				}					
				else if(functionName.equals("getTotalTime")){
					if(!node1.type.equals("double")){
						System.err.println("Error: Cannot perform the operation " + node1.type + " " + op + " " + functionName);						
					}
					else{
						type = "double";
					}					
				}					
				else if(functionName.equals("getPrevious")){
					if(!node1.type.equals("WoWNodes")){
						System.err.println("Error: Cannot perform the operation " + node1.type + " " + op + " " + functionName);
					}
					else{
						type = "WoWNodes";
					}					
				}
		}
		
		else {
			if (op.equals("+")) {
				if (node1.type.equals("int") && node3.type.equals("int"))
					type = "int";
				if (node1.type.equals("double") && node3.type.equals("double"))
					type = "double";
				else if((node1.type.equals("double") && node3.type.equals("int")))
					type = "double";
				else if((node1.type.equals("String") && node3.type.equals("String")))
					type = "String";
				else if((node1.type.equals("int") && node3.type.equals("double")))
					type = "double";				
				else if ((node1.type.equals("double")||node1.type.equals("int"))&&node3.type.equals("String"))
					type = "String";
				else if ((node3.type.equals("double")||node3.type.equals("int"))&&node1.type.equals("String"))
					type = "String";
				else
					System.err.println("Error: Cannot perform the operation "+node1.type+" "+op+" "+node3.type);
			}

			if (op.equals("-")||op.equals("*")||op.equals("/")||op.equals("^")||op.equals("%")) {
				if (node1.type.equals("int") && node3.type.equals("int"))
					type = "int";
				else if((node1.type.equals("double") && node3.type.equals("int")))
					type = "double";
				else if((node1.type.equals("int") && node3.type.equals("double")))
					type = "double";
				else if((node1.type.equals("double") && node3.type.equals("double"))) type = "double";
				else
					System.err.println("Error: Cannot perform the operation "+node1.type+" "+op+" "+node3.type);
			}

			if (op.equals("<")||op.equals(">")||op.equals("<=")||op.equals(">=")) {
				if (node1.type.equals("boolean")||node1.type.equals("String")||node3.type.equals("boolean")||node3.type.equals("String")) {
					System.err.println("Error: Cannot perform the operation "+node1.type+" "+op+" "+node3.type);
				}
				else
					type = "boolean";
			}

			if (op.equals("!=")||op.equals("==")) {
				if (node1.type.equals("boolean")&& node3.type.equals("boolean"))
					type = "boolean";
				else if ((node1.type.equals("double")||node1.type.equals("int"))&&(node3.type.equals("double")||node3.type.equals("int")))
					type = "boolean";
				if(node1.type.equals("String") && node3.type.equals("String"))
					type = "boolean";
				else
					System.err.println("Error: Cannot perform the operation "+node1.type+" "+op+" "+node3.type);

			}

			if (op.equals ("&&") || op.equals("||")) {
				if (node1.type.equals("boolean") && node3.type.equals("boolean"))
					type = "boolean";
			else 
				System.err.println("Error: Cannot perform the operation "+node1.type+" "+op+" "+node3.type);

			}
		}
	}


	public String toString() {
		if(children.size() == 0) 
			return "";
		else if (children.size() == 1) {
			if (children.get(0) instanceof ExpressionNode) 
				return "("+children.get(0)+")";
			else
				return ""+children.get(0);
		}
		//overloading == operator for Strings (must use IDs, not from String pool)
		else if("==".equals(op)){
			String lhsType = SymbolTable.symbolTable.get(children.get(0).toString());
			String rhsType = SymbolTable.symbolTable.get(children.get(1).toString());
			String tempOp = "==";
			if(tempOp.equals(op) && "String".equals(rhsType) && "String".equals(lhsType))
				return "" + children.get(0) + ".equals(" + children.get(1) + ")";
		}

		return "" + children.get(0) + op + children.get(1);
	}

}