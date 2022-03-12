package edu.ycp.cs320.tbag_943.classes;

import java.util.ArrayList; 


public class Game {
	private int difficulty; 
	private String saveFile; 
	private ArrayList<String> outputLog; 
	
	
	public Game() {
		this.difficulty = 1; 
		this.saveFile = null; 
		this.outputLog = new ArrayList<String>(); 
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
