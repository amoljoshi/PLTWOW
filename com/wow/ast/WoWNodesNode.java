package com.wow.ast;

public class WoWNodesNode extends ASTNode {

	public String type;

	public WoWNodesNode() {
		type = "WoWNodes";
	}

	public String toString() {
		/*returnArray.append("{");
		for(int i=0;i<nodeNames.length();i++)
		{
			returnArray.append(nodeNames[i]);
			if(i!=nodeNames.length()-1)
				returnArray.append(",");
		}
		returnArray.append("}");*/
		return "String[] ";
	}

}