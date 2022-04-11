package edu.ycp.cs320.tbag_943.classes;

import java.util.ArrayList; 
import java.util.HashMap; 


public class Player {
	private String name; 
	private String icon; 
	private HashMap<String, Item> inventory; 
	private Location location; 
	private HashMap<String, Stat> stats; 
	private String weapon; 
	private boolean playerCreated = false;
	private WinCondition winCondition;
	
	public Player() { 
		this.name = "John Johnson"; 
		this.icon = "path"; 
		this.location = null;
		winCondition = new WinCondition();
	}
	
	public Player(String name, Location location) { 
		this.name = name; 
		this.icon = "path"; 
		this.location = location; 
		this.location.setHidden(false);
		this.inventory = new HashMap<String, Item>(); 
		this.stats = new HashMap<String, Stat>();
		Item dagger = new Item("dagger", 3); 
		this.inventory.put("dagger", dagger); 
		this.weapon = "dagger"; 
		winCondition = new WinCondition();
	}
	
	public Player(String name, Location location, int health, int armor, int strength, int speed) { 
		this.name = name; 
		this.icon = "path"; 
		this.location = location; 
		this.location.setHidden(false);
		this.inventory = new HashMap<String, Item>(); 
		this.stats = new HashMap<String, Stat>();
		Item dagger = new Item("dagger", 3); 
		this.inventory.put("dagger", dagger); 
		this.weapon = "dagger"; 
		
		// Generate stats 
		Stat h = new Stat("health", health); 
		Stat a = new Stat("armor", armor); 
		Stat st = new Stat("strength", strength); 
		Stat sp = new Stat("speed", speed); 
		
		HashMap<String, Stat> stat = new HashMap<String, Stat>(); 
		stat.put("health", h);
		stat.put("armor", a);
		stat.put("strength", st);
		stat.put("speed", sp); 
		this.stats = stat;
		
		playerCreated = true;
		winCondition = new WinCondition();
	}
	
	public boolean getPlayerCreated() {
		return playerCreated;
	}
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name; 
	}

	public String getWeapon() {
		return weapon;
	}

	public void setWeapon(String weapon) {
		this.weapon = weapon;
	}

	public Location getLocation() {
		return location;
	}

	public void move(Location location) {
		// Player discovers location; no longer hidden. 
		location.setHidden(false);
		this.location = location;
	}

	public String getIcon() {
		return icon;
	}
	
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	public WinCondition getWinCondition() {
		return winCondition;
	}
	
	public void setWinCondition(WinCondition winConditionIn) {
		this.winCondition = winConditionIn;
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
	
	public void levelUp() {
		//TODO
	}

}
