package edu.ycp.cs320.tbag_943.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import edu.ycp.cs320.tbag_943.classes.*;
import java.math.*;

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
	
	public void giveXp(int xp) {
		Player player = model.getPlayer();
		player.setXp(xp);
		String s = player.getXp() + " xp"; 
		model.addOutput(s);
		
	}
	
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
			// Check if the player has input the name of a room. 
			// Also check if that room is discovered. This is legal
			// thanks to short-circuiting booleans! 
			if(map.getLocations().keySet().contains(direction) && 
					!map.getLocations().get(direction).isHidden()) {
				
				String start = player.getLocation().getName(); 
				
				// Move the player to this location. 
				Location end = map.getLocations().get(direction); 
				player.move(end);
				
				String move = "Player has moved from " + start + " to " + end.getName(); 
				model.addOutput(move);
				
				return; 
			}
			
			 
			
			// Since the room exists and is discovered, move the player to that room. 
			String err = "Invalid move."; 
			model.addOutput(err);
		}
	}
	
	public static void doMove(Game model, Map map, Player player, int direction) {
		String start = player.getLocation().getName();
		ArrayList<String> connections = map.getConnections().get(start.toLowerCase()); 
		
		// The player can move here.  
		if(!connections.get(direction).equals("-1")) { 
			
			Location end = map.getLocations().get(connections.get(direction).toLowerCase()); 
			if(!end.getBlocked()) {
				player.move(end); 
				
				String desc = player.getName() + " has moved from " + start + " to " + end.getName(); 
				model.addOutput(desc);
				
				// Print the room description
				model.addOutput(player.getLocation().getDescription());
			}
			else {
				model.addOutput("This direction is blocked. Look for a way past it...");
			}
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
				
			} else {
				// We will want to ensure that a combat can be initiated with this target. 
				if(tar.isCombat()) {
					// The combat will then need to start! 
					model.setInCombat(true);
					// We will need to find the combat that contains this target. 
					ArrayList<Combat> combats = model.getPlayer().getLocation().getCombats(); 
					
					if(combats.isEmpty()) {
						// There are no combats in this location, so they can't fight the NPC here. 
						model.addOutput("You cannot fight this enemy here.");
						model.setInCombat(false);
						return; 
					}
					
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
					
					// Compute the Turn Order for the combat. 
					model.getCurrentCombat().calculateTurnOrder();
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
		String s = "The commands you may input: Move, Attack, Talk, and Collect."
				+ ""
				+ " These commands need an object directly after being stated: attack Elon Musk."
				+" However, for move you may simply state the direction"; 
		model.addOutput(s);
	}
	
	public int collect(String item) {
		Player p = model.getPlayer();
		if(p.getLocation().getTreasure().pickUpItem(p, item)) {
			String s = p.getName() + " picked up " + item + "."; 
			model.addOutput(s);
			return p.getInventory().get(item).getId();
		} else {
			model.addOutput(item + " does not exist here.");
			return -1; 
		}
	}
	
	//Gets talk targets
	public void talk() {
		String s = "";
		if(!model.getPlayer().getLocation().getNPCs().isEmpty())
		{
			s = "You can talk to: ";
			Set<String> set = model.getPlayer().getLocation().getNPCs().keySet();
			for(String key : set) 
			{
				String NPCName = model.getPlayer().getLocation().getNPCs().get(key).getName();
				s = s + NPCName + ", ";
			}
			s = s.substring(0,s.length()-2);
		}
		else
		{
			s = "There is no one here";
		}
		model.addOutput(s);
	}
	
	//Gets talk options
	public void talk(String NPCName) 
	{
		if(model.getPlayer().getLocation().getNPCs().containsKey(NPCName))
		{
			
			ArrayList<String> speeches = model.getPlayer().getLocation().getNPCs().get(NPCName).getSpeech().getSpeeches();
			if(!speeches.isEmpty() 
					|| model.getPlayer().getLocation().getNPCs().get(NPCName).canIntimidate()
					|| model.getPlayer().getLocation().getNPCs().get(NPCName).canPersuade()) 
			{
				String s = "You can say the following: ";
				model.addOutput(s);
				for(int i = 0; i < speeches.size();i++)
				{
					String op = (i+1) +". " + speeches.get(i) + "\n";
					model.addOutput(op);
				}
				if(model.getPlayer().getLocation().getNPCs().get(NPCName).canIntimidate()){
					model.addOutput(model.getPlayer().getLocation().getNPCs().get(NPCName).getSpeech().getIntimOp() + " [INTIMIDATION]\n");
				}
				if(model.getPlayer().getLocation().getNPCs().get(NPCName).canPersuade()) {
					model.addOutput(model.getPlayer().getLocation().getNPCs().get(NPCName).getSpeech().getPersOp() + " [PERSUASION]\n");
				}
				
				s ="Type a number:";
				model.addOutput(s);
			}
			else{
				model.addOutput("'I have nothing to say to you.'");
			}
		}
		else{
			model.addOutput("There is no NPC by that name here...");
		}
	}
	
	//gets talk response
	public void talk(String NPCName, String option) {
		
		int choice = 100;
		try {
		choice = Integer.parseInt(option);
		Math.abs(choice);
		}
		catch(Exception ie) {
			model.addOutput("Incorrect Syntax: talk [target name] [option #]");
			return;
		}
		
		if(model.getPlayer().getLocation().getNPCs().containsKey(NPCName)){
			if(model.getPlayer().getLocation().getNPCs().get(NPCName).getSpeech().getResponses().size()>choice-1){
				model.addOutput("Response: " + model.getPlayer().getLocation().getNPCs().get(NPCName).getSpeech().getResponse(choice-1));
			}
			else{
				model.addOutput("That is not a valid response.");
			}
		}
		else{
			model.addOutput("There is no NPC by that name here...");
		}
	}
	
	public void intimidate(String NPCName)
	{
		if(model.getPlayer().getLocation().getNPCs().containsKey(NPCName)){
			if(model.getPlayer().getLocation().getNPCs().get(NPCName).isIntimidated()){
				model.addOutput("[SUCCESS] " + model.getPlayer().getLocation().getNPCs().get(NPCName).getSpeech().getIntimRes());
				giveXp(30);
			}
			else if(!model.getPlayer().getLocation().getNPCs().get(NPCName).canIntimidate()){
				model.addOutput("This target cannot be intimidated");
			}
			else if(model.getPlayer().getLocation().getNPCs().get(NPCName).canIntimidate() 
					&& !model.getPlayer().getLocation().getNPCs().get(NPCName).isIntimidated()){
				model.addOutput("[FAILURE] " + model.getPlayer().getLocation().getNPCs().get(NPCName).getSpeech().getIntimResFail());
			}
		}
		else{
			model.addOutput("There is no NPC by that name here...");
		}
	}
	
	public void persuade(String NPCName)
	{
		if(model.getPlayer().getLocation().getNPCs().containsKey(NPCName)){
			if(model.getPlayer().getLocation().getNPCs().get(NPCName).isPersuaded()){
				model.addOutput("[SUCCESS] " + model.getPlayer().getLocation().getNPCs().get(NPCName).getSpeech().getPersRes());
				giveXp(30);
			}
			else if(!model.getPlayer().getLocation().getNPCs().get(NPCName).canPersuade()){
				model.addOutput("This target cannot be intimidated");
			}
			else if(model.getPlayer().getLocation().getNPCs().get(NPCName).canPersuade() 
					&& !model.getPlayer().getLocation().getNPCs().get(NPCName).isPersuaded()){
				model.addOutput("[FAILURE] " + model.getPlayer().getLocation().getNPCs().get(NPCName).getSpeech().getPersResFail());
			}
		}
		else{
			model.addOutput("There is no NPC by that name here...");
		}
	}
	
	public void win(){
		model.getPlayer().getWinCondition().setBestCase(true);
		model.getPlayer().getWinCondition().setDefaultCase(false);
		System.out.println("Best Case: " + model.getPlayer().getWinCondition().getBestCase());
		System.out.println("Default Case: " + model.getPlayer().getWinCondition().getDefaultCase());
	}
	
	public void lose() {
		model.getPlayer().getWinCondition().setLost(true);
		model.getPlayer().getWinCondition().setDefaultCase(false);
	}
	
	public void inventory() {
		String s = "";
		Set<String> set = model.getPlayer().getInventory().keySet();
		if(!set.isEmpty()) 
		{
			for(String key : set) 
			{
				String itemName = model.getPlayer().getInventory().get(key).getName();
				s = s + itemName + " (" + model.getPlayer().getInventory().get(key).getAmount() + ")" + ", ";
			}
			s = s.substring(0, s.length() - 2);
			model.addOutput("You currently have: " + s);
		}
		else
		{
			model.addOutput("Your inventory is empty");
		}
	}
	
	public void look()
	{
		// Print Long Description: 
		model.addOutput("Description:");
		model.addOutput(model.getPlayer().getLocation().getLongDescription());
		giveXp(10);
		
		// Print Item names
		Set<String> itemNames = model.getPlayer().getLocation().getTreasure().getItems().keySet();
		if(!itemNames.isEmpty())
		{
			model.addOutput("Item:");
			for(String name : itemNames)
			{
				model.addOutput("\n" + name);
			}
		}
		else{
			model.addOutput("There are no items here.");
		}
		
		// Print NPC Names
		Set<String> npcNames = model.getPlayer().getLocation().getNPCs().keySet();
		if(!npcNames.isEmpty())
		{
			String s = "";
			for(String key : npcNames)
			{
				String i = model.getPlayer().getLocation().getNPCs().get(key).getName();
				s = s + i + ", ";
			}
			s = s.substring(0, s.length()-2);
			if(npcNames.size() == 1) {
			model.addOutput("NPC: " + s + " is in this room.");
			}
			else if(npcNames.size()>1) {
			model.addOutput("NPC's: " + s + " are in this room.");
			}
		}
		else if(npcNames.isEmpty())
		{
			model.addOutput("There are no people in this room");
		}
		
		// Print Connections in current location
		ArrayList<String> connections = model.getMap().getConnections().get(model.getPlayer().getLocation().getName().toLowerCase()); 
		HashMap<String, Location> locations = model.getMap().getLocations(); 
		for(int i = 0; i <= 3; i++) {
			// Set direction
			String direction; 
			switch(i) {
			case 0:
				direction = "north"; 
				break;
			case 1:
				direction = "east"; 
				break;
			case 2: 
				direction = "south"; 
				break; 
			case 3: 
				direction = "west"; 
				break; 
			default: 
				direction = null; 
				break; 
			}
			
			if(connections.get(i).equals("-1")) {
				// There is nothing in this direction. 
				model.addOutput("There is nothing to the " + direction);
			} else {
				// There is a room in this direction. Print its name. 
				model.addOutput("To the " + direction + " is " + locations.get(connections.get(i)));
			}
		}
		
		// Describe Extra Connection
		if(connections.get(4).equals("-1")) {
			// No extra connection.
			model.addOutput("There is no secret tunnel. How tragic.");
		} else {
			// There is an extra connection. 
			model.addOutput("There is a hidden connection to another room! Perhaps it is a way to the " + locations.get(connections.get(4)));
		}
	}
	
	public void equip(String item) {
		HashMap<String, Item> inventory = model.getPlayer().getInventory(); 
		if(inventory.containsKey(item)) {
			// Consider if item is a weapon or armor. 
			if(inventory.get(item).getIsWeapon()) {
				// Equip the weapon.
				model.getPlayer().setWeapon(item);
				String s = item + " is now equipped."; 
				model.addOutput(s);
			} else if (inventory.get(item).getIsArmor()) {
				// Equip the armor. 
				model.getPlayer().setArmor(item);
				int armorInc = inventory.get(item).getArmor(); 
				
				// Set the player's Armor stat. 
				model.getPlayer().getStats().get("armor").setRank(10 + armorInc);
				String s = item + " is now equipped."; 
				model.addOutput(s);
			} else {
				String er = item + " is not a weapon or armor.";
				model.addOutput(er);
			}
			
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
		if(input.equalsIgnoreCase("cheat") && loc.getPuzzles().size() != 0){
			for(int i = 0; i < loc.getPuzzles().size();i++)
			{
				Puzzle puz = loc.getPuzzle(i);
				String s = "Answer: Puzzle " + (i+1) + " : " + puz.getAnswer();
				model.addOutput(s);
			}
			
		}
		else if(loc.getPuzzles().size() != 0){
				for(int i = 0; i < loc.getPuzzles().size();i++)
				{
					Puzzle puz = loc.getPuzzle(i);
					String s = "Puzzle " + (i+1) + " : " + puz.getPrompt();
					model.addOutput(s);
				}
			}
	}
	
	public void puzzle() {
		puzzle("No input");
	}
	
	public void solve(String input,String response)
	{
		int choice = Integer.parseInt(input) - 1;
		Location loc = model.getPlayer().getLocation();
			if(loc.getPuzzles().size() != 0)
			{
				Puzzle puz = loc.getPuzzle(choice);
				String s = "Uninit";
				if(!puz.isSolved() 
						&& !puz.getBreakable()) 
				{
					if(puz.solve(response))
					{
						s = "Correct!";
						
						Set<String> itemNames = puz.getLoot().getItems().keySet();
						if(!itemNames.isEmpty()) {
							s = s + " You have the following new items: ";
							for(String name : itemNames) {
								s = s + "\n" + name;
							}
							puz.getLoot().giveItems(model.getPlayer());
              giveXp(100);
						}
						}
						else
						{
							s = "your answer of: '" + response + "' is incorrect";
							model.getTimer().decrementTime(300);
						}
					}
					else if(!puz.isSolved() && puz.getBreakable()) 
					{
						if(puz.solve(response) && puz.checkRequiredSkill(model.getPlayer().getStats().get(
								puz.getRequiredSkill().getName())))
						{
							s = "You demonstrated your skills and got past the obstacle!";
							if(!puz.getRoomCon().equals(""))
							{
								model.getMap().getLocations().get(puz.getRoomCon()).setBlocked(false);
								
								giveXp(50);
							}
						//puz connections

						}
						else if(puz.solve(response) && !puz.checkRequiredSkill(model.getPlayer().getStats().get("strength")))
						{
							s = "You are far too unskilled for this task. Your " + puz.getRequiredSkill().getName() + " is "
								+ model.getPlayer().getStats().get("speed").getRank() 
								+ " and must be " + puz.getRequiredSkill().getRank();
						}
						else
						{
							s = "Your answer of: '" + response + "' is not what you do here.";
						}
					}
					else if(puz.isSolved())
					{
						s = "You already solved this puzzle";
					}
					
					model.addOutput(s);
			}
	}
	
	public void use(String item) {
		// Check if Item is in inventory
		if(model.getPlayer().getInventory().containsKey(item)) {
			// Item is in inventory. 
			
			// Check the Item's type. Use only works if the item type is Consumable or Tool.
			Item i = model.getPlayer().getInventory().get(item); 
			if(i.isConsumable()) {
				
				// Apply the item's effect: Only healing/damage currently.
				// Consumables only apply to the player currently. 
				
				// Check if damage or healing. 
				if(i.getHealthGain() > 0) {
					// Item heals the player. 
					// Increase the player's health by this amount. 
					int health = model.getPlayer().getStats().get("health").getRank(); 
					model.getPlayer().getStats().get("health").setRank(health + i.getHealthGain());
					model.addOutput(model.getPlayer().getName() + " used " + i.getName() + " and healed for " + i.getHealthGain() + " hitpoints.");
				} else {
					// Item deals damage to the Player. 
					// Add the damage to the Player's health. 
					int health = model.getPlayer().getStats().get("health").getRank(); 
					model.getPlayer().getStats().get("health").setRank(health + i.getHealthGain());
					int damage = i.getHealthGain() - (2 * i.getHealthGain()); 
					model.addOutput(model.getPlayer().getName() + " used " + i.getName() + " and took " + damage + " damage!");
					
					// If health reduces to 0, set Player as dead and end game. 
					if(model.getPlayer().getStats().get("health").getRank() < 1) {
						model.addOutput(model.getPlayer().getName() + " died due to consuming " + i.getName() + "!");
					}
				}
				
				// Reduce amount of item by 1.
				i.setAmount(i.getAmount() - 1);
				model.addOutput(model.getPlayer().getName() + " has " + i.getAmount() + " " + i.getName() + "s remaining.");
				
			} else if (i.getIsTool()) {
				// Check if the tool is used in the Location's Puzzles. 
				// If so, solve the puzzle. 
				// If not, return an error. 
				
				for(Puzzle p : model.getPlayer().getLocation().getPuzzles()) {
					if(p.getRequiredItem().getName().equals(i.getName())) {
						model.addOutput(model.getPlayer().getName() + " used their " + i.getName() + 
								" to complete Puzzle #" + p.getId());
						p.setSolved(true);
						// TODO: Reward the Player's Loot (if applicable) - Needs Conrad's Update.
						
						// Add the connection (if applicable) to the current location. 
						model.getMap().getConnections().get(
								model.getPlayer().getLocation().getName()).set(4, p.getRoomCon()); 
					}
				}
				
			} else {
				// Item is a weapon or armor. 
				String s = "Error: Use command only works on items that are tools or consumables."; 
				model.addOutput(s);
				return; 
			}
			
		} else {
			
			// Item is not in inventory. 
			String s = model.getPlayer().getName() + " does not have " + item + "."; 
			model.addOutput(s);
			return; 
		}
		// Check item type
		// Consumable: use and discard
		// Tool: check if this solves a puzzle
	}
	
	// Certain commands cannot be taken while in combat. This method is called in such a case
	// when the player attempts certain actions while in combat to inform them that they cannot.
	public static String inCombatMessage(String command) {
		
		return "The command " + command + " cannot be used in combat.";
		
	}
	
}
