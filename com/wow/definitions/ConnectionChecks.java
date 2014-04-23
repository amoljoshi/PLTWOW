package com.wow.definitions;
import java.util.*;

public class ConnectionChecks
{

	public static boolean detectHangingParts(Connection connections)
	{
		//initialization
		ArrayList<ArrayList<String>> connectionsList = new ArrayList<ArrayList<String>>(connections.connection);

		//if the workflow is a single node, no hanging parts
		if(connections.connection.size() == 0)
			return false;

		HashSet<String> allNodes = new HashSet<String>();
		allNodes = formNodeList(connections);

		Map<String,ArrayList<String>> adjacencyList = new HashMap<String, ArrayList<String>>(formAdjacencyList(connectionsList));
		Iterator adjListIterator = adjacencyList.keySet().iterator();

		//form the adjacency list for an undirected graph
		Map<String,ArrayList<String>> undirectedGraph = new HashMap<String, ArrayList<String>>(adjacencyList);

		while(adjListIterator.hasNext())
		{
			String node = (String)adjListIterator.next();
			ArrayList<String> children = adjacencyList.get(node);

			for(String child: children)
			{
				if(undirectedGraph.containsKey(child))
				{
					if(!undirectedGraph.get(child).contains(node))
					{
						undirectedGraph.get(child).add(node);
					}
				}
				else
				{
					//initialize a new list and add a new entry in the hash map
					ArrayList<String> valueList = new ArrayList<String>();
					valueList.add(node);
					undirectedGraph.put(child, valueList);
				}
			}
		}
		// printList(undirectedGraph);

		HashSet<String> bfsVisits = new HashSet<String>();
		bfsVisits = performBFS(undirectedGraph);

		for(String node: allNodes)
		{
			// System.out.println(node);
			if(!bfsVisits.contains(node))
			{
				return true;
			}
		}


		return false;
	}


	private static HashSet<String> performBFS(Map<String,ArrayList<String>> undirectedGraph)
	{
		HashSet<String> visitedSet = new HashSet<String>();
		ArrayList<String> frontierList = new ArrayList<String>();

		String start = undirectedGraph.keySet().iterator().next();
		frontierList.add(start);
		while(!frontierList.isEmpty())
		{
			String n = frontierList.remove(0);
			// System.out.println(n);
			for(String node: undirectedGraph.get(n))
			{
				if(!visitedSet.contains(node))
				{
					frontierList.add(node);
				}
			}
			visitedSet.add(n);
		}
		return visitedSet;
	}

	private static HashSet<String> formNodeList(Connection connectionsList)
	{
		HashSet<String> allNodes = new HashSet<String>();
		for(ArrayList<String> connection: connectionsList.connection)
		{
			String source = connection.get(0);
			String destination = connection.get(1);

			if(!allNodes.contains(source))
			{
				allNodes.add(source);
			}

			if(!allNodes.contains(destination))
			{
				allNodes.add(destination);
			}
		}
		return allNodes;
	}

	//check if the workflow entered by the user contains any cycles
	//returns true if the graph contains a cycle, otherwise false
	public static boolean detectCycles(Connection connections)
	{
		//initialization
		ArrayList<ArrayList<String>> connectionsList = new ArrayList<ArrayList<String>>(connections.connection);

		//no cycle in a single node workflow
		if(connections.connection.size() == 0)
			return false;

		// String startNode = connections.connection.get(0).get(0);

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
		//formation of the adjacency list
		Map<String,ArrayList<String>> adjacencyList = new HashMap<String, ArrayList<String>>(formAdjacencyList(connectionsList));
		// List<String> nodeList = new LinkedList<String>();
		
		// printList(adjacencyList);
		int vertices = adjacencyList.size();
		Set<String> visitedSet = new HashSet<String>();
		LinkedList<String> frontierList = new LinkedList<String>();

		Iterator mapIterator = adjacencyList.keySet().iterator();

		while(mapIterator.hasNext())
		{
			String currentNode = (String)mapIterator.next();
			if(visitedSet.contains(currentNode))
				continue;

			if(dfsHelper(currentNode, adjacencyList, visitedSet))
				return true;

		}
		return false;

	}

	private static boolean dfsHelper(String currentNode, Map<String,ArrayList<String>> adjacencyList, Set<String> visitedSet)
	{
		Set<String> cycleCheckSet = new HashSet<String>();
		Stack<String> visitingStack = new Stack<String>();
		visitingStack.push(currentNode);
		visitedSet.add(currentNode);

		while(!visitingStack.empty())
		{
			String n = visitingStack.pop();
			cycleCheckSet.add(n);
			if(adjacencyList.containsKey(n))
			{
				for(String node: adjacencyList.get(n))
				{
					//if it is already contained in the visited set, then the workflow has a cycle
					if(cycleCheckSet.contains(node))
					{
						return true;
					}
					//if it is not visited yet, add it to the frontier list
					visitingStack.push(node);
				}
			}
		}
		return false;
	}

	private static Map<String,ArrayList<String>> formAdjacencyList(ArrayList<ArrayList<String>> connectionsList)
	{
		Map<String,ArrayList<String>> adjacencyList = new HashMap<String, ArrayList<String>>();
		for(ArrayList<String> connection: connectionsList)
		{
			String source = connection.get(0);
			String destination = connection.get(1);

			if(adjacencyList.containsKey(source))
			{
				adjacencyList.get(source).add(destination);
			}
			else
			{
				//initialize a new list and add a new entry in the hash map
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