import com.wow.definitions.*;

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
    	System.out.println("Node " + node.name + " running now");
    	node.setFirstResourceReceived(); 		// sets the timestamp for start of node --- node now waits for all inputs to be received
    	while (true){
    		if (node.canExecute()){
    			System.out.println("ALL INPUTS RECEIVED. CALLING THE EXECUTION CODE.");
    			node.setAllResourceReceived();	// sets the timestamp for the moment when all input resources are received
    			// ############ call method to start executing combine convert
    			if (node.name.equals("A")){
    				this.mc.send("A", "B", "bread1", 20);
    				this.mc.send("A", "B", "butter1", 10);
    			}
    			if (node.name.equals("B")){
    				this.mc.send("B", "", "sandwich", 10);
    			}
    			break;
    		}
    	}
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
