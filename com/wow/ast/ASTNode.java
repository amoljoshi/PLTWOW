package com.wow.ast;
import java.util.*;
public  abstract class Node{
	private ArrayList<Node> children;
	public Node(){
		children = new ArrayList<Node>();
	}
	public abstract String toString();
}