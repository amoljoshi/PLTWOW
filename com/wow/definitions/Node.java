package com.wow.definitions;
import java.util.*;
import java.lang.*;
//	Class representing each Node in the Workflow
public class Node{
    private String name;
    //	Each line in the node definition is stored as a HashMap with following structure
    //	Key = ID, Value = ArrayList of integers
    //	ArrayList of Integers contain the type of this line -	1 represents INPUT
    //															2 represents OUTPUT
    //															3 represents FINAL
    //									quantity of this line
    private HashMap<String, ArrayList<Integer>> entries;
    public Node(String name){
    	this.name = name;
    	this.entries = new HashMap<String, ArrayList<Integer>> ();
    }
    //	Method to insert a new node line
    public void addEntry(String ID, NodeEntryType n, Integer quantity){
    	//Making an arraylist for storing type of this line (Entry) and its quantity
    	ArrayList<Integer> attributes = new ArrayList<Integer> ();
    	attributes.add(n.getValue());
    	attributes.add(quantity);
    	entries.put(ID, attributes);
    }
    //	Method to get a particular line from this NODE
    public ArrayList<Integer> getEntry(String ID){
    	if(entries.containsKey(ID)){
    		return entries.get(ID);
    	}
    	else{
    		//	Entry node found, returning an empty ArrayList
    		return new ArrayList<Integer> ();
    	}
    }
    public String getName(){
        return this.name;
    }
    //	Method to get all node lines for the this NODE
    public HashMap<String, ArrayList<Integer>> getAllEntries(){
    	return entries;
    }
    public void setEntries(HashMap<String, ArrayList<Integer>> entries){
        this.entries = entries;
    }
      private String getNodeEntryType(int id){
        int counter = 1;
        for(NodeEntryType type : NodeEntryType.values()){
          if(counter == id)
            return type.toString();
          counter++;
        }
        return "type not defined!";
      }    
    public String toString(){
        String s = new String();
        s+= "Node " + this.name + "\n";
        List<String> keys = new ArrayList<String>(this.entries.keySet());
        for(String key : keys){
            ArrayList<Integer> attributes = new ArrayList<Integer>(entries.get(key));
            s+= "\tID = " + key + " Type = " + this.getNodeEntryType(attributes.get(0)) + " quantity = " 
                + attributes.get(1).toString() + "\n";
        }
        return s;
    }
  }
