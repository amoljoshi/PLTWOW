package com.wow.definitions;
import java.util.*;
import java.lang.*;
import java.util.Date;
import com.wow.compute.*;
//  Class representing each Node in the Workflow
public class Node{
    String name;
    public HashMap<String, ArrayList<Integer>> UnsyncRawInputResources = new HashMap <String, ArrayList<Integer>>();
    public HashMap<String, ArrayList<Integer>> UnsyncIntermediateInputResources = new HashMap <String, ArrayList<Integer>>();
    public HashMap<String, ArrayList<Integer>> UnsyncOutputResources = new HashMap <String, ArrayList<Integer>>();
    

    public Date firstResourceReceived ;
    public Date allResourceReceived ;
    public Date allOutputGenerated ;
    public boolean generatesFinalOutput;
    
    
    //making the maps synchronized now --
    public Map<String, ArrayList<Integer>> rawInputResources = Collections.synchronizedMap(UnsyncRawInputResources);
    public Map<String, ArrayList<Integer>> intermediateInputResources = Collections.synchronizedMap(UnsyncIntermediateInputResources);
    public Map<String, ArrayList<Integer>> outputResources = Collections.synchronizedMap(UnsyncOutputResources);
    
    //-- new code added by Nimai--
    public HashMap<String, Integer> intermediateCreatedResources = new HashMap<String, Integer>();
    //-- new code added by Nimai--
    // -- added for convert combine mechanism by dharmen
    public ArrayList <ComputeFunction> computeArray = new ArrayList <ComputeFunction>();
    
    public void addComputeFunction (ComputeFunction cf){
        this.computeArray.add(cf);
    }
    
    public Boolean get_generatesFinalOutput(){
        return this.generatesFinalOutput;
    }
    // -- added for convert combine mechanism by dharmen

    /*
        the following map has the resource name as a key and a hashmap of node-name to quantity list of initial and 
        current values as value --> [ <Resource_name , < Node_name, qtylist > >] 
        -- one each for input & output--
    */
    public HashMap <String , HashMap <String , ArrayList<Integer>>> UnsyncInResources = new HashMap <String, HashMap<String, ArrayList<Integer>>>();
    public HashMap <String , HashMap <String , ArrayList<Integer>>> UnsyncOutResources = new HashMap <String, HashMap<String, ArrayList<Integer>>>();
    //synchronized versions below
    public Map<String, HashMap <String , ArrayList<Integer>>> inResources = Collections.synchronizedMap(UnsyncInResources);
    public Map<String, HashMap <String , ArrayList<Integer>>> outResources = Collections.synchronizedMap(UnsyncOutResources);
    
    /*
        the following map has the node name as a key and a hashmap of resource name to quantity list of initial and 
        current values as value --> [ <Node_name , < Resource_name, qtylist > >] 
        -- one each for input & output--
    */
    public HashMap <String , HashMap <String , ArrayList<Integer>>> UnsyncInNodes = new HashMap <String, HashMap<String, ArrayList<Integer>>>();
    public HashMap <String , HashMap <String , ArrayList<Integer>>> UnsyncOutNodes = new HashMap <String, HashMap<String, ArrayList<Integer>>>();
    //synchronized versions below
    public Map<String, HashMap <String , ArrayList<Integer>>> inNodes = Collections.synchronizedMap(UnsyncInNodes);
    public Map<String, HashMap <String , ArrayList<Integer>>> outNodes = Collections.synchronizedMap(UnsyncOutNodes);

    public HashMap<String, Long> resourceWaitingTime = new HashMap<String , Long>();

    public void setResourceWaitingTime (String resourceName){
        Date first = getFirstResourceReceived();
        Date now = new Date();
        resourceWaitingTime.put(resourceName, (now.getTime() - first.getTime())/1000);
    }

    public long getResourceWaitingTime (String resourceName){
        if (!resourceWaitingTime.containsKey(resourceName))
            return -1;
        else return resourceWaitingTime.get(resourceName);
    }

    
    public Node(String name, boolean generatesFinalOutput){
        this.name = name;
        /*this.rawInputResources = new HashMap<String, ArrayList<Integer>> ();
        this.outputResources = new HashMap<String, Integer> ();
        this.intermediateInputResources = new HashMap<String, Integer> ();*/
        this.generatesFinalOutput = generatesFinalOutput;            
    }
//#############################################################################################################################//
    
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
    //  Method to add a new input resource
    public void addInputResource(String resourceName, Integer quantity, boolean intermediate){
        if(intermediate){
            //  Intermediate Input resources i.e. resource generated by some other node
            //  which user can't create and pass it to this node
            //  which means this node depends on the other node for getting this resource
           ArrayList<Integer> toAddToIntermediateInputResources = new ArrayList<Integer>();
           toAddToIntermediateInputResources.add(0,quantity);
           toAddToIntermediateInputResources.add(1,quantity);
           this.intermediateInputResources.put(resourceName, toAddToIntermediateInputResources);
           return;
        }
        ArrayList<Integer> quantities = new ArrayList<Integer> ();
        quantities.add(quantity);
        quantities.add(0);
        this.rawInputResources.put(resourceName, quantities);
    }
    //  Method to add a new input resource
    public void addOutputResource(String resourceName, Integer quantity){
        ArrayList<Integer> toAddToOutputResources = new ArrayList<Integer>();
        toAddToOutputResources.add(quantity);
        toAddToOutputResources.add(0);
        this.outputResources.put(resourceName, toAddToOutputResources);
    }
//#############################################################################################################################//    
    
