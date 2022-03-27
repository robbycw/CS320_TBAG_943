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

	// Fields
	private Game model; 
	
	// Constructor
	public GameController(Game model) {
		this.model = model;  
	}

	// Getters
	public Game getModel() {
		return model;
	}
	
	// Setters
	public void setModel(Game model) {
		this.model = model;
	}

	// Methods
	
	public void move(String direction) {
		Map map = model.getMap(); 
		Player player = model.getPlayer(); 
		
		// The player cannot move if in combat. 
		if(model.isInCombat()) { 
			model.addOutput(GameController.inCombatMessage("move"));
			return; 
		}
		
		if(direction.equals("north")) {

			GameController.doMove(model, map, player, 0);
			
		} else if (direction.equals("east")) { 

			GameController.doMove(model, map, player, 1);
			
		} else if (direction.equals("south")) { 
			
			GameController.doMove(model, map, player, 2);
			
		} else if (direction.equals("west")) { 
			
			GameController.doMove(model, map, player, 3);
			
		} else {
			String err = "Invalid move."; 
			model.addOutput(err);
		}
	}
	
	public static void doMove(Game model, Map map, Player player, int direction) {
		String start = player.getLocation().getName();
		ArrayList<String> connections = map.getConnections().get(start); 
		// The player can move here. 
		System.out.println("Printing null string:" + connections.get(direction)); 
		if(!connections.get(direction).equals("-1")) { 
			
			Location end = map.getLocations().get(connections.get(direction)); 
			player.move(end); 
			
			String desc = player.getName() + " has moved from " + start + " to " + end.getName(); 
			model.addOutput(desc);
			
			// Print the room description
			model.addOutput(player.getLocation().getDescription());
			
		} else {
			// The player cannot move here. 
			String err = player.getName() + " cannot move in this direction";  
			model.addOutput(err);
		}
		
	}
	
	public void attack(String target) { 
		// First, we need to ensure that the target exists as an NPC. 
		System.out.println(target); 
		if(model.getPlayer().getLocation().getNPCs().containsKey(target)) {
			// The target exists as an NPC
			NPC tar = model.getPlayer().getLocation().getNPCs().get(target); 
			if(model.isInCombat()) {
				// There should be an ongoing combat in this case, which will be referenced in model. 
				// It must be the player's turn if this branch is accessed. 
				
				// We will first want to check if the player is still alive. 
				if(model.getPlayer().getStats().get("health").getRank() <= 0) {
					model.setInCombat(false);
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
						model.setInCombat(false); 
						String x = "All enemies have been slain!"; 
						model.addOutput(x);
					} else {
						// Combat is not over
						model.setPlayerTurnTaken(true);
						c.runCombat(model, model.getPlayer(), model.isPlayerTurnTaken());
					}
				}
				
			} else {
				// We will want to ensure that a combat can be initiated with this target. 
				if(tar.isCombat()) {
					// The combat will then need to start! 
					model.setInCombat(true);
					// We will need to find the combat that contains this target. 
					ArrayList<Combat> combats = model.getPlayer().getLocation().getCombats(); 
					
					// Finds the combat that contains the target. 
					for(Combat c : combats) {
						if(c.getNpcs().containsKey(target)) {
							model.setCurrentCombat(c);
							break; 
						}
					}
					
					// The combat can now be started. 
					model.setPlayerTurnTaken(false);
					String s = "Combat initiated!"; 
					model.addOutput(s);
					model.getCurrentCombat().runCombat(model, model.getPlayer(), model.isPlayerTurnTaken());
					
				} else {
					String s = tar.getName() + " cannot be fought."; 
					model.addOutput(s);
				}
				
			}
			
		} else {
			// The target is not an NPC. 
			model.addOutput(target + " is not a creature.");
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
	
	public void inventory() {
		
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
	
	
	public void puzzle(String input)
	{
		// The player cannot access/solve a puzzle while in combat. 
		if(model.isInCombat()) { 
			model.addOutput(GameController.inCombatMessage("puzzle"));
			return; 
		}
		Location loc = model.getPlayer().getLocation();
		if(input.equalsIgnoreCase("cheat") && loc.getPuzzles().size() != 0)
		{
			for(int i = 0; i < loc.getPuzzles().size();i++)
			{
				Puzzle puz = loc.getPuzzle(0);
				String s = "Puzzle " + (i+1) + " : " + puz.getAnswer();
				model.addOutput(s);
			}
			
		}
		else if(loc.getPuzzles().size() != 0)
			{
				for(int i = 0; i < loc.getPuzzles().size();i++)
				{
					Puzzle puz = loc.getPuzzle(0);
					String s = "Puzzle " + (i+1) + " : " + puz.getPrompt();
					model.addOutput(s);
				}
			}
	}
	
	public void puzzle() {
		puzzle("No input");
	}
	
	public void solve(String response)
	{
		Location loc = model.getPlayer().getLocation();
			if(loc.getPuzzles().size() != 0)
			{
					Puzzle puz = loc.getPuzzle(0);
					String s = "Uninit";
					if(!puz.isSolved()) 
					{
						if(puz.solve(response))
						{
						s = "Correct! You now have a " + puz.getReward().getName();
						puz.getLoot().giveItems(model.getPlayer());
						}
						else
						{
							s = "your answer of: '" + response + "' is incorrect";
						}
					}
					else if(puz.isSolved())
					{
						s = "You already solved this puzzle";
					}
					
					model.addOutput(s);
			}
	}
	
	// Certain commands cannot be taken while in combat. This method is called in such a case
	// when the player attempts certain actions while in combat to inform them that they cannot.
	public static String inCombatMessage(String command) {
		
		return "The command " + command + " cannot be used in combat.";
		
	}
	
}
