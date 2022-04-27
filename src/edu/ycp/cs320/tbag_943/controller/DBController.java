package edu.ycp.cs320.tbag_943.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import edu.ycp.cs320.tbag_943.classes.*;
import edu.ycp.cs320.tbag_943.gamedb.persist.DatabaseProvider;
import edu.ycp.cs320.tbag_943.gamedb.persist.DerbyDatabase;
import edu.ycp.cs320.tbag_943.gamedb.persist.IDatabase;

import java.util.List;

public class DBController {
	/*
	 * This class will call methods to the database based on the use cases 
	 * involving the database. For instance, loading a saved game, saving a game,
	 * and creating an account. 
	 * 
	 * This controller should work on all servlets. 
	 * 
	 */

	// Fields
	private IDatabase db = null;
	
	 
	
	// Constructor
	public DBController() {
		// Initialize the DerbyDB Connection.
		DatabaseProvider.setInstance(new DerbyDatabase());
		db = DatabaseProvider.getInstance();
	}


	// Methods
	
	// Create an account. Returns the User ID of the created account. 
	// If the account was not created, returns -1. 
	public User createAccount(String username, String password) {
		int attempt = db.insertNewUserByUsernameAndPassword(username, password); 
		
		if(attempt == -1) {
			// The account was not created due to matching usernames. 
			return null; 
			
		} else {
			
			// Account was inserted. Login the User! 
			return login(username, password); 
		}
	}
	
	// Login to an existing account. Returns the User of the associated account,
	// or null if the username and password do not match. 
	public User login(String username, String password) {
		User user = db.findUserByUsernameAndPassword(username, password); 
		
		return user; 
	}
	
	// Returns the chosen Game for the given game_id. 
	public Game loadGame(int game_id) {
		return db.findGameByGameID(game_id);
	}
	
	// Creates a new game assigned to the user's ID. 
	// Also loads this new game for the user. 
	public Game newGame(int user_id) {
		
		int game_id = db.createNewGame(user_id);
		
		return loadGame(game_id); 
	}
	
	// Saves the given game's progress. 
	public void saveGame(Game game, Location previous) {
		db.updateGameByGameId(game, previous); 
	}
	
	// Returns a list of the user's saved games. DOES NOT FETCH ALL GAME DATA. 
	public List<Game> loadGamesInfo(int user_id) {
		return db.findGamesByUserID(user_id); 
	}
	
	// 
	public void addItemToPlayerInventory(int player_id, int item_id) {
		db.insertItemIntoPlayerInventoryByPlayerIdAndItemId(player_id, item_id); 
	}
	
	public void removeItemFromPlayerInventory(int player_id, int item_id) {
		db.removeItemFromInventoryByItemIdAndPlayerId(item_id, player_id); 
	}
	
	public void addOutputToLog(String output, int log_id, int log_size) {
		db.insertOutputIntoGameLogByLogId(output, log_id, log_size);
	}
	
}