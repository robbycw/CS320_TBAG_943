package edu.ycp.cs320.tbag_943.classes;

public class Item {
	private boolean consumable, isWeapon;
	private int damage, healthGain, value, amount;
	private double accuracy;
	private String name; 
	
	public Item() {
	}
	
	public Item(String name) {
		this.name = name; 
	}
	
	public Item(String name, int damage) {
		this.name = name;
		this.damage = damage; 
	}
	
	public Item(String name, int damage, int amount) {
		this.name = name;
		this.damage = damage; 
		this.amount = amount; 
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
	public void setAccuracy(double accuracy) {
		this.accuracy = accuracy;
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
	public double getAccuracy() {
		return accuracy;
	}
	
								/// big boy methods///
	public void useItem() {
		
	}
	public boolean isItemCompatible() {
		return false;
	}
}
