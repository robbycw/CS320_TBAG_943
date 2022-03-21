package edu.ycp.cs320.tbag_943.classes;

public class Item {
	private boolean consumable, isWeapon;
	private int damage, healthGain, value;
	private double acuracy;
	private String name; 
	
	public Item() {
	}
	
	public Item(String name) {
		this.name = name; 
	}
	
	public void setIcon() {
		
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public String getName() {
		return name; 
	}
	
	public void setName(String name) {
		this.name = name; 
	}
								///consumable setter stuff//
	public void isConsumable(boolean consumable) {
		this.consumable = consumable;
	}
	public void setHealthGain(int healthGain) {
		this.healthGain = healthGain;
	}
								
	
								/// weapon setter stuff///
	public void isWeapon(boolean isWeapon) {
		this.isWeapon = isWeapon;
	}
	public void setDamage(int damage) {
		this.damage = damage;
	}
	public void setAcuracy(double acuracy) {
		this.acuracy = acuracy;
	}
	
	
								/// these are the getters /////
	public int getValue() {
		return value;
	}
	
	
								/// consumable getter stuff ///
	public boolean getIsConsumbale() {
		return consumable;
	}
	public int getHealthGain() {
		return healthGain;
	}
	
	
	
								/// weapon getter stuff ///
	public boolean getIsWeapon() {
		return isWeapon;
	}
	public int getDamage() {
		return damage;
	}
	public double getAcuracy() {
		return acuracy;
	}
	
								/// big boy methods///
	public void useItem() {
		
	}
	public boolean isItemCompatable() {
		return false;
	}
}
