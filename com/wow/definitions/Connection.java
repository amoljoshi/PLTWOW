package com.wow.definitions;
import java.util.*;

public class Connection{
	static int id = 1;
	int connectionId;
	ArrayList<ArrayList<String>> connection;
	public Connection(){
		connectionId = id++;
		connection = new ArrayList<ArrayList<String>> ();
	}
	public void addConnection(String originNode, String destinationNode){
		ArrayList<String> newConnection = new ArrayList<String>();
		newConnection.add(originNode);
		newConnection.add(destinationNode);
		connection.add(newConnection);
	}
	public String toString(){
		String s = new String();
		s += "\nConnections defined as follows:\n";
		for(ArrayList<String> each : connection){
			s += "\t"+connectionId+". Connection from " + each.get(0) + " to " + each.get(1);
		}
		return s;
	}
}