package edu.ycp.cs320.tbag_943.model;

public class Stat {

	private int rank;
	private String name; 
	
	public Stat(String name) {
		
		this.name = name;
		this.rank = 0;
	}
	
	public Stat(String name, int rank) {
		
		this.name = name;
		this.rank = rank;
	}
	
	public int getRank() {
		return rank;
	}
	
	public void setRank(int set) {
		rank = set;
	}
	
	public void addToRank(int add) {
		rank = rank + add;
	}
	
}
