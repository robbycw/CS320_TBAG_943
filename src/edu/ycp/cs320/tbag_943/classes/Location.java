package edu.ycp.cs320.tbag_943.classes;

import java.util.ArrayList;
import java.util.HashMap; 


public class Location {
	private int id;
	private String name, description, longDescription; 
	private boolean hidden; 
	private ArrayList<Puzzle> puzzles; 
	private ArrayList<Combat> combats;
	private Loot treasure;
	private HashMap<String, NPC> npcs;
	private boolean blocked;
	private WinCondition winCondition; 
	

	// Constructors

	public Location() {
		this.name = "Start"; 
		this.puzzles = new ArrayList<Puzzle>(); 
		this.combats = null; 
		this.treasure = new Loot(); 
		this.npcs = new HashMap<>(); 
		this.puzzles = new ArrayList<Puzzle>();
	}
	
	public Location(String name) {
		this.name = name; 
		this.puzzles = new ArrayList<Puzzle>(); 
		this.combats = null; 
		this.treasure = null; 
		this.npcs = new HashMap<>(); 
		this.puzzles = new ArrayList<Puzzle>(); 
		this.hidden = true; 
	}
	
	// Methods
	
	public void addPuzzle(Puzzle puzzleIn)
	{
		puzzles.add(puzzleIn);
	}
	
	public void addNPC(NPC npc)
	{
		npcs.put(npc.getName(),npc);
	}
	
	// Getters 
	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}
	
	public int getId() {
		return id;
	}
	
	public String getLongDescription() {
		return longDescription;
	}

	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

	public boolean isHidden() {
		return hidden;
	}

	public ArrayList<Puzzle> getPuzzles() {
		return puzzles;
	}

	public Puzzle getPuzzle(int num)
	{
		return puzzles.get(num);
	}
	
	public HashMap<String, NPC> getNPCs() {
		return npcs;
	}

	public ArrayList<Combat> getCombats() {
		return combats;
	}

	public Loot getTreasure() {
		return treasure;
	}
	
	public WinCondition getWinCondition() {
		return winCondition;
	}


	// Setters 

	public void setWinCondition(WinCondition winCondition) {
		this.winCondition = winCondition;
	}

	public void setId(int id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setPuzzles(ArrayList<Puzzle> puzzles) {
		this.puzzles = puzzles;
	}

	public void setCombats(ArrayList<Combat> combats) {
		this.combats = combats;
	}

	public void setTreasure(Loot treasure) {
		this.treasure = treasure;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	public void setNPCs(HashMap<String, NPC> npcs) {
		this.npcs = npcs;
	}
	
	public void setNPCs(ArrayList<NPC> npcs) {
		HashMap<String, NPC> npc = new HashMap<String, NPC>();
		for(NPC n : npcs) {
			String key = n.getName().toLowerCase(); 
			npc.put(key, n); 
		}
		this.npcs = npc; 
	}

	public boolean getBlocked()
	{
		return blocked;
	}
	
	public void setBlocked(boolean boolIn)
	{
		blocked = boolIn;
	}

}
