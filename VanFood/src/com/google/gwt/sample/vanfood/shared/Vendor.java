package com.google.gwt.sample.vanfood.shared;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Vendor implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7851654394103627831L;
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	@Persistent
	private String name;
	@Persistent
	private String address;
	@Persistent
	private String foodtype;
	@NotPersistent 
	private boolean highlighted;
	
	

	public Vendor(){
		
	}

	public Vendor(String name, String address, String foodtype) {
		this.setName(name);
		this.setAddress(address);
		this.setFoodtype(foodtype);
		this.highlighted = false;
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
	
	public void setHighlighted(boolean highlighted) {
		this.highlighted = highlighted;
	}
	
	public boolean isHighlighted(){
		return this.highlighted;
	}
	
	public String toString(){
		return this.name + ", " + this.address + ",  " + this.foodtype;
	}

	public Vendor getVendor() {
		return this;
	}
	
	public long getVendorID() {
		return id;
	}

}
