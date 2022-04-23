package edu.ycp.cs320.tbag_943.gamedb.persist;

import java.io.IOException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import edu.ycp.cs320.tbag_943.classes.*; 

//Code is based on CS320 Library Example. 
public class DerbyDatabase implements IDatabase {
	static {
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
		} catch (Exception e) {
			throw new IllegalStateException("Could not load Derby driver");
		}
	}
	
	private interface Transaction<ResultType> {
		public ResultType execute(Connection conn) throws SQLException;
	}

	private static final int MAX_ATTEMPTS = 10;
	
	
	public User findUserByUsernameAndPassword(String username, String password) {
		return executeTransaction(new Transaction<User>() {
			@Override
			public User execute(Connection conn) throws SQLException{
				PreparedStatement stmt1 = null;
				ResultSet resultSet1 = null;
				User user = new User();
				boolean found = false;
				
				try {
					stmt1 = conn.prepareStatement(
							"select User.user_id, User.username, User.created" + 
							"from User" +
							"User.username = ? and User.password = ?");
					stmt1.setString(1,username);
					stmt1.setString(2, username);
					
					
					resultSet1 = stmt1.executeQuery();
					
					while(resultSet1.next()) {
						found = true;
						
						user.setId(resultSet1.getInt(1));
						user.setUsername(resultSet1.getString(2));
						user.setCreated(resultSet1.getBoolean(3));
						
					}
					if(!found) {
						System.out.println("This user doesn't exist");
					}
					
					return user;
				}finally {
					DBUtil.closeQuietly(resultSet1);
					DBUtil.closeQuietly(stmt1);
				}
			}
		});
	}
	
	public List<Game> findGamesByUserID(int userID){
		return executeTransaction(new Transaction<List<Game>>() {
			@Override
			public List<Game> execute(Connection conn) throws SQLException{
				PreparedStatement stmt1 = null;
				ResultSet resultSet1 = null;
				Boolean found = false;
				
				try {
					stmt1 = conn.prepareStatement(
							"select UserToGame.game_id, Game.difficulty, Game.timeRemaining, Player.name " +
							"from UserToGame, Game, Player " +
							"where UserToGame.user_id = ? " +
							"and UserToGame.game_id = Game.game_id " +
							"and Game.player_id = Player.player_id");
					
					stmt1.setInt(1, userID);
					
					ArrayList<Game> userGames = new ArrayList<Game>();
					Game game = new Game();
					
					resultSet1 = stmt1.executeQuery();
					
					while (resultSet1.next()) {
						found = true;
						
						game.setId(resultSet1.getInt(1));
						game.setDifficulty(resultSet1.getInt(2));
						game.getTimer().setTime(resultSet1.getInt(3));
						game.getPlayer().setName(resultSet1.getString(4));
						userGames.add(game);
					}
					
					if(!found) {
						System.out.println("user doesn't have any current games");
					}
					
					return userGames;
				}finally {
					
				}
			}
		});
	}
	
	public Game findGameByGameID(int gameId){
		return executeTransaction(new Transaction<Game>() {
			@Override
			public Game execute(Connection conn) throws SQLException{
				PreparedStatement stmt1 = null;
				ResultSet resultSet1 = null;
				Boolean found = false;
				int log_id, player_id, map_id, combat_id; 
				
				// TODO
				// This method will need to call the other find methods in order to 
				// load the entire game with properly assigned references. 
				
				try {
					stmt1 = conn.prepareStatement(
							"select Game.game_id, Game.difficulty, Game.inCombat, Game.playerTurnTaken, Game.playerCreated, " +
							"Game.timeRemaining, Game.timerRate, Game.log_id, Game.player_id, Game.map_id, Game.combat_id " +
							"from Game " +
							"where Game.game_id = ? ");
					
					stmt1.setInt(1, gameId);
					
					Game game = new Game();
					
					resultSet1 = stmt1.executeQuery();
					
					while (resultSet1.next()) {
						found = true;
						
						game.setId(resultSet1.getInt(1));
						game.setDifficulty(resultSet1.getInt(2));
						game.setInCombat(Boolean.parseBoolean(resultSet1.getString(3)));
						game.setPlayerTurnTaken(Boolean.parseBoolean(resultSet1.getString(4)));
						game.setPlayerCreated(Boolean.parseBoolean(resultSet1.getString(5)));
						// Create a Timer based on the time remaining and timerRate. 
						Timer timer = new Timer(); 
						timer.setTime(resultSet1.getInt(6));
						timer.setTimerRate(resultSet1.getInt(7));
						
						// Get the IDs for the log, player, map, and combat. 
						log_id = resultSet1.getInt(8); 
						player_id = resultSet1.getInt(9); 
						map_id = resultSet1.getInt(10); 
						combat_id = resultSet1.getInt(11);
					}
					
					if(!found) {
						System.out.println("Game #" + gameId + " not found.");
					}
					
					// Use the Find Methods and IDs to get the Player, Map, and Log. 
					// These methods may need parameters so that subtables aren't loaded more than once!
					// In fact, we may need to find all data and then use them as parameters. 
					// Ex: Calling findLocationsByMapId to have a list of locations
					Map map = findMapByMapId(map_id);
					Player player = findPlayerByPlayerId(player_id); 
					ArrayList<String> log = findGameLogByGameLogId(log_id);
					
					
					// Load the current Combat by accessing the player's location and searching for it, if
					// it is not equal to -1. -1 means there is no current combat! 
					Location current = player.getLocation(); 
					if(combat_id != -1) {
						ArrayList<Combat> combats = current.getCombats(); 
						if(combats.isEmpty()) {
							game.setCurrentCombat(null);
						} else {
							for(Combat c : combats) {
								if (c.getId() == combat_id) {
									game.setCurrentCombat(c);
									break; 
								}
							}
						}
					} else {
						game.setCurrentCombat(null);
					}
					
					return game;
				} finally {
					DBUtil.closeQuietly(resultSet1);
					DBUtil.closeQuietly(stmt1);
				}
			}
		});
	}
	public List<NPC> findNPCIdsByLocationID(int locationID){
		return executeTransaction(new Transaction<List<NPC>>() {
			@Override
			public List<NPC> execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				ResultSet resultSet1 = null;
				Boolean found = false;
				
				try {
					stmt1 = conn.prepareStatement(
							"select NPC.id*" +
							"from Location, NPC" + 
									"where Location.location_id = ?"
							);
					stmt1.setInt(1, locationID);
					
					ArrayList<NPC> NPCsIds = new ArrayList<NPC>();
					NPC npc = new NPC();
					
					resultSet1 = stmt1.executeQuery();
					
					while(resultSet1.next()) {
						found = true;
						
						npc.setId(resultSet1.getInt(1));
						
						NPCsIds.add(npc);
					}
					
					if(!found) {
						System.out.println("No NPCs in this location");
					}
					return NPCsIds;
				}finally {
					DBUtil.closeQuietly(resultSet1);
					DBUtil.closeQuietly(stmt1);
				}
			}
		});
	}
	public List<NPC> findNPCByNPCId(int NPCId){
		return executeTransaction(new Transaction<List<NPC>>() {
			@Override
			public List<NPC> execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				ResultSet resultSet1 = null;
				Boolean found = false;
				
				try {
					stmt1 = conn.prepareStatement(
							"select " +
							"from Location, NPC, Item" + 
									"where NPC.NPC_id = ?"
							);
					stmt1.setInt(1, NPCId);
					
					ArrayList<NPC> NPCsIds = new ArrayList<NPC>();
					NPC npc = new NPC();
					
					resultSet1 = stmt1.executeQuery();
					
					while(resultSet1.next()) {
						found = true;
						
						npc.setName(resultSet1.getString(1));
						npc.setCombat(resultSet1.getBoolean(2));
						npc.setIntimidated(resultSet1.getBoolean(3));
						npc.setCanIntimidate(resultSet1.getBoolean(4));
						npc.setIntimidationThreshold(resultSet1.getInt(5));
						npc.setPersuaded(resultSet1.getBoolean(6));
						npc.setCanPersuade(resultSet1.getBoolean(7));
						npc.setPersuasionThreshold(resultSet1.getInt(8));
						
						NPCsIds.add(npc);
					}
					
					if(!found) {
						System.out.println("No NPCs in this location");
					}
					return NPCsIds;
				}finally {
					DBUtil.closeQuietly(resultSet1);
					DBUtil.closeQuietly(stmt1);
				}
			}
		});
	}
	
	public List<Integer> findNPCStatsIdsByNPCId(int NPCId){
		return executeTransaction(new Transaction<List<Integer>>() {
			@Override
			public List<Integer> execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				ResultSet resultSet1 = null;
				Boolean found = false;
				
				try {
					stmt1 = conn.prepareStatement(
							"select Stat.Stat_id" +
							"from Stat, NPC" + 
									"where NPC.NPC_id = ?"
							);
					stmt1.setInt(1, NPCId);
					
					ArrayList<Integer> NPCStatIds = new ArrayList<Integer>();
					NPC npc = new NPC();
					
					while(resultSet1.next()) {
						found = true;
						
						npc.getStat().setId(resultSet1.getInt(1));
						
						NPCStatIds.add(npc.getStat().getId());
					}
					
					if(!found) {
						System.out.println("stats were not found for NPC");
					}
					
					return NPCStatIds;
				}finally {
					DBUtil.closeQuietly(resultSet1);
					DBUtil.closeQuietly(stmt1);
				}
			}
		});
	}
	public Stat findNPCStatByStatId(int StatId){
		return executeTransaction(new Transaction<Stat>() {
			@Override
			public Stat execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				ResultSet resultSet1 = null;
				Boolean found = false;
				
				try {
					stmt1 = conn.prepareStatement(
							"select Stat.name, Stat.rank" +
							"from Stat, NPC" + 
									"where Stat.Stat_id = ?"
							);
					stmt1.setInt(1, StatId);
					
					Stat stat = new Stat();
					
					while(resultSet1.next()) {
						found = true;
						
						stat.setName(resultSet1.getString(1));
						stat.setRank(resultSet1.getInt(2));
						
					}
					
					if(!found) {
						System.out.println("this ID doesn't have a pertaining stat");
					}
					
					return stat;
				}finally {
					DBUtil.closeQuietly(resultSet1);
					DBUtil.closeQuietly(stmt1);
				}
			}
		});
	}
	public List<Integer> findNPCsIdByCombatID(int combatID){
		return executeTransaction(new Transaction<List<Integer>>() {
			@Override
			public List<Integer> execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				ResultSet resultSet1 = null;
				Boolean found = false;
				
				try {
					stmt1 = conn.prepareStatement(
							"select NPC.NPC_id" +
							"from Combat, NPC" + 
									"where Combat.Combat_id = ?"
							);
					stmt1.setInt(1, combatID);
					
					ArrayList<Integer> NPCIds = new ArrayList<Integer>();
					NPC npc = new NPC();
					
					resultSet1 = stmt1.executeQuery();
					
					while(resultSet1.next()) {
						found = true;
						
						npc.setId(resultSet1.getInt(1));
						
						NPCIds.add(npc.getId());
					}
					
					if(!found) {
						System.out.println("No NPCs in this combat");
					}
					return NPCIds;
				}finally {
					DBUtil.closeQuietly(resultSet1);
					DBUtil.closeQuietly(stmt1);
				}
			}
		});
	}
	
	public Loot findLootByLocationID(int locationID) {
		return executeTransaction(new Transaction<Loot>() {
			@Override
			public Loot execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				ResultSet resultSet1 = null;
				
				try {
					stmt1 = conn.prepareStatement(
							"select Loot.loot_id, Loot.xp, Loot.collected, Loot.item_id " +
							"  from  Loot, Location " +
							"  where Location.location_id = ? " +
							"    and Location.loot_id = Loot.loot_id"
					);
					stmt1.setInt(1, locationID);
					
					Loot loot = new Loot(); 
					
					resultSet1 = stmt1.executeQuery();
					
					// for testing that a result was returned
					Boolean found = false;
					
					while (resultSet1.next()) {
						found = true;
						
						loot.setId(resultSet1.getInt(1));
						loot.setXP(resultSet1.getInt(2));
						loot.setCollected(resultSet1.getBoolean(3)); 
						loot.setItems(findItemByItemID(resultSet1.getInt(4)));
						
					}
					
					// check if the locationID was found
					if (!found) {
						System.out.println("<" + locationID + "> was not found in the Location table");
					}
					
					return loot;
				} finally {
					DBUtil.closeQuietly(resultSet1);
					DBUtil.closeQuietly(stmt1);
				}
			}
		});
	}
	
	public HashMap<String, Item> findInventoryByPlayerID(int playerID) {
		return executeTransaction(new Transaction<HashMap<String, Item>>() {
			@Override
			public HashMap<String, Item> execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				ResultSet resultSet1 = null;
				
				try {
					stmt1 = conn.prepareStatement(
							"select Item.* " +
							"  from  Item, PlayerInventory " +
							"  where PlayerInventory.player_id = ? " +
							"    and Item.item_id = PlayerInventory.item_id"
					);
					stmt1.setInt(1, playerID);
					
					HashMap<String, Item> inventory = new HashMap<String, Item>(); 
					
					resultSet1 = stmt1.executeQuery();
					
					// for testing that a result was returned
					Boolean found = false;
					
					while (resultSet1.next()) {
						found = true;
						
						Item item = new Item(); 
						
						// TODO : Need to set all fields in Item based on entries in table.
						
						item.setId(resultSet1.getInt(1));
						item.setName(resultSet1.getString(2));
						
						//loot.setCollected(resultSet1.getBoolean(3)); 
						//loot.setItems(findItemByItemID(resultSet1.getInt(4)));
						
						// Add the Item to the inventory HashMap
						
						String itemName = item.getName(); 
						
						inventory.put(itemName.toLowerCase(), item); 
					}
					
					// check if the playerID was found
					if (!found) {
						System.out.println("<" + playerID + "> was not found in the PlayerInventory table");
					}
					
					return inventory;
					
				} finally {
					DBUtil.closeQuietly(resultSet1);
					DBUtil.closeQuietly(stmt1);
				}
			}
		});
	}
	
	public HashMap<String, Stat> findPlayerStatsByPlayerID(int playerID) {
		return executeTransaction(new Transaction<HashMap<String, Stat>>() {
			@Override
			public HashMap<String, Stat> execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				ResultSet resultSet1 = null;
				
				try {
					stmt1 = conn.prepareStatement(
							"select PlayerStats.* " +
							"  from  PlayerStats, PlayerToStats " +
							"  where PlayerToStats.player_id = ? " +
							"    and PlayerStats.stat_id = PlayerToStats.stat_id"
					);
					stmt1.setInt(1, playerID);
					
					HashMap<String, Stat> playerStats = new HashMap<String, Stat>(); 
					
					resultSet1 = stmt1.executeQuery();
					
					// for testing that a result was returned
					Boolean found = false;
					
					while (resultSet1.next()) {
						found = true;
						
						Stat stat = new Stat(); 
						
						// Set values of stat from current result set row. 
						stat.setId(resultSet1.getInt(1));
						stat.setName(resultSet1.getString(2));
						stat.setRank(resultSet1.getInt(3));
	
						
						// Add the stat to the playerStats HashMap
						playerStats.put(stat.getName(), stat); 
					}
					
					// check if the playerID was found
					if (!found) {
						System.out.println("<" + playerID + "> was not found in the PlayerToStats table");
					}
					
					return playerStats;
					
				} finally {
					DBUtil.closeQuietly(resultSet1);
					DBUtil.closeQuietly(stmt1);
				}
			}
		});
	}
	
	public HashMap<String, Stat> findNPCStatsByNPCID(int npcID) {
		return executeTransaction(new Transaction<HashMap<String, Stat>>() {
			@Override
			public HashMap<String, Stat> execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				ResultSet resultSet1 = null;
				
				try {
					stmt1 = conn.prepareStatement(
							"select NPCStats.* " +
							"  from  NPCStats, NPCToStats " +
							"  where NPCToStats.npc_id = ? " +
							"    and NPCStats.stat_id = NPCToStats.stat_id"
					);
					stmt1.setInt(1, npcID);
					
					HashMap<String, Stat> npcStats = new HashMap<String, Stat>(); 
					
					resultSet1 = stmt1.executeQuery();
					
					// for testing that a result was returned
					Boolean found = false;
					
					while (resultSet1.next()) {
						found = true;
						
						Stat stat = new Stat(); 
						
						// Set values of stat from current result set row. 
						stat.setId(resultSet1.getInt(1));
						stat.setName(resultSet1.getString(2));
						stat.setRank(resultSet1.getInt(3));
	
						
						// Add the stat to the npcStats HashMap
						npcStats.put(stat.getName(), stat); 
					}
					
					// check if the playerID was found
					if (!found) {
						System.out.println("<" + npcID + "> was not found in the NPCToStats table");
					}
					
					return npcStats;
					
				} finally {
					DBUtil.closeQuietly(resultSet1);
					DBUtil.closeQuietly(stmt1);
				}
			}
		});
	}
	
	public Item findItemByItemID(int itemID) {
		return executeTransaction(new Transaction<Item>() {
			@Override
			public Item execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				ResultSet resultSet1 = null;
				
				try {
					stmt1 = conn.prepareStatement(
							"select Item.* " +
							"  from  Item " +
							"  where Item.item_id = ? "
					);
					stmt1.setInt(1, itemID);
					
					Item item = new Item(); 
					
					resultSet1 = stmt1.executeQuery();
					
					// for testing that a result was returned
					Boolean found = false;
					
					while (resultSet1.next()) {
						found = true;
						
						// TODO : Need to set all fields in Item based on entries in table.
						
						item.setId(resultSet1.getInt(1));
						item.setName(resultSet1.getString(2));
						
					}
					
					// check if the playerID was found
					if (!found) {
						System.out.println("<" + itemID + "> was not found in the Item table");
					}
					
					return item;
					
				} finally {
					DBUtil.closeQuietly(resultSet1);
					DBUtil.closeQuietly(stmt1);
				}
			}
		});
	}
	
	public List<Integer> findPlayerStatIdsByPlayerId(int playerID) {
		return executeTransaction(new Transaction<List<Integer>>() {
			@Override
			public List<Integer> execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				ResultSet resultSet1 = null;
				
				try {
					stmt1 = conn.prepareStatement(
							"select PlayerToStats.stat_id " +
							"  from  PlayerToStats " +
							"  where PlayerToStats.player_id = ? "
					);
					stmt1.setInt(1, playerID);
					
					ArrayList<Integer> statIds = new ArrayList<Integer>();
					
					resultSet1 = stmt1.executeQuery();
					
					// for testing that a result was returned
					Boolean found = false;
					
					while (resultSet1.next()) {
						found = true;
						
						// Add each stat_id to the list. 
						
						statIds.add(resultSet1.getInt(1));
						
					}
					
					// check if the playerID was found
					if (!found) {
						System.out.println("<" + playerID + "> was not found in the PlayerToStats table");
					}
					
					return statIds;
					
				} finally {
					DBUtil.closeQuietly(resultSet1);
					DBUtil.closeQuietly(stmt1);
				}
			}
		});
	}
	

	public Speech findSpeechOptionsBySpeechId(int speechID) {
		return executeTransaction(new Transaction<Speech>() {
			@Override
			public Speech execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				ResultSet resultSet1 = null;
				
				try {
					stmt1 = conn.prepareStatement(
							"select Speech.speechOptions* " +
							"  from  Speech " +
							"  where Speech.speech_id = ? "
					);
					stmt1.setInt(1, speechID);
					
					Item item = new Item(); 
					
					resultSet1 = stmt1.executeQuery();
					
					// for testing that a result was returned
					Boolean found = false;
					
					while (resultSet1.next()) {
						found = true;
						
						// TODO : Need to set all fields in Item based on entries in table.
						
						item.setId(resultSet1.getInt(1));
						item.setName(resultSet1.getString(2));
						
					}
					
					// check if the playerID was found
					if (!found) {
						System.out.println("<" + itemID + "> was not found in the Item table");
					}
					
					return item;
					
				} finally {
					DBUtil.closeQuietly(resultSet1);
					DBUtil.closeQuietly(stmt1);
				}
			}
		
		});
	}
        
        
	public List<Integer> findPlayerInventoryIdsByPlayerId(int playerID) {
		return executeTransaction(new Transaction<List<Integer>>() {
			@Override
			public List<Integer> execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				ResultSet resultSet1 = null;
				
				try {
					stmt1 = conn.prepareStatement(
							"select PlayerInventory.item_id " +
							"  from  PlayerInventory " +
							"  where PlayerInventory.player_id = ? "
					);
          
					stmt1.setInt(1, playerID);
					
					ArrayList<Integer> inventoryIds = new ArrayList<Integer>();
					
					resultSet1 = stmt1.executeQuery();
					
					// for testing that a result was returned
					Boolean found = false;
					
					while (resultSet1.next()) {
						found = true;
						// Add each item_id to the list. 
						
						inventoryIds.add(resultSet1.getInt(1));
						
					}
					
					// check if the playerID was found
					if (!found) {
						System.out.println("<" + playerID + "> was not found in the PlayerInventory table");
					}
					
					return inventoryIds;
				} finally {
					DBUtil.closeQuietly(resultSet1);
					DBUtil.closeQuietly(stmt1);
				}
			}
		});
	}
	
	public Map findMapByMapID(int mapId) {
		return executeTransaction(new Transaction<Map>() {
			@Override 
			public Map execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement(
							" select Map.* "
							+ " from Map "
							+ " where Map.map_id = ? "
					);
					stmt.setInt(1, mapId);
					
					resultSet = stmt.executeQuery();
					
					Map map = new Map();
					
					boolean found = false;
					
					while(resultSet.next()) {
						found = true;
						
						map.setId(resultSet.getInt(1));
					}
					
					if (!found) {
						System.out.println("<" + mapId + "> was not found in the Map table");
					}
					
					return map;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	
	public Location findLocationByLocationID(int locationId) {
		return executeTransaction(new Transaction<Location>() {
			@Override
			public Location execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement(
							" select Location.* "
							+ " from Location "
							+ " where location_id = ? "
					);
					stmt.setInt(1, locationId);
					
					resultSet = stmt.executeQuery();
					
					Location location = new Location();
					
					boolean found = false;
					
					while(resultSet.next()) {
						found = true;
						
						location.setId(resultSet.getInt(1));
						location.setName(resultSet.getString(2));
						location.setDescription(resultSet.getString(3));
						location.setHidden(resultSet.getBoolean(4));
						location.setBlocked(resultSet.getBoolean(5));
					}
					
					if (!found) {
						System.out.println("<" + locationId + "> was not found in the Location table");
					}
					
					return location;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	
	public List<Integer> findPuzzleIdsByLocationID(int locationId) {
		return executeTransaction(new Transaction<List<Integer>>() {
			@Override
			public List<Integer> execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement(
							"select Puzzle.puzzle_id "
							+ " from Location, Puzzle, LocationToPuzzle "
							+ " where Location.location_id = LocationToPuzzle.location_id "
							+ " and Puzzle.puzzle_id = LocationToPuzzle.puzzle_id "
							+ " and Location.location_id = ? "
					);
					stmt.setInt(1, locationId);
					
					resultSet = stmt.executeQuery();
					
					ArrayList<Integer> puzzleIds = new ArrayList<Integer>();
					
					boolean found = false;
					
					while(resultSet.next()) {
						found = true;
						
						puzzleIds.add(resultSet.getInt(1));
					}
					
					if (!found) {
						System.out.println("<" + locationId + "> was not found in the Location table");
					}
					
					return puzzleIds;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	
	public Puzzle findPuzzleByPuzzleId(int puzzle_id) {
		return executeTransaction(new Transaction <Puzzle>() {
			@Override
			public Puzzle execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement(
							" select Puzzle.* "
							+ " from Puzzle "
							+ " where Puzzle.puzzle_id = ? "
					);
					stmt.setInt(1, puzzle_id);
					
					Puzzle puzzle = new Puzzle();
					
					boolean found = false;
					
					while(resultSet.next()) {
						found = true;
						
						puzzle.setId(resultSet.getInt(1));
						puzzle.setPrompt(resultSet.getString(2));
						puzzle.setAnswer(resultSet.getString(3));
						puzzle.setResult(resultSet.getBoolean(6));
						puzzle.setCanSolve(resultSet.getBoolean(7));
						puzzle.setSolved(resultSet.getBoolean(8));
						puzzle.setBreakable(resultSet.getBoolean(9));
						puzzle.setJumpable(resultSet.getBoolean(10));
						puzzle.setRoomCon(resultSet.getString(11));
					}
					
					if (!found) {
						System.out.println("<" + puzzle_id + "> was not found in the Puzzle table");
					}
					
					return puzzle;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	
	
	public WinCondition findWinConditionByWinConditionId(int winCondition_id) {
		return executeTransaction(new Transaction<WinCondition>() {
			@Override
			public WinCondition execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement(
							"select WinCondition.* "
							+ " from WinCondition "
							+ " where WinCondition.winCondition_id = ? "
					);
					stmt.setInt(1, winCondition_id);
					
					resultSet = stmt.executeQuery();
					
					WinCondition winCondition = new WinCondition();
					
					boolean found = false;
					
					while(resultSet.next()) {
						found = true;
						
						winCondition.setId(resultSet.getInt(1));
						winCondition.setComplete(resultSet.getBoolean(2));
						winCondition.setLost(resultSet.getBoolean(3));
						winCondition.setWonRooms(resultSet.getBoolean(4));
						winCondition.setBestCase(resultSet.getBoolean(5));
						winCondition.setDefaultCase(resultSet.getBoolean(6));
					}
					
					if (!found) {
						System.out.println("<" + winCondition_id + "> was not found in the Puzzle table");
					}
					
					return winCondition;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	
	public HashMap<String, ArrayList<String>> findConnectionsByMapID(int mapID) {
		return executeTransaction(new Transaction <HashMap<String, ArrayList<String>>>() {
			@Override
			public HashMap<String, ArrayList<String>> execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement(
						"select Map.* "
						+ " from Map "
						+ " where Map.map_id = ?"	
					);
					stmt.setInt(1,  mapID);
					
					resultSet = stmt.executeQuery();
					
					HashMap<String, ArrayList<String>> connections = new HashMap<String, ArrayList<String>>();
					Map m = new Map();
					
					boolean found = false;
					
					while(resultSet.next()) {
						found = true;
						
						m.setId(resultSet.getInt(1));
						connections = m.getConnections();
					}
					
					if (!found) {
						System.out.println("<" + mapID + "> was not found in the  table");
					}
					
					return connections;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	
	public Integer insertNewPlayer(Player player, int loc_rows, int game_rows) {
		// Note, this method inserts a new row for the new player and default values.
		// Character creation should call an update for the Player class! 
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
											
				try {
					
					// insert new Player into Player table
					// prepare SQL insert statement to add new Player to Player table
					stmt1 = conn.prepareStatement(
							"insert into Player (player_id, name, icon, weapon, armor, playerCreated, location_id) " +
							"  values(?, ?, ?, ?, ?, ?, ?) "
					);
					// Number of players = number of game_rows
					stmt1.setInt(1, player.getId() + game_rows);
					// Name will update upon character creation; this insert is called before creation.
					stmt1.setString(2, player.getName());
					stmt1.setString(3, player.getIcon());
					stmt1.setString(4, player.getWeapon());
					stmt1.setString(5, player.getArmor());
					stmt1.setString(6, "false");
					stmt1.setInt(7, player.getLocation().getId() + loc_rows); 
					
					// execute the update
					stmt1.executeUpdate();
					
					System.out.println("Player #" + game_rows + " inserted into Player table");					
					
					return game_rows;
					
				} finally {
					DBUtil.closeQuietly(stmt1);
				}
			}
		});
	}
	
	public Integer insertNewPlayerToStats(Player player, int player_rows, int playerstat_rows) {
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
											
				try {
					
					// insert new player_id and player_stats
					// prepare SQL insert statement to add new player_id and stat_id
					stmt1 = conn.prepareStatement(
							"insert into PlayerToStats (player_id, stat_id) " +
							"  values(?, ?) "
					);

					// We want to insert all of the stats stored in player. 
					// Hence, we need a batch of inserts!
					
					int playerId = player.getId() + player_rows;
					
					// Add each stat with matching playerId to the table, incremented by number of rows.
					for (Stat stat : player.getStats().values()) {
						stmt1.setInt(1, playerId);
						int statId = stat.getId() + playerstat_rows;
						stmt1.setInt(2, statId);
						stmt1.addBatch();
					}
					stmt1.executeBatch();
					
					System.out.println("Player #" + playerId + " inserted into PlayerToStats table");
									
					
					return 0;
					
				} finally {
					DBUtil.closeQuietly(stmt1);
				}
			}
		});
	}
	
	public Integer insertNewPlayerInventory(Player player, int player_rows, int item_rows) {
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
											
				try {
					
					// insert new player_id and item_id
					// prepare SQL insert statement to add new player_id and item_id
					stmt1 = conn.prepareStatement(
							"insert into PlayerToStats (player_id, item_id) " +
							"  values(?, ?) "
					);

					// We want to insert all of the items stored in player. 
					// Hence, we need a batch of inserts!
					
					int playerId = player.getId() + player_rows;
					
					// Add each item with matching playerId to the table, incremented by number of rows in Item.
					for (Item item : player.getInventory().values()) {
						stmt1.setInt(1, playerId);
						int itemId = item.getId() + item_rows;
						stmt1.setInt(2, itemId);
						stmt1.addBatch();
					}
					stmt1.executeBatch();
					
					System.out.println("Player #" + playerId + " inserted into PlayerInventory table");
									
					
					return 0;
					
				} finally {
					DBUtil.closeQuietly(stmt1);
				}
			}
		});
	}
	
	public Integer insertNewLocationToCombat(List<Pair<Integer, Integer>> ltcPairs, int location_rows, int combat_rows) {
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
											
				try {
					
					// insert all location to combat pairs. 
					// prepare SQL insert statement to add each pair.
					stmt1 = conn.prepareStatement(
							"insert into LocationToCombat (location_id, combat_id) " +
							"  values(?, ?) "
					);

					// Add each pair, incremented by number of rows in their table.
					for (Pair<Integer, Integer> p : ltcPairs) {
						stmt1.setInt(1, p.getLeft() + location_rows);
						stmt1.setInt(2, p.getRight() + combat_rows);
						stmt1.addBatch();
					}
					stmt1.executeBatch();
					
					System.out.println("LocationToCombat table inserted.");
									
					
					return 0;
					
				} finally {
					DBUtil.closeQuietly(stmt1);
				}
			}
		});
	}
	
	public Integer insertNewMap(Map map, int game_rows, int location_rows) {
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
											
				try {
					
					// insert new Map
					// prepare SQL insert statement to add new Map
					stmt1 = conn.prepareStatement(
							"insert into Map (map_id, location_id, north, east, south, west, extra) " +
							"  values(?, ?, ?, ?, ?, ?, ?) "
					);

					// We want to insert all of the Map's locations and their connections.
					// Hence, we need a batch of inserts!
					
					int mapId = map.getId() + game_rows;
					
					// Add each stat with matching playerId to the table, incremented by number of rows.
					for (Location loc : map.getLocations().values()) {
						stmt1.setInt(1, mapId);
						
						// Compute the values for each column in the row first. 
						int locId = loc.getId() + location_rows;
						ArrayList<String> locationConn = map.getConnections().get(loc.getName().toLowerCase());
						String northName = locationConn.get(0); 
						String eastName = locationConn.get(1);
						String southName = locationConn.get(2);
						String westName = locationConn.get(3);
						String extraName = locationConn.get(4);
						
						int northId = map.getLocations().get(northName).getId(); 
						int eastId = map.getLocations().get(eastName).getId();
						int southId = map.getLocations().get(southName).getId();
						int westId = map.getLocations().get(westName).getId();
						int extraId = map.getLocations().get(extraName).getId();
						
						// Assign these values. 
						stmt1.setInt(2, locId);
						stmt1.setInt(3, northId);
						stmt1.setInt(4, eastId);
						stmt1.setInt(5, southId);
						stmt1.setInt(6, westId);
						stmt1.setInt(7, extraId);
						stmt1.addBatch();
					}
					stmt1.executeBatch();
					
					System.out.println("Map #" + mapId + " inserted into Map table");
									
					
					return 0;
					
				} finally {
					DBUtil.closeQuietly(stmt1);
				}
			}
		});
	}
	
	public Integer insertNewGameLog(ArrayList<String> log, int game_rows) {
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
											
				try {
					
					// insert new player_id and item_id
					// prepare SQL insert statement to add new player_id and item_id
					stmt1 = conn.prepareStatement(
							"insert into GameLog (log_id, order, output) " +
							"  values(?, ?, ?) "
					);

					// We want to insert all of the outputs stored in the log individually. 
					// Hence, we need a batch of inserts!
					
					// log_id is always same as game ID, which will be 1 greater than current # of games.
					int logId = 1 + game_rows;
					
					// Iterate through ArrayList.
					for (int j = 0; j < log.size(); j++) {
						stmt1.setInt(1, logId);
						// Order is the index of the string.
						stmt1.setInt(2, j);
						// Then add in the string. 
						stmt1.setString(3, log.get(j));
						stmt1.addBatch();
					}
					stmt1.executeBatch();
					
					System.out.println("Log #" + logId + " inserted into GameLog table");
									
					
					return 0;
					
				} finally {
					DBUtil.closeQuietly(stmt1);
				}
			}
		});
	}
	
	public Integer insertOutputIntoGameLogByLogId(String output, int log_id, int log_size) {
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
											
				try {
					
					// insert new string of output into the GameLog table. 
					// prepare SQL insert statement to add new output.
					stmt1 = conn.prepareStatement(
							"insert into GameLog (log_id, order, output) " +
							"  values(?, ?, ?) "
					);
					
					stmt1.setInt(1, log_id);
					// Order is the current length of the log (the next index in the list)
					stmt1.setInt(2, log_size);
					// Then add in the string. 
					stmt1.setString(3, output);
						
					
					stmt1.executeUpdate();
					
					System.out.println("Output #" + log_size + " inserted into GameLog table for Log #" + log_id);
									
					
					return 0;
					
				} finally {
					DBUtil.closeQuietly(stmt1);
				}
			}
		});
	}
	
	public WinCondition insertNewWinConditions(WinCondition winCondition) {
		return executeTransaction(new Transaction<WinCondition>() {
			@Override
			public WinCondition execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				
				try {
					stmt = conn.prepareStatement(
							"insert into WinCondition(winCondition_id, complete, lost, wonRooms, bestCase, defaultCase) "
							+ " values(?, ?, ?, ?, ?, ?) "
					);
					stmt.setInt(1, winCondition.getId());
					stmt.setBoolean(2, winCondition.getComplete());
					stmt.setBoolean(3, winCondition.getLost());
					stmt.setBoolean(4,  winCondition.getWonRooms());
					stmt.setBoolean(5,  winCondition.getBestCase());
					stmt.setBoolean(6,  winCondition.getDefaultCase());
					
					stmt.executeQuery();
					
					System.out.println("Win Condition: " + winCondition + " has been inserted into WinConditions table");
					
					return winCondition;
				} finally {
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	
	
	public Integer insertItemIntoPlayerInventoryByPlayerIdAndItemId(int player_id, int item_id) {
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
											
				try {
					
					// insert new string of output into the GameLog table. 
					// prepare SQL insert statement to add new output.
					stmt1 = conn.prepareStatement(
							"insert into PlayerInventory (player_id, item_id) " +
							"  values(?, ?) "
					);
					
					stmt1.setInt(1, player_id);
					stmt1.setInt(2, item_id);
						
					stmt1.executeUpdate();
					
					System.out.println("Item #" + item_id + " inserted into PlayerInventory table for Player #" + player_id);
									
					
					return 0;
					
				} finally {
					DBUtil.closeQuietly(stmt1);
				}
			}
		});
	}

	public Stat insertNewPlayerStats(Stat playerStats) {
		return executeTransaction(new Transaction<Stat>() {
			@Override
			public Stat execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				
				try {
					stmt = conn.prepareStatement(
						"insert into PlayerStats(stat_id, name, amount) "
						+ " values(?, ?, ?)"	
					);
					stmt.setInt(1,  playerStats.getId());
					stmt.setString(2,  playerStats.getName());
					stmt.setInt(3,  playerStats.getRank());
					
					stmt.executeQuery();
					
					System.out.println("Player Stats: " + playerStats + " has been inserted into Stats table");
					
					return playerStats;
				} finally {
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	
	public Location insertNewLocations(Location location) {
		return executeTransaction(new Transaction<Location>() {
			@Override
			public Location execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				
				try {
					stmt = conn.prepareStatement(
						"insert into Location(location_id, name, description, hidden, blocked) "
						+ " values(?, ?, ?, ?, ?)"	
					);
					stmt.setInt(1,  location.getId());
					stmt.setString(2,  location.getName());
					stmt.setString(3,  location.getDescription());
					stmt.setBoolean(4,  location.isHidden());
					stmt.setBoolean(5,  location.getBlocked());
					
					
					stmt.executeQuery();
					
					System.out.println("Location: " + location + " has been inserted into Location table");
					
					return location;
				} finally {
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	
	public boolean updateCombatByCombatId(Combat combat) {
		return executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
											
				try {
					
					// Update Combat by CombatID
					stmt1 = conn.prepareStatement(
							"update Combat " +
							"  set turn = ?, difficulty = ?, dead = ? " +
							"  where combat_id = ?"
					);
					
					// Set based on fields in Combat
					stmt1.setInt(1, combat.getTurn());
					stmt1.setInt(2, combat.getDifficulty());
					stmt1.setString(3, Boolean.toString(combat.isDead()));
					stmt1.setInt(4, combat.getId());
					
					// execute the update
					stmt1.executeUpdate();
					
					System.out.println("Combat #" + combat.getId() + " updated");					
					
					return true;
					
				} finally {
					DBUtil.closeQuietly(stmt1);
				}
			}
		});
	}
	
	public boolean updateLootByLootId(Loot loot) {
		return executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
											
				try {
					
					// Update Loot by Loot ID
					stmt1 = conn.prepareStatement(
							"update Loot " +
							"  set collected = ? " +
							"  where loot_id = ?"
					);
					
					// Set based on fields in Loot
					stmt1.setString(1, Boolean.toString(loot.isCollected()));
					stmt1.setInt(2, loot.getId());
					
					// execute the update
					stmt1.executeUpdate();
					
					System.out.println("Loot #" + loot.getId() + " updated");					
					
					return true;
					
				} finally {
					DBUtil.closeQuietly(stmt1);
				}
			}
		});
	}
		
	public boolean updatePuzzleByPuzzleId(Puzzle puzzle) {
		return executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				
				try {
					stmt = conn.prepareStatement(
						"update Puzzle "
						+ " set prompt = ?, answer = ?, requiredSkill = ?, requiredItem = ?, result = ?, canSolve = ?, solved = ?, breakable = ?, jumpable = ?, roomCon = ? "
						+ " where Puzzle.puzzle_id = ? "	
					);
					stmt.setString(1,  puzzle.getPrompt());
					stmt.setString(2, puzzle.getAnswer());
					stmt.setString(3,  puzzle.getRequiredSkill().toString());
					stmt.setString(4,  puzzle.getRequiredItem().toString());
					stmt.setBoolean(5,  puzzle.getResult());
					stmt.setBoolean(6, puzzle.isCanSolve());
					stmt.setBoolean(7,  puzzle.isSolved());
					stmt.setBoolean(8,  puzzle.getBreakable());
					stmt.setBoolean(9, puzzle.isJumpable());
					stmt.setString(10, puzzle.getRoomCon());
					stmt.setInt(11,  puzzle.getId());
					
					stmt.executeQuery();
					
					System.out.println("Puzzle #: " + puzzle.getId() + " has been updated");
					
					return true;
				} finally {
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	
	public boolean updateWinConditionByWinConditionId(WinCondition winCondition) {
		return executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				
				try {
					stmt = conn.prepareStatement(
						"update WinCondition "
						+ " set winCondition_id = ?, complete = ?, lost = ?, wonRooms = ?, bestCase = ?, defaultCase = ? "
						+ " where WinCondition.winCondition_id = ?"
					);
					stmt.setInt(1,  winCondition.getId());
					stmt.setBoolean(2,  winCondition.getComplete());
					stmt.setBoolean(3,  winCondition.getLost());
					stmt.setBoolean(4,  winCondition.getWonRooms());
					stmt.setBoolean(5,  winCondition.getBestCase());
					stmt.setBoolean(6,  winCondition.getDefaultCase());
					stmt.setInt(7,  winCondition.getId());
					
					stmt.executeQuery();
					
					System.out.println("Win Condition #: " + winCondition.getId() + " has been updated");
					
					return true;
				} finally {
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	
	public boolean removeItemFromInventoryByItemIdAndPlayerId(int itemID, int playerID) {
		return executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
											
				try {
					
					// remove item from inventory given playerID and itemID by CombatID
					// prepare SQL insert statement to add new Player to Player table
					stmt1 = conn.prepareStatement(
							"delete from PlayerInventory " +
							"  where player_id = ? and item_id = ?"
					);
					// Set based on fields in Combat
					stmt1.setInt(1, playerID);
					stmt1.setInt(2, itemID);
					
					// execute the deletion
					stmt1.executeUpdate();
					
					System.out.println("Item #" + itemID + " deleted from Player #" + playerID + " inventory");					
					
					return true;
					
				} finally {
					DBUtil.closeQuietly(stmt1);
				}
			}
		});
	}
	
	
	
	public int getLargestIdInTable(String table, String idName) {
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				ResultSet resultSet1 = null;
				
				try {
					// This will get a list of all the IDs in the table. 
					stmt1 = conn.prepareStatement(
							"select ? from ?" + 
								" sort by ? DESC"
					);
					stmt1.setString(1, idName);
					stmt1.setString(2, table);
					stmt1.setString(3, idName);
					
					int rows = 0; 
					
					resultSet1 = stmt1.executeQuery();
					
					// for testing that a result was returned
					Boolean found = false;
					
					// We only need to access the first row, as it will contain the largest ID. 
					if (resultSet1.next()) {
						found = true;
						
						rows = resultSet1.getInt(1);
					}
					
					// check if the playerID was found
					if (!found) {
						System.out.println("No rows were found in <" + table + ">");
					}
					
					return rows;
					
				} finally {
					DBUtil.closeQuietly(resultSet1);
					DBUtil.closeQuietly(stmt1);
				}
			}
		});
	}
	

	@Override
	public Integer insertNewNPCs(List<NPC> npcList, int maxNPCId) {
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				int resultSet1 = 0;
				PreparedStatement stmt2 = null;
				ResultSet resultSet2 = null;			
				
				// for saving NPC ID 
				Integer NPC_id   = -1;

				try {
					stmt1 = conn.prepareStatement(
							"insert into NPC (npc_id, name, combat, item_id, speech_id, isIntimidated, canIntimidate, "
							+ " intimidationThreshold, isPersuaded, canPersuade, persuasionThreshold)  " +
							" values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "
					);
					
					// Add each NPC, incremented by number of rows in their table.
					for (NPC n : npcList) {
						stmt1.setInt(1, n.getId() + maxNPCId);
						stmt1.setString(2, n.getName());
						stmt1.setString(3, Boolean.toString(n.isCombat()));
						stmt1.addBatch();
					}
					stmt1.executeBatch();
					
					System.out.println("NPC table inserted.");
					
					return NPC_id;
				} finally {
					
					DBUtil.closeQuietly(resultSet2);
					DBUtil.closeQuietly(stmt1);
					DBUtil.closeQuietly(stmt2);
				}
			}
		});
	}
	
	@Override
	public Integer insertNewNPCStats( final String name, final int health, final int armor, final int strength, final int speed) {
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				int resultSet1 = 0;
				PreparedStatement stmt2 = null;
				ResultSet resultSet2 = null;			
				
				// for saving NPC ID 
				Integer NPC_id   = -1;

				// try to retrieve author_id (if it exists) from DB, for Author's full name, passed into query
				try {
					stmt1 = conn.prepareStatement(
							"insert into authors (name, health, combat, stats)  " +
							" values(?, ?, ?, ?) "
					);
					stmt1.setInt(1, health);
					stmt1.setInt(2, armor);
					stmt1.setInt(3 , strength);
					stmt1.setInt(4,  speed);
					
					// execute the query, get the result
					stmt1.executeUpdate();
					
					stmt2 = conn.prepareStatement(
							"select Stat_id from Stats" +
							" where name = ?"
					);
					stmt2.setString(1, name);
					
					resultSet2 = stmt2.executeQuery();
					
					
					while(resultSet2.next()) {
						NPC_id = resultSet2.getInt(1);
					}
					
					
					return NPC_id;
				} finally {
					
					DBUtil.closeQuietly(resultSet2);
					DBUtil.closeQuietly(stmt1);
					DBUtil.closeQuietly(stmt2);
				}
			}
		});
	}
	
	
  	public Integer InsertNewLocationToNPC(int NPC_Id) {
  		return executeTransaction(new Transaction<Integer>() {
  			@Override
			public Integer execute(Connection conn) throws SQLException {
  				PreparedStatement stmt1 = null;
				int resultSet1 = 0;
  				
  				try {
  					stmt1 = conn.prepareStatement(
							"insert into authors (name, health, combat, stats)  " +
							" values(?, ?, ?, ?) "
					);
  					
  					
	  				// execute the query, get the result
					stmt1.executeUpdate();
						
					return 0; 
  					
  				} finally {
  					DBUtil.closeQuietly(stmt1);
  				}
  			}
  		});
  	}

	public Integer InsertNewNPCToStats(int Stats_id) {
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				int resultSet1 = 0;
				try {
					stmt1 = conn.prepareStatement(
							"insert into authors (name, health, combat, stats)  " +
							" values(?, ?, ?, ?) "
					);
  					
  					
		  			// execute the query, get the result
					stmt1.executeUpdate();
				}finally {
					DBUtil.closeQuietly(stmt1);
				}
			}
		});
	}

	public Integer InsertNewCombatToNPC(int NPC_id) {
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				int resultSet1 = 0;
				try {
					stmt1 = conn.prepareStatement(
							"insert into authors (name, health, combat, stats)  " +
							" values(?, ?, ?, ?) "
					);
  					
  					
		  				// execute the query, get the result
						stmt1.executeUpdate();
				}finally{
					DBUtil.closeQuietly(stmt1);
				}
			}
		});
	}

	public Boolean UpdateNPCByNPCId(NPC npc) {
		return executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				ResultSet resultSet1 = null;
				
				try {
					stmt1 = conn.prepareStatement(
							"Update NPC" +
							"Set NPC.combat = ?, NPC.isIntimidated = ?, NPC.canIntimidate = ?, NPC.isPersuaded = ?, NPC.canPersuade = ? " + 
									"where NPC.NPC_id = "
					);
  					
					
					resultSet1 = stmt1.executeQuery();
				}finally{
					DBUtil.closeQuietly(stmt1);
					DBUtil.closeQuietly(resultSet1);
				}
			}
		});
	}
	
	
	
	
	// wrapper SQL transaction function that calls actual transaction function (which has retries)
	public<ResultType> ResultType executeTransaction(Transaction<ResultType> txn) {
		try {
			return doExecuteTransaction(txn);
		} catch (SQLException e) {
			throw new PersistenceException("Transaction failed", e);
		}
	}
	
	// SQL transaction function which retries the transaction MAX_ATTEMPTS times before failing
	public<ResultType> ResultType doExecuteTransaction(Transaction<ResultType> txn) throws SQLException {
		Connection conn = connect();
		
		try {
			int numAttempts = 0;
			boolean success = false;
			ResultType result = null;
			
			while (!success && numAttempts < MAX_ATTEMPTS) {
				try {
					result = txn.execute(conn);
					conn.commit();
					success = true;
				} catch (SQLException e) {
					if (e.getSQLState() != null && e.getSQLState().equals("41000")) {
						// Deadlock: retry (unless max retry count has been reached)
						numAttempts++;
					} else {
						// Some other kind of SQLException
						throw e;
					}
				}
			}
			
			if (!success) {
				throw new SQLException("Transaction failed (too many retries)");
			}
			
			// Success!
			return result;
		} finally {
			DBUtil.closeQuietly(conn);
		}
	}

	// TODO: Here is where you name and specify the location of your Derby SQL database
	// TODO: Change it here and in SQLDemo.java under CS320_LibraryExample_Lab06->edu.ycp.cs320.sqldemo
	// TODO: DO NOT PUT THE DB IN THE SAME FOLDER AS YOUR PROJECT - that will cause conflicts later w/Git
	private Connection connect() throws SQLException {
		Connection conn = DriverManager.getConnection("jdbc:derby:C:/CS320-943-DB/game.db;create=true");		
		
		// Set autocommit() to false to allow the execution of
		// multiple queries/statements as part of the same transaction.
		conn.setAutoCommit(false);
		
		return conn;
	}
	
	//  creates the Authors and Books tables
	public void createTables() {
		executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement user = null;
				PreparedStatement userToGame = null;
				PreparedStatement game = null;
				PreparedStatement gameLog = null;
				PreparedStatement player = null;
				PreparedStatement playerToStats = null;	
				PreparedStatement playerStats = null;
				PreparedStatement playerInventory = null;
				PreparedStatement item = null;	
				PreparedStatement loot = null;
				PreparedStatement map = null;
				PreparedStatement location = null;	
				PreparedStatement locationToNPC = null;
				PreparedStatement npc = null;
				PreparedStatement winCondition = null;	
				PreparedStatement npcToStats = null;
				PreparedStatement npcStats = null;
				PreparedStatement speech = null;	
				PreparedStatement speechOptions = null;
				PreparedStatement speechResponses = null;
				PreparedStatement locationToCombat = null;	
				PreparedStatement combat = null;
				PreparedStatement combatToNPC = null;
				PreparedStatement locationToPuzzle = null;	
				PreparedStatement puzzle = null;
			
				try {
					user = conn.prepareStatement(
						"create table User (" +
						"	user_id integer primary key " +
						"		generated always as identity (start with 1, increment by 1), " +									
						"	username varchar(40)," +
						"	password varchar(40)" +
						")"
					);	
					user.executeUpdate();
					
					System.out.println("User table created");
					
					userToGame = conn.prepareStatement(
						"create table UserToGame (" +
						"	user_id integer constraint user_id references User, " +
						"	game_id integer constraint game_id references Game" +
						")"
					);
					userToGame.executeUpdate();
					
					System.out.println("UserToGame table created");					
					
					game = conn.prepareStatement(
						"create table Game (" +
						"	game_id integer primary key " +
						"		generated always as identity (start with 1, increment by 1), " +
						"	difficulty integer," +
						"	inCombat varchar(5)," +
						"	playerTurnTaken varchar(5)," +
						"	playerCreated varchar(5)," +
						"	timeRemaining integer," +
						"	timerRate integer," +
						"	log_id integer constraint log_id references GameLog," +
						"	player_id integer constraint player_id references Player," +
						"	map_id integer constraint map_id references Map," +
						"	combat_id integer" +
						")"
					);
					game.executeUpdate();
					
					System.out.println("Game table created");	
					
					gameLog = conn.prepareStatement(
						"create table GameLog (" +
						"	log_id integer primary key " +
						"		generated always as identity (start with 1, increment by 1), " +									
						"	order integer," +
						"	output varchar(500)" +
						")"
					);	
					gameLog.executeUpdate();
					
					System.out.println("GameLog table created");
					
					player = conn.prepareStatement(
						"create table Player (" +
						"	player_id integer primary key " +
						"		generated always as identity (start with 1, increment by 1), " +
						"	name varchar(50)," +
						"	icon varchar(200)," +
						"	weapon varchar(50)," +
						"	armor varchar(50)," +
						"	playerCreated varchar(5)," +
						"	location_id integer constraint location_id references Location" +
						")"
					);
					player.executeUpdate();
					
					System.out.println("Player table created");
					
					playerToStats = conn.prepareStatement(
						"create table PlayerToStats (" +
						"	player_id integer constraint player_id references Player, " +
						"	stat_id integer constraint stat_id references PlayerStats" +
						")"
					);
					playerToStats.executeUpdate();
					
					System.out.println("PlayerToStats table created");
						
					
					playerInventory = conn.prepareStatement(
						"create table PlayerInventory (" +
						"	player_id integer constraint player_id references Player, " +
						"	item_id integer constraint item_id references Item" +
						")"
					);
					playerInventory.executeUpdate();
					
					System.out.println("PlayerInventory table created");
					
					
					playerStats = conn.prepareStatement(
						"create table PlayerStats (" +
						"	stat_id integer primary key " +
						"		generated always as identity (start with 1, increment by 1), " +
						"	name varchar(50)," +
						"	amount integer" +
						")"
					);
					playerStats.executeUpdate();
					
					System.out.println("PlayerStats table created");
					
					npcStats = conn.prepareStatement(
						"create table NPCStats (" +
						"	stat_id integer primary key " +
						"		generated always as identity (start with 1, increment by 1), " +
						"	name varchar(50)," +
						"	amount integer" +
						")"
					);
					npcStats.executeUpdate();
					
					System.out.println("NPCStats table created");
					
					npcToStats = conn.prepareStatement(
						"create table NPCToStats (" +
						"	npc_id integer constraint npc_id references NPC, " +
						"	stat_id integer constraint stat_id references NPCStats" +
						")"
					);
					npcToStats.executeUpdate();
					
					System.out.println("NPCToStats table created");
					
					
					item = conn.prepareStatement(
						"create table Item (" +
						"	item_id integer primary key " +
						"		generated always as identity (start with 1, increment by 1), " +
						"	name varchar(50)," +
						"	description varchar(300)," +
						"	isConsumable varchar(5)," +
						"	isWeapon varchar(5)," +
						"	isArmor varchar(5)," +
						"	isTool varchar(5)," +
						"	damage integer," +
						"	healthGain integer," +
						"	value integer," +
						"	amount integer," +
						"	armor integer," +
						"	accuracy double" +
						")"
					);
					item.executeUpdate();
						
					System.out.println("Item table created");
					
					loot = conn.prepareStatement(
						"create table Loot (" +
						"	loot_id integer primary key " +
						"		generated always as identity (start with 1, increment by 1), " +
						"	xp integer," +
						"	collected varchar(5)," +
						"	item_id integer constraint item_id references Item" +
						")"
					);
					loot.executeUpdate();
						
					System.out.println("Loot table created");
					
					map = conn.prepareStatement(
						"create table Map (" +
						"	map_id integer," +
						"	location_id integer constraint location_id references Location," +
						"	north integer," +
						"	east integer," +
						"	south integer," +
						"	west integer," +
						"	extra integer" +
						")"
					);
					map.executeUpdate();
						
					System.out.println("Map table created");
					
					winCondition = conn.prepareStatement(
						"create table WinCondition (" +
						"	winCondition_id integer primary key " +
						"		generated always as identity (start with 1, increment by 1), " +
						"	complete varchar(5)," +
						"	lost varchar(5)," +
						"	wonRooms varchar(5)," +
						"	bestCase varchar(5)," +
						"	defaultCase varchar(5)" +
						")"
					);
					winCondition.executeUpdate();
						
					System.out.println("WinCondition table created");
					
					location = conn.prepareStatement(
						"create table Location (" +
						"	location_id integer primary key " +
						"		generated always as identity (start with 1, increment by 1), " +
						"	name varchar(50)," +
						"	description varchar(500)," +
						"	hidden varchar(5)," +
						"	blocked varchar(5)," +
						"	loot_id integer," +
						"	winCondition_id integer constraint winCondition_id references WinCondition" +
						")"
					);
					location.executeUpdate();
					
					System.out.println("Location table created");
					
					locationToNPC = conn.prepareStatement(
						"create table LocationToNPC (" +
						"	location_id integer constraint location_id references Location, " +
						"	npc_id integer constraint npc_id references NPC" +
						")"
					);
					locationToNPC.executeUpdate();
					
					System.out.println("LocationToNPC table created");
					
					locationToCombat = conn.prepareStatement(
						"create table LocationToCombat (" +
						"	location_id integer constraint location_id references Location, " +
						"	combat_id integer constraint combat_id references Combat" +
						")"
					);
					locationToCombat.executeUpdate();
					
					System.out.println("LocationToCombat table created");
					
					locationToPuzzle = conn.prepareStatement(
						"create table LocationToPuzzle (" +
						"	location_id integer constraint location_id references Location, " +
						"	puzzle_id integer constraint puzzle_id references Puzzle" +
						")"
					);
					locationToPuzzle.executeUpdate();
					
					System.out.println("LocationToPuzzle table created");
					
					combatToNPC = conn.prepareStatement(
						"create table CombatToNPC (" +
						"	combat_id integer constraint combat_id references Combat, " +
						"	npc_id integer constraint npc_id references NPC" +
						")"
					);
					combatToNPC.executeUpdate();
					
					System.out.println("CombatToNPC table created");
					
					npcToStats = conn.prepareStatement(
						"create table NPCToStats (" +
						"	npc_id integer constraint npc_id references NPC," +
						"	stat_id integer constraint stat_id references NPCStats" +
						")"
					);
					npcToStats.executeUpdate();
					
					System.out.println("NPCToStats table created");
					
					npc = conn.prepareStatement(
						"create table NPC (" +
						"	npc_id integer primary key " +
						"		generated always as identity (start with 1, increment by 1), " +
						"	name varchar(50)," +
						"	combat varchar(5)," +
						"	item_id integer," +
						"	speech_id integer constraint speech_id references Speech," +
						"	isIntimidated varchar(5)," +
						"	canIntimidate varchar(5)," +
						"	intimidationThreshold integer," +
						"	isPersuaded varchar(5)," +
						"	canPersuade varchar(5)," +
						"	persuasionThreshold integer" +
						")"
					);
					npc.executeUpdate();
						
					System.out.println("NPC table created");
					
					combat = conn.prepareStatement(
						"create table Combat (" +
						"	combat_id integer primary key " +
						"		generated always as identity (start with 1, increment by 1), " +
						"	turn integer," +
						"	difficulty integer," +
						"	dead varchar(5)" +
						")"
					);
					combat.executeUpdate();
						
					System.out.println("Combat table created");
					
					puzzle = conn.prepareStatement(
						"create table Puzzle (" +
						"	puzzle_id integer primary key " +
						"		generated always as identity (start with 1, increment by 1), " +
						"	prompt varchar(500)," +
						"	answer varchar(50)," +
						"	stat_id integer constraint stat_id references PlayerStats," +
						"	item_id integer constraint item_id references Item," +
						"	result varchar(5)," +
						"	canSolve varchar(5)," +
						"	solved varchar(5)," +
						"	breakable varchar(5)," +
						"	jumpable varchar(5)," +
						"	roomCon varchar(50)" +
						")"
					);
					puzzle.executeUpdate();
						
					System.out.println("Puzzle table created");
					
					speech = conn.prepareStatement(
						"create table Speech (" +
						"	speech_id integer primary key " +
						"		generated always as identity (start with 1, increment by 1), " +
						"	intimidateOption varchar(200)," +
						"	intimidateResponse varchar(200)," +
						"	intimidateResponseFail varchar(200)," +
						"	persuadeOption varchar(200)," +
						"	persuadeResponse varchar(200)," +
						"	persuadeResponseFail varchar(200)," +
						"	prompt varchar(200)" +
						")"
					);
					speech.executeUpdate();
						
					System.out.println("Speech table created");
					
					speechOptions = conn.prepareStatement(
						"create table speechOptions (" +
						"	speech_id integer constraint speech_id references Speech," +								
						"	order integer," +
						"	option varchar(500)" +
						")"
					);	
					speechOptions.executeUpdate();
					
					System.out.println("SpeechOptions table created");
					
					
					speechResponses = conn.prepareStatement(
						"create table speechResponses (" +
						"	speech_id integer constraint speech_id references Speech," +								
						"	order integer," +
						"	response varchar(500)" +
						")"
					);	
					speechResponses.executeUpdate();
					
					System.out.println("SpeechResponses table created");
					
					
					return true;
				} finally {
					DBUtil.closeQuietly(user);
					DBUtil.closeQuietly(userToGame);
					DBUtil.closeQuietly(game);
					DBUtil.closeQuietly(gameLog);
					DBUtil.closeQuietly(player);
					DBUtil.closeQuietly(playerToStats);	
					DBUtil.closeQuietly(playerStats);
					DBUtil.closeQuietly(playerInventory);
					DBUtil.closeQuietly(item);	
					DBUtil.closeQuietly(loot);
					DBUtil.closeQuietly(map);
					DBUtil.closeQuietly(location);	
					DBUtil.closeQuietly(locationToNPC);
					DBUtil.closeQuietly(npc);
					DBUtil.closeQuietly(winCondition);	
					DBUtil.closeQuietly(npcToStats);
					DBUtil.closeQuietly(npcStats);
					DBUtil.closeQuietly(speech);	
					DBUtil.closeQuietly(speechOptions);
					DBUtil.closeQuietly(speechResponses);
					DBUtil.closeQuietly(locationToCombat);	
					DBUtil.closeQuietly(combat);
					DBUtil.closeQuietly(combatToNPC);
					DBUtil.closeQuietly(locationToPuzzle);	
					DBUtil.closeQuietly(puzzle);
				}
			}
		});
	}
	
	// Create a new game, which will insert onto the existing table! 
	// Takes a User's ID, which will be inserted as a new pair in the UserToGame table.
	public void createNewGame(int user_id) {
		executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				//
				// Load Initial Data from the CSVs. 
				//
				List<User> userList;
				List<Pair<Integer, Integer>> userToGame; 
				List<Game> gameList;  
				List<ArrayList<String>> gameLogList; 
				List<Player> playerList; 
				List<Map> mapList; 
				List<Pair<Integer, Integer>> playerToStats;
				List<Pair<Integer, Integer>> playerInventory; 
				List<Stat> playerStatsList; 
				List<Item> itemList; 
				List<Loot> lootList;
				List<Location> locationList; 
				List<Pair<Integer, Integer>> locationToNPC; 
				List<WinCondition> winConditionList;
				List<NPC> npcList; 
				List<Pair<Integer, Integer>> npcToStats; 
				List<Stat> npcStatsList; 
				List<Speech> speechList; 
				HashMap<Integer, ArrayList<String>> speechOptions;
				HashMap<Integer, ArrayList<String>> speechResponses; 
				List<Pair<Integer, Integer>> locationToCombat; 
				List<Pair<Integer, Integer>> combatToNPC; 
				List<Pair<Integer, Integer>> locationToPuzzle; 
				List<Combat> combatList; 
				List<Puzzle> puzzleList; 
				
				userList = new ArrayList<User>();
				userToGame = new ArrayList<Pair<Integer, Integer>>();
				gameList = new ArrayList<Game>();
				gameLogList = new ArrayList<ArrayList<String>>();
				playerList = new ArrayList<Player>();
				mapList = new ArrayList<Map>();
				playerToStats = new ArrayList<Pair<Integer, Integer>>();
				playerInventory = new ArrayList<Pair<Integer, Integer>>();
				playerStatsList = new ArrayList<Stat>();
				itemList = new ArrayList<Item>();
				lootList = new ArrayList<Loot>();
				locationList = new ArrayList<Location>();
				locationToNPC = new ArrayList<Pair<Integer, Integer>>();
				winConditionList = new ArrayList<WinCondition>();
				npcList = new ArrayList<NPC>();
				npcToStats = new ArrayList<Pair<Integer, Integer>>();
				npcStatsList = new ArrayList<Stat>();
				speechList = new ArrayList<Speech>();
				speechOptions = new HashMap<Integer, ArrayList<String>>();
				speechResponses = new HashMap<Integer, ArrayList<String>>();
				locationToCombat = new ArrayList<Pair<Integer, Integer>>();
				combatToNPC = new ArrayList<Pair<Integer, Integer>>();
				locationToPuzzle = new ArrayList<Pair<Integer, Integer>>();
				combatList = new ArrayList<Combat>();
				puzzleList = new ArrayList<Puzzle>();
				
				try {
					userList.addAll(InitialData.getUser());
					userToGame.addAll(InitialData.getUserToGame());
					 
					gameLogList.addAll(InitialData.getGameLog());
					
					playerToStats.addAll(InitialData.getPlayerToStats());
					playerInventory.addAll(InitialData.getPlayerInventory());
					playerStatsList.addAll(InitialData.getPlayerStats()); 
					itemList.addAll(InitialData.getItem());
					lootList.addAll(InitialData.getLoot(itemList));
					
					locationToNPC.addAll(InitialData.getLocationToNPC());
					winConditionList.addAll(InitialData.getWinCondition());
					
					npcToStats.addAll(InitialData.getNPCToStats());
					npcStatsList.addAll(InitialData.getNPCStats());
					
					speechOptions = InitialData.getSpeechOptions(); 
					speechResponses = InitialData.getSpeechResponses(); 
					speechList.addAll(InitialData.getSpeech(speechOptions, speechResponses)); 
					
					locationToCombat.addAll(InitialData.getLocationToCombat());
					combatToNPC.addAll(InitialData.getCombatToNPC());
					locationToPuzzle.addAll(InitialData.getLocationToPuzzle()); 
					puzzleList.addAll(InitialData.getPuzzle(playerStatsList, itemList));
					
					npcList.addAll(InitialData.getNPC(itemList, speechList, npcToStats, npcStatsList));
					combatList.addAll(InitialData.getCombat(npcList, combatToNPC));
					locationList.addAll(InitialData.getLocation(lootList, winConditionList, 
							locationToNPC, npcList, locationToCombat, combatList, locationToPuzzle, puzzleList));
					mapList.addAll(InitialData.getMap(locationList));
					playerList.addAll(InitialData.getPlayer(playerStatsList, playerToStats, 
							itemList, playerInventory, locationList));
					gameList.addAll(InitialData.getGame(playerList,gameLogList, mapList, combatList));
				} catch (IOException e) {
					throw new SQLException("Couldn't read initial data", e);
				}

				//	
				// Get constants for the largest ID in each table, as this will determine the
				// shift forward for the IDs in the new game, as to avoid overwritten data. 
				//
				
				int userMax, gameMax, gameLogMax, playerMax, playerStatsMax, itemMax, lootMax, mapMax, locationMax, 
					npcMax, npcStatsMax, speechMax, combatMax, puzzleMax, winConditionMax; 
				
				gameMax = getLargestIdInTable("Game", "game_id");
				gameLogMax = getLargestIdInTable("GameLog", "log_id");
				playerMax = getLargestIdInTable("Player", "player_id");
				playerStatsMax = getLargestIdInTable("PlayerStats", "stat_id");
				itemMax = getLargestIdInTable("Item", "item_id");
				lootMax = getLargestIdInTable("Loot", "loot_id");
				mapMax = getLargestIdInTable("Map", "map_id");
				locationMax = getLargestIdInTable("Location", "location_id");
				npcMax = getLargestIdInTable("NPC", "npc_id");
				npcStatsMax = getLargestIdInTable("NPCStats", "stat_id");
				//speechMax = getLargestIdInTable("Speech", "speech_id");
				combatMax = getLargestIdInTable("Combat", "combat_id");
				puzzleMax = getLargestIdInTable("Puzzle", "puzzle_id");
				winConditionMax = getLargestIdInTable("WinCondition", "winCondition_id");
				
				
				//
				// Call the insert methods for inserting a new game. 
				//

				
				
				
				
				return true; 
				
			}
		});
	}

	
	// The main method creates the database tables and loads the initial data.
	public static void main(String[] args) throws IOException {
		System.out.println("Creating tables...");
		DerbyDatabase db = new DerbyDatabase();
		db.createTables();
		
		// Add default users: robbyw (tbags) and admin (admin)
		System.out.println("Adding default users...");
		
		System.out.println("Creating new game for User ID 1 and User ID 2...");
		db.createNewGame(1);
		db.createNewGame(2);
		
		System.out.println("Game DB successfully initialized!");
	}
}
