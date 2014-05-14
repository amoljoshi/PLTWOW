package com.wow.ast;

public class LibraryFunctionsNode extends ASTNode {

	public String type;
	public String functionName;
	public String parameter;

	public LibraryFunctionsNode(String functionName) {
		this.functionName = functionName;
		type = "LibraryFunctions-"+functionName;
		// System.out.println("Hello - " + functionName);
	}

	public LibraryFunctionsNode(String functionName, ExpressionNode expr) {
		// System.out.println("Type of expression is = " + expr.type + " where function = " + functionName);
		this.parameter = expr.toString();
		this.functionName = functionName;
		type = "LibraryFunctions-"+functionName;
		if(!"String".equals(expr.type)){
			System.err.println("Error: You cannot call library function " + functionName + " with parameter of type "+expr.type);
		}
		// System.err.println("expr type = " + expr.type + " for function " + functionName);
	}
	public LibraryFunctionsNode(String functionName, String parameter) {
		this.functionName = functionName;
		this.parameter = parameter;
		type = "LibraryFunctions-"+functionName;
		// System.err.println("Hello - " + functionName);
	}

	public String toString() {
		
		if(functionName.equals("getAllNodes"))
			return "syslib.getAllNodes()";
		else if(functionName.equals("getTime"))
			return "syslib.getTime(" + parameter + ")";
		else if(functionName.equals("getNodeWaitingTime"))
			return "syslib.getNodeWaitingTime(" + parameter + ")";
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
		else if(functionName.equals("getTotalOutputQuantity"))
			return "syslib.getTotalOutputQuantity(" + parameter + ")";
		return "error";
	}

}