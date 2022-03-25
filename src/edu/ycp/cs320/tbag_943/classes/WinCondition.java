package edu.ycp.cs320.tbag_943.classes;


public class WinCondition {
	// Fields
	
	// complete - Player beats a room
	// lost - Player runs out of time
	// wonRooms - Player beats all rooms
	// bestCase - Player beats all rooms and solves mystery
	// defaultCase - True until one of the other win conditions is triggered
	private boolean complete;
	private boolean lost;
	private boolean wonRooms;
	private boolean bestCase;
	private boolean defaultCase;
	
	// Constructor
	public WinCondition() {
		complete = false;
		lost = false;
		wonRooms = false;
		bestCase = false;
		defaultCase = true;
	}
	
	// Method
	public boolean winCondition() {
		if(complete == true) {
			return true;
		} else {
			return false;
		}
	}
	
	public String currentWinCondition() {
		if(complete == true) {
			return "complete";
		} else if(wonRooms == true) {
			return "wonRooms";
		} else if(bestCase == true) {
			return "bestCase";
		} else if(lost == true){
			return "lost";
		} else {
			return "defaultCase";
		}
	}
	
	// Getters
	public boolean getComplete() {
		return complete;
	}
	
	public boolean getLost() {
		return lost;
	}
	
	public boolean getWonRooms() {
		return wonRooms;
	}
	
	public boolean getBestCase() {
		return bestCase;
	}
	
	public boolean getDefaultCase() {
		return defaultCase;
	}
	
	// Setters
	public void setComplete(boolean value) {
		complete = value;
	}
	
	public void setLost(boolean value) {
		lost = value;
	}
	
	public void setWonRooms(boolean value) {
		wonRooms = value;
	}
	
	public void setBestCase(boolean value) {
		bestCase = value;
	}
	
	public void setDefaultCase(boolean value) {
		defaultCase = value;
	}
}
