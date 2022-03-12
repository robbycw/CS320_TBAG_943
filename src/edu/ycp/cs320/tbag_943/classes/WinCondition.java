package edu.ycp.cs320.tbag_943.classes;


public class WinCondition {
	// Fields
	private String name;
	private boolean complete;
	
	// Constructor
	public WinCondition() {
		complete = false;
	}
	
	// Method
	public boolean winCondition() {
		if(complete == true) {
			return true;
		} else {
			return false;
		}
	}
	
	// Getters
	public String getName() {
		return name;
	}
	
	public boolean getComplete() {
		return complete;
	}
	
	// Setters
	public void setName(String newName) {
		name = newName;
	}
	
	public void setComplete(boolean value) {
		complete = value;
	}
}
