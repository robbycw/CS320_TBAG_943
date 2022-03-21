package edu.ycp.cs320.tbag_943.controller;

import java.util.ArrayList;
import java.util.HashMap;
import edu.ycp.cs320.tbag_943.classes.*;

public class GameController {
	/*
	 * This class will contain methods that are called whenever the user 
	 * inputs commands into the front-end console. Hence, the controller is 
	 * responsible for making the necessary changes to the Game model and
	 * also for adding the resulting text for the player's actions. 
	 * 
	 * Ex:
	 * 	input: move east --> Servlet calls GameController's move method.
	 * 	output: Player moved east into the Dining Hall. *Insert Dining Hall Description*
	 */

	//fields
	private Game model; 
	private boolean inCombat; 
	
	//constructor
	public GameController(Game model) {
		this.model = model; 
		this.inCombat = false; 
	}

	public Game getModel() {
		return model;
	}

	public void setModel(Game model) {
		this.model = model;
	}

	public boolean isInCombat() {
		return inCombat;
	}

	public void setInCombat(boolean inCombat) {
		this.inCombat = inCombat;
	}
	
	public void move(String direction) {
		Map map = model.getMap(); 
		Player player = model.getPlayer(); 
		String start = player.getLocation().getName(); 
		ArrayList<String> connections = map.getConnections().get(start); 
		
		if(direction == "north") {
			if(connections.get(0) != null) { 
				
				Location end = map.getLocations().get(connections.get(0)); 
				player.move(end); 
				
				String desc = player.getName() + " has moved from " + start + " to " + end.getName(); 
				model.addOutput(desc);
			}
		} else if (direction == "east") { 
			if(connections.get(1) != null) { 
			
				Location end = map.getLocations().get(connections.get(1)); 
				player.move(end); 
				
				String desc = player.getName() + " has moved from " + start + " to " + end.getName(); 
				model.addOutput(desc);
			}
		} else if (direction == "south") { 
			if(connections.get(2) != null) { 
 
				Location end = map.getLocations().get(connections.get(2)); 
				player.move(end); 
				
				String desc = player.getName() + " has moved from " + start + " to " + end.getName(); 
				model.addOutput(desc);
			}
		} else if (direction == "west") { 
			if(connections.get(3) != null) { 
				
				Location end = map.getLocations().get(connections.get(3)); 
				player.move(end); 
				
				String desc = player.getName() + " has moved from " + start + " to " + end.getName(); 
				model.addOutput(desc);
			}
		} else {
			String err = "Invalid move."; 
			model.addOutput(err);
		}
	}
	
	public void attack(String target) { 
		if(inCombat) {
			
		} else {
			inCombat = true; 
			
		}
	}
	
	// Prints a help 
	public void help() {
		String s = "You want help? Sorry, I'm all out of pity..."; 
		model.addOutput(s);
	}
	
	public void collect(String item) {
		Player p = model.getPlayer();
		if(p.getLocation().getTreasure().pickUpItem(p, item)) {
			String s = p.getName() + " picked up " + item + "."; 
			model.addOutput(s);
		} else {
			model.addOutput(item + " does not exist here.");
		}
	}
	
	public void talk(String NPC) {
		
	}
	
}
