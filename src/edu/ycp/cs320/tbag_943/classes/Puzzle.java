package edu.ycp.cs320.tbag_943.classes;


public class Puzzle {
	// Fields
	private String prompt;
	private int requiredSkill, requiredItem;
	private boolean result;
	
	// Constructor
	public Puzzle() {
		result = false;
	}
	
	// Methods
	public boolean calculateResult() {
		if(result == true) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean checkRequiredSkill(int skillValue) {
		if(requiredSkill >= skillValue) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean checkRequiredItem(int itemValue) {
		if(requiredItem >= itemValue) {
			return true;
		} else {
			return false;
		}
	}
	
	// Getters
	public String getPrompt() {
		return prompt;
	}
	
	public int getRequiredSkill() {
		return requiredSkill;
	}
	
	public int getRequiredItem() {
		return requiredItem;
	}
	
	public boolean getResult() {
		return result;
	}
	
	//Setters
	public void setPrompt(String newPrompt) {
		prompt = newPrompt;
	}
	
	public void setRequiredSkill(int skill) {
		requiredSkill = skill;
	}
	
	public void setRequiredItem(int item) {
		requiredItem = item;
	}
	
	public void setResult(boolean newResult) {
		result = newResult;
	}
}
