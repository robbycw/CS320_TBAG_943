package edu.ycp.cs320.tbag_943.classes;

import java.util.ArrayList;

public class NPC {

	private String name; 
	private String[] attackMoves;
	private int health;
	private boolean combat;
	
	public NPC(String name, int health, boolean combat) {
		this.health = health;
		this.combat = combat;
		this.name = name;
	}
	
	public String getAttackMove(int get) {
		
		return attackMoves[get];
		
	}
	
	public void setAttackMove(int set, String attackAdd) {
		
		attackMoves[set] = attackAdd;
		
	}
	
	public boolean isAlive(int get) {
		if(health > 0) {
		return true;}
		else {return false;}
		
	}
	
}
