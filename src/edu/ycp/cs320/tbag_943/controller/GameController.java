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
	
	public void giveXp() {
		Player player = model.getPlayer();
		player.setXp(20);
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
		model.getPlayer().getWinCondition().setComplete(true);
	}
	
	public void inventory() {
		String s = "";
		Set<String> set = model.getPlayer().getInventory().keySet();
		if(!set.isEmpty()) 
		{
			for(String key : set) 
			{
				String itemName = model.getPlayer().getInventory().get(key).getName();
				s = s + itemName + ", ";
			}
			s = s.substring(0,s.length()-2);
			model.addOutput("You currently have: " + s);
		}
		else
		{
			model.addOutput("Your inventory is empty");
		}
	}
	
	public void look()
	{
		model.addOutput("Description:");
		model.addOutput(model.getPlayer().getLocation().getLongDescription());
		
		try
		{
			String itemName = model.getPlayer().getLocation().getTreasure().getItem().getName();
			if(!itemName.equals(""))
			{
				model.addOutput("Item: " + model.getPlayer().getLocation().getTreasure().getItem().getDes());
			}
		}
		catch(Exception ie){
			model.addOutput("There are no items here.");
		}
		
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
						s = "Correct! You now have a " + puz.getReward().getName();
						puz.getLoot().giveItems(model.getPlayer());
						}
						else
						{
							s = "your answer of: '" + response + "' is incorrect";
						}
					}
					else if(!puz.isSolved() 
							&& puz.getBreakable()) 
					{
						if(puz.solve(response) && puz.checkRequiredSkill(model.getPlayer().getStats().get(puz.getRequiredSkill().getName())))
						{
						s = "You demonstrated your skills and got past the obstacle!";
							if(!puz.getRoomCon().equals(""))
							{
								model.getMap().getLocations().get(puz.getRoomCon()).setBlocked(false);
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
							s = "your answer of: '" + response + "' is not what you do here.";
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
