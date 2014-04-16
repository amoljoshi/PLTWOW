package com.wow.definitions;
import java.util.*;
import java.lang.*;
import java.util.Date;
//	Class representing each Node in the Workflow
public class Node extends Thread{
    private String name;
    private HashMap<String, ArrayList<Integer>> UnsyncRawInputResources = new HashMap <String, ArrayList<Integer>>();
    private HashMap<String, Integer> UnsyncIntermediateInputResources = new HashMap <String, Integer>();
    private HashMap<String, Integer> UnsyncOutputResources = new HashMap <String, Integer>();
    private Date firstResourceReceived ;
    private Date allResourceReceived ;
    private Date allOutputGenerated ;
    private boolean generatesFinalOutput;
    private MainClass mc = null;
    
    //making the maps synchronized now --
    Map<String, ArrayList<Integer>> rawInputResources = Collections.synchronizedMap(UnsyncRawInputResources);
    Map<String, Integer> intermediateInputResources = Collections.synchronizedMap(UnsyncIntermediateInputResources);
    Map<String, Integer> outputResources = Collections.synchronizedMap(UnsyncOutputResources);
    
    
    
    /*
        the following map has the resource name as a key and a hashmap of node-name to quantity list of initial and 
        current values as value --> [ <Resource_name , < Node_name, qtylist > >] 
        -- one each for input & output--
    */
    private HashMap <String , HashMap <String , ArrayList<Integer>>> UnsyncInResources = new HashMap <String, HashMap<String, ArrayList<Integer>>>();
    private HashMap <String , HashMap <String , ArrayList<Integer>>> UnsyncOutResources = new HashMap <String, HashMap<String, ArrayList<Integer>>>();
    //synchronized versions below
    Map<String, HashMap <String , ArrayList<Integer>>> inResources = Collections.synchronizedMap(UnsyncInResources);
    Map<String, HashMap <String , ArrayList<Integer>>> outResources = Collections.synchronizedMap(UnsyncOutResources);
    
    /*
        the following map has the node name as a key and a hashmap of resource name to quantity list of initial and 
        current values as value --> [ <Node_name , < Resource_name, qtylist > >] 
        -- one each for input & output--
    */
	private HashMap <String , HashMap <String , ArrayList<Integer>>> UnsyncInNodes = new HashMap <String, HashMap<String, ArrayList<Integer>>>();
	private HashMap <String , HashMap <String , ArrayList<Integer>>> UnsyncOutNodes = new HashMap <String, HashMap<String, ArrayList<Integer>>>();
	//synchronized versions below
    Map<String, HashMap <String , ArrayList<Integer>>> inNodes = Collections.synchronizedMap(UnsyncInNodes);
    Map<String, HashMap <String , ArrayList<Integer>>> outNodes = Collections.synchronizedMap(UnsyncOutNodes);
	
    public Node(String name, boolean generatesFinalOutput){
    	this.name = name;
    	/*this.rawInputResources = new HashMap<String, ArrayList<Integer>> ();
        this.outputResources = new HashMap<String, Integer> ();
        this.intermediateInputResources = new HashMap<String, Integer> ();*/
        this.generatesFinalOutput = generatesFinalOutput;            
    }
//#############################################################################################################################//
    public void setMainClass(MainClass m){
    	this.mc = m;
    }
    
    public void run() {
    	setFirstResourceReceived(); 		// sets the timestamp for start of node --- node now waits for all inputs to be received
    	while (true){
    		if (canExecute()){
    			System.out.println("ALL INPUTS RECEIVED. CALLING THE EXECUTION CODE.");
    			setAllResourceReceived();	// sets the timestamp for the moment when all input resources are received
    			// ############ call method to start executing combine convert
    			break;
    		}
    	}
    }
    
    public boolean canExecute(){
    	// following checks if all raw inputs are recieved from the hashmap - rawInputResources
    	for (String s : rawInputResources.keySet()){
    		if (rawInputResources.get(s).get(0) != rawInputResources.get(s).get(1))
    			return false;   		
    	}
    	
    	// and following checks if all intermediate resources are received from the hashmap - inResources
    	for (String s : inResources.keySet()){
    		for (String t : inResources.get(s).keySet()){
    			if (inResources.get(s).get(t).get(0) != inResources.get(s).get(t).get(1))
    				return false;
    		}
    	}
    	return true;
    }
//#############################################################################################################################//    

    public void setFirstResourceReceived(){
        this.firstResourceReceived = new Date();
    }
    public void setAllResourceReceived(){
        this.allResourceReceived = new Date();
    }
    public void setAllOutputGenerated(){
        this.allOutputGenerated = new Date();
    }
    public Date getFirstResourceReceived(){
        return this.firstResourceReceived;
    }
    public Date getAllResourceReceived(){
        return this.allResourceReceived;
    }
    public Date getAllOutputGenerated(){
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
        quantities.add(0);
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
        quantities.add(0);
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
        quantities.add(0);
        this.rawInputResources.put(resourceName, quantities);
    }
    //  Method to add a new input resource
    public void addOutputResource(String resourceName, Integer quantity){
        this.outputResources.put(resourceName, quantity);
    }
//#############################################################################################################################//    
    public synchronized void receiveRawInput (String resourceName , int quantity){
		quantity = Math.min(quantity, this.rawInputResources.get(resourceName).get(0) - this.rawInputResources.get(resourceName).get(1));
		Integer current = this.rawInputResources.get(resourceName).get(1);
		this.rawInputResources.get(resourceName).set(1, current + quantity); 
	}
    
    public synchronized void receiveIntermediate (String nodeName , String resourceName , int quantity ){
    	// first check if quantity is more than the expected quantity #parseCheck ?
    	Integer current = this.inResources.get(resourceName).get(nodeName).get(1);
    	quantity = Math.min(quantity, this.inResources.get(resourceName).get(nodeName).get(0) - this.inResources.get(resourceName).get(nodeName).get(1));
    	//updating tables now
    	this.inResources.get(resourceName).get(nodeName).set(1, current + quantity);
    	this.inNodes.get(nodeName).get(resourceName).set(1, current + quantity);
    }
	
	public boolean isFirstInput (){
		for (String resource : rawInputResources.keySet())
			if (rawInputResources.get(resource).get(1) != 0) return false;
		return true;
	}
//#############################################################################################################################//    
    public String getNodeName(){
        return this.name;
    }
    //	Method to get all raw input resources for this node
    public Map<String, ArrayList<Integer>> getAllRawInputResources(){
    	return this.rawInputResources;
    }
    //  Method to get all intermediate input resources for this node
    public Map<String, Integer> getAllIntermediateInputResources(){

        return this.intermediateInputResources;
    }
    //  Method to get all output resources for this node
    public Map<String, Integer> getAllOutputResources(){
        return this.outputResources;
    }
    public void setRawInputResources(HashMap<String, ArrayList<Integer>> resources){
        this.rawInputResources = new HashMap<String, ArrayList <Integer>>(resources);
    }
    public void setIntermediateInputResources(HashMap<String, Integer> resources){
        this.intermediateInputResources = new HashMap<String, Integer>(resources);
    }
    public void setOutputResources(HashMap<String, Integer> resources){
        this.outputResources = new HashMap<String , Integer>(resources);
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
