package com.google.gwt.sample.vanfood.shared;

import java.io.Serializable;


public class Vendor implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7851654394103627831L;
	private String name;
	private String address;
	private String foodtype;
	private boolean highlighted;
	
	public Vendor(){
		
	}

	public Vendor(String name, String address, String foodtype) {
		this.setName(name);
		this.setAddress(address);
		this.setFoodtype(foodtype);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getFoodtype() {
		return foodtype;
	}

	public void setFoodtype(String foodtype) {
		this.foodtype = foodtype;
	}
	
	public boolean isHighlighted(){
		return this.highlighted;
	}
	public String toString(){
		return this.name + ", " + this.address + ",  " + this.foodtype;
	}

}
