package edu.ycp.cs320.tbag_943.classes;

public class Item {
	private boolean isConsumable, isWeapon, isArmor, isTool;
	private int damage, healthGain, value, amount, id, armor;
	private double accuracy;
	private String name, description; 
	
	
	// Constructors
	public Item() {
		this.name = "";
		this.description = "";
	}

	public Item(String name) {
		this.name = name; 
		this.description = "";
	}
	
	public Item(String name, int damage) {
		this.name = name;
		this.damage = damage; 
		this.description = "";
	}
	
	public Item(String name, int damage, int amount) {
		this.name = name;
		this.damage = damage; 
		this.amount = amount; 
		this.description = "";
	}
	
	public void setIcon() {
		
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public void setName(String name) {
		this.name = name; 
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getDes() {
		return description; 
	}
	
	public void setDes(String des) {
		this.description = des; 
	}
								///consumable setter stuff//
	public void isConsumable(boolean isConsumable) {
		this.isConsumable = isConsumable;
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
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name; 
	}

								/// consumable getter stuff ///
	public boolean getIsConsumbale() {
		return isConsumable;
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
	
								/// armor setter stuff//
	public void isArmor(boolean isArmor) {
		this.isArmor = isArmor;
	}
	public void setArmor(int armor) {
		this.armor = armor;
	}
								/// armor getter stuff ///
	public boolean getIsArmor() {
		return isArmor;
	}
	public int getArmor() {
		return armor;
	}
	
								// tool setter stuff //
	public void isTool(boolean isTool) {
		this.isTool = isTool;
	}
								// tool getter stuff //
	public boolean getIsTool() {
		return isTool;
	}
								/// big boy methods///
	public void useItem() {
		
	}
	public boolean isItemCompatible() {
		return false;
	}
}
