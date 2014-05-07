import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class MainClass {
	HashMap<String, Node> nodeSet = new HashMap<String, Node>();
	HashMap<String, NodeThread> nodeThreadSet = new HashMap<String, NodeThread>();
	HashMap<String, Integer> nodeStatus = new HashMap<String, Integer>();

	public MainClass() {

	}

	final String finalOutputResource = "sandwich";
	final int totalOutputQuantity = 4; // final output quantity defined in the
										// Node A resource block

	int currentOutputQuantity = 0;
	ArrayList<String> rawInputResources = new ArrayList<String>();

	public void setRawInputResources(ArrayList<String> resources) {
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

		System.out.println("start");

		for (String s : nodeSet.keySet()) {
			nodeThreadSet.get(s).setMainClass(this);
		}

		// retrieve the resource name from resource table formed after parsing
		// --------------------- just printing i guess
		for (String s : nodeSet.get("A").rawInputResources.keySet()) {
			System.out.println("Resource " + s);
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
								resourceName).get(1) == nodeSet.get(nodeName).rawInputResources
								.get(resourceName).get(0))
							continue;
						else {
							boolean isFirstInput = nodeSet.get(nodeName)
									.isFirstInput();
							System.out.println(isFirstInput);
							if (isFirstInput) {
								nodeStatus.put(nodeName, 1);
								nodeThreadSet.get(nodeName).start();
								// add code to start thread
							}
							nodeThreadSet.get(nodeName).receiveRawInput(
									resourceName, qty);
						}
					} else
						System.out.println("Node " + nodeName
								+ " does not require " + resourceName + ".");

				}

			}
		}

	}

	public synchronized void send(String senderNode, String nodeName,
			String resourceName, int quantity) {
		if (resourceName.equals(finalOutputResource))
			currentOutputQuantity += quantity;
		if (currentOutputQuantity >= totalOutputQuantity) {
			System.out.println("SUCCESS!!! REJOICE!! CODE WORKS!!!");
			return;
		}
		// add any checks if required - discuss with teammates
		boolean isFirstInput = nodeSet.get(nodeName).isFirstInput();
		if (isFirstInput && !nodeThreadSet.get(nodeName).isAlive()) {
			nodeThreadSet.get(nodeName).start();
			// code to start thread
		}
		nodeThreadSet.get(nodeName).receiveIntermediate(senderNode,
				resourceName, quantity);
	}

	public static void main(String[] args) throws IOException {

		// Node name retrieved after parsing
		String name1 = "A";

		// TODO Auto-generated method stub
		MainClass mc = new MainClass();
		ArrayList<String> temp = new ArrayList<String>();

		// resource names retrieved after parsing
		temp.add("bread");
		temp.add("butter");
		mc.setRawInputResources(temp); // resources is hashmap of raw input
										// resources

		// declarations for creating nodes
		// for each node do the following
		// ////////////////////////////////////////////////////////////////////////////////////////////////
		Node A = new Node(name1, true);

		NodeThread A_thread = new NodeThread(A);

		// arraylist for every input resource

		// bread
		ArrayList<Integer> q = new ArrayList<Integer>();
		q.add(8);
		q.add(0);
		HashMap<String, ArrayList<Integer>> w = new HashMap<String, ArrayList<Integer>>();
		w.put("bread", q);

		// butter
		ArrayList<Integer> q1 = new ArrayList<Integer>();
		q1.add(4);
		q1.add(0);
		w.put("butter", q1);

		for (String d : w.keySet()) {
			System.out.println(d + " " + w.get(d) + "===========");
		}

		// for each node do the following
		A.setRawInputResources(w); // para1 = Hashmap of raw input resources
									// (String Resource : integer qty)
		w.remove("bread");
		w.remove("butter");

		HashMap<String, Integer> e = new HashMap<String, Integer>();
		e.put("sandwich", 4);
		A.setOutputResources(e); // para1 = Hashmap of output resources (String
									// Resource : integer qty)

		e.remove("sandwich");

		mc.nodeSet.put(name1, A);
		mc.nodeThreadSet.put(name1, A_thread);
		mc.nodeStatus.put(name1, 0);

		// adding all computations
		
		// NOTE- the following hashmap can be made using copy constructor while
		// translating //
		HashMap<String, Integer> ratio = new HashMap<String, Integer>();
		ratio.put("bread", 2);
		ratio.put("butter", 1);
		// --//

		Combine combine = new Combine("sandwich", 4, ratio, 4,
				"combining bread and butter to form sandwich");
		A.addComputeFunction(combine);

		Library_Functions system = new Library_Functions(mc.nodeSet,
				mc.nodeStatus);

		// /////////////////////////////////////////////////////////////////////////////////////////////////////////////

		// /////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// initialize connections
		// /////////////////////////////////////////////////////////////////////////////////////////////////////////////

		mc.start();

	}

}
