package edu.ycp.cs320.tbag_943.classes;

import java.util.HashMap;

public class Loot {
	private int xp;
	//private enum loot[];
	private Item item; 
	private boolean collected;

	public Loot(Item item) {
		this.item = item; 
		collected = false;
	}
	
	public Item getItem() {
		return item;
	}

	public void setItems(Item item) {
		this.item = item;
	}
	
	public void calculateLoot() {
		
	}
	
	public void giveXP() {
		
	}
	
	public boolean isCollected() {
		return collected;
	}
	
	public void giveItems(Player player) {
		if(!collected)
		{
			player.getInventory().put(item.getName(), item); 
			collected = true;
		}
	}
	
	public boolean pickUpItem(Player player, String itemName) {
		if(item.getName().equalsIgnoreCase(itemName) && !collected) {
			player.collect(item);
			collected = true;
			return true; 
		} else {
			return false;
		}
	}
	
	public void giveInfo() {
		
	}
	
}
