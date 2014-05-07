package com.wow.target;
import com.wow.definitions.*;
import java.util.ArrayList;
import java.util.HashMap;


public class Library_Functions {
	HashMap <String , Node> nodeSet;
	HashMap<String, Integer> nodeStatus;
	public Library_Functions (HashMap<String, Node> nodes, HashMap<String,Integer> status){
		nodeSet = new HashMap<String , Node> (nodes);
		nodeStatus = new HashMap<String,Integer>(status);
	}
	
	
	// returns a set of all node names
	String[] getAllNodes(){
		ArrayList<String> set = new ArrayList<String>();
		for (String s :nodeSet.keySet()){
			set.add(s);
		}
		String[] result = new String[set.size()];
		result = set.toArray(result);
		return result;
	}
	
	//returns the total time for a node
	// returns -1 if nodename does not exist ?????????????
	// returns total number of seconds as of now
	double getTime (String nodeName){
		if (!nodeSet.containsKey(nodeName)) return -1;
		Node node = nodeSet.get(nodeName);
		double time = 0;
		time = (node.getAllOutputGenerated().getTime() - node.getFirstResourceReceived().getTime())/ (1000);
		return time;
	}
	
	// returns the total time that a node waits for beginning execution once it receive its first input resource
	// returns -1 if nodename does not exist ?????????????
	// returns total number of seconds as of now
	double getNodeWaitingTime(String nodeName){
		if (!nodeSet.containsKey(nodeName)) return -1;
		Node node = nodeSet.get(nodeName);
		double time = 0;
		time = (node.getAllResourceReceived().getTime() - node.getFirstResourceReceived().getTime())/ (1000);
		return time;
	}

	String getLastNode(){
		return "";
	}

	String[] getAllFirstNodes(){
		return null;
	}

	double getTotalWaitingTime(){
		return 0;
	}

	double getTotalTime(){
		return 0;
	}

	double getResourceWaitingTime(String resource){
		return 0;
	}
	
	//returns a set of all nodes who sends resources to current node
	//returns null if nodename does not exist ????????????????????????????????
	String [] getPrevious(String nodeName){
		if (!nodeSet.containsKey(nodeName)) return null;
		ArrayList <String> set = new ArrayList<String>();
		Node node = nodeSet.get(nodeName);
		for (String s :node.inNodes.keySet())
			set.add(s);
		String[] result = new String[set.size()];
		result = set.toArray(result);
		return result;
	}
	
	//returns a set of all nodes who expect resources from current node
	//returns null if nodename does not exist
	String [] getNext(String nodeName){
		if (!nodeSet.containsKey(nodeName)) return null;
		ArrayList <String> set = new ArrayList<String>();
		Node node = nodeSet.get(nodeName);
		for (String s :node.outNodes.keySet())
			set.add(s);
		String[] result = new String[set.size()];
		result = set.toArray(result);
		return result;
	}
	
	// the following three methods will not return the updated values, or will it ???? confirm !*
	String[] getFinishedNodeSet() {
		ArrayList <String> set = new ArrayList<String>();
		for (String s :nodeStatus.keySet())
			if (nodeStatus.get(s) == 2) set.add(s);
		String[] result = new String[set.size()];
		result = set.toArray(result);
		return result;
	}
	
	String[] getUnfinishedNodeSet(){
		ArrayList <String> set = new ArrayList<String>();
		for (String s :nodeStatus.keySet())
			if (nodeStatus.get(s) < 2) set.add(s);
		String[] result = new String[set.size()];
		result = set.toArray(result);
		return result;
	}
	
	String[] getCurrentNodeSet(){
		ArrayList <String> set = new ArrayList<String>();
		for (String s :nodeStatus.keySet())
			if (nodeStatus.get(s) == 1) set.add(s);
		String[] result = new String[set.size()];
		result = set.toArray(result);
		return result;
	}
	
	// got to implement
	String getPriority(String _nodeName, String _resourceName){
		return null;
	}
}
