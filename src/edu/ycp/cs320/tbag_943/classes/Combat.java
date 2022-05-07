package edu.ycp.cs320.tbag_943.classes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random; 

public class Combat {
	private int turn, difficulty;
	private boolean dead;
	private ArrayList<NPC> turnOrder; 
	private HashMap<String, NPC> npcs; 
	private Random rng; 
	private int id; 

	// Constructors 
	
	public Combat() {
		this.turn = 0; 
		rng = new Random(); 
	}
	
	public Combat(ArrayList<NPC> npcs) {
		this.turn = 0; 
		rng = new Random(); 
		// Sort the turnOrder. 
		this.turnOrder = npcs; 
		Collections.sort(turnOrder);
		
		// We will want to have NPCs added to the HashMap with lowercase keys
		HashMap<String, NPC> npc = new HashMap<String, NPC>();
		for(NPC n : npcs) {
			String key = n.getName().toLowerCase(); 
			npc.put(key, n); 
		}
		this.npcs = npc; 
	}
	
	
	// Methods
	
	public void calculateTurnOrder() {
		turnOrder = new ArrayList<NPC>();
		
		// Add NPCs to turnOrder from HashMap
		turnOrder.addAll(npcs.values()); 
		
		// Sort the list. 
		Collections.sort(turnOrder);
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
		String s = "";
		
		if(roll >= tar.getStats().get("armor").getRank()) {
			// Attack hits
			 
			int damage = Combat.calculateDamage(player); 
			
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
			String x = tar.getName() + " has been slain! Good Job!... Murderer"; 
			game.addOutput(x);
			
			turnOrder.remove(tar); 
		}
	}
	
	// Loops through the turn order and has NPCs take attacks on the player. 
	public void runCombat(Game game, Player player, boolean playerTurnTaken) {
			
		Stat playerSpeed = player.getStats().get("speed");
		
		for(int i = turn; i < turnOrder.size(); i++) {
			
			NPC enemy = turnOrder.get(i); 
			System.out.println(enemy.getName() + "'s turn"); 
			// If the player's speed is greater than the current NPC's speed and the player
			// has not gone, it will be the player's turn. 
			if(playerSpeed.compareTo(turnOrder.get(i).getStats().get("speed")) > 0 && !playerTurnTaken) {
				// The next NPC's turn after Player takes their turn.
				System.out.println("Actually, player's speed is higher. They go first."); 
				playerTurnTaken = true; 
				turn = i; 
				System.out.println("Turn: " + i);
				String h = "It is now your turn!"; 
				game.addOutput(h);
				break; // We break out of the runCombat method to allow the player to take action. 
			}
			
			// Calculate and apply current Enemy's Attack
			int roll = rng.nextInt(20) + 1; 
			String s = "";
			
			if(roll >= player.getStats().get("armor").getRank()) {
				// Attack hits
				 
				int damage = Combat.calculateDamage(enemy); 
				
				// Double damage on a critical hit. 
				if(roll == 20) {
					damage = damage * 2; 
					s = "Critical hit! "; 
				}
				
				player.getStats().get("health").addToRank(-damage);
				
				s = s + enemy.getName() + " dealt " + Integer.toString(damage) + " damage with their " + enemy.getWeapon().getName(); 
				game.addOutput(s);
				
				// Check if the attack killed the player. 
				if(player.getStats().get("health").getRank() <= 0) {
					// Player is dead. End the combat. 
					// Also set the player's health to 0. 
					String end = player.getName() + " has been slain! Better luck next time. GAME OVER."; 
					game.addOutput(end);
					player.getStats().get("health").setRank(0);
					return; 
				}
			} else {
				// The attack missed. 
				s = enemy.getName() + "'s attack missed!"; 
				game.addOutput(s);
			}
			
			// Goes back to start of turnOrder turn if at the end of list. 
			if(i == turnOrder.size() - 1) {
				// We write that i = -1 because the increment will add one itself! 
				i = -1; 
				turn = 0; 
				// Checks if Player goes last and let's player go if they have not gone yet. 
				if(playerSpeed.compareTo(turnOrder.get(turnOrder.size() - 1).getStats().get("speed")) < 0) {
					// The player's turn stays false because they are the last in the round. 
					// Hence, they did not go yet for the next round. 
					playerTurnTaken = false; 
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
		
	// Getters 
	
	public HashMap<String, NPC> getNpcs() {
		return npcs;
	}
	
	public int getDifficulty() {
		return difficulty;
	}
	
	public int getId() {
		return id;
	}

	public int getTurn() {
		return turn;
	}
	

	// Setters 
	
	public void setNpcs(HashMap<String, NPC> npcs) {
		this.npcs = npcs;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}

	
}
