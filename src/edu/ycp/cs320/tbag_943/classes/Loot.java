package edu.ycp.cs320.tbag_943.classes;

import java.util.HashMap;

public class Loot {
	private int xp;
	//private enum loot[];
	private HashMap<String, Item> items; 

	public Loot(HashMap<String, Item> items) {
		this.items = items; 
	}
	
	public HashMap<String, Item> getItems() {
		return items;
	}

	public void setItems(HashMap<String, Item> items) {
		this.items = items;
	}
	
	public void calculateLoot() {
		
	}
	
	public void giveXP() {
		
	}
	
	public void giveItems(Player player) {
		for(String i : items.keySet()) {
			player.getInventory().put(i, items.get(i)); 
		}
	}
	
	public boolean pickUpItem(Player player, String item) {
		if(items.containsKey(item)) {
			player.collect(items.get(item));
			
			items.remove(item); 
			
			return true; 
		} else {
			return false;
		}
	}
	
	public void giveInfo() {
		
	}
	
}
