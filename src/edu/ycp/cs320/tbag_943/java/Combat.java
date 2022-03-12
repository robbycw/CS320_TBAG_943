package edu.ycp.cs320.tbag_943.java;

// model class for GuessingGame
// only the controller should be allowed to call the set methods
// the JSP will call the "get" and "is" methods implicitly
// when the JSP specifies game.min, that gets converted to
//    a call to model.getMin()
// when the JSP specifies if(game.done), that gets converted to
//    a call to model.isDone()
public class Combat {
	private Double hitChance;
	private int damage, turn, turnOrder[], difficulty;
	private boolean dead;
	
	public Combat() {
	}
	
	public int getDifficulty() {
		return difficulty;
	}
	public void calculateTurnOrder() {
		
	}
	public double calculateAcuracy() {
		return hitChance;
	}
	public void doesHit() {
		
	}
	public int calculateDamage() {
		return damage;
	}
	public boolean isDead() {
		return dead;
	}
	public void endCombat() {
		
	}

}
