package edu.ycp.cs320.tbag_943.classes;

import java.util.ArrayList;
import java.util.HashMap;

public class NPC implements Comparable {

	private String name; 
	private String[] attackMoves;
	private boolean combat;
	private HashMap<String, Stat> stats; 
	private Item weapon; 
	private Speech speech;
	private boolean isIntimidated;
	private boolean canIntimidate;
	private int intimidationThreshold;
	private boolean isPersuaded;
	private boolean canPersuade;
	private int persuasionThreshold;
	
	
	
	public NPC(String name, int health, boolean combat, HashMap<String, Stat> stats) {
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
		this.speech = new Speech();
	}
	
	public NPC(String name, boolean combat, Item weapon, int health, int armor, int strength, int speed) {
		this.name = name;
		this.combat = combat; 
		this.weapon = weapon; 
		this.speech = new Speech();
		
		// Generate Stats
		Stat h = new Stat("health", health); 
		Stat a = new Stat("armor", armor); 
		Stat st = new Stat("strength", strength); 
		Stat sp = new Stat("speed", speed); 
		
		HashMap<String, Stat> stat = new HashMap<String, Stat>(); 
		stat.put("health", h);
		stat.put("armor", a);
		stat.put("strength", st);
		stat.put("speed", sp); 
		this.stats = stat; 
		
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

	public void setName(String string) {
		name = string;
	}
	
	public Speech getSpeech() {
		return speech;
	}

	public void setSpeech(Speech set) {
		speech = set;
	}
	
	public boolean isIntimidated() {
		return isIntimidated;
	}

	public void setIntimidated(boolean bool) {
		isIntimidated = bool;
	}
	
	public int getIntimidationThreshold() {
		return intimidationThreshold;
	}

	public void setIntimidationThreshold(int in) {
		intimidationThreshold = in;
	}
	
	public boolean isPersuaded() {
		return isPersuaded;
	}

	public void setPersuaded(boolean bool) {
		isPersuaded = bool;
	}
	
	public boolean canIntimidate() {
		return canIntimidate;
	}

	public void setCanIntimidate(boolean bool) {
		canIntimidate = bool;
	}
	
	public boolean canPersuade() {
		return canPersuade;
	}

	public void setCanPersuade(boolean bool) {
		canPersuade = bool;
	}
	
	public int getPersuasionThreshold() {
		return persuasionThreshold;
	}

	public void setPersuasionThreshold(int in) {
		persuasionThreshold = in;
	}

	public String getAttackMove(int get) {
		return attackMoves[get];
		
	}
	
	public void setAttackMove(int set, String attackAdd) {
		
		attackMoves[set] = attackAdd;
		
	}
	
	public boolean isAlive() {
		if(stats.get("health").getRank() > 0) {
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
