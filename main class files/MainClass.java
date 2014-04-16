import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Node;

public class MainClass {
	HashMap<String, Node> nodeSet = new HashMap<String, Node>();

	public MainClass() {

	}

	final String finalOutputResource = "add value from parser here";
	final int totalOutputQuantity = 10; // add value from parser again
	int currentOutputQuantity = 0;
	ArrayList<String> rawInputResources = new ArrayList<String>();

	public void setRawInputResources(ArrayList <String> resources) {
		rawInputResources = resources;
	}

	public boolean isNumber(String s) {
		char[] c = s.toCharArray();
		for (char a : c)
			if (!Character.isDigit(a))
				return false;
		return true;
	}

	private void start() throws IOException {
		for (String s : nodeSet.keySet()){
			nodeSet.get(s).setMainClass(this);
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line;
		while (currentOutputQuantity < totalOutputQuantity) {
			line = br.readLine();
			line = line.trim();
			if (line.toLowerCase().startsWith("send ")) {
				line = line.substring(5);
				String[] arguments = line.split(" ");
				if (arguments.length != 3) {
					System.out.println("Illegal command");
				}
				if (!nodeSet.containsKey(arguments[0]))
					System.out
							.println("Node does not exist in the workflow. Please try again.");
				else if (!rawInputResources.contains(arguments[1]))
					System.out
							.println("Resource does not exist in the workflow. Please try again.");
				else if (!isNumber(arguments[2]))
					System.out
							.println("Third Argument must be an integer. Please try again.");
				else {
					String nodeName = arguments[0];
					String resourceName = arguments[1];
					Integer qty = Integer.parseInt(arguments[2]);
					if (nodeSet.get(nodeName).rawInputResources
							.containsKey(resourceName)) {
						if (nodeSet.get(nodeName).rawInputResources.get(
								resourceName).get(1) == nodeSet.get(nodeName).rawInputResources.get(
										resourceName).get(0))
							continue;
						else {
							boolean isFirstInput = nodeSet.get(nodeName).isFirstInput();
							if (isFirstInput && !nodeSet.get(nodeName).isAlive())
								{
								nodeSet.get(nodeName).start();
								// add code to start thread
							}
							nodeSet.get(nodeName).receiveRawInput(resourceName, qty);
						}
					}
					else System.out.println("Node " + nodeName + " does not require " + resourceName + ".");
					
				}

			}
		}

	}
	
	public synchronized void send (String senderNode, String nodeName , String resourceName , int quantity){
		// add any checks if required - discuss with teammates
		// add check for final output generation
		
		boolean isFirstInput = nodeSet.get(nodeName).isFirstInput();
		if (isFirstInput && !nodeSet.get(nodeName).isAlive())
			{
			nodeSet.get(nodeName).start();
			// code to start thread
		}
		nodeSet.get(nodeName).receiveIntermediate (senderNode,resourceName,quantity);
	}

	public static void main(String[] args) throws IOException {
		// bullshit
		String name1 = "a";
		String name2 = "b";
		//

		// TODO Auto-generated method stub
		MainClass mc = new MainClass();
		mc.setRawInputResources(resources); // resources is hashmap of raw input
											// resources

		// declarations for creating nodes
		// for each node do the following
		// ////////////////////////////////////////////////////////////////////////////////////////////////
		Node A = new Node(name1, false);

		// initialize data structures of nodes

		// for each node do the following
		A.setRawInputResources(); // para1 = Hashmap of raw input resources
									// (String Resource : integer qty)
		A.setOutputResources(); // para1 = Hashmap of output resources (String
								// Resource : integer qty)
		A.setIntermediateResources(); // para1 = Hashmap of intermediate
										// resources (String Resource : integer
										// qty)

		// do the following for each resource of each node
		A.addNewInResource(); // para1 = String resourcename , para2 = String
								// nodeName , para3 = Integer qty
		A.addnewOutResource(); // para1 = String resourcename , para2 = String
								// nodeName , para3 = Integer qty
		A.addInputResource(); // para1 = String resourcename , para2 = Integer
								// qty , para3 = boolean intermediate to check
								// if intermediate or not
		A.addOutputResource(); // para1 = String resourcename , para2 = Integer
								// qty , para3 = boolean intermediate to check
								// if intermediate or not

		mc.nodeSet.put(name1, A);
		// /////////////////////////////////////////////////////////////////////////////////////////////////////////////

		// /////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// initialize connections
		// /////////////////////////////////////////////////////////////////////////////////////////////////////////////

		mc.start();

	}

}
