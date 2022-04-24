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
	public GameLog findGameLogByGameLogId(int gameLogId);
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
	
	
	// Inserts
	public Integer insertNewNPCs(List<NPC> npcList, int loc_rows, int game_rows);
	public Integer insertNewPlayer(Player player, int loc_rows, int game_rows);
	public Integer InsertNewNPCStats(NPC npc, int npc_rows, int npcStat_rows);
	public Integer insertNewPlayerToStats(Player player, int player_rows, int playerstat_rows);
	public Integer insertNewPlayerInventory(Player player, int player_rows, int playerInventory_rows);
	public Integer insertNewMap(Map map, int game_rows, int location_rows);
	public Integer insertNewGameLog(ArrayList<String> log, int game_rows);
	public Boolean UpdateNPCByNPCId(NPC npc);
	public WinCondition insertNewWinConditions(WinCondition winCondition);
	public Stat insertNewPlayerStats(Stat playerStats);
	public Boolean updateNPCStatsByStatId(Stat stat);
	public Location insertNewLocations(Location location);
	public Integer insertNewPlayer(Item item);
	public Integer insertNewLoot(Loot loot);
	public Integer insertNewPuzzle(Puzzle puzzle);
	public Integer insertNewSpeech(Speech speech);
	public Integer insertNewSpeechOption(Speech speech, int speechOp);
	public Integer insertNewSpeechResponse(Speech speech, int speechOp);
	public Integer insertNewNPCs(final String name, final int health, final boolean combat, final HashMap<String, Stat> stats);
	public Void InsertNewLocationToNPC(NPC npc);
	public Void InsertNewCombatToNPC(NPC npc);
	
		// This method will need to fetch the most recently added player and log
		// IDs when the game is first inserted (saved)
	public Integer insertGameIntoGameTable(Game game); 
	public Integer insertPlayerIDAndItemIDIntoInventoryTable(int playerID, int itemID);
	
	
	
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
	public boolean updateCombatByCombatId(Combat combat)
	public boolean updatePuzzleByPuzzleId(Puzzle puzzle);
	public boolean updateWinConditionByWinConditionId(WinCondition winCondition);
	
	public boolean updateGame(Game game);
	public boolean updateGameLog(ArrayList<String> log); 
	public boolean updatePlayer(Player player); 
	public boolean updateNPCs(List<NPC> npcs); 
	public boolean updatePuzzle(Puzzle puzzle);  
	public boolean updateLoot(Loot loot); 
	
	
	// Removals
	public boolean removeItemFromInventoryByItemIdAndPlayerId(int itemID, int playerID);
	
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
