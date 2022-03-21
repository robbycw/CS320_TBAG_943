package edu.ycp.cs320.tbag_943.classes;

import java.util.ArrayList;
import java.util.HashMap; 


public class Location {
	private String name; 
	private ArrayList<Puzzle> puzzles; 
	private ArrayList<Combat> combats;
	private Loot treasure;
	private HashMap<String, NPC> npcs;
	
	public Location() {
		this.name = "Start"; 
		this.puzzles = null; 
		this.combats = null; 
		this.treasure = null; 
		this.npcs = null; 
	}
	
	public Location(String name) {
		this.name = name; 
		this.puzzles = null; 
		this.combats = null; 
		this.treasure = null; 
		this.npcs = null; 
	}
	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public ArrayList<Puzzle> getPuzzles() {
		return puzzles;
	}


	public void setPuzzles(ArrayList<Puzzle> puzzles) {
		this.puzzles = puzzles;
	}


	public ArrayList<Combat> getCombats() {
		return combats;
	}


	public void setCombats(ArrayList<Combat> combats) {
		this.combats = combats;
	}


	public Loot getTreasure() {
		return treasure;
	}


	public void setTreasure(Loot treasure) {
		this.treasure = treasure;
	}


	public HashMap<String, NPC> getNPCs() {
		return npcs;
	}


	public void setNPCs(HashMap<String, NPC> npcs) {
		this.npcs = npcs;
	}
	

}
