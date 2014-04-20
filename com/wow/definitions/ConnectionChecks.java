package com.wow.definitions;
import java.util.*;

public class ConnectionChecks
{
	//ceck if the workflow entered by the user contains any cycles
	//returns true if the graph contains a cycle, otherwise false
	public static boolean detectCycles(Connection connections)
	{
		//initialization
		ArrayList<ArrayList<String>> connectionsList = new ArrayList<ArrayList<String>>(connections.connection);

		if(connections.connection.size() == 0)
			return false;

		String startNode = connections.connection.get(0).get(0);

		// System.out.println("********************************");
		// System.out.println(connections);
		// System.out.println("Start Node: "+startNode);

		//use the code below to deep copy in case the copy constructor doesnt work
		/*connectionsList = new ArrayList<ArrayList<String>>(connections.connection);
		for(ArrayList<String> connection: connections.connection)
		{
			ArrayList<String> singleConnection = new ArrayList<String>();	
			for(String node: connection)
			{
				singleConnection.add(node);
			}
			connectionsList.add(singleConnection);
		}*/

		//initialize and create the adjacency list
		//formation of the adjacency list and formation of node list
		Map<String,ArrayList<String>> adjacencyList = new HashMap<String, ArrayList<String>>(formAdjacencyList(connectionsList));
		// Set<String> visitedSet = new HashSet<String>();
		// List<String> nodeList = new LinkedList<String>();
		

		// printList(adjacencyList);

		Set<String> visitedSet = new HashSet<String>();
		LinkedList<String> frontierList = new LinkedList<String>();

		//add the start node of the workflow and begin BFS
		frontierList.add(startNode);

		//continue till all nodes have been visited
		while(!frontierList.isEmpty())
		{
			String n = frontierList.remove();
			visitedSet.add(n);
			// System.out.println(n);
			//for each connected node of the current node, check if it is there in the visited set
			if(adjacencyList.containsKey(n))
			{
				for(String node: adjacencyList.get(n))
				{
					//if it is already contained in the visited set, then the workflow has a cycle
					if(visitedSet.contains(node))
					{
						return true;
					}
					//if it is not visited yet, add it to the frontier list
					frontierList.add(node);
				}
			}
		}
			// System.out.println("*******************************");
		//successful exit from the loop indicates no cycle
		return false;
	}

	private static Map<String,ArrayList<String>> formAdjacencyList(ArrayList<ArrayList<String>> connectionsList)
	{
		Map<String,ArrayList<String>> adjacencyList = new HashMap<String, ArrayList<String>>();
		for(ArrayList<String> connection: connectionsList)
		{
			String source = connection.get(0);
			String destination = connection.get(1);

			/*if(!visitedSet.contains(source))
			{
				visitedSet.add(source);
				nodeList.add(source);
			}

			if(!visitedSet.contains(destination))
			{
				visitedSet.add(destination);
				nodeList.add(destination);
			}*/

			if(adjacencyList.containsKey(source))
			{
				adjacencyList.get(source).add(destination);
			}
			else
			{
				ArrayList<String> valueList = new ArrayList<String>();
				valueList.add(destination);
				adjacencyList.put(source, valueList);
			}
		}
		return adjacencyList;
	} 


	//prints the adjacencty list formed - used for debugging
	private static void printList(Map<String,ArrayList<String>> adjacencyList)
	{
		for (Map.Entry entry : adjacencyList.entrySet()) {
			System.out.print("key,val: ");
			System.out.print(entry.getKey());
			ArrayList<String> values = (ArrayList<String>)entry.getValue();
			for(int i=0;i<values.size();i++)
			{
				System.out.print(", "+values.get(i));
			}
			System.out.println();
		}
	}
}