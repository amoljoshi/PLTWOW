package com.wow.definitions;
import java.util.*;

public class CycleDetection
{
	//ceck if the workflow entered by the user contains any cycles
	//returns true if the graph contains a cycle, otherwise false
	public static boolean detectCycles(Connection connections)
	{
		//initialization
		ArrayList<ArrayList<String>> connectionsList = new ArrayList<ArrayList<String>>(connections.connection);

		String startNode = connections.connection.get(0).get(0);

		//use the code below to deep copy in case the constructor doesnt work
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
		Map<String,ArrayList<String>> adjacencyList = new HashMap<String, ArrayList<String>>();
		// Set<String> visitedSet = new HashSet<String>();
		// List<String> nodeList = new LinkedList<String>();
		//formation of the adjacency list and formation of node list
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
		//successful exit from the loop indicates no cycle
		return false;
	}
}