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
	private boolean playerTurnTaken; 
	
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
		// First, we need to ensure that the target exists as an NPC. 
		if(model.getPlayer().getLocation().getNPCs().containsKey(target)) {
			// The target exists as an NPC
			NPC tar = model.getPlayer().getLocation().getNPCs().get(target); 
			if(inCombat) {
				// There should be an ongoing combat in this case, which will be referenced in model. 
				// It must be the player's turn if this branch is accessed. 
				// We will first want to check if the player is still alive. 
				if(model.getPlayer().getStats().get("health").getRank() <= 0) {
					inCombat = false; 
					String x = "You have died. GAME OVER."; 
					model.addOutput(x);
				} else {
					// The Player is still alive! 
					Combat c = model.getCurrentCombat(); 
					
					// Calculate and apply player's attack. 
					c.playerAttack(model, model.getPlayer(), target);
					
					// Continue the NPCs turns if combat isn't over! 
					if(c.isDead()) { 
						// Combat is complete! 
						inCombat = false; 
						String x = "All enemies have been slain!"; 
						model.addOutput(x);
					} else {
						// Combat is not over
						playerTurnTaken = true; 
						c.runCombat(model, model.getPlayer(), playerTurnTaken);
					}
				}
				
			} else {
				// We will want to ensure that a combat can be initiated with this target. 
				if(tar.isCombat()) {
					// The combat will then need to start! 
					inCombat = true; 
					// We will need to find the combat that contains this target. 
					ArrayList<Combat> combats = model.getPlayer().getLocation().getCombats(); 
					
					for(Combat c : combats) {
						if(c.getNpcs().containsKey(target)) {
							model.setCurrentCombat(c);
							break; 
						}
					}
					
					// The combat can now be started. 
					playerTurnTaken = false; 
					String s = "Combat initiated!"; 
					model.addOutput(s);
					model.getCurrentCombat().runCombat(model, model.getPlayer(), playerTurnTaken);
					
				} else {
					String s = tar.getName() + " cannot be fought."; 
					model.addOutput(s);
				}
				
	 
				
			}
			
		} else {
			// The target is not an NPC. 
			
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
	
	public void equip(String item) {
		HashMap<String, Item> inventory = model.getPlayer().getInventory(); 
		if(inventory.containsKey(item)) {
			model.getPlayer().setWeapon(item);
			String s = item + " is now equipped."; 
			model.addOutput(s);
		} else {
			String s = "You do not have a " + item + '.'; 
			model.addOutput(s);
		}
	}
	
}
