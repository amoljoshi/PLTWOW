package com.wow.definitions;
//Enum Type defining different types of node lines
public enum NodeEntryType{
	//	Each node line can be an INPUT, OUTPUT or FINAL output
	INPUT(1), OUTPUT(2), FINAL(3);	
	private int value;
	private NodeEntryType(int value){
		this.value = value;
	}
	public Integer getValue(){
		return value;
	}
};