    public boolean isFinished(){
    for (String resource: outputResources.keySet()){
        if (outputResources.get(resource).get(0) != outputResources.get(resource).get(1)) return false;
        }
    return true;
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
    //  Method to get all raw input resources for this node
    public Map<String, ArrayList<Integer>> getAllRawInputResources(){
        return this.rawInputResources;
    }
    //  Method to get all intermediate input resources for this node
    public Map<String, ArrayList<Integer>> getAllIntermediateInputResources(){

        return this.intermediateInputResources;
    }
    //  Method to get all output resources for this node
    public Map<String, ArrayList<Integer>> getAllOutputResources(){
        return this.outputResources;
    }

    // Method to get all the inResources for this node
     public Map<String, HashMap<String, ArrayList<Integer>>> getAllInResources(){
        return this.inResources;
    }
    // Method to get all the outResources for this node   
    public Map<String, HashMap<String, ArrayList<Integer>>> getAllOutResources(){
        return this.outResources;
    }


    public void setRawInputResources(HashMap<String, ArrayList<Integer>> resources){
        this.rawInputResources = new HashMap<String, ArrayList <Integer>>(resources);
    }
    public void setIntermediateInputResources(HashMap<String, ArrayList<Integer>> resources){
        this.intermediateInputResources = new HashMap<String, ArrayList<Integer>>(resources);
    }
    public void setOutputResources(HashMap<String, ArrayList<Integer>> resources){
        this.outputResources = new HashMap<String , ArrayList<Integer>>(resources);
    }

    public void setInResources(HashMap<String, HashMap<String, ArrayList<Integer>>> inresources){
        this.inResources = inresources;
    }

    public void setOutResources(HashMap<String, HashMap<String, ArrayList<Integer>>> outresources){
        this.outResources = outresources;
    } 

    public void setInNodes(HashMap<String, HashMap<String,ArrayList<Integer>>> innodes){
        this.inNodes = innodes;
    }

    public void setOutNodes(HashMap<String, HashMap<String, ArrayList<Integer>>> outnodes){
        this.outNodes = outNodes;
    }

    public void setAllComputations(ArrayList<ComputeFunction> computeArray){
        this.computeArray = new ArrayList<ComputeFunction> (computeArray);
    }
    public void combine(String target_resource, int target_qty, HashMap<String,Integer> input_resources_ratio,int rate, String print_statement){
       for (int i = 1; i <= target_qty; i++)
       {
        Iterator itr = input_resources_ratio.entrySet().iterator();
        while(itr.hasNext()){
            Map.Entry pair = (Map.Entry)itr.next();
            String res = (String)pair.getKey();
            int quantity_of_resource = (Integer)pair.getValue();
            updateValue(res, quantity_of_resource);
            }
            try{
                Thread.sleep(rate); 
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }
        }
       // -- in the following piece of code, I have added the number of units of the target_resource produced to the outputresources table Iff the target_resource is an output resource 
        if(outputResources.containsKey(target_resource))
        {
            int x = outputResources.get(target_resource).get(1);
            x = x + target_qty;
            outputResources.get(target_resource).set(1,x);
            // update the output resource table & update the intermediate created resource
        }
        // -- irrespective of whether the target_resource is an output resource, it is added to the intermediateCreatedResource to handle cases where the target_resource may be used by subsequent combine or convert steps
        if(intermediateCreatedResources.containsKey(target_resource))
            {
                int x = intermediateCreatedResources.get(target_resource);
                x = x + target_qty;
                intermediateCreatedResources.put(target_resource,x);
            }
        else intermediateCreatedResources.put(target_resource,target_qty);
        System.out.println(print_statement);
       }

    public void convert(String original_resource, int ratio_original_resource, String converted_resource, int ratio_converted_resource, int quantity, int rate, String print_statement){
        for(int i = 1; i <= quantity; i += ratio_original_resource)
        {
            updateValue(original_resource,ratio_original_resource);
            try{
                Thread.sleep(rate);
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }
        }

        if(outputResources.containsKey(converted_resource))
        {
            int x = outputResources.get(converted_resource).get(1);
            x = x + (quantity * ratio_converted_resource / ratio_original_resource);
            outputResources.get(converted_resource).set(1,x);
        }

        if(intermediateCreatedResources.containsKey(converted_resource))
        {
                int x = intermediateCreatedResources.get(converted_resource);
                x = x + (quantity * ratio_converted_resource / ratio_original_resource);
                intermediateCreatedResources.put(converted_resource,x);
        }
        else intermediateCreatedResources.put(converted_resource,quantity * ratio_converted_resource / ratio_original_resource);
        System.out.println(print_statement);
    }

    public void updateValue(String res_name, int quantity_of_resource){
        if (rawInputResources.containsKey(res_name))
        {
            int new_qty = rawInputResources.get(res_name).get(1) - quantity_of_resource;
            rawInputResources.get(res_name).set(1,new_qty);
        }

        else if (intermediateCreatedResources.containsKey(res_name))
        {
            int new_qty = intermediateCreatedResources.get(res_name) - quantity_of_resource;
            intermediateCreatedResources.put(res_name, new_qty);
        }

        else if (intermediateInputResources.containsKey(res_name))
        {
            int new_qty = intermediateInputResources.get(res_name).get(1) - quantity_of_resource;
            intermediateInputResources.get(res_name).set(1,new_qty);   
        }
    }

    public void resetValues (){
        for (String resource : rawInputResources.keySet()){
            int quantity = rawInputResources.get(resource).get(0);
            rawInputResources.get(resource).set(1,quantity);
        }
        for (String resource : intermediateInputResources.keySet()){
            int quantity = intermediateInputResources.get(resource).get(0);
            intermediateInputResources.get(resource).set(1,quantity);
        }
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
            s+= "\t\t ID = " + pair.getKey() + " quantity = " + (ArrayList<Integer>)pair.getValue() + "\n";
        }
        s+= "\t Showing Intermediate Input Resources generated by this node --" + "\n";
        it = this.intermediateInputResources.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            // ArrayList<Integer> value = new ArrayList<Integer> (pair.getValue());
            s+= "\t\t ID = " + pair.getKey() + " quantity = " + (ArrayList<Integer>)pair.getValue() + "\n";
        }
        s+= "\t Showing Ouput Resources generated by this node --" + "\n";
        it = this.outputResources.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            // ArrayList<Integer> value = new ArrayList<Integer> (pair.getValue());
            s+= "\t\t ID = " + pair.getKey() + " quantity = " + (ArrayList<Integer>)pair.getValue() + "\n";
        }
        if(this.generatesFinalOutput){
            s+= "\tThis node generates final Output!\n";
        }
        s += "\t\nShowing computations = \n";
        for(ComputeFunction c : computeArray){
            s += c.toString();
            // System.out.println(c.toString());
        }
        return s;
    }
  


