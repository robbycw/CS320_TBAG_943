package edu.ycp.cs320.tbag_943.classes;

import java.util.ArrayList; 


public class Player {
	private String name; 
	private String icon; 
	private ArrayList<Item> inventory; 
	private Location location; 
	private ArrayList<Stat> stats; 
	
	
	public Player() { 
		this.name = "John Johnson"; 
		this.icon = "path"; 
		this.location = null; 
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name; 
	}

	public Location getLocation() {
		return location;
	}

	public void move(Location location) {
		this.location = location;
	}

	public String getIcon() {
		return icon;
	}
	
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	public ArrayList<Item> getInventory() {
		return inventory;
	}

	public void setInventory(ArrayList<Item> inventory) {
		this.inventory = inventory;
	}

	public ArrayList<Stat> getStats() {
		return stats;
	}

	public void setStats(ArrayList<Stat> stats) {
		this.stats = stats;
	}

	public void collect(Item item) {
		this.inventory.add(item); 
	}
	
	public void interactWithObject() {
		//TODO
	}
	
	public void attackEntity() {
		//TODO
	}
	
	public void levelUp() {
		//TODO
	}

}
