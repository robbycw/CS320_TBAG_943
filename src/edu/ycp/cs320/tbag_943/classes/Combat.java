package edu.ycp.cs320.tbag_943.classes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random; 

public class Combat {
	private Double hitChance;
	private int damage, turn, difficulty;
	private boolean dead;
	private ArrayList<NPC> turnOrder; 
	private HashMap<String, NPC> npcs; 
	private Random rng; 

	public Combat() {
		this.turn = 0; 
		rng = new Random(); 
	}
	
	public int getDifficulty() {
		return difficulty;
	}
	
	public void calculateTurnOrder() {
		Collections.sort(turnOrder);
	}
	
	public double calculateAccuracy() {
		return hitChance;
	}
	
	public void doesHit() {
		
	}
	
	public static int calculateDamage(NPC npc) {
		int d = npc.getWeapon().getDamage() + npc.getStats().get("strength").getRank();
		return d;
	}
	
	public static int calculateDamage(Player player) {
		Item weapon = player.getInventory().get(player.getWeapon());
		int d = weapon.getDamage() + player.getStats().get("strength").getRank();
		return d;
	}
	
	public boolean isDead() {
		for(NPC n : npcs.values()) {
			if(n.isAlive()) {
				return false; 
			}
		}
		return true; 
	}
	
	// Method called by Controller when player takes their attack. 
	public void playerAttack(Game game, Player player, String target) {
		NPC tar = game.getCurrentCombat().getNpcs().get(target); 
		
		// Apply attack to target if it hits!
		
		// Calculate and apply Player's Attack
		int roll = rng.nextInt(20) + 1; 
		String s = null;
		
		if(roll >= tar.getStats().get("armor").getRank()) {
			// Attack hits
			 
			damage = Combat.calculateDamage(player); 
			
			// Double damage on a critical hit. 
			if(roll == 20) {
				damage = damage * 2; 
				s = "Critical hit! "; 
			}
			
			player.getStats().get("health").addToRank(-damage);
			
			s = s + player.getName() + " dealt " + Integer.toString(damage) + " damage with their " + player.getWeapon(); 
			game.addOutput(s);
		} else {
			s = player.getName() + "'s attack missed!"; 
			game.addOutput(s);
		}
		
		if(!tar.isAlive()) {
			
			// Will need to remove target from combat upon death and inform Player
			String x = tar.getName() + " has been slain!"; 
			game.addOutput(x);
			
			turnOrder.remove(tar); 
		}
	}
	
	public HashMap<String, NPC> getNpcs() {
		return npcs;
	}

	public void setNpcs(HashMap<String, NPC> npcs) {
		this.npcs = npcs;
	}
	
	// Loops through the turn order and has NPCs take attacks on the player. 
	public void runCombat(Game game, Player player, boolean playerTurnTaken) {
		
		Stat playerSpeed = player.getStats().get("speed");
		
		for(int i = turn; i < turnOrder.size(); i++) {
			
			NPC enemy = turnOrder.get(i); 
			// If the player's speed is greater than the current NPC's speed and the player
			// has not gone, it will be the player's turn. 
			if(playerSpeed.compareTo(turnOrder.get(i).getStats().get("speed")) > 0 && !playerTurnTaken) {
				// The next NPC's turn after Player takes their turn. 
				playerTurnTaken = true; 
				turn = i; 
				String h = "It is now your turn!"; 
				game.addOutput(h);
				break; // We break out of the runCombat method to allow the player to take action. 
			}
			
			// Calculate and apply current Enemy's Attack
			int roll = rng.nextInt(20) + 1; 
			String s = null;
			
			if(roll >= player.getStats().get("armor").getRank()) {
				// Attack hits
				 
				damage = Combat.calculateDamage(enemy); 
				
				// Double damage on a critical hit. 
				if(roll == 20) {
					damage = damage * 2; 
					s = "Critical hit! "; 
				}
				
				player.getStats().get("health").addToRank(-damage);
				
				s = s + enemy.getName() + " dealt " + Integer.toString(damage) + " damage with their " + enemy.getWeapon().getName(); 
				game.addOutput(s);
			} else {
				s = enemy.getName() + "'s attack missed!"; 
				game.addOutput(s);
			}
			
			// Goes back to start of turnOrder turn if at the end of list. 
			if(i == turnOrder.size() - 1) {
				i = 0; 
				turn = 0; 
				// Checks if Player goes last and let's player go if they have not gone yet. 
				if(playerSpeed.compareTo(turnOrder.get(turnOrder.size() - 1).getStats().get("speed")) > 0 && !playerTurnTaken) {
					playerTurnTaken = true; 
					String h = "It is now your turn!"; 
					game.addOutput(h);
					break;
				} else {
					// Otherwise, the player should take their turn next round. 
					playerTurnTaken = false; 
				}
			}
		}
	}
	
	public void endCombat() {
		
	}

}
