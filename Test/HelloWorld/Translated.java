package com.wow.target;

import com.wow.target.*;
import com.wow.definitions.*;
import com.wow.compute.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.*;

public class MainClass {
	HashMap<String, Node> nodeSet = new HashMap<String, Node>();
	HashMap<String, NodeThread> nodeThreadSet = new HashMap<String, NodeThread>();
	HashMap<String, Integer> nodeStatus = new HashMap<String, Integer>();
	Library_Functions syslib;

	public MainClass() {

	}

	String finalOutputResource = "";
	int totalOutputQuantity = 0; // add value from parser again
	int currentOutputQuantity = 0;
	ArrayList<String> rawInputResources = new ArrayList<String>();

	public void setRawInputResources(ArrayList<String> resources) {
		rawInputResources = resources;
	}

	private void printStatusTable() {

		synchronized (System.out) {
			System.out.println("Current Waiting Status: ");
			System.out
					.println("--------------------------------------------------------------------------------------");
			System.out
					.println("|                  NODE                  |                  RESOURCE                 |");
			System.out
					.println("--------------------------------------------------------------------------------------");
			for (String status_node : nodeSet.keySet()) {
				Map<String, ArrayList<Integer>> reference_rawInputResources = nodeSet
						.get(status_node).rawInputResources;
				for (String data : reference_rawInputResources.keySet()) {
					if (reference_rawInputResources.get(data).get(0) != reference_rawInputResources
							.get(data).get(1))
						System.out.println("|                  " + status_node
								+ "                  |                  "
								+ data + "                 |");
				}
			}
			System.out
					.println("--------------------------------------------------------------------------------------");
		}
	}

	public boolean isNumber(String s) {
		char[] c = s.toCharArray();
		for (char a : c)
			if (!Character.isDigit(a))
				return false;
		return true;
	}

	private void start() throws IOException {
		for (String name : nodeSet.keySet()) {
			if (nodeSet.get(name).generatesFinalOutput) {
				for (String s : nodeSet.get(name).outputResources.keySet()) {
					finalOutputResource = s;
					totalOutputQuantity += nodeSet.get(name).outputResources
							.get(s).get(0);
				}
			}

		}
		System.out.println("start");
		for (String s : nodeSet.keySet()) {
			nodeThreadSet.get(s).setMainClass(this);
		}
		this.printStatusTable();
		// Commented below code as it had hardcoded node name
		// for (String s : nodeSet.get("Amol").rawInputResources.keySet()) {
		// System.out.println("Resouece " + s);
		// }
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
				// System.out.println("Argument 0 = " + arguments[0] +
				// ", Argument 1 =" + arguments[1]);
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
							if (isFirstInput
									&& !nodeThreadSet.get(nodeName).isAlive()) {
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
			this.printStatusTable();
		}
	}

	public synchronized void send(String senderNode, String nodeName,
			String resourceName, int quantity) {
		System.out.println("sending " + resourceName + " to " + nodeName
				+ " from " + senderNode);
		if (resourceName.equals(finalOutputResource))
			currentOutputQuantity += quantity;
		if (currentOutputQuantity >= totalOutputQuantity) {
			System.out.println("SUCCESS!!! REJOICE!! CODE WORKS!!!");
			end_func();
			return;
		}
		boolean hasFinished = nodeSet.get(nodeName).isFinished();
		if (hasFinished)
			nodeStatus.put(senderNode, 2);
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
		MainClass mc = new MainClass();
		Node A = new Node("A", true);
		mc.rawInputResources.add("bread");
		A.addInputResource("bread", 8, false);
		mc.rawInputResources.add("butter");
		A.addInputResource("butter", 4, false);
		A.addOutputResource("sandwich", 4);
		NodeThread A_thread = new NodeThread(A);
		mc.nodeSet.put("A", A);
		mc.nodeThreadSet.put("A", A_thread);
		mc.nodeStatus.put("A", 0);
		Convert compute_function_A1 = new Convert("bread", 1, "bread_temp", 1,
				8, 4, "null");
		A.addComputeFunction(compute_function_A1);
		HashMap<String, Integer> resource_ratio_A2 = new HashMap<String, Integer>();
		resource_ratio_A2.put("bread_temp", 2);
		resource_ratio_A2.put("butter", 1);
		Combine compute_function_A2 = new Combine("sandwich", 4,
				resource_ratio_A2, 4, "null");
		A.addComputeFunction(compute_function_A2);

		mc.syslib = new Library_Functions(mc.nodeSet, mc.nodeStatus);
		mc.start();
	}

	public void end_func() {
		System.out.println("Program Terminating successfully.");
		System.exit(0);
	}
}
