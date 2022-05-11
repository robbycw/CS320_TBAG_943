package edu.ycp.cs320.tbag_943.classes;

import java.util.HashMap;
import java.util.Set;

public class Loot {
	private int xp, id;
	//private enum loot[];
	private HashMap<String, Item> items; 

	// Constructors 
	
	public Loot()
	{
		this.items = new HashMap<String, Item>();
	}
	
	public Loot(Item item) {
		this.items = new HashMap<String, Item>(); 
		items.put(item.getName(), item);
	}
	
	// Methods
	
	public void calculateLoot() {
		
	}
	

	public void giveItems(Player player) {
		Set<String> keys = items.keySet();
		for(String item : keys) {
			player.getInventory().put(item, items.get(item)); 
			items.remove(item);
		}
	}
	
	public boolean pickUpItem(Player player, String itemName) {
		if(items.containsKey(itemName)) {
			player.collect(items.get(itemName));
			items.remove(itemName);
			return true; 
		} else {
			return false;
		}
	}
	
	public void giveInfo() {
		
	}
	
	// Getters
	
	public HashMap<String,Item> getItems() {
		return items;
	}
	
	public int getId() {
		return id;
	}
	
	public int getXp() {
		return xp;
	}
	
	public void addItem(Item item) {
		items.put(item.getName().toLowerCase(), item);
	}
	// Setters 
	
	public void setXP(int xp) {
		this.xp = xp; 
	}
	
	public void setItems(HashMap<String,Item> itemsIn) {
		this.items = itemsIn;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
}
