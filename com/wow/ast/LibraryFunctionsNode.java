package com.wow.ast;

public class LibraryFunctionsNode extends ASTNode {

	public String type;
	public String functionName;

	public LibraryFunctionsNode(String functionName) {
		this.functionName = functionName;
		type = "LibraryFunctions";
	}

	public String toString() {
		
		if(functionName.equals("getAllNodes"))
			return "syslib.getAllNodes()";
		return "error";
	}

}