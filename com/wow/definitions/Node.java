package com.wow.definitions;
import java.util.*;
import java.lang.*;
import java.util.Date;
//	Class representing each Node in the Workflow
public class Node{
    private String name;
    private HashMap<String, ArrayList<Integer>> rawInputResources = new HashMap <String, ArrayList<Integer>>();
    private HashMap<String, Integer> intermediateInputResources = new HashMap <String, Integer>();
    private HashMap<String, Integer> outputResources = new HashMap <String, Integer>();
    private Date firstResourceReceived = new Date();
    private Date allResourceReceived = new Date();
    private Date allOutputGenerated = new Date();
    private boolean generatesFinalOutput;
    /*
        the following map has the resource name as a key and a hashmap of node-name to quantity list of initial and 
        current values as value→ [ <Resource_name , < Node_name, qtylist > >] 
        -- one each for input & output--
    */
    private HashMap <String , HashMap <String , ArrayList<Integer>>> inResources = new HashMap <String, HashMap<String, ArrayList<Integer>>>();
    private HashMap <String , HashMap <String , ArrayList<Integer>>> outResources = new HashMap <String, HashMap<String, ArrayList<Integer>>>();
    /*
        the following map has the node name as a key and a hashmap of resource name to quantity list of initial and 
        current values as value→ [ <Node_name , < Resource_name, qtylist > >] 
        -- one each for input & output--
    */
	private HashMap <String , HashMap <String , ArrayList<Integer>>> inNodes = new HashMap <String, HashMap<String, ArrayList<Integer>>>();
	private HashMap <String , HashMap <String , ArrayList<Integer>>> outNodes = new HashMap <String, HashMap<String, ArrayList<Integer>>>();
    public Node(String name, boolean generatesFinalOutput){
    	this.name = name;
    	this.rawInputResources = new HashMap<String, ArrayList<Integer>> ();
        this.outputResources = new HashMap<String, Integer> ();
        this.intermediateInputResources = new HashMap<String, Integer> ();
        this.generatesFinalOutput = generatesFinalOutput;            
    }
    public void setFirstResourceReceived(Date date){
        this.firstResourceReceived = date;
    }
    public void setAllResourceReceived(Date date){
        this.allResourceReceived = date;
    }
    public void setAllOutputGenerated(Date date){
        this.allOutputGenerated = date;
    }
    public Date getFirstResourceReceived(){
        return this.firstResourceReceived;
    }
    public Date setAllResourceReceived(){
        return this.allResourceReceived;
    }
    public Date setAllOutputGenerated(){
        return this.allOutputGenerated;
    }
    //  Method which checks if resource name passed as method argument is defined as output resource in this node
    public boolean checkIfOutputResource(String resourceName){
        if(this.outputResources.containsKey(resourceName)){
            return true;
        }
        return false;
    }
    //  Method which checks if resource name passed as method argument is defined as intermediate input resource in this node
    public boolean checkIfIntermediateInputResource(String resourceName){
        if(this.intermediateInputResources.containsKey(resourceName))
        {
            return true;
        }
        return false;
    }
    //  Method which checks if resource name passed as method argument is defined as raw input resource in this node
    public boolean checkIfRawInputResource(String resourceName){
        if(this.rawInputResources.containsKey(resourceName)){
            return true;
        }
        return false;
    }
    public void addNewInResource(String resourceName, String nodeName, Integer quantity){
        //  Updating inResources map
        //  [ <Resource_name , < Node_name, qtylist > >]
        HashMap<String, ArrayList<Integer>> value;
        ArrayList<Integer> quantities = new ArrayList<Integer> ();
        quantities.add(quantity);
        quantities.add(quantity);
        if(this.inResources.containsKey(resourceName)){
            //  Entry for this resource is already added
            value = this.inResources.get(resourceName);
        }
        else{
            //  Adding new entry for this resource
            value = new HashMap<String, ArrayList<Integer>> ();
        }
        value.put(nodeName, quantities);
        this.inResources.put(resourceName, value);        
        //  Updating inNodes map
        //  [ <Node_name , < Resource_name, qtylist > >] 
        value = null;
        if(this.inNodes.containsKey(nodeName)){
            value = this.inNodes.get(nodeName);
        }
        else{
            value = new HashMap<String, ArrayList<Integer>> ();
        }
        value.put(resourceName, quantities);
        this.inNodes.put(nodeName, value);
    }
    public void addNewOutResource(String resourceName, String nodeName, Integer quantity){
        //  Updating outResources map
        //  [ <Resource_name , < Node_name, qtylist > >]
        HashMap<String, ArrayList<Integer>> value;
        ArrayList<Integer> quantities = new ArrayList<Integer> ();
        quantities.add(quantity);
        quantities.add(quantity);
        if(this.outResources.containsKey(resourceName)){
            //  Entry for this resource is already added
            value = this.outResources.get(resourceName);
        }
        else{
            //  Adding new entry for this resource
            value = new HashMap<String, ArrayList<Integer>> ();
        }
        value.put(nodeName, quantities);
        this.outResources.put(resourceName, value);        
        //  Updating outNodes map
        //  [ <Node_name , < Resource_name, qtylist > >] 
        value = null;
        if(this.outNodes.containsKey(nodeName)){
            value = this.outNodes.get(nodeName);
        }
        else{
            value = new HashMap<String, ArrayList<Integer>> ();
        }
        value.put(resourceName, quantities);
        this.outNodes.put(nodeName, value);        
    }
    //	Method to add a new input resource
    public void addInputResource(String resourceName, Integer quantity, boolean intermediate){
        if(intermediate){
            //  Intermediate Input resources i.e. resource generated by some other node
            //  which user can't create and pass it to this node
            //  which means this node depends on the other node for getting this resource
    	   this.intermediateInputResources.put(resourceName, quantity);
           return;
        }
        ArrayList<Integer> quantities = new ArrayList<Integer> ();
        quantities.add(quantity);
        quantities.add(quantity);
        this.rawInputResources.put(resourceName, quantities);
    }
    //  Method to add a new input resource
    public void addOutputResource(String resourceName, Integer quantity){
        this.outputResources.put(resourceName, quantity);
    }
    public String getName(){
        return this.name;
    }
    //	Method to get all raw input resources for this node
    public HashMap<String, ArrayList<Integer>> getAllRawInputResources(){
    	return this.rawInputResources;
    }
    //  Method to get all intermediate input resources for this node
    public HashMap<String, Integer> getAllIntermediateInputResources(){

        return this.intermediateInputResources;
    }
    //  Method to get all output resources for this node
    public HashMap<String, Integer> getAllOutputResources(){
        return this.outputResources;
    }
    public void setRawInputResources(HashMap<String, ArrayList<Integer>> resources){
        this.rawInputResources = resources;
    }
    public void setIntermediateInputResources(HashMap<String, Integer> resources){
        this.intermediateInputResources = resources;
    }
    public void setOutputResources(HashMap<String, Integer> resources){
        this.outputResources = resources;
    }
    /* dead code! 
    public HashMap <String, ArrayList<Integer>> setUpRuntimeInputResources(){
        HashMap<String, ArrayList<Integer>> result = new HashMap<String, ArrayList<Integer>> ();
        Iterator it = this.rawInputResources.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            ArrayList<Integer> quantities = new ArrayList<Integer> ();
            quantities.add((Integer)pair.getValue());
            quantities.add((Integer)pair.getValue());
            result.put((String)pair.getKey(), quantities);
        }
        it = this.intermediateInputResources.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            ArrayList<Integer> quantities = new ArrayList<Integer> ();
            quantities.add((Integer)pair.getValue());
            quantities.add((Integer)pair.getValue());
            result.put((String)pair.getKey(), quantities);
        }
        return result;
    }
    public HashMap <String, ArrayList<Integer>> setUpRuntimeOuputResources(){
        HashMap<String, ArrayList<Integer>> result = new HashMap<String, ArrayList<Integer>> ();
        Iterator it = this.outputResources.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            ArrayList<Integer> quantities = new ArrayList<Integer> ();
            quantities.add((Integer)pair.getValue());
            quantities.add(0);
            result.put((String)pair.getKey(), quantities);
        }
        return result;
    }
    */
    public String toString(){
        String s = new String();
        s+= "Node " + this.name + "\n";
        s+= "\t Showing Raw Input Resources generated by this node --" + "\n";
        Iterator it = this.rawInputResources.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            s+= "\t\t ID = " + pair.getKey() + " quantity = " + pair.getValue() + "\n";
        }
        s+= "\t Showing Intermediate Input Resources generated by this node --" + "\n";
        it = this.intermediateInputResources.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            s+= "\t\t ID = " + pair.getKey() + " quantity = " + pair.getValue() + "\n";
        }
        s+= "\t Showing Ouput Resources generated by this node --" + "\n";
        it = this.outputResources.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            s+= "\t\t ID = " + pair.getKey() + " quantity = " + pair.getValue() + "\n";
        }
        if(this.generatesFinalOutput){
            s+= "\tThis node generates final Output!";
        }        
        return s;
    }
  }
