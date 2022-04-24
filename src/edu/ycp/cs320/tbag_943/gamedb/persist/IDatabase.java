package edu.ycp.cs320.tbag_943.gamedb.persist;

import java.util.List;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import edu.ycp.cs320.tbag_943.classes.*;

//Code comes from CS320 Library Example. 
public interface IDatabase {
	
	//
	// Find Queries
	//
	
	public User findUserByUsernameAndPassword(String username, String password);
	public List<Game> findGamesByUserID(int userID);
	public Game findGameByGameID(int gameId);
	public Map findMapByMapID(int mapId);
	public Location findLocationByLocationID(int locationId);
	public List<Integer> findNPCIdsByLocationID(int locationID);
	public NPC findNPCByNPCId(int NPCId);
	public HashMap<String, Stat> findNPCStatsByNPCID(int npcID);
	public Speech findSpeechBySpeechId(int speechId);
	public ArrayList<String> findSpeechOptionsBySpeechId(int speechId);
	public ArrayList<String> findSpeechResponsesBySpeechId(int speechId);
	public List<Integer> findNPCsIdByCombatID(int combatID);
	public Loot findLootByLocationID(int locationID);
	public Loot findLootByLootID(int lootID);
	public Item findItemByItemID(int itemId);
	public ArrayList<Integer> findCombatIdsByLocationID(int locationId);
	public Combat findCombatsByCombatID(int combatId);
	public List<Integer> findPuzzleIdsByLocationID(int locationId);
	public Puzzle findPuzzleByPuzzleId(int puzzle_id);
	public WinCondition findWinConditionByWinConditionId(int winCondition_id);
	public Player findPlayerByPlayerId(int playerId, Collection<Location> locations);
	public HashMap<String, Stat> findPlayerStatsByPlayerID(int playerID);
	//public Stat findPlayerStatByStatId(int statId);
	public List<Integer> findPlayerInventoryIdsByPlayerId(int playerID);
	public ArrayList<String> findGameLogByGameLogId(int gameLogId);
	

	//
	// Insert Queries
	//	
	
	public Integer insertNewUserByUsernameAndPassword(String username, String password);
	public Integer insertUserToGameByUserIdAndGameId(int userId, int gameId);
	public Integer insertNewUser(List<User> users);
	public Integer insertNewGame(Game game, int gameMax);
	public Integer insertNewItem(List<Item> items, int itemMaxId);
	public Integer insertNewWinConditions(List<WinCondition> winConditions, int WCMaxID);
	public Integer insertNewLoot(List<Loot> loots, int lootMaxId);
	public Integer insertNewPuzzle(List<Puzzle> puzzles, int puzzleMaxId, int statMax, int itemMax);
	public Integer insertNewCombat(List<Combat> combats, int combatMax);
	public Integer insertNewSpeech(List<Speech> speeches);
	public Integer insertNewSpeechOption(HashMap<Integer, ArrayList<String>> speechOptions);
	public Integer insertNewSpeechResponse(HashMap<Integer, ArrayList<String>> speechOptions);
	public Integer insertNewNPCs(List<NPC> npcList, int npcMaxId, int itemMaxId);
	public Integer InsertNewNPCStats(List<Stat> npcStats, int npcStatsMaxId);
	public Integer insertNewPlayerStats(List<Stat> playerStats, int playerStatsMaxId);
	public Integer insertNewLocations(List<Location> locations, int locationMaxId, int lootMaxId, 
			int winConditionMaxId);
	public Integer insertNewMap(Map map, int game_rows, int location_rows);
	public Integer insertNewPlayer(Player player, int loc_rows, int game_rows);
	public Integer insertNewGameLog(ArrayList<String> log, int game_rows);
	public Integer insertNewPlayerToStats(List<Pair<Integer, Integer>> playerToStats, 
			int player_rows, int playerstat_rows);
	public Integer insertNewPlayerInventory(Player player, int player_rows, int item_rows);
	public Integer InsertNewLocationToNPC(List<Pair<Integer, Integer>> locationToNPC, 
			int maxLocationId, int maxNPCId);
	public Integer InsertNewNPCToStats(List<Pair<Integer, Integer>> NPCToStats, 
			int maxNPCId, int maxNPCStatsId);
	public Integer InsertNewCombatToNPC(List<Pair<Integer, Integer>> CombatToNPC, int maxCombatId, 
			int maxNPCId);
	public Integer insertNewLocationToCombat(List<Pair<Integer, Integer>> ltcPairs, int location_rows, 
			int combat_rows);
	public Integer insertNewLocationToPuzzle(List<Pair<Integer, Integer>> ltpPairs, int location_rows, 
			int puzzle_rows);
	public Integer insertNewUserToGame(List<Pair<Integer, Integer>> utgPairs);
	public Integer insertOutputIntoGameLogByLogId(String output, int log_id, int log_size);
	public Integer insertItemIntoPlayerInventoryByPlayerIdAndItemId(int player_id, int item_id);
	
	
	//
	// Update Queries
	//
	
	public Boolean updateGameByGameId(Game game, Location previous);
	public Boolean updatePlayerByPlayerId(Player player);
	public Boolean updatePlayerStatByStatId(Stat stat);
	//public Boolean updateMapByMapId(Map map);	
	public Boolean updateLocationByLocationId(Location location);
	public Boolean UpdateNPCByNPCId(NPC npc);
	public Boolean updateNPCStatsByStatId(Stat stat);
	public boolean updateCombatByCombatId(Combat combat);
	public boolean updatePuzzleByPuzzleId(Puzzle puzzle);
	public boolean updateWinConditionByWinConditionId(WinCondition winCondition);
	public boolean updateLootByLootId(Loot loot);
	
