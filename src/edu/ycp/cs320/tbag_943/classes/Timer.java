package edu.ycp.cs320.tbag_943.classes;


public class Timer {
	// Fields
	private int timeRemaining, timerRate;
	
	// Constructor
	public Timer() {
		timeRemaining = 120;
		timerRate = 1;
	}
	
	// Methods
	public void incrementTime(int value) {
		timeRemaining += value;
	}
	
	public void decrementTime(int value) {
		timeRemaining -= value;
	}
	
	public void incrementRate(int value) {
		timerRate += value;
	}
	
	// Setters
	public void setTime(int value) {
		timeRemaining = value;
	}
	
	public void setTimerRate(int value) {
		timerRate = value;
	}
	
	// Getters
	public int getTime() {
		return timeRemaining;
	}
	
	public int getTimerRate() {
		return timerRate;
	}
}
