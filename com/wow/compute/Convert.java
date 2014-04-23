package com.wow.compute;
public class Convert extends ComputeFunction{
	private String original_resource;
	private int ratio_original_resource;
	private String converted_resource;
	private int ratio_converted_resource;
	private int quantity;
	private int rate;
	private String print_statement;
	public Convert(String original_resource, int ratio_original_resource, String converted_resource, 
					int ratio_converted_resource, int quantity, int rate, String print_statement){
		super("convert");
		this.original_resource = original_resource;
		this.ratio_original_resource = ratio_original_resource;
		this.converted_resource = converted_resource;
		this.ratio_converted_resource = ratio_converted_resource;
		this.quantity = quantity;
		this.rate = rate;
		this.print_statement = print_statement;
	}
	public String toString(){
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("\n Converting resource " + original_resource + " with ratio " + ratio_original_resource + " to resource " + converted_resource
			+ " with ratio " + ratio_converted_resource + "\n");
		sbuf.append("\t rate = " + rate + " quantity = " + quantity + " printing = " + print_statement + "\n");
		return sbuf.toString();
	}
}