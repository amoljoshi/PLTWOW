package com.wow.compute;
import java.util.*;
public class Combine extends ComputeFunction{
	private String target_resource;
	private int target_qty;
	private HashMap<String,Integer> input_resources_ratio;
	private int rate;
	private String print_statement;
	public Combine(String target_resource, int target_qty, HashMap<String,Integer> input_resources_ratio, 
			int rate, String print_statement){
		super("combine");
		this.target_resource = target_resource;
		this.target_qty = target_qty;
		this.input_resources_ratio = new HashMap<String, Integer> (input_resources_ratio);
		this.rate = rate;
		this.print_statement = print_statement;
	}
	public String toString(){
		String s = "Combining \n";
		Iterator it = input_resources_ratio.entrySet().iterator();
      	while (it.hasNext()) {
        	Map.Entry pair = (Map.Entry)it.next();

        }
        return s;
	}
}