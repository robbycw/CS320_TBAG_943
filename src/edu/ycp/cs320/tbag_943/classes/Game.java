package edu.ycp.cs320.tbag_943.classes;

import java.util.ArrayList; 

public class Game {
	private int difficulty; 
	private ArrayList<String> outputLog; 
	private Timer timer; 
	private User user;
	private Map map; 
	private Player player; 
	private Combat currentCombat; 
	private boolean inCombat, playerTurnTaken; 
	private boolean playerNotCreated;
	private int id; 
	
	// Constructors

	public Game() {
		this.difficulty = 1; 
		this.outputLog = new ArrayList<String>(); 
		this.timer = null; 
		this.map = null; 
		this.player = null; 
	}
	
	public Game(int difficulty, Map map, Player player) {
		this.difficulty = difficulty;  
		this.outputLog = new ArrayList<String>(); 
		this.timer = new Timer(); 
		this.map = map; 
		this.player = player;
		this.inCombat = false; 
		this.playerTurnTaken = false; 
	}
	
	// Getters
	
	public int getId() {
		return id;
	}
	
	public String getIdString() {
		return "Game" + Integer.toString(id); 
	}

	public boolean isInCombat() {
		return inCombat;
	}
	
	public boolean isPlayerTurnTaken() {
		return playerTurnTaken;
	}
	
	public int getDifficulty() {
		return difficulty;
	}
	
	public ArrayList<String> getOutputLog() {
		return outputLog;
	}
	
	public Timer getTimer() {
		return timer;
	}
	
	public Map getMap() {
		return map;
	}
	public User getUser() {
		return user;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Combat getCurrentCombat() {
		return currentCombat;
	}
	public boolean getPlayerNotCreated() {
		return playerNotCreated;
	}
	
	// Setters
	
	public void setId(int id) {
		this.id = id;
	}

	public void setInCombat(boolean inCombat) {
		this.inCombat = inCombat;
	}
	public void setPlayerCreated(boolean created) {
		this.playerNotCreated = created;
	}

	public void setPlayerTurnTaken(boolean playerTurnTaken) {
		this.playerTurnTaken = playerTurnTaken;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	public void setMap(Map map) {
		this.map = map;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}
	
	public void setCurrentCombat(Combat currentCombat) {
		this.currentCombat = currentCombat;
	}
	
	public void setOutputLog(ArrayList<String> outputLog) {
		this.outputLog = outputLog;
	}
	
	// Methods

	public void addOutput(String s) {
		outputLog.add(s);
	}
	
	public void determineEnding() {
		WinCondition test = new WinCondition();
		String winCondition = test.currentWinCondition();
		System.out.println(winCondition);
	}
}
