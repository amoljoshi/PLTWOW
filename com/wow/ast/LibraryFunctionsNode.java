package com.wow.ast;

public class LibraryFunctionsNode extends ASTNode {

	public String type;
	public String functionName;
	public String parameter;

	public LibraryFunctionsNode(String functionName) {
		this.functionName = functionName;
		type = "LibraryFunctions";
	}
	public LibraryFunctionsNode(String functionName, String parameter) {
		this.functionName = functionName;
		this.parameter = parameter;
		type = "LibraryFunctions";
	}

	public String toString() {
		
		if(functionName.equals("getAllNodes"))
			return "syslib.getAllNodes()";
		else if(functionName.equals("getTime"))
			return "syslib.getTime(" + parameter + ")";
		else if(functionName.equals("getNodeWaitingTime"))
			return "syslib.getTime(" + parameter + ")";
		else if(functionName.equals("getLastNode"))
			return "syslib.getLastNode()";
		else if(functionName.equals("getTotalWaitingTime"))
			return "syslib.getTotalWaitingTime()";
		else if(functionName.equals("getAllFirstNodes"))
			return "syslib.getAllFirstNodes()";
		else if(functionName.equals("getNext()"))
			return "syslib.getNext(" + parameter + ")";
		else if(functionName.equals("getTotalTime"))
			return "syslib.getTotalTime()";
		else if(functionName.equals("getPrevious"))
			return "syslib.getPrevious(" + parameter + ")";
		return "error";
	}

}