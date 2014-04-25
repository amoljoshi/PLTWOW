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
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("Combining \n");
		Iterator it = input_resources_ratio.entrySet().iterator();
      	while (it.hasNext()) {
        	Map.Entry pair = (Map.Entry)it.next();
        	sbuf.append("\t\t Resource = " + (String)pair.getKey() + " with ratio = " + (Integer)pair.getValue() + "\n"); 
        }
        sbuf.append("\t Target Resource = " + target_resource + " with quantity = " + target_qty + " at rate " + rate + "\n");
        sbuf.append("\t Printing " + print_statement);
        return sbuf.toString();
	}

	public String getTarget_resource() {
		return target_resource;
	}
	public int getTarget_qty() {
		return target_qty;
	}
	public HashMap<String, Integer> getInput_resources_ratio() {
		return input_resources_ratio;
	}
	public int getRate() {
		return rate;
	}
	public String getPrint_statement() {
		return print_statement;
	}
}