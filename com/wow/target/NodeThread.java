package com.wow.target;
import com.wow.definitions.*;
import com.wow.compute.*;
public class NodeThread extends Thread {
	private MainClass mc;
	Node node ;
	public NodeThread (Node n){
		this.node = n;
	}
	public void setMainClass(MainClass m){
    	this.mc = m;
    }
    public void run() {
    	System.out.println("Node " + this.node.getNodeName() + " running now");
    	node.setFirstResourceReceived(); 		// sets the timestamp for start of node --- node now waits for all inputs to be received
    	while (true){
    		if (node.canExecute()){
    			System.out.println("ALL INPUTS RECEIVED. CALLING THE EXECUTION CODE.");
    			node.setAllResourceReceived();	// sets the timestamp for the moment when all input resources are received
    			// ############ call method to start executing combine convert
                for (ComputeFunction cf: this.node.computeArray){
                    if(cf instanceof Convert){
                        Convert con = (Convert) cf;
                        this.node.convert(con.getOriginal_resource(), con.getRatio_original_resource(), con.getConverted_resource(), con.getRatio_converted_resource(), con.getQuantity(), con.getRate(), con.getPrint_statement());
                    }
                    else if (cf instanceof Combine){
                        Combine com = (Combine) cf;
                        this.node.combine(com.getTarget_resource(), com.getTarget_qty(), com.getInput_resources_ratio(), com.getRate(), com.getPrint_statement());
                    }
                }
                for (String receiver : this.node.outNodes.keySet())
                    for (String resourceName : this.node.outNodes.get(receiver).keySet())
                        this.mc.send(this.node.getNodeName(), receiver, resourceName, this.node.outNodes.get(receiver).get(resourceName).get(0));
                
                if (this.node.get_generatesFinalOutput()){
                    String resourceName = this.node.outputResources.keySet().iterator().next();
                    this.mc.send(this.node.getNodeName(), "", resourceName ,this.node.outputResources.get(resourceName).get(0));
                }
    			break;
    		}
    	}
        stopThread();
    }
    // add stopping checks in this method
    public void stopThread(){
        System.out.println("Stopping thread");
        stop();
    }  
    public synchronized void receiveRawInput (String resourceName , int quantity){
		quantity = Math.min(quantity, node.rawInputResources.get(resourceName).get(0) - node.rawInputResources.get(resourceName).get(1));
		Integer current = node.rawInputResources.get(resourceName).get(1);
		node.rawInputResources.get(resourceName).set(1, current + quantity); 
	}
    
    public synchronized void receiveIntermediate (String nodeName , String resourceName , int quantity ){
    	// first check if quantity is more than the expected quantity #parseCheck ?
    	Integer current = node.inResources.get(resourceName).get(nodeName).get(1);
    	quantity = Math.min(quantity, node.inResources.get(resourceName).get(nodeName).get(0) - node.inResources.get(resourceName).get(nodeName).get(1));
    	//updating tables now
    	node.inResources.get(resourceName).get(nodeName).set(1, current + quantity);
    	node.inNodes.get(nodeName).get(resourceName).set(1, current + quantity);
    }
	
}
