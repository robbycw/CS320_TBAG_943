package edu.ycp.cs320.tbag_943.classes;


public class WinCondition {
	// Fields
	private boolean complete;
	private boolean lost;
	private boolean wonRooms;
	private boolean bestCase;
	
	// Constructor
	public WinCondition() {
		complete = false;
		lost = true;
		wonRooms = false;
		bestCase = false;
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
		} else {
			return "lost";
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
}