	//
	// Removal Queries
	//
	
	public boolean removeItemFromInventoryByItemIdAndPlayerId(int itemID, int playerID);
	
	// 
	// Other Queries
	// 
	
	public int getLargestIdInTable(String table, String idName);
	public void createNewGame(int user_id);

	
	/*
	
	// Finds
	public User findUserByUsernameAndPassword(String username, String password); 
	public List<Game> findGamesByUserID(int userID);
	public List<NPC> findNPCsIdsByLocationID(int locationID); 
	public NPC findNPCByNPCId(int NPCId);
	public List<Integer> findNPCStatsIdsByNPCId(int NPCId);
	public Stat findNPCStatByStatId(int StatId);
	public List<Integer> findNPCsIdByCombatID(int combatID);
	public Loot findLootByLocationID(int locationID);
	public ArrayList<Integer> findCombatIdsByLocationID(int locationId);
	public Combat findCombatsByCombatID(int combatId);
	public Player findPlayerByPlayerId(int playerId);
	public Loot findLootByLootID(int lootID);
	public Stat findPlayerStatByStatId(int statId);
	public ArrayList<String> findGameLogByGameLogId(int gameLogId);
	public Item findItemByItemID(int itemId);
	public Speech findSpeechBySpeechId(int speechId);
	public ArrayList<String> findSpeechOptionsBySpeechId(int speechId);
	public ArrayList<String> findSpeechResponsesBySpeechId(int speechId);
	public Speech findSpeechByNPCId(int npcId);
	public HashMap<String, Item> findInventoryByPlayerID(int playerID);
	public HashMap<String, Stat> findPlayerStatsByPlayerID(int playerID);
	public HashMap<String, Stat> findNPCStatsByNPCID(int npcID);
	public List<Integer> findPlayerStatIdsByPlayerId(int playerID);
	public List<Integer> findPlayerInventoryIdsByPlayerId(int playerID);
	public Map findMapByMapID(int mapId);
	public Location findLocationByLocationID(int locationId);
	public List<Integer> findPuzzleIdsByLocationID(int locationId);
	public Puzzle findPuzzleByPuzzleId(int puzzle_id);
	public WinCondition findWinConditionByWinConditionId(int winCondition_id);

	public HashMap<String, ArrayList<String>> findConnectionsByMapID(int mapID);
 
	public List<Integer> findPlayerInventoryIdsByPlayerID(int playerID); 
	public List<Integer> findPlayerStatsIdsByPlayerID(int playerID); 
	
	
	// Inserts
	public Integer insertNewNPCs(List<NPC> npcList, int loc_rows, int game_rows);
	public Integer insertNewPlayer(Player player, int loc_rows, int game_rows);
	public Integer InsertNewNPCStats(NPC npc, int npc_rows, int npcStat_rows);
	public Integer insertNewPlayerToStats(Player player, int player_rows, int playerstat_rows);
	public Integer insertNewPlayerInventory(Player player, int player_rows, int playerInventory_rows);
	public Integer insertNewMap(Map map, int game_rows, int location_rows);
	public Integer insertNewGameLog(ArrayList<String> log, int game_rows);
	
	public Integer insertNewWinConditions(WinCondition winCondition);
	public Integer insertNewPlayerStats(Stat playerStats);
	
	public Integer insertNewLocations(Location location);
	public Integer insertNewPlayer(Item item);
	public Integer insertNewLoot(Loot loot);
	public Integer insertNewPuzzle(Puzzle puzzle);
	public Integer insertNewSpeech(Speech speech);
	public Integer insertNewSpeechOption(Speech speech, int speechOp);
	public Integer insertNewSpeechResponse(Speech speech, int speechOp);

	public Integer InsertNewLocationToNPC(NPC npc);
	public Integer InsertNewCombatToNPC(NPC npc);
	
		// This method will need to fetch the most recently added player and log
		// IDs when the game is first inserted (saved)
	public Integer insertGameIntoGameTable(Game game); 
	

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
	
	
	
	// Updates
	// We could get away with only implementing updateGame, as Game
	// already contains all of these classes. However, we need to 
	// keep in mind that doing so will require the same queries that
	// we would use in the other updates, as we need to change each 
	// table directly, not just the Game table. We could probably 
	// just have updateGame call all update methods after they are all
	// implemented. 
	
	// updatePlayer will need to include update to Inventory Table.
	public Boolean UpdateNPCByNPCId(NPC npc);
	public boolean updateCombatByCombatId(Combat combat);
	public boolean updatePuzzleByPuzzleId(Puzzle puzzle);
	public boolean updateWinConditionByWinConditionId(WinCondition winCondition);
	public Boolean updateNPCStatsByStatId(Stat stat);
	public boolean updateGame(Game game);
	public boolean updatePlayer(Player player); 
	public boolean updateNPCs(List<NPC> npcs); 
	public boolean updatePuzzle(Puzzle puzzle);  
	public boolean updateLootByLootId(Loot loot); 
	public boolean updateLoot(Loot loot); 
	
	
	// Removals
	public boolean removeItemFromInventoryByItemIdAndPlayerId(int itemID, int playerID);
	
	public boolean removeGameByGameID(int gameID); 
	public boolean removeItemFromInventoryByItemIDAndPlayerID(int itemID, int playerID);

	
	
	
	// Other
	
	
	
	
	
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
	*/ 	
	
}
