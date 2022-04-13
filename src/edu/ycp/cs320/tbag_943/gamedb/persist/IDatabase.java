package edu.ycp.cs320.tbag_943.gamedb.persist;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import edu.ycp.cs320.booksdb.model.Author;
import edu.ycp.cs320.booksdb.model.Book;
import edu.ycp.cs320.booksdb.model.Pair;

import edu.ycp.cs320.tbag_943.classes.*;

//Code comes from CS320 Library Example. 
public interface IDatabase {
	
	// Finds
	public List<NPC> findNPCsByLocationID(int locationID); 
	public List<NPC> findNPCsByCombatID(int combatID);
	public List<Speech> findSpeechByNPCID(int npcID); 
	public List<String> findGameLogByGameID(int gameID); 
	public Player findPlayerByGameID(int gameID); 
	public List<Combat> findCombatsByLocationID(int locationID); 
	public List<Puzzle> findPuzzlesByLocationID(int locationID); 
	public Map findMapByGameID(int gameID); 
	public HashMap<String, ArrayList<String>> findConnectionsByMapID(int mapID); 
	public List<Location> findLocationsByMapID(int mapID); 
	public Loot findLootByLocationID(int locationID); 
	public List<Item> findInventoryByPlayerID(int playerID); 
	public HashMap<String, Stat> findPlayerStatsByPlayerID(int playerID); 
	public HashMap<String, Stat> findNPCStatsByNPCID(int npcID); 
	
	
	// Inserts
	public Integer insertPlayerIntoPlayerTable(Player player); 
	public Integer insertGameLogIntoGameLogTable(List<String> log);
		// This method will need to fetch the most recently added player and log
		// IDs when the game is first inserted (saved)
	public Integer insertGameIntoGameTable(Game game); 
	public Integer insertPlayerIDAndItemIDIntoInventoryTable(int playerID, int itemID);
	
	
	// Updates
	// Player update will need to include update to Inventory Table
	public boolean updatePlayer(Player player); 
	public boolean updateNPCs(List<NPC> npcs); 
	public boolean updateCombat(Combat combat);
	public boolean updatePuzzle(Puzzle puzzle); 
	public boolean updateGameLog(ArrayList<String> log); 
	public boolean updateGame(Game game); 
	public boolean updateLoot(Loot loot); 
	
	
	// Removals
	public boolean removeGameByGameID(int gameID); 
	
	
	// Library Example: 
	// Finds
	public List<Pair<Author, Book>> findAuthorAndBookByTitle(String title);
	public List<Pair<Author, Book>> findAuthorAndBookByAuthorLastName(String lastName);
	public List<Author> findAllAuthors();
	public List<Pair<Author, Book>> findAllBooksWithAuthors();
	
	
	// Inserts 
	public Integer insertBookIntoBooksTable(String title, String isbn, int published, String lastName, String firstName);
	
	
	// Removals
	public List<Author> removeBookByTitle(String title);		
}
