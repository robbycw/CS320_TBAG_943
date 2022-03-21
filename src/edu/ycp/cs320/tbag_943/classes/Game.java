package edu.ycp.cs320.tbag_943.classes;

import java.util.ArrayList; 


public class Game {
	private int difficulty; 
	private String saveFile; 
	private ArrayList<String> outputLog; 
	private Timer timer; 
	private Map map; 
	private Player player; 
	
	
	public Game() {
		this.difficulty = 1; 
		this.saveFile = null; 
		this.outputLog = new ArrayList<String>(); 
		this.timer = null; 
		this.map = null; 
		this.player = null; 
	}
	
	public Game(int difficulty, Map map, Player player) {
		this.difficulty = difficulty; 
		this.saveFile = null; 
		this.outputLog = new ArrayList<String>(); 
		this.timer = new Timer(); 
		this.map = map; 
		this.player = player; 
	}
	
	public int getDifficulty() {
		return difficulty;
	}
	
	public String getSaveFile() {
		return saveFile;
	}
	
	public ArrayList<String> getOutputLog() {
		return outputLog;
	}
	
	public Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}
	
	public void setSaveFile(String saveFile) {
		this.saveFile = saveFile;
	}
	
	public void setOutputLog(ArrayList<String> outputLog) {
		this.outputLog = outputLog;
	}

	public void addOutput(String s) {
		outputLog.add(s);
	}
	

}
