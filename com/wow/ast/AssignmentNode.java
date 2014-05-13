package com.wow.ast;

public class AssignmentNode extends ASTNode{

	public String id;

	public AssignmentNode (IdentifierNode identifier, ExpressionNode expr) {
		children.add(identifier);
		children.add(expr);
		this.id=identifier.id;	
		String idtype = SymbolTable.symbolTable.get(identifier.id);
		if(expr.type.startsWith("LibraryFunctions")){
				String splits[] = expr.type.split("-");
				String functionName = splits[1];
				if(functionName.equals("getAllNodes")){
					if(!idtype.equals("WoWNodes")){
						System.err.println("Error: cannot assign type " + expr.type  + " to type " + idtype);
					}
				}					
				else if(functionName.equals("getTime")){
					if(!idtype.equals("double")){
						System.err.println("Error1: cannot assign type " + expr.type  + " to type " + idtype);
					}
				}					
				else if(functionName.equals("getNodeWaitingTime")){
					if(!idtype.equals("double")){
						System.err.println("Error: cannot assign type " + expr.type  + " to type " + idtype);
					}
				}					
				else if(functionName.equals("getLastNode")){
					if(!idtype.equals("WoWNode")){
						System.err.println("Error: cannot assign type " + expr.type  + " to type " + idtype);						
					}
				}					
				else if(functionName.equals("getTotalWaitingTime")){
					if(!idtype.equals("double")){
						System.err.println("Error: cannot assign type " + expr.type  + " to type " + idtype);						
					}
				}					
				else if(functionName.equals("getAllFirstNodes")){
					if(!idtype.equals("WoWNodes")){
						System.err.println("Error: cannot assign type " + expr.type  + " to type " + idtype);						
					}
				}					
				else if(functionName.equals("getNext()")){
					if(!idtype.equals("WoWNodes")){
						System.err.println("Error: cannot assign type " + expr.type  + " to type " + idtype);						
					}
				}					
				else if(functionName.equals("getTotalTime")){
					if(!idtype.equals("double")){
						System.err.println("Error: cannot assign type " + expr.type  + " to type " + idtype);						
					}
				}					
				else if(functionName.equals("getPrevious")){
					if(!idtype.equals("WoWNodes")){
						System.err.println("Error: cannot assign type " + expr.type  + " to type " + idtype);
					}
				}
			}
		else if (SymbolTable.symbolTable.get(identifier.id) != null) {
			if (!SymbolTable.symbolTable.get(identifier.id).equals(expr.type) && !(SymbolTable.symbolTable.get(identifier.id).equals("double")&&expr.type.equals("int")))
				System.err.println("Error2: cannot assign type " + expr.type  + " to type " + SymbolTable.symbolTable.get(identifier.id));
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
				System.err.println("Error3: cannot assign type " + SymbolTable.symbolTable.get(identifier.id) + " to type " + init.type);
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