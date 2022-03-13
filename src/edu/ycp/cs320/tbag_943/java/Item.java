package edu.ycp.cs320.tbag_943.java;

// model class for GuessingGame
// only the controller should be allowed to call the set methods
// the JSP will call the "get" and "is" methods implicitly
// when the JSP specifies game.min, that gets converted to
//    a call to model.getMin()
// when the JSP specifies if(game.done), that gets converted to
//    a call to model.isDone()
public class Item {
	private boolean consumable, isWeapon;
	private int damage, healthGain, value;
	private double acuracy;
	
	public Item() {
	}
	
	public void setIcon() {
		
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	
								///consumable setter stuff//
	public void isConsumable(boolean consumable) {
		this.consumable = consumable;
	}
	public void setHealthGain(int healthGain) {
		this.healthGain = healthGain;
	}
								
	
								/// weapon setter stuff///
	public void isWeapon(boolean isWeapon) {
		this.isWeapon = isWeapon;
	}
	public void setDamage(int damage) {
		this.damage = damage;
	}
	public void setAcuracy(double acuracy) {
		this.acuracy = acuracy;
	}
	
	
								/// these are the getters /////
	public int getValue() {
		return value;
	}
	
	
								/// consumable getter stuff ///
	public boolean getIsConsumbale() {
		return consumable;
	}
	public int getHealthGain() {
		return healthGain;
	}
	
	
	
								/// weapon getter stuff ///
	public boolean getIsWeapon() {
		return isWeapon;
	}
	public int getDamage() {
		return damage;
	}
	public double getAcuracy() {
		return acuracy;
	}
	
								/// big boy methods///
	public void useItem() {
		
	}
	public boolean isItemCompatable() {
		return false;
	}
}
