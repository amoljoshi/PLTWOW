package com.wow.ast;
public class SymbolTable{
	public HashMap<String, String> symbolTable;
	public SymbolTable(){
		//	Symbol table of Identifier to its type mapping
		symbolTable = new HashMap<String, String> ();
	}
	//	Method to add declaration of new identifier
	public void addNewVariable(String type, String identifier){
		symbolTable.put(identifier, type);
	}
	//	This method will check if variable is already declared
	public boolean checkForVariable(String identifier){
		return symbolTable.containsKey(identifier);
	}
}