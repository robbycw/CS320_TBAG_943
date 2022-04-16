package edu.ycp.cs320.tbag_943.classes;

public class User {

	// Fields
	String username, password; 
	int id; 
	Game currentGame; 
	boolean created; 
	
	
	// Constructors
	public User() {
		this.created = false; 
	}
	public User(String username, String password) {
		this.username = username; 
		this.password = password; 
		this.created = true; 
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
}
