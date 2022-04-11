package edu.ycp.cs320.tbag_943.classes;


public class Puzzle {
	// Fields
	private String prompt;
	private Stat requiredSkill;
	private Item requiredItem;
	private boolean result;
	private String answer;
	private Loot loot;
	private boolean canSolve;
	private boolean solved;
	private boolean breakable;
	private boolean jumpable;
	
	// Constructors
	public Puzzle() {
		result = false;
		canSolve = true;
	}
	
	public Puzzle(String promptIn, String answerIn) {
		result = false;
		canSolve = true;
		prompt = promptIn;
		answer = answerIn;
	}
	
	// Methods
	public boolean solve(String response) {
		if(response.equalsIgnoreCase(answer) && canSolve == true) {
			solved = true;
			return true;
		} else {
			solved = false;
			return false;
		}
	}
	
	public boolean checkRequiredSkill(Stat skill) {
		if(requiredSkill.getRank() <= skill.getRank()) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean checkRequiredItem(Item item) {
		if(requiredItem.equals(item)) {
			return true;
		} else {
			return false;
		}
	}
	
	// Getters
	public String getPrompt() {
		return prompt;
	}
	
	public String getAnswer() {
		return answer;
	}
	
	public Stat getRequiredSkill() {
		return requiredSkill;
	}
	
	public Item getRequiredItem() {
		return requiredItem;
	}
	
	public boolean getResult() {
		return result;
	}
	
	public Item getReward() {
		return loot.getItem();
	}
	
	public Loot getLoot() {
		return loot;
	}
	
	public boolean isSolved() {
		return solved;
	}
	
	//Setters
	public void setPrompt(String newPrompt) {
		prompt = newPrompt;
	}
	
	public void setAnswer(String answerIn)
	{
		answer = answerIn;
	}
	
	public void setRequiredSkill(Stat skill) {
		requiredSkill = skill;
	}
	
	public void setRequiredItem(Item item) {
		requiredItem = item;
	}
	
	public void setResult(boolean newResult) {
		result = newResult;
	}
	
	public void setLoot(Loot lootIn)
	{
		loot = lootIn;
	}
	
	public boolean getBreakable()
	{
		return breakable;
	}
	
	public void setBreakable(boolean bool)
	{
		breakable = bool;
	}
	
	public boolean getJumpable()
	{
		return jumpable;
	}
	
	public void setJumpable(boolean bool)
	{
		jumpable = bool;
	}
}
