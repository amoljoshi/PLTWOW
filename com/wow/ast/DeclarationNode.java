package com.wow.ast;

public class DeclarationNode extends ASTNode {

	public String id;

	public DeclarationNode(TypeNode node1, DeclaratorListNode node2) {
		children.add(node1);
		children.add(node2);
		id = node2.id;
		// System.out.println(node2.type + " - " + node1.type);
		if(!node2.type.startsWith("LibraryFunctions"))
		{
			if (!(node2.type.equals(node1.type)) && !node2.type.equals("--")){
				if(!"WoWNode".equals(node1.type) && "String".equals(node2.type))
					System.err.println("Error: Identifier "+id+" is defined for type "+node1.type+", but is being assigned to "+node2.type+".");
			}
		}
		else{
			String splits[] = node2.type.split("-");
			String functionName = splits[1];
			if(functionName.equals("getAllNodes")){
				// System.out.println("Function = " +functionName + " node1 = " + node1.type);
				if(!node1.type.equals("WoWNodes")){
					System.err.println("Error: Function "+ functionName + " returns WoWNodes which can't be assigned to " + node1.type);
				}
			}					
			else if(functionName.equals("getTime")){
				if(!node1.type.equals("double")){
					System.err.println("Error: Function "+ functionName + " returns double which can't be assigned to " + node1.type);
				}
			}					
			else if(functionName.equals("getNodeWaitingTime")){
				if(!node1.type.equals("double")){
					System.err.println("Error: Function "+ functionName + " returns double which can't be assigned to " + node1.type);						
				}
			}					
			else if(functionName.equals("getLastNode")){
				if(!node1.type.equals("WoWNode")){
					System.err.println("Error: Function "+ functionName + " returns WoWNode which can't be assigned to " + node1.type);						
				}
			}					
			else if(functionName.equals("getTotalWaitingTime")){
				if(!node1.type.equals("double")){
					System.err.println("Error: Function "+ functionName + " returns double which can't be assigned to " + node1.type);						
				}
			}					
			else if(functionName.equals("getAllFirstNodes")){
				if(!node1.type.equals("WoWNodes")){
					System.err.println("Error: Function "+ functionName + " returns WoWNodes which can't be assigned to " + node1.type);						
				}
			}					
			else if(functionName.equals("getNext()")){
				if(!node1.type.equals("WoWNodes")){
					System.err.println("Error: Function "+ functionName + " returns WoWNodes which can't be assigned to " + node1.type);						
				}
			}					
			else if(functionName.equals("getTotalTime")){
				if(!node1.type.equals("double")){
					System.err.println("Error: Function "+ functionName + " returns double which can't be assigned to " + node1.type);						
				}
			}					
			else if(functionName.equals("getPrevious")){
				if(!node1.type.equals("WoWNodes")){
					System.err.println("Error: Function "+ functionName + " returns WoWNodes which can't be assigned to " + node1.type);						
				}
			}
			else if(functionName.equals("getTotalOutputQuantity")){
				if(!node1.type.equals("double")){
					System.err.println("Error: Function "+ functionName + " returns double which can't be assigned to " + node1.type);						
				}
			}
			else{				
				System.err.println("Library functions give WoWNodes/ WoWNode/ double as return type");
			}
		}

		String[] ids = id.split(",");

		for(int i=0;i<ids.length;i++)
		{
			if (SymbolTable.symbolTable.get(ids[i]) != null)
				System.err.println("Error: Identifier "+id+" is already defined");
			else
				SymbolTable.symbolTable.put(ids[i], (node1.toString()));
		}
	}

	public DeclarationNode() {

	}

	public String toString() {

		return children.get(0).toString() + " " + children.get(1).toString();
		
	}

}
