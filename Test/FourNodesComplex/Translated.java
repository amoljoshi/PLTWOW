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
		Node body_assembly2 = new Node("body_assembly2", false);
		mc.rawInputResources.add("chassis");
		body_assembly2.addInputResource("chassis", 1, false);
		body_assembly2.addInputResource("base", 1, true);
		body_assembly2.addOutputResource("body", 1);
		body_assembly2.addNewInResource("base", "base_assembly", 1);
		body_assembly2.addNewOutResource("body", "car_assembly", 1);
		NodeThread body_assembly2_thread = new NodeThread(body_assembly2);
		mc.nodeSet.put("body_assembly2", body_assembly2);
		mc.nodeThreadSet.put("body_assembly2", body_assembly2_thread);
		mc.nodeStatus.put("body_assembly2", 0);
		HashMap<String, Integer> resource_ratio_body_assembly21 = new HashMap<String, Integer>();
		resource_ratio_body_assembly21.put("chassis", 1);
		resource_ratio_body_assembly21.put("base", 1);
		Combine compute_function_body_assembly21 = new Combine("body", 1,
				resource_ratio_body_assembly21, 100, "null");
		body_assembly2.addComputeFunction(compute_function_body_assembly21);
		Node body_assembly1 = new Node("body_assembly1", false);
		mc.rawInputResources.add("chassis");
		body_assembly1.addInputResource("chassis", 1, false);
		body_assembly1.addInputResource("base", 1, true);
		body_assembly1.addOutputResource("body", 1);
		body_assembly1.addNewInResource("base", "base_assembly", 1);
		body_assembly1.addNewOutResource("body", "car_assembly", 1);
		NodeThread body_assembly1_thread = new NodeThread(body_assembly1);
		mc.nodeSet.put("body_assembly1", body_assembly1);
		mc.nodeThreadSet.put("body_assembly1", body_assembly1_thread);
		mc.nodeStatus.put("body_assembly1", 0);
		HashMap<String, Integer> resource_ratio_body_assembly11 = new HashMap<String, Integer>();
		resource_ratio_body_assembly11.put("chassis", 1);
		resource_ratio_body_assembly11.put("base", 1);
		Combine compute_function_body_assembly11 = new Combine("body", 1,
				resource_ratio_body_assembly11, 100, "null");
		body_assembly1.addComputeFunction(compute_function_body_assembly11);
		Node car_assembly = new Node("car_assembly", true);
		car_assembly.addInputResource("body", 2, true);
		car_assembly.addInputResource("windshield", 2, true);
		car_assembly.addOutputResource("car", 2);
		car_assembly.addNewInResource("body", "body_assembly2", 1);
		car_assembly.addNewInResource("body", "body_assembly1", 1);
		NodeThread car_assembly_thread = new NodeThread(car_assembly);
		mc.nodeSet.put("car_assembly", car_assembly);
		mc.nodeThreadSet.put("car_assembly", car_assembly_thread);
		mc.nodeStatus.put("car_assembly", 0);
		HashMap<String, Integer> resource_ratio_car_assembly1 = new HashMap<String, Integer>();
		resource_ratio_car_assembly1.put("body", 1);
		resource_ratio_car_assembly1.put("windshield", 1);
		Combine compute_function_car_assembly1 = new Combine("car", 1,
				resource_ratio_car_assembly1, 50, "null");
		car_assembly.addComputeFunction(compute_function_car_assembly1);
		Node base_assembly = new Node("base_assembly", false);
		mc.rawInputResources.add("wheel");
		base_assembly.addInputResource("wheel", 8, false);
		mc.rawInputResources.add("axle");
		base_assembly.addInputResource("axle", 4, false);
		base_assembly.addOutputResource("base", 2);
		base_assembly.addNewOutResource("base", "body_assembly2", 1);
		base_assembly.addNewOutResource("base", "body_assembly1", 1);
		NodeThread base_assembly_thread = new NodeThread(base_assembly);
		mc.nodeSet.put("base_assembly", base_assembly);
		mc.nodeThreadSet.put("base_assembly", base_assembly_thread);
		mc.nodeStatus.put("base_assembly", 0);
		HashMap<String, Integer> resource_ratio_base_assembly1 = new HashMap<String, Integer>();
		resource_ratio_base_assembly1.put("axle", 1);
		resource_ratio_base_assembly1.put("wheel", 2);
		Combine compute_function_base_assembly1 = new Combine("base", 2,
				resource_ratio_base_assembly1, 10, "null");
		base_assembly.addComputeFunction(compute_function_base_assembly1);

		mc.syslib = new Library_Functions(mc.nodeSet, mc.nodeStatus);
		mc.start();
	}

	public void end_func() {
		double i = 0.0;
		String[] all = syslib.getAllNodes();
		for (String node : all) {
			double d = syslib.getNodeWaitingTime(node);
			if (d < 10.0) {
				System.out.println(node);
			}
		}
		System.out.println("Program Terminating successfully.");
		System.exit(0);
	}
}
