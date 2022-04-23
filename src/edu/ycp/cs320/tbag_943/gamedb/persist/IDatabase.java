package edu.ycp.cs320.tbag_943.gamedb.persist;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import edu.ycp.cs320.tbag_943.classes.*;

//Code comes from CS320 Library Example. 
public interface IDatabase {
	
	// Finds
	public User findUserByUsernameAndPassword(String username, String password); 
	public List<Game> findGamesByUserID(int userID); 
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
	public List<Integer> findPlayerInventoryIdsByPlayerID(int playerID); 
	public List<Integer> findPlayerStatsIdsByPlayerID(int playerID); 
	public HashMap<String, Stat> findNPCStatsByNPCID(int npcID); 
	
	public Item findItemByItemID(int itemID); 
	
	public List<Integer> findPlayerStatIdsByPlayerId(int playerID); 
	
	
	// Inserts
	public Integer insertPlayerIntoPlayerTable(Player player); 
	public Integer insertGameLogIntoGameLogTable(List<String> log);
	public Integer insertNewNPCs(String name, int health, boolean combat, HashMap<String, Stat> stats); 
	public Integer insertNewNPCStats(String name, int health, int armor, int strength, int speed);
	public Integer InsertNewLocationToNPC(int NPC_Id);
	public Integer InsertNewNPCToStats(int Stats_id);
		// This method will need to fetch the most recently added player and log
		// IDs when the game is first inserted (saved)
	public Integer insertGameIntoGameTable(Game game); 
	
	public Integer insertNewPlayer(Player player, int loc_rows, int game_rows); 
	public Integer insertNewMap(Map map, int game_rows, int location_rows); 
	public Integer insertNewGameLog(ArrayList<String> log, int game_rows);
	public Integer insertNewPlayerToStats(Player player, int player_rows, int playerstat_rows);
	public Integer insertNewPlayerInventory(Player player, int player_rows, int item_rows); 
	public Integer insertNewLocationToCombat(List<Pair<Integer, Integer>> ltcPairs, 
			int location_rows, int combat_rows);
	public Integer insertOutputIntoGameLogByLogId(String output, int log_id, int log_size);
	public Integer insertItemIntoPlayerInventoryByPlayerIdAndItemId(int player_id, int item_id);
	
	
	// When we go to implement insertNewGame in the SQL DB, remember that
	// creating a new game will require reading the CSVs and properly 
	// adding the Game onto the existing DB without overwriting existing data. 
	// This insert will probably replace insertGameIntoGameTable. 
	//
	// We need to consider how to use the auto-generation of IDs properly, 
	// as if the first game's ids for NPCs go from 1 to n, then the second
	// game's ids for NPCs will go from n+1 to 2n and all of these IDs need 
	// to be properly mapped in the junction table. There should be a mathe-
	// matical relationship that we can use given the current highest game_id. 
	// Perhaps just by fetching the number of rows in the UserToGame table?
	// Need # of rows in each subtable as well, starting the ID counts from 
	// (# of rows in UserToGame) * (# of rows in a given table) + 1. 
	public Integer insertNewGame(User user, Player player); 
	
	
	// Updates
	public Boolean UpdateNPCByNPCId(NPC npc);
	// We could get away with only implementing updateGame, as Game
	// already contains all of these classes. However, we need to 
	// keep in mind that doing so will require the same queries that
	// we would use in the other updates, as we need to change each 
	// table directly, not just the Game table. We could probably 
	// just have updateGame call all update methods after they are all
	// implemented. 
	
	// updatePlayer will need to include update to Inventory Table.
	public boolean updateGame(Game game);
	public boolean updatePlayer(Player player); 
	public boolean updateNPCs(List<NPC> npcs); 
	public boolean updateCombatByCombatId(Combat combat); 
	public boolean updatePuzzle(Puzzle puzzle);  
	public boolean updateLootByLootId(Loot loot); 
	
	
	// Removals
	public boolean removeGameByGameID(int gameID); 
	public boolean removeItemFromInventoryByItemIDAndPlayerID(int itemID, int playerID);

	
	
	
	// Other
	public int getNumberRowsInTable(String table); 
	
	/* Library Example: 
	// Finds
	public List<Pair<Author, Book>> findAuthorAndBookByTitle(String title);
	public List<Pair<Author, Book>> findAuthorAndBookByAuthorLastName(String lastName);
	public List<Author> findAllAuthors();
	public List<Pair<Author, Book>> findAllBooksWithAuthors();
	
	
	// Inserts 
	public Integer insertBookIntoBooksTable(String title, String isbn, int published, String lastName, String firstName);
	
	
	// Removals
	public List<Author> removeBookByTitle(String title);	
	*/ 	
}
