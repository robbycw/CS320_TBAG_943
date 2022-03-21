package edu.ycp.cs320.tbag_943.classes;

import java.util.ArrayList; 
import java.util.HashMap; 


public class Player {
	private String name; 
	private String icon; 
	private HashMap<String, Item> inventory; 
	private Location location; 
	private HashMap<String, Stat> stats; 
	
	
	public Player() { 
		this.name = "John Johnson"; 
		this.icon = "path"; 
		this.location = null; 
	}
	
	public Player(String name, Location location) { 
		this.name = name; 
		this.icon = "path"; 
		this.location = location; 
		this.inventory = new HashMap<String, Item>(); 
		this.stats = new HashMap<String, Stat>();
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
	
	public HashMap<String, Item> getInventory() {
		return inventory;
	}

	public void setInventory(HashMap<String, Item> inventory) {
		this.inventory = inventory;
	}

	public HashMap<String, Stat> getStats() {
		return stats;
	}

	public void setStats(HashMap<String, Stat> stats) {
		this.stats = stats;
	}

	public void collect(Item item) {
		this.inventory.put(item.getName(), item); 
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
