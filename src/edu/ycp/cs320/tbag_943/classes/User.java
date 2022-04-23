package edu.ycp.cs320.tbag_943.classes;

import java.util.ArrayList;

public class User {

	// Fields
	String username, password; 
	int id; 
	Game currentGame; 
	boolean created; 
	ArrayList<Game> gameList; 
	
	
	// Constructors
	public User() {
		this.created = false; 
		this.currentGame = null; 
		this.gameList = new ArrayList<Game>();
	}
	public User(String username, String password) {
		this.username = username; 
		this.password = password; 
		this.created = true; 
		this.gameList = new ArrayList<Game>();
	}

	
	// Getters
	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public int getId() {
		return id;
	}

	public Game getCurrentGame() {
		return currentGame;
	}
	
	public boolean getCreated() {
		return created;
	}
	
	public ArrayList<Game> getGameList() {
		return gameList;
	}
	
	
	// Setters
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setCurrentGame(Game currentGame) {
		this.currentGame = currentGame;
	}	
	
	public void setCreated(boolean created) {
		this.created = created;
	}
	
	public void setGameList(ArrayList<Game> gameList) {
		this.gameList = gameList;
	}
}
