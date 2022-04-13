package edu.ycp.cs320.tbag_943.classes;

public class Stat implements Comparable {

	private int rank;
	private String name; 
	private int id; 
	
	public Stat(String name) {
		
		this.name = name;
		this.rank = 0;
	}
	
	public Stat(String name, int rank) {
		
		this.name = name;
		this.rank = rank;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	@Override
	public int compareTo(Object stat) {

		// We will assume a Stat object is passed. 
		Stat s = (Stat) stat; 
		
		if(this.rank > s.getRank()) {
			return 1; 
		} else if (this.rank < s.getRank()) {
			return -1; 
		} else {
			return 0; 
		}
	}
	
}
