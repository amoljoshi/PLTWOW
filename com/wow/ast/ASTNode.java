package com.wow.ast;
import java.util.*;
public  abstract class ASTNode{
	public ArrayList<ASTNode> children;
	public ASTNode(){
		children = new ArrayList<ASTNode>();
	}
	public abstract String toString();
}