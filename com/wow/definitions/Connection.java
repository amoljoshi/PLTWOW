package com.wow.definitions;
import java.util.*;

public class Connection{
	ArrayList<ArrayList<String>> connection;
	public Connection(){
		connection = new ArrayList<ArrayList<String>> ();
	}
	public void addConnection(String from, String to){
		ArrayList<String> newConnection = new ArrayList<String>();
		newConnection.add(from);
		newConnection.add(to);
		connection.add(newConnection);
	}
	public String toString(){
		String s = new String();
		s += "\nConnections defined as follow : \n";
		for(ArrayList<String> each : connection){
			s += "\tConnection from " + each.get(0) + " to " + each.get(1);
		}
		return s;
	}
}