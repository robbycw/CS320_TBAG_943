package edu.ycp.cs320.tbag_943.classes;

import java.util.ArrayList;
import java.util.HashMap;

public class NPC implements Comparable {

	private String name; 
	private String[] attackMoves;
	private int health;
	private boolean combat;
	private HashMap<String, Stat> stats; 
	private Item weapon; 
	
	public NPC(String name, int health, boolean combat, HashMap<String, Stat> stats) {
		this.health = health;
		this.combat = combat;
		this.name = name;
		this.stats = stats;
	}
	
	// This constructor is helpful for testing the calculateTurnOrder method in Combat. 
	public NPC(int speed, String name) {
		Stat sp = new Stat("speed", speed); 
		HashMap<String, Stat> st = new HashMap<String, Stat>(); 
		st.put("speed", sp); 
		this.stats = st; 
		this.name = name; 
	}
	
	public Item getWeapon() {
		return weapon;
	}

	public void setWeapon(Item weapon) {
		this.weapon = weapon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAttackMove(int get) {
		return attackMoves[get];
		
	}
	
	public void setAttackMove(int set, String attackAdd) {
		
		attackMoves[set] = attackAdd;
		
	}
	
	public boolean isAlive() {
		if(health > 0) {
			return true;
		}
		else {
			return false;
		}
		
	}
	
	public HashMap<String, Stat> getStats(){
		return stats; 
	}
	
	public boolean isCombat() {
		return combat;
	}

	public void setCombat(boolean combat) {
		this.combat = combat;
	}

	@Override
	public int compareTo(Object n) {
		NPC npc = (NPC) n; 
		
		int leftSpeed = this.stats.get("speed").getRank(); 
		int rightSpeed = npc.getStats().get("speed").getRank(); 
		
		// This compareTo method will sort NPCs from highest speed to lowest speed. 
		if(leftSpeed < rightSpeed) {
			return 1; 
		} else if (leftSpeed == rightSpeed) {
			return 0; 
		} else {
			return -1; 
		}
	}
	
}