/*
Yet to add to translateNodeCreation()
- Creating threads for each node
- add threads to the nodeThreadSet
- add data to nodeStatus
- Create an object for LibrayFunciton class
*/

public String translateNodeCreation(){
        String tc = "";
        tc += "Node " + this.getNodeName() + " = new Node(\""+this.getNodeName()+"\","+this.generatesFinalOutput+");\n";
        // Code to create objects of rawIpRes, intIpRes, opRes, inRes, outRes
        
        // ==== comment this code. May not be needed

        /*tc += "rawIpRes = new HashMap<String, ArrayList<Integer>>();\n"; 
        tc += "intIpRes = new HashMap<String,ArrayList<Integer>>();\n";
        tc += "opRes = new HashMap<String, ArrayList<Integer>>();\n";
        tc += "inRes = new HashMap<String, HashMap<String, ArrayList<Integer>>>();\n";
        tc += "outRes = new HashMap<String, HashMap<String, ArrayList<Integer>>>();\n";
        tc += "inNodes = new HashMap<String, HashMap<String, ArrayList<Integer>>>();\n";
        tc += "outNodes = new HashMap<String, HashMap<String, ArrayList<Integer>>>();\n";

        // Code to call the set() methods on all this to assignment objects to class variables
        tc += this.getNodeName()+".setRawInputResources(rawIpRes);\n";
        tc += this.getNodeName()+".setIntermediateInputResources(intIpRes);\n";
        tc += this.getNodeName()+".setOutputResources(opRes);\n";
        tc += this.getNodeName()+".setInResources(inRes);\n";
        tc += this.getNodeName()+".setOutResources(outRes);\n";
        tc += this.getNodeName()+".setInNodes(inNodes);\n";
        tc += this.getNodeName()+".setoutNodes(outNodes);\n";*/

        // Get all the keys for the hash maps and probe and start inserting
        Set<String> rawIpResKeys = getAllRawInputResources().keySet();
        Set<String> intIpResKeys = getAllIntermediateInputResources().keySet();
        Set<String> opResKeys = getAllOutputResources().keySet();
        Set<String> inResKeys = getAllInResources().keySet();
        Set<String> outResKeys = getAllOutResources().keySet();

        // === Comment this code. May not be needed

        // Add data to rawInputResources table
        for (String i : rawIpResKeys)
        {
            tc += "mc.rawInputResources.add(\""+i+"\");\n";
            int qty = rawInputResources.get(i).get(0);
            tc+= this.getNodeName() +".addInputResource(\""+i + "\"," + qty + ",false);\n";
        }

        // Add data to intermediateInputResources table
        for (String i : intIpResKeys)
        {
            int qty = intermediateInputResources.get(i).get(0);
            tc+= this.getNodeName() +".addInputResource(\""+i + "\" ," + qty + ",true);\n";   
        }

        // Add data to outputResKeys table
        for (String i : opResKeys)
        {
            int qty = outputResources.get(i).get(0);
            tc+= this.getNodeName() +".addOutputResource(\""+i+"\" ,"+ qty +");\n";
        }

        // Add data to inResources table
        for (String i : inResKeys)
        {
            Set<String> temp_keys = inResources.get(i).keySet();
            for (String j: temp_keys)
            {
                int qty = inResources.get(i).get(j).get(0);
                tc += this.getNodeName() + ".addNewInResource(\"" + i + "\",\"" + j + "\", "+ qty +");\n";
            }
        }

        // Add data to outResources table
        for (String i : outResKeys)
        {
            Set<String> temp_keys = outResources.get(i).keySet();
            for (String j: temp_keys)
            {
                int qty = outResources.get(i).get(j).get(0);
                tc += this.getNodeName() + ".addNewOutResource(\"" + i + "\",\"" + j + "\", "+ qty +");\n";
            }
        }

        // -- Create a thread for the node. 
        tc += "NodeThread "+ this.getNodeName() + "_thread = new NodeThread("+this.getNodeName()+");\n";
        
        // Add node to nodeSet
        tc += "mc.nodeSet.put(\""+this.getNodeName()+"\","+this.getNodeName()+");\n";

        // add node thread created above to nodeThreadSet
        tc += "mc.nodeThreadSet.put(\""+this.getNodeName()+"\","+ this.getNodeName()+"_thread);\n";
        
        // add node status to nodeStatus map
        tc += "mc.nodeStatus.put(\""+this.getNodeName()+"\","+0+");\n";


        // compute translation
        int compute_counter = 0;
        for (ComputeFunction cf : this.computeArray){
            compute_counter+=1;
            if (cf instanceof Combine){
                Combine com = (Combine) cf;
                tc+= "HashMap <String, Integer> resource_ratio_" + this.getNodeName() + String.valueOf(compute_counter) + " = new HashMap<String, Integer> ();\n";
                HashMap <String, Integer> resource_ratio = new HashMap <String,Integer> (com.getInput_resources_ratio());
                for (String res : resource_ratio.keySet()){
                    tc += "resource_ratio_"+this.getNodeName()+String.valueOf(compute_counter)+".put(\""+ res + "\" , " + resource_ratio.get(res).toString() + ");\n";
                }
                tc += "Combine compute_function_"+this.getNodeName() + String.valueOf(compute_counter) + " = new Combine( \"" + com.getTarget_resource()+"\" , " + String.valueOf(com.getTarget_qty()) + ", resource_ratio_" + this.getNodeName() + String.valueOf(compute_counter)+ " , " + String.valueOf(com.getRate())+ " , \"" + com.getPrint_statement() + "\" );\n";
                tc += this.getNodeName()+".addComputeFunction(compute_function_"+this.getNodeName() + String.valueOf(compute_counter) + ");\n";
            }
            if (cf instanceof Convert){
                Convert con = (Convert) cf;
                tc += "Convert compute_function_"+this.getNodeName() + String.valueOf(compute_counter) + " = new Convert(\"" + con.getOriginal_resource()+"\" , " + String.valueOf(con.getRatio_original_resource()) + ", \"" + con.getConverted_resource() + "\", " + String.valueOf(con.getRatio_converted_resource())+ " , " + String.valueOf(con.getQuantity()) + " , " + String.valueOf(con.getRate()) +  " , \"" + con.getPrint_statement() + "\" );\n";
                tc += this.getNodeName()+".addComputeFunction(compute_function_"+this.getNodeName() + String.valueOf(compute_counter) + ");\n";
            }
        }
        // compute translation


        //System.out.println(tc);
        return tc;
    }
}
