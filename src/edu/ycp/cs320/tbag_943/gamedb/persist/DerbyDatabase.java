package edu.ycp.cs320.tbag_943.gamedb.persist;

import java.io.IOException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
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
	
	
	//
	// Find Queries
	//
	
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
							"select User.user_id, User.username, User.password" + 
							"from User" +
							"User.username = ? and User.password = ?");
					stmt1.setString(1, username);
					stmt1.setString(2, password);
					
					
					resultSet1 = stmt1.executeQuery();
					
					while(resultSet1.next()) {
						found = true;
						
						user.setId(resultSet1.getInt(1));
						user.setUsername(resultSet1.getString(2));
						user.setPassword(resultSet1.getString(3));
						
					}
					if(!found) {
						System.out.println("This user doesn't exist.");
						return null; 
					}
					
					// Set user as created; always is created if in DB!
					user.setCreated(true);
					
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
					
					resultSet1 = stmt1.executeQuery();
					
					while (resultSet1.next()) {
						found = true;
						
						Game game = new Game();
						
						game.setId(resultSet1.getInt(1));
						game.setDifficulty(resultSet1.getInt(2));
						game.getTimer().setTime(resultSet1.getInt(3));
						game.getPlayer().setName(resultSet1.getString(4));
						userGames.add(game);
					}
					
					if(!found) {
						System.out.println("User doesn't have any current games");
						return null;
					}
					
					return userGames;
					
				} finally {
					DBUtil.closeQuietly(resultSet1);
					DBUtil.closeQuietly(stmt1);
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
				int log_id = 0, player_id = 0, map_id = 0, combat_id = -1; 
				
				// This method will need to call the other find methods in order to 
				// load the entire game with properly assigned references. 
				
				try {
					stmt1 = conn.prepareStatement(
							"select Game.game_id, Game.difficulty, Game.inCombat, " +
							"Game.playerTurnTaken, Game.playerNotCreated, " +
							"Game.timeRemaining, Game.timerRate, Game.log_id, " +
							"Game.player_id, Game.map_id, Game.combat_id " +
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
						game.setPlayerNotCreated(Boolean.parseBoolean(resultSet1.getString(5)));
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
						return null; 
					}
					
					// Use the Find Methods and IDs to get the Player, Map, and Log. 
					// These methods may need parameters so that subtables aren't loaded more than once!
					// In fact, we may need to find all data and then use them as parameters. 
					// Ex: Calling findLocationsByMapId to have a list of locations
					Map map = findMapByMapID(map_id);
					
					// Set Player Location by accessing generated Map. 
					Player player = findPlayerByPlayerId(player_id, map.getLocations().values()); 
					
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
					
					HashMap<Integer, ArrayList<Integer>> locIdsAndConn = new HashMap<Integer, ArrayList<Integer>>();
					
					boolean found = false;
					
					while(resultSet.next()) {
						found = true;
						
						map.setId(resultSet.getInt(1));
						
						int locId = resultSet.getInt(2); 
						
						ArrayList<Integer> connections = new ArrayList<Integer>();
						
						connections.add(resultSet.getInt(3)); 
						connections.add(resultSet.getInt(4));
						connections.add(resultSet.getInt(5));
						connections.add(resultSet.getInt(6));
						connections.add(resultSet.getInt(7));
						
						// Put Connections into map. 
						
						locIdsAndConn.put(locId, connections); 
						
					}
					
					if (!found) {
						System.out.println("<" + mapId + "> was not found in the Map table");
						return null; 
					}
					
					// Generate HashMap of Locations from keyset: 
					HashMap<String, Location> locations = new HashMap<String, Location>();
					for(Integer locId : locIdsAndConn.keySet()) {
						Location loc = findLocationByLocationID(locId); 
						locations.put(loc.getName().toLowerCase(), loc); 
					}
					
					// Generate the Connections map using list of Locations. 
					HashMap<String, ArrayList<String>> cons = new HashMap<String, ArrayList<String>>(); 
					
					for(Location loc : locations.values()) {
						String name = loc.getName().toLowerCase(); 
						ArrayList<Integer> connections = locIdsAndConn.get(loc.getId());
						ArrayList<String> stringCons = new ArrayList<String>();
						for(int i = 0; i < connections.size(); i++) {
							if(connections.get(i) == -1) {
								stringCons.add("-1"); 
							} else {
								Location l = findLocationByLocationID(connections.get(i)); 
								stringCons.add(l.getName().toLowerCase());
							}	
						}
						
						cons.put(name, stringCons); 
					}
					
					// Put locations and cons into Map. 
					map.setConnections(cons);
					map.setLocations(locations);
					
					return map;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	
	public Location findLocationByLocationID(int locationId) {
		
		// Add NPCs to Combat by first adding NPCs to Location, then finding IDs to put 
		// in combat so that the same objects aren't generated more than once! 
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
						location.setHidden(Boolean.parseBoolean(resultSet.getString(4)));
						location.setBlocked(Boolean.parseBoolean(resultSet.getString(5)));
						
						// Set Loot
						Loot loot = findLootByLootID(resultSet.getInt(6));
						location.setTreasure(loot);
						
						// Set WinCondition
						WinCondition wc = findWinConditionByWinConditionId(resultSet.getInt(7));
						location.setWinCondition(wc);
						
						// Set the NPCs
						List<Integer> npcIds = findNPCIdsByLocationID(location.getId());
						ArrayList<NPC> npcs = new ArrayList<NPC>();
						for(Integer i : npcIds) {
							NPC n = findNPCByNPCId(i);
							npcs.add(n);
						}
						location.setNPCs(npcs);
						
						// Set Combats using list of NPCs to set NPCs in Location. 
						List<Integer> combatIds = findCombatIdsByLocationID(location.getId());
						ArrayList<Combat> combats = new ArrayList<Combat>();
						for(Integer i : combatIds) {
							Combat c = findCombatsByCombatID(i);
							
							// Fetch NPCs
							HashMap<String, NPC> cnpcs = new HashMap<String, NPC>(); 
							List<Integer> combatNPCIds = findNPCsIdByCombatID(i); 
							for(Integer cn : combatNPCIds) {
								for(NPC n : npcs) {
									if(n.getId() == cn) {
										cnpcs.put(n.getName().toLowerCase(), n);
									}
								}
							}
							c.setNpcs(cnpcs);
							combats.add(c);
						}
						
						location.setCombats(combats);
						
						// Set Puzzles
						List<Integer> puzzleIds = findPuzzleIdsByLocationID(location.getId());
						ArrayList<Puzzle> puzzles = new ArrayList<Puzzle>();
						for(Integer i : puzzleIds) {
							Puzzle p = findPuzzleByPuzzleId(i); 
							puzzles.add(p);
						}
						
						location.setPuzzles(puzzles);
						
					}
					
					if (!found) {
						System.out.println("<" + locationId + "> was not found in the Location table");
						return null; 
					}
					
					return location;
					
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}


		
						// Location_ID input
						// this method goes into the junction table and returns the corresponding NPC_ID from the 
						// Location_ID input
				public List<Integer> findNPCIdsByLocationID(int locationID){
					return executeTransaction(new Transaction<List<Integer>>() {
						@Override
						public List<Integer> execute(Connection conn) throws SQLException {
							PreparedStatement stmt1 = null;
								ResultSet resultSet1 = null;
								Boolean found = false;
								
								try {
									stmt1 = conn.prepareStatement(
											"select LocationToNPC.npc_id " +
											"from LocationToNPC " + 
													"where LocationToNPC.location_id = ?"
											);
									stmt1.setInt(1, locationID);
									
									// creating list and NPC object to set the ID and store the ID
									ArrayList<Integer> NPCsIds = new ArrayList<Integer>();
									NPC npc = new NPC();
									
									resultSet1 = stmt1.executeQuery();
										
									// running through the npc_id and assigning them to the NPC object and adding them
									//to the array list
									while(resultSet1.next()) {
										found = true;
											
										npc.setId(resultSet1.getInt(1));
										NPCsIds.add(npc.getId());
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
				
	// this method finds the NPC by the NPC_ID and gets all of its passive attributes

	public NPC findNPCByNPCId(int NPCId){
		return executeTransaction(new Transaction<NPC>() {
			@Override
			public NPC execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				ResultSet resultSet1 = null;
				Boolean found = false;
				
				try {
					stmt1 = conn.prepareStatement(
							"select NPC.* " +
							"from NPC " + 
							"where NPC.npc_id = ? "
							);
					stmt1.setInt(1, NPCId);
					

					// creating NPC object to apply desired attributes 

					NPC npc = new NPC();
					
					resultSet1 = stmt1.executeQuery();
					
					
					// applying the attributes to the NPC
					if(resultSet1.next()) {
						found = true;
						

						npc.setId(NPCId);
						npc.setName(resultSet1.getString(2));
						npc.setCombat(Boolean.parseBoolean(resultSet1.getString(3)));
						npc.setWeapon(findItemByItemID(resultSet1.getInt(4)));
						npc.setSpeech(findSpeechBySpeechId(resultSet1.getInt(5)));
						npc.setIntimidated(Boolean.parseBoolean(resultSet1.getString(6)));
						npc.setCanIntimidate(Boolean.parseBoolean(resultSet1.getString(7)));
						npc.setIntimidationThreshold(resultSet1.getInt(8));
						npc.setPersuaded(Boolean.parseBoolean(resultSet1.getString(9)));
						npc.setCanPersuade(Boolean.parseBoolean(resultSet1.getString(10)));
						npc.setPersuasionThreshold(resultSet1.getInt(11));
						
						// Set NPC Stats
						npc.setStats(findNPCStatsByNPCID(NPCId));
						
						
					}
					
					if(!found) {
						System.out.println("No NPCs in this location");
						return null; 
					}
					return npc;
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

	// this method goes into the Stat table and retrieves the Stat attributes

	public Stat findNPCStatByStatId(int StatId){
			return executeTransaction(new Transaction<Stat>() {
				@Override
				public Stat execute(Connection conn) throws SQLException {
					PreparedStatement stmt1 = null;
					ResultSet resultSet1 = null;
					Boolean found = false;
					
					try {
						stmt1 = conn.prepareStatement(
								"select Stat.name, Stat.rank " +
								"from Stat " + 
										"where Stat.stat_id = ? "
								);
						stmt1.setInt(1, StatId);
						
						// creating stat object
						Stat stat = new Stat();
						
						// running through and translating stat from the DB
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
			}
		});
	}
	
	public Speech findSpeechBySpeechId(int speechId) {
		return executeTransaction(new Transaction<Speech>() {
			@Override
			public Speech execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				ResultSet resultSet1 = null;
				
				try {
					stmt1 = conn.prepareStatement(
							"select Speech.* " +
							"  from  Speech" +
							"  where speech_id = ? "
					);
					stmt1.setInt(1, speechId);
					
					resultSet1 = stmt1.executeQuery();
					
					// for testing that a result was returned
					Boolean found = false;

					Speech speechGet = new Speech(); 
					
					while (resultSet1.next()) {
						found = true;
						
						speechGet.setId(resultSet1.getInt(1));
						speechGet.setIntimOp(resultSet1.getString(2));
						speechGet.setIntimRes(resultSet1.getString(3));
						speechGet.setIntimResFail(resultSet1.getString(4));
						speechGet.setPersOp(resultSet1.getString(5));
						speechGet.setPersRes(resultSet1.getString(6));
						speechGet.setPersResFail(resultSet1.getString(7));
						
						// Set SpeechOptions
						speechGet.setSpeechOptions(findSpeechOptionsBySpeechId(speechGet.getId()));
						
						// Set SpeechResponses
						speechGet.setSpeechResponses(findSpeechResponsesBySpeechId(speechGet.getId()));
						
					}
					
					// check if the speechID was found
					if (!found) {
						System.out.println("<" + speechId + "> was not found in the Speech table");
					}
					
					return speechGet;
					
				} finally {
					DBUtil.closeQuietly(resultSet1);
					DBUtil.closeQuietly(stmt1);
				}
			});
		}
	
	public ArrayList<String> findSpeechOptionsBySpeechId(int speechId) {
		return executeTransaction(new Transaction<ArrayList<String>>() {
			@Override
			public ArrayList<String> execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				ResultSet resultSet1 = null;
				
				try {
					stmt1 = conn.prepareStatement(
							"select SpeechOptions.option " +
							"  from  SpeechOptions " +
							"  where speech_id = ? " +
							"  order by SpeechOptions.order ASC"
					);
					stmt1.setInt(1, speechId);
					
					resultSet1 = stmt1.executeQuery();
					
					// for testing that a result was returned
					Boolean found = false;

					ArrayList<String> speechGet = new ArrayList<String>(); 
					
					while (resultSet1.next()) {
						found = true;
						
						// add speech option to array list, then loop
						speechGet.add(resultSet1.getString(1));
					}
					
					// check if the speechID was found
					if (!found) {
						System.out.println("<" + speechId + "> was not found in the SpeechOptions table");
					}
					
					return speechGet;
					
				} finally {
					DBUtil.closeQuietly(resultSet1);
					DBUtil.closeQuietly(stmt1);
				}
			}
		});
	}
	
	public ArrayList<String> findSpeechResponsesBySpeechId(int speechId) {
		return executeTransaction(new Transaction<ArrayList<String>>() {
			@Override
			public ArrayList<String> execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				ResultSet resultSet1 = null;
				
				try {
					stmt1 = conn.prepareStatement(
							"select SpeechResponses.response " +
							"  from  SpeechResponses" +
							"  where speech_id = ? " +
							"  order by SpeechResponses.order ASC"
					);
					stmt1.setInt(1, speechId);
					
					resultSet1 = stmt1.executeQuery();
					
					// for testing that a result was returned
					Boolean found = false;

					ArrayList<String> speechGet = new ArrayList<String>(); 
					
					while (resultSet1.next()) {
						found = true;
						
						// add speech option to array list, then loop
						speechGet.add(resultSet1.getString(1));
					}
					
					// check if the speechID was found
					if (!found) {
						System.out.println("<" + speechId + "> was not found in the SpeechResponses table");
					}
					
					return speechGet;
					
				} finally {
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
							"select CombatToNPC.npc_id " +
							"from CombatToNPC " + 
								"where CombatToNPC.combat_id = ? " 
							);
					stmt1.setInt(1, combatID);
					
					ArrayList<Integer> NPCIds = new ArrayList<Integer>();
					
					resultSet1 = stmt1.executeQuery();
					
					while(resultSet1.next()) {
						found = true;
						
						NPCIds.add(resultSet1.getInt(1));
					}
					
					if(!found) {
						System.out.println("No NPCs in this combat...");
						return null; 
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
						loot.setCollected(Boolean.parseBoolean(resultSet1.getString(3))); 
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
	
	public Loot findLootByLootID(int lootID) {
		return executeTransaction(new Transaction<Loot>() {
			@Override
			public Loot execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				ResultSet resultSet1 = null;
				
				try {
					stmt1 = conn.prepareStatement(
							"select Loot.*" +
							"  from  Loot " +
							"  where loot_id = ?"
					);
					stmt1.setInt(1, lootID);
					
					Loot loot = new Loot(); 
					
					resultSet1 = stmt1.executeQuery();
					
					// for testing that a result was returned
					Boolean found = false;
					
					while (resultSet1.next()) {
						found = true;
						
						loot.setId(resultSet1.getInt(1));
						loot.setXP(resultSet1.getInt(2));
						loot.setCollected(Boolean.parseBoolean(resultSet1.getString(3))); 
						loot.setItems(findItemByItemID(resultSet1.getInt(4)));
						
					}
					
					// check if the locationID was found
					if (!found) {
						System.out.println("<" + lootID + "> was not found in the Loot table");
					}
					
					return loot;
				} finally {
					DBUtil.closeQuietly(resultSet1);
					DBUtil.closeQuietly(stmt1);
				}
			}
		});
	}
	
	public Item findItemByItemID(int itemId) {
		return executeTransaction(new Transaction<Item>() {
			@Override
			public Item execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				ResultSet resultSet1 = null;
				
				try {
					stmt1 = conn.prepareStatement(
							"select Item.*" +
							"  from  Item " +
							"  where item_id = ?"
					);
					stmt1.setInt(1, itemId);
					
					Item item = new Item();
					
					resultSet1 = stmt1.executeQuery();
					
					// for testing that a result was returned
					Boolean found = false;
					
					while (resultSet1.next()) {
						found = true;
						
						item.setId(resultSet1.getInt(1));
						item.setName(resultSet1.getString(2));
						item.setDes(resultSet1.getString(3));
						item.setConsumable(Boolean.parseBoolean(resultSet1.getString(4)));
						item.setArmor(Boolean.parseBoolean(resultSet1.getString(6)));
						item.setWeapon(Boolean.parseBoolean(resultSet1.getString(5)));
						item.setTool(Boolean.parseBoolean(resultSet1.getString(7)));
						item.setDamage(resultSet1.getInt(8));
						item.setHealthGain(resultSet1.getInt(9));
						item.setValue(resultSet1.getInt(10));
						item.setAmount(resultSet1.getInt(11));
						item.setArmor(resultSet1.getInt(12));
						item.setAccuracy(resultSet1.getDouble(13));
						
					}
					
					// check if the locationID was found
					if (!found) {
						System.out.println("<" + itemId + "> was not found in the Item table");
					}
					
					return item;
				} finally {
					DBUtil.closeQuietly(resultSet1);
					DBUtil.closeQuietly(stmt1);
				}
			}
		});
	}
	
	public ArrayList<Integer> findCombatIdsByLocationID(int locationId) {
		return executeTransaction(new Transaction<ArrayList<Integer>>() {
			@Override
			public ArrayList<Integer> execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				ResultSet resultSet1 = null;
				
				try {
					stmt1 = conn.prepareStatement(
							"select combat_id " +
							"  from  LocationToCombat " +
							"  where location_id = ? "
					);
					stmt1.setInt(1, locationId);
					
					ArrayList<Integer> combatIds = new ArrayList<Integer>();
					
					resultSet1 = stmt1.executeQuery();
					
					// for testing that a result was returned
					Boolean found = false;
					
					while (resultSet1.next()) {
						found = true;
						
						combatIds.add(resultSet1.getInt(1));
						
					}
					// check if the locationID was found
					if (!found) {
						System.out.println("<" + locationId + "> was not found in the Location table");
					}
					
					return combatIds;
				} finally {
					DBUtil.closeQuietly(resultSet1);
					DBUtil.closeQuietly(stmt1);
				}
			}
		});
	}
	
	public Combat findCombatsByCombatID(int combatId) {
		return executeTransaction(new Transaction<Combat>() {
			@Override
			public Combat execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				ResultSet resultSet1 = null;
				
				try {
					stmt1 = conn.prepareStatement(
							"select * " +
							"  from  Combat " +
							"  where combat_id = ? "
					);
					stmt1.setInt(1, combatId);
					
					Combat combat = new Combat();
					
					resultSet1 = stmt1.executeQuery();
					
					// for testing that a result was returned
					Boolean found = false;
					
					while (resultSet1.next()) {
						found = true;
						
						combat.setId(resultSet1.getInt(1));
						combat.setTurn(resultSet1.getInt(2));
						combat.setDifficulty(resultSet1.getInt(3));
						combat.setDead(Boolean.parseBoolean(resultSet1.getString(4)));
						
					}
					// check if the combatID was found
					if (!found) {
						System.out.println("<" + combatId + "> was not found in the Combat table");
					}
					
					return combat;
				} finally {
					DBUtil.closeQuietly(resultSet1);
					DBUtil.closeQuietly(stmt1);
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
							"select puzzle_id "
							+ " from LocationToPuzzle "
							+ " where location_id = ? "
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
						//TODO: Set Required Skill and Item
						
						puzzle.setResult(Boolean.parseBoolean(resultSet.getString(6)));
						puzzle.setCanSolve(Boolean.parseBoolean(resultSet.getString(7)));
						puzzle.setSolved(Boolean.parseBoolean(resultSet.getString(8)));
						puzzle.setBreakable(Boolean.parseBoolean(resultSet.getString(9)));
						puzzle.setJumpable(Boolean.parseBoolean(resultSet.getString(10)));
						
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
						winCondition.setComplete(Boolean.parseBoolean(resultSet.getString(2)));
						winCondition.setLost(Boolean.parseBoolean(resultSet.getString(3)));
						winCondition.setWonRooms(Boolean.parseBoolean(resultSet.getString(4)));
						winCondition.setBestCase(Boolean.parseBoolean(resultSet.getString(5)));
						winCondition.setDefaultCase(Boolean.parseBoolean(resultSet.getString(6)));
					}
					
					if (!found) {
						System.out.println("<" + winCondition_id + "> was not found in the WinCondition table");
					}
					
					return winCondition;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	
	public Player findPlayerByPlayerId(int playerId, Collection<Location> locations) {
		return executeTransaction(new Transaction<Player>() {
			@Override
			public Player execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				ResultSet resultSet1 = null;
				
				try {
					stmt1 = conn.prepareStatement(
							"select * " +
							"  from  Player " +
							"  where player_id = ? "
					);
					stmt1.setInt(1, playerId);
					
					Player player = new Player();
					int locationId;
					
					resultSet1 = stmt1.executeQuery();
					
					// for testing that a result was returned
					Boolean found = false;
					
					while (resultSet1.next()) {
						found = true;
						
						player.setId(resultSet1.getInt(1));
						player.setName(resultSet1.getString(2));
						player.setIcon(resultSet1.getString(3));
						player.setWeapon(resultSet1.getString(4));
						player.setArmor(resultSet1.getString(5));
						player.setPlayerCreated(Boolean.parseBoolean(resultSet1.getString(6)));
						//get location id from result, find location from id
						locationId = resultSet1.getInt(7);
						Location playerLoc = null; 
						for(Location l : locations) {
							if(l.getId() == locationId) {
								playerLoc = l; 
								break; 
							}
						}
						player.setLocation(playerLoc);
						
						// Get Player Inventory
						List<Integer> inventoryIds = findPlayerInventoryIdsByPlayerId(player.getId()); 
						HashMap<String, Item> inventory = new HashMap<String, Item>();
						for(Integer i : inventoryIds) {
							Item item = findItemByItemID(i); 
							inventory.put(item.getName().toLowerCase(), item);
						}
						
						player.setInventory(inventory);
						
						// Get Player Stats
						HashMap<String, Stat> stats = findPlayerStatsByPlayerID(player.getId());
						player.setStats(stats);
						
					}
					// check if the locationID was found
					if (!found) {
						System.out.println("<" + playerId + "> was not found in the Combat table");
					}
					
					return player;
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
	
	/*public Stat findPlayerStatByStatId(int statId) {
		return executeTransaction(new Transaction<Stat>() {
			@Override
			public Stat execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				ResultSet resultSet1 = null;
				
				try {
					stmt1 = conn.prepareStatement(
							"select *" +
							"  from  PlayerStats " +
							"  where stat_d = ?"
					);
					stmt1.setInt(1, statId);
					
					Stat stat = new Stat(); 
					
					resultSet1 = stmt1.executeQuery();
					
					// for testing that a result was returned
					Boolean found = false;
					
					while (resultSet1.next()) {
						found = true;
						
						stat.setId(resultSet1.getInt(1));
						stat.setName(resultSet1.getString(2));
						stat.setRank(resultSet1.getInt(3));
						
					}
					
					// check if the locationID was found
					if (!found) {
						System.out.println("<" + statId + "> was not found in the Loot table");
					}
					
					return stat;
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
	}*/
	
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
	
	public ArrayList<String> findGameLogByGameLogId(int gameLogId) {
		return executeTransaction(new Transaction<ArrayList<String>>() {
			@Override
			public ArrayList<String> execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				ResultSet resultSet1 = null;
				
				try {
					stmt1 = conn.prepareStatement(
							"select output " +
							"  from  GameLog " +
							"  where log_id = ?" +
							"  order by GameLog.order ASC"
					);
					stmt1.setInt(1, gameLogId);
					
					ArrayList<String> gameLog = new ArrayList<String>(); 
					
					resultSet1 = stmt1.executeQuery();
					
					// for testing that a result was returned
					Boolean found = false;
					
					while (resultSet1.next()) {
						found = true;
						
						gameLog.add(resultSet1.getString(1));
						
					}
					
					// check if the locationID was found
					if (!found) {
						System.out.println("<" + gameLogId + "> was not found in the GameLog table");
					}
					
					return gameLog;
				} finally {
					DBUtil.closeQuietly(resultSet1);
					DBUtil.closeQuietly(stmt1);
				}
			}
		});
	}

	

	//
	// Insert Queries
	//	
	
	public Integer insertNewUserByUsernameAndPassword(String username, String password) {
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
											
				try {
					
					// insert new string of output into the GameLog table. 
					// prepare SQL insert statement to add new output.
					stmt1 = conn.prepareStatement(
							"insert into User (username, password) " +
							"  values(?, ?) "
					);
					
					stmt1.setString(1, username);
					stmt1.setString(2, password);
						
					stmt1.executeUpdate();
					
					System.out.println("User " + username + " inserted into User table");
									
					
					return 0;
					
				} finally {
					DBUtil.closeQuietly(stmt1);
				}
			}
		});
	}

	
	public Integer insertUserToGameByUserIdAndGameId(int userId, int gameId) {
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
											
				try {
					
					stmt1 = conn.prepareStatement(
							"insert into UserToGame (user_id, game_id) " +
							"  values(?, ?) "
					);
					
					stmt1.setInt(1, userId);
					stmt1.setInt(2, gameId);
				
					
					// execute the update
					stmt1.executeUpdate();					
					
					System.out.println("User #" + userId + " matched to Game #" + gameId + " in UserToGame");					
					
					return 0;
				} finally {
					DBUtil.closeQuietly(stmt1);
				}
			}
		});
	}
	
	public Integer insertNewUser(List<User> users) {
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
											
				try {
					
					// insert new Player into Player table
					// prepare SQL insert statement to add new Player to Player table
					stmt1 = conn.prepareStatement(
							"insert into User (username, password) " +
							"  values(?, ?) "
					);
					
					for(User user : users) {
						// Need to insert all Users in. 
						// ID is auto-generated. 
						
						stmt1.setString(1, user.getUsername());
						stmt1.setString(2, user.getPassword());
						stmt1.addBatch();
					}
					
					// execute the batch
					stmt1.executeBatch();					
					
					System.out.println("New User Entries inserted into User table");					
					
					return 0;
				} finally {
					DBUtil.closeQuietly(stmt1);
				}
			}
		});
	}
	
	public Integer insertNewGame(Game game, int gameMax) {
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
							"insert into Game (game_id, difficulty, inCombat, playerTurnTaken, " +
							"playerNotCreated, timeRemaining, timerRate, log_id, player_id, " +
							"map_id, combat_id) " +
							"  values(?,?,?, ?,?,?,?,?, ?,?,?) "
					);
					
					
					stmt1.setInt(1, game.getId() + gameMax);
					stmt1.setInt(2, game.getDifficulty());
					stmt1.setString(3, Boolean.toString(game.isInCombat()));
					stmt1.setString(4, Boolean.toString(game.isPlayerTurnTaken()));
					stmt1.setString(5, Boolean.toString(game.getPlayerNotCreated()));
					stmt1.setInt(6, game.getTimer().getTime());
					stmt1.setInt(7, game.getTimer().getTimerRate());
					stmt1.setInt(8, game.getId() + gameMax);
					stmt1.setInt(9, game.getId() + gameMax);
					stmt1.setInt(10, game.getId() + gameMax);
					stmt1.setInt(11, -1);
					
					
					
					// execute the update
					stmt1.executeUpdate();					
					
					System.out.println("New Game Entries inserted into Game table");					
					
					return 0;
				} finally {
					DBUtil.closeQuietly(stmt1);
				}
			}
		});
	}
	
	public Integer insertNewItem(List<Item> items, int itemMaxId) {
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
							"insert into Item (item_id, name, description, isConsumable, " +
							"isWeapon, isArmor, isTool, damage, healthGain, value, amount, armor, accuracy) " +
							"  values(?,?,?, ?,?,?,?,?, ?,?,?,?,?) "
					);
					
					for(Item item : items) {
						// Need to insert all items in. 
						// Increase item ID by max ID in Item table
						stmt1.setInt(1, item.getId() + itemMaxId);
						stmt1.setString(2, item.getName());
						stmt1.setString(3, item.getDes());
						stmt1.setString(4, Boolean.toString(item.isConsumable()));
						stmt1.setString(5, Boolean.toString(item.getIsWeapon()));
						stmt1.setString(6, Boolean.toString(item.getIsArmor()));
						stmt1.setString(7, Boolean.toString(item.getIsTool()));
						stmt1.setInt(8, item.getDamage());
						stmt1.setInt(9, item.getHealthGain());
						stmt1.setInt(10, item.getValue());
						stmt1.setInt(11, item.getAmount());
						stmt1.setInt(12, item.getArmor());
						stmt1.setDouble(13, item.getAccuracy());
						stmt1.addBatch();
					}
					
					// execute the batch
					stmt1.executeBatch();					
					
					System.out.println("New Item Entries inserted into Item table");					
					
					return 0;
				} finally {
					DBUtil.closeQuietly(stmt1);
				}
			}
		});
	}
	
	public Integer insertNewWinConditions(List<WinCondition> winConditions, int WCMaxID) {
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				
				try {
					stmt = conn.prepareStatement(
							"insert into WinCondition(winCondition_id, complete, lost, wonRooms, bestCase, defaultCase) "
							+ " values(?, ?, ?, ?, ?, ?) "
					);
					for(WinCondition winCondition : winConditions) {
						stmt.setInt(1, winCondition.getId() + WCMaxID);
						stmt.setString(2, Boolean.toString(winCondition.getComplete()));
						stmt.setString(3, Boolean.toString(winCondition.getLost()));
						stmt.setString(4,  Boolean.toString(winCondition.getWonRooms()));
						stmt.setString(5,  Boolean.toString(winCondition.getBestCase()));
						stmt.setString(6,  Boolean.toString(winCondition.getDefaultCase()));
						stmt.addBatch();
					}
					
					stmt.executeBatch();
					
					System.out.println("New WinConditions have been inserted into WinConditions table");
					
					return 0;
				} finally {
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	
	public Integer insertNewLoot(List<Loot> loots, int lootMaxId) {
		// Note, this method inserts a new row for the new player and default values.
		// Character creation should call an update for the Player class! 
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
											
				try {
					
					
					stmt1 = conn.prepareStatement(
							"insert into Loot (loot_id, xp, collected, item_id) " +
							"  values(?,?,?,?) "
					);


					for(Loot loot : loots) {
						stmt1.setInt(1, loot.getId() + lootMaxId);
						stmt1.setInt(2, loot.getXp());
						stmt1.setString(3, Boolean.toString(loot.isCollected()));
						stmt1.setInt(4, loot.getItem().getId());
						stmt1.addBatch();
					}
					
					// execute the insertions
					stmt1.executeBatch();
					
					System.out.println("New Loots inserted into Loot table");					
					
					return 0;
				} finally {
					DBUtil.closeQuietly(stmt1);
				}
			}
		});
	}
	
	public Integer insertNewPuzzle(List<Puzzle> puzzles, int puzzleMaxId, int statMax, int itemMax) {
		// Note, this method inserts a new row for the new player and default values.
		// Character creation should call an update for the Player class! 
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
											
				try {
					
					
					stmt1 = conn.prepareStatement(
							"insert into Puzzle (puzzle_id, prompt, answer, stat_id, item_id,"
							+ " result, canSolve, solved, breakable, jumpable, "
							+ " roomCon) " +
							"  values(?,?,?,?,? ,?,?,?,?,? ,?) "
					);
					for(Puzzle puzzle : puzzles) {
						stmt1.setInt(1, puzzle.getId() + puzzleMaxId);
						stmt1.setString(2, puzzle.getPrompt());
						stmt1.setString(3, puzzle.getAnswer());
						stmt1.setInt(4,puzzle.getRequiredSkill().getId() + statMax);
						stmt1.setInt(5,puzzle.getRequiredItem().getId() + itemMax);
						stmt1.setString(6, Boolean.toString(puzzle.getResult()));
						stmt1.setString(7, Boolean.toString(puzzle.isCanSolve()));
						stmt1.setString(8, Boolean.toString(puzzle.isSolved()));
						stmt1.setString(9, Boolean.toString(puzzle.getBreakable()));
						stmt1.setString(10, Boolean.toString(puzzle.isJumpable()));
						stmt1.setString(11, puzzle.getRoomCon());
						stmt1.addBatch();
					}
					
					
					// execute the update
					stmt1.executeBatch();
					
					System.out.println("New Puzzles inserted into Puzzle table");					
					
					return 0;
				} finally {
					DBUtil.closeQuietly(stmt1);
				}
			}
		});
	}
	
	public Integer insertNewCombat(List<Combat> combats, int combatMax) {
		// Note, this method inserts a new row for the new player and default values.
		// Character creation should call an update for the Player class! 
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
											
				try {
					
					
					stmt1 = conn.prepareStatement(
							"insert into Combat (combat_id, turn, difficulty, dead) " +
							"  values(?, ?, ?, ?) "
					);
					for(Combat combat : combats) {
						stmt1.setInt(1, combat.getId() + combatMax);
						stmt1.setInt(2, combat.getTurn());
						stmt1.setInt(3, combat.getDifficulty());
						stmt1.setString(4, Boolean.toString(combat.isDead()));
						stmt1.addBatch();
					}
					
					
					// execute the update
					stmt1.executeBatch();
					
					System.out.println("New Combats inserted into Combat table");					
					
					return 0;
				} finally {
					DBUtil.closeQuietly(stmt1);
				}
			}
		});
	}
	
	public Integer insertNewSpeech(List<Speech> speeches) {
		// Note, this method inserts a new row for the new player and default values.
		// Character creation should call an update for the Player class! 
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
											
				try {
					
					stmt1 = conn.prepareStatement(
							"insert into Speech (speech_id, intimidateOption, intimidateResponse, intimidateResponseFail,"
							+ " persuadeOption, persuadeResponse, persuadeResponseFail " +
							"  values(?,?,?,?,? ,?,?) "
					);
					for(Speech speech : speeches) {
						stmt1.setInt(1,speech.getId());
						stmt1.setString(2, speech.getIntimOp());
						stmt1.setString(3, speech.getIntimRes());
						stmt1.setString(4, speech.getIntimResFail());
						stmt1.setString(5, speech.getPersOp());
						stmt1.setString(6, speech.getPersRes());
						stmt1.setString(7, speech.getPersResFail());
						stmt1.addBatch();
					}
					
					
					// execute the update
					stmt1.executeBatch();
					
					System.out.println("New Speech inserted into Speech table");					
					
					return 0;
				} finally {
					DBUtil.closeQuietly(stmt1);
				}
			}
		});
	}
	
	public Integer insertNewSpeechOption(HashMap<Integer, ArrayList<String>> speechOptions) {
		// Note, this method inserts a new row for the new player and default values.
		// Character creation should call an update for the Player class! 
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
											
				try {
					
					stmt1 = conn.prepareStatement(
							"insert into SpeechOptions (speech_id, order, option)" +
							"  values(?, ?, ?) "
					);
					
					for(Integer i : speechOptions.keySet()) {
						ArrayList<String> options = speechOptions.get(i); 
						for(int j = 0; j < options.size(); j++) {
							stmt1.setInt(1, i);
							stmt1.setInt(2, j);
							stmt1.setString(3, options.get(j));
							stmt1.addBatch();
						}
						
					}
					
					// execute the update
					stmt1.executeUpdate();
					
					System.out.println("New SpeechOptions inserted into SpeechOptions table");					
					
					return 0;
				} finally {
					DBUtil.closeQuietly(stmt1);
				}
			}
		});
	}
	
	public Integer insertNewSpeechResponse(HashMap<Integer, ArrayList<String>> speechOptions) {
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
											
				try {
					
					stmt1 = conn.prepareStatement(
							"insert into SpeechResponses (speech_id, order, response)" +
							"  values(?, ?, ?) "
					);
					
					for(Integer i : speechOptions.keySet()) {
						ArrayList<String> options = speechOptions.get(i); 
						for(int j = 0; j < options.size(); j++) {
							stmt1.setInt(1, i);
							stmt1.setInt(2, j);
							stmt1.setString(3, options.get(j));
							stmt1.addBatch();
						}
						
					}
					
					// execute the update
					stmt1.executeUpdate();
					
					System.out.println("New SpeechResponses inserted into SpeechResponses table");					
					
					return 0;
				} finally {
					DBUtil.closeQuietly(stmt1);
				}
			}
		});
	}
	
	public Integer insertNewNPCs(List<NPC> npcList, int npcMaxId, int itemMaxId) {
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				// for saving NPC ID 

				// try to retrieve author_id (if it exists) from DB, for Author's full name, passed into query
				try {
					stmt1 = conn.prepareStatement(
							"insert into NPC (NPC.npc_id, NPC.name, NPC.combat, NPC.item_id, NPC.speech_id, "
							+ "NPC.isIntimidatedm, NPC.canIntimidate, NPC.intimidationThreshold, "
							+ "NPC.isPersuaded, NPC.canPersuade, NPC.persuasionThreshold)  " +
							" values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "
					);
					
					for(NPC npc : npcList) {
						stmt1.setInt(1, npc.getId() + npcMaxId);
						stmt1.setString(2, npc.getName());
						stmt1.setString(3, Boolean.toString(npc.isCombat()));
						stmt1.setInt(4, npc.getWeapon().getId() + itemMaxId);
						stmt1.setInt(5, npc.getSpeech().getId());
						stmt1.setString(6, Boolean.toString(npc.isIntimidated()));
						stmt1.setString(7, Boolean.toString(npc.canIntimidate()));
						stmt1.setInt(8, npc.getIntimidationThreshold());
						stmt1.setString(9, Boolean.toString(npc.isPersuaded()));
						stmt1.setString(10, Boolean.toString(npc.canPersuade()));
						stmt1.setInt(11, npc.getPersuasionThreshold());
						stmt1.addBatch();
					}
					
					// execute the query, get the result
					stmt1.executeBatch();
					
					System.out.println("New NPCs inserted into NPC table");	
					return 0;
				} finally {
					DBUtil.closeQuietly(stmt1);
				}
        	}
		});
	}

	public Integer InsertNewNPCStats(List<Stat> npcStats, int npcStatsMaxId) {
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				
				try {
					stmt1 = conn.prepareStatement(
							"insert into NPCStats (stat_id, name, amount)  " +
							" values(?, ?, ?) "
					);
  					
  					
  					for(Stat stat: npcStats) {
  						stmt1.setInt(1, stat.getId() + npcStatsMaxId);
  						stmt1.setString(2, stat.getName());
  						stmt1.setInt(3, stat.getRank());
  						stmt1.addBatch();
  					}
  					stmt1.executeBatch();
  					
  					System.out.println("New NPCStats inserted into NPCStats table");
  					
  					return 0;
				}finally {
					DBUtil.closeQuietly(stmt1);
				}
				
			}
		});
	}
	
	public Integer insertNewPlayerStats(List<Stat> playerStats, int playerStatsMaxId) {
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				
				try {
					stmt1 = conn.prepareStatement(
							"insert into PlayerStats (stat_id, name, amount)  " +
							" values(?, ?, ?) "
					);
  					
  					
  					for(Stat stat: playerStats) {
  						stmt1.setInt(1, stat.getId() + playerStatsMaxId);
  						stmt1.setString(2, stat.getName());
  						stmt1.setInt(3, stat.getRank());
  						stmt1.addBatch();
  					}
  					stmt1.executeBatch();
  					
  					System.out.println("New PlayerStats inserted into PlayerStats table");
  					
  					return 0;
				}finally {
					DBUtil.closeQuietly(stmt1);
				}
				
			}
		});
	}
	
	public Integer insertNewLocations(List<Location> locations, int locationMaxId, int lootMaxId, int winConditionMaxId) {
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				
				try {
					stmt = conn.prepareStatement(
						"insert into Location (location_id, name, description, hidden, blocked, loot_id, winCondition_id) "
						+ " values(?, ?, ?, ?, ?, ?, ?)"	
					);
					
					for(Location location : locations) {
						stmt.setInt(1,  location.getId() + locationMaxId);
						stmt.setString(2,  location.getName());
						stmt.setString(3,  location.getDescription());
						stmt.setString(4,  Boolean.toString(location.isHidden()));
						stmt.setString(5,  Boolean.toString(location.getBlocked()));
						stmt.setInt(6, location.getTreasure().getId() + lootMaxId);
						stmt.setInt(7, location.getWinCondition().getId() + winConditionMaxId);
						stmt.addBatch();
					}
					
		
					stmt.executeBatch();
					
					System.out.println("New Locations have been inserted into Location table");
					
					return 0;
				} finally {
					DBUtil.closeQuietly(stmt);

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
					
					
					for (Location loc : map.getLocations().values()) {
						stmt1.setInt(1, mapId);
						
						// Compute the values for each column in the row first. 
						int locId = loc.getId() + location_rows;
						stmt1.setInt(2, locId);
						
						ArrayList<String> locationConn = map.getConnections().get(loc.getName().toLowerCase());
						
						for(int a = 0; a < locationConn.size(); a++) {
							String name = locationConn.get(a); 
							// Check if there is no connection in this direction. 
							if(name.equals("-1")) {
								// No connection, assign value of -1. 
								stmt1.setInt(3 + a, -1); 
							} else {
								int connId = map.getLocations().get(a).getId() + location_rows;
								stmt1.setInt(3 + a, connId);
							}
						}
						
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
	

	public Integer insertNewPlayerToStats(List<Pair<Integer, Integer>> playerToStats, int player_rows, int playerstat_rows) {
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
					
					// Add each stat with matching playerId to the table, incremented by number of rows.
					for (Pair<Integer, Integer> PTS : playerToStats) {
						stmt1.setInt(1, PTS.getLeft() + player_rows);
						int statId = PTS.getRight() + playerstat_rows;
						stmt1.setInt(2, statId);
						stmt1.addBatch();
					}
					stmt1.executeBatch();
					
					System.out.println("Player #" + (player_rows+1) + " inserted into PlayerToStats table");
									
					
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
							"insert into PlayerInventory (player_id, item_id) " +
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
	
	public Integer InsertNewLocationToNPC(List<Pair<Integer, Integer>> locationToNPC, int maxLocationId, int maxNPCId) {
  		return executeTransaction(new Transaction<Integer>() {
  			@Override
			public Integer execute(Connection conn) throws SQLException {
  				PreparedStatement stmt1 = null;
				int resultSet1 = 0;
  				
  				try {
  					stmt1 = conn.prepareStatement(
							"insert into LocationToNPC (location_id, npc_id)  " +
							" values(?, ?)"
					);
  					
  					for(Pair<Integer, Integer> ltn : locationToNPC) {
  						stmt1.setInt(1, ltn.getLeft() + maxLocationId);
  						stmt1.setInt(2, ltn.getRight() + maxNPCId); 
  						stmt1.addBatch();
  					}
  					
  					
		  			// execute the query, get the result
					stmt1.executeBatch();

					return 0; 
  					
  				} finally {
  					DBUtil.closeQuietly(stmt1);
  				}
  			}
  		});
  	}

	public Integer InsertNewNPCToStats(List<Pair<Integer, Integer>> NPCToStats, int maxNPCId, int maxNPCStatsId) {
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				int resultSet1 = 0;
				
				try {
					stmt1 = conn.prepareStatement(
							"insert into NPCToStats (npc_id, stat_id)  " +
							" values(?, ?) "
					);
  					
  					for(Pair<Integer, Integer> nts : NPCToStats) {
  						stmt1.setInt(1, nts.getLeft() + maxNPCId);
  						stmt1.setInt(2, nts.getRight() + maxNPCStatsId);
  						stmt1.addBatch();
  					}
  					stmt1.executeBatch();
  					
  					System.out.println("New NPCToStats inserted into NPCToStats table");
  					
  					return 0;
				}finally {
					DBUtil.closeQuietly(stmt1);
				}
				
			}
		});
	}
	
	public Integer InsertNewCombatToNPC(List<Pair<Integer, Integer>> CombatToNPC, int maxCombatId, int maxNPCId) {
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				int resultSet1 = 0;
				try {
					stmt1 = conn.prepareStatement(
							"insert into CombatToNPC (combat_id, npc_id)  " +
							" values(?, ?) "
					);
					
					for(Pair<Integer, Integer> ctn : CombatToNPC) {
  						stmt1.setInt(1, ctn.getLeft() + maxCombatId);
  						stmt1.setInt(2, ctn.getRight() + maxNPCId);
  						stmt1.addBatch();
					}
  					
	  				// execute the query, get the result
					stmt1.executeBatch();
					
					System.out.println("New CombatToNPC values inserted.");
					
				} finally {
					DBUtil.closeQuietly(stmt1);
				}
				return 0;
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
					
					System.out.println("New LocationToCombat table inserted.");
									
					
					return 0;
					
				} finally {
					DBUtil.closeQuietly(stmt1);
				}
			}
		});
	}
	
	public Integer insertNewLocationToPuzzle(List<Pair<Integer, Integer>> ltpPairs, int location_rows, int puzzle_rows) {
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
											
				try {
					
					// insert all location to combat pairs. 
					// prepare SQL insert statement to add each pair.
					stmt1 = conn.prepareStatement(
							"insert into LocationToPuzzle (location_id, puzzle_id) " +
							"  values(?, ?) "
					);

					// Add each pair, incremented by number of rows in their table.
					for (Pair<Integer, Integer> p : ltpPairs) {
						stmt1.setInt(1, p.getLeft() + location_rows);
						stmt1.setInt(2, p.getRight() + puzzle_rows);
						stmt1.addBatch();
					}
					stmt1.executeBatch();
					
					System.out.println("New LocationToPuzzle table inserted.");
									
					
					return 0;
					
				} finally {
					DBUtil.closeQuietly(stmt1);
				}
			}
		});
	}
	
	public Integer insertNewUserToGame(List<Pair<Integer, Integer>> utgPairs) {
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
											
				try {
					
					// insert all location to combat pairs. 
					// prepare SQL insert statement to add each pair.
					stmt1 = conn.prepareStatement(
							"insert into UserToGame (user_id, game_id) " +
							"  values(?, ?) "
					);

					// Add each pair, incremented by number of rows in their table.
					for (Pair<Integer, Integer> p : utgPairs) {
						stmt1.setInt(1, p.getLeft());
						stmt1.setInt(2, p.getRight());
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
						
					// Update GameLog table
					stmt1.executeUpdate();
					
					System.out.println("Output #" + log_size + " inserted into GameLog table for Log #" + log_id);
									
					
					return 0;
					
				} finally {
					DBUtil.closeQuietly(stmt1);
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

	//
	// Update Queries
	//
	
	public Boolean updateGameByGameId(Game game, Location previous) {
		// Update the previous Location. 
		updateLocationByLocationId(previous); 
		
		// Update the Player.
		updatePlayerByPlayerId(game.getPlayer()); 
		
		// Update the Player's current Location.
		updateLocationByLocationId(game.getPlayer().getLocation()); 
		return true; 
	}

	public Boolean updatePlayerByPlayerId(Player player) {
		// Update the player's stats.
		for(Stat stat : player.getStats().values()) {
			updatePlayerStatByStatId(stat);
		}
		
		// Update player fields. 
		return executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				
				try {
					stmt1 = conn.prepareStatement(
							"update Player " +
							"  set name = ?, weapon = ?, armor = ?, playerCreated = ?, location_id = ?" +
							"  where player_id = ?"
					);
					
					stmt1.setString(1, player.getName());
					stmt1.setString(2, player.getWeapon());
					stmt1.setString(3, player.getArmor());
					stmt1.setString(4, Boolean.toString(player.getPlayerCreated()));
					stmt1.setInt(5, player.getLocation().getId());
					stmt1.setInt(6, player.getId());
					
					stmt1.executeUpdate();
					
					System.out.println("Player #" + player.getId() + " updated");					
					
					return true;
				} finally {
					DBUtil.closeQuietly(stmt1);
				}
			}
        });
	}
	
	public Boolean updatePlayerStatByStatId(Stat stat) {
		
		return executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				
				try {
					stmt1 = conn.prepareStatement(
							"update PlayerStats " +
							"  set amount = ? " +
							"  where stat_id = ?"
					);
					
					stmt1.setInt(1, stat.getRank());
					stmt1.setInt(2, stat.getId());
					
					stmt1.executeUpdate();
					
					System.out.println("Stat #" + stat.getId() + " updated");					
					
					return true;
				} finally {
					DBUtil.closeQuietly(stmt1);
				}
			}
        });
		
	}
	
	//public Boolean updateMapByMapId(Map map) {
		// Update each Location. 
	//}
	
	public Boolean updateLocationByLocationId(Location location) {
		// Update NPCs
		for(NPC npc : location.getNPCs().values()) {
			UpdateNPCByNPCId(npc); 
		}
		
		// Update Combats
		for(Combat c : location.getCombats()) {
			updateCombatByCombatId(c); 
		}
		
		// Update Puzzles
		for(Puzzle p : location.getPuzzles()) {
			updatePuzzleByPuzzleId(p);
		}
		
		// Update Loot
		updateLootByLootId(location.getTreasure());
		
		// Update Location fields. 
		return executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				
				try {
					stmt1 = conn.prepareStatement(
							"update Location " +
							"  set hidden = ?, blocked = ?" +
							"  where location_id = ?"
					);
					
					stmt1.setString(1, Boolean.toString(location.isHidden()));
					stmt1.setString(2, Boolean.toString(location.getBlocked()));
					stmt1.setInt(3, location.getId());
					
					stmt1.executeUpdate();
					
					System.out.println("Location #" + location.getId() + " updated");					
					
					return true;
				} finally {
					DBUtil.closeQuietly(stmt1);
				}
			}
        });
	}
	
	public Boolean UpdateNPCByNPCId(NPC npc) {
		// Update NPCStats
		for(Stat stat : npc.getStats().values()) {
			updateNPCStatsByStatId(stat); 
		}
		
		// Update NPC Fields
		return executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				
				try {
					stmt1 = conn.prepareStatement(
							"Update NPC " +
							" Set NPC.combat = ?, NPC.isIntimidated = ?, NPC.canIntimidate = ?, NPC.isPersuaded = ?, NPC.canPersuade = ?" + 
									"where NPC.NPC_id = ?"
					);
  					stmt1.setString(1, Boolean.toString(npc.isIntimidated()));
  					stmt1.setString(2, Boolean.toString(npc.getCanIntimidate()));
  					stmt1.setString(3, Boolean.toString(npc.isPersuaded()));
  					stmt1.setString(4, Boolean.toString(npc.getCanPersuade()));
  					stmt1.setInt(5, npc.getId());
  					
					stmt1.executeUpdate();
					
					System.out.println("NPC #" + npc.getId() + " updated");		
					
					return true;
				}finally{
					DBUtil.closeQuietly(stmt1);
				}
			}
        });
	}

	public Boolean updateNPCStatsByStatId(Stat stat) {
		return executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				
				try {
					stmt1 = conn.prepareStatement(
							"update NPCStats " +
							"  set rank = ? " +
							"  where stat_id = ?"
					);
					
					stmt1.setInt(1, stat.getRank());
					stmt1.setInt(2, stat.getId());
					
					stmt1.executeUpdate();
					
					System.out.println("NPCStat #" + stat.getId() + " updated");					
					
					return true;
				} finally {
					DBUtil.closeQuietly(stmt1);
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
	
	public boolean updatePuzzleByPuzzleId(Puzzle puzzle) {
		return executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				
				try {
					stmt = conn.prepareStatement(
						"update Puzzle "
						+ " set result = ?, solved = ? "
						+ " where Puzzle.puzzle_id = ? "	
					);
					stmt.setString(1, Boolean.toString(puzzle.getResult()));
					stmt.setString(2, Boolean.toString(puzzle.isSolved()));
					stmt.setInt(3,  puzzle.getId());
					
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
					stmt.setString(2,  Boolean.toString(winCondition.getComplete()));
					stmt.setString(3,  Boolean.toString(winCondition.getLost()));
					stmt.setString(4,  Boolean.toString(winCondition.getWonRooms()));
					stmt.setString(5,  Boolean.toString(winCondition.getBestCase()));
					stmt.setString(6,  Boolean.toString(winCondition.getDefaultCase()));
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
		
	
	//
	// Removal Queries
	//
	
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
	
	
	// 
	// Other Queries
	// 
	
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
	
	//  creates the tables for the Game DB
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
						"	username varchar(40) unique," +
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
						"	game_id integer primary key, " +
						"	difficulty integer," +
						"	inCombat varchar(5)," +
						"	playerTurnTaken varchar(5)," +
						"	playerNotCreated varchar(5)," +
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
						"	log_id integer, " +									
						"	order integer," +
						"	output varchar(500)" +
						")"
					);	
					gameLog.executeUpdate();
					
					System.out.println("GameLog table created");
					
					player = conn.prepareStatement(
						"create table Player (" +
						"	player_id integer primary key, " +
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
						"	stat_id integer primary key, " +
						"	name varchar(50)," +
						"	amount integer" +
						")"
					);
					playerStats.executeUpdate();
					
					System.out.println("PlayerStats table created");
					
					npcStats = conn.prepareStatement(
						"create table NPCStats (" +
						"	stat_id integer primary key, " +
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
						"	item_id integer primary key, " +
						"	name varchar(50), " +
						"	description varchar(300), " +
						"	isConsumable varchar(5), " +
						"	isWeapon varchar(5), " +
						"	isArmor varchar(5), " +
						"	isTool varchar(5), " +
						"	damage integer, " +
						"	healthGain integer, " +
						"	value integer, " +
						"	amount integer, " +
						"	armor integer, " +
						"	accuracy double" +
						")"
					);
					item.executeUpdate();
						
					System.out.println("Item table created");
					
					loot = conn.prepareStatement(
						"create table Loot (" +
						"	loot_id integer primary key, " +
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
						"	winCondition_id integer primary key, " +
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
						"	location_id integer primary key, " +
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
						"	npc_id integer primary key, " +
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
						"	combat_id integer primary key, " +
						"	turn integer," +
						"	difficulty integer," +
						"	dead varchar(5)" +
						")"
					);
					combat.executeUpdate();
						
					System.out.println("Combat table created");
					
					puzzle = conn.prepareStatement(
						"create table Puzzle (" +
						"	puzzle_id integer primary key, " +
						"	prompt varchar(500)," +
						"	answer varchar(50)," +
						"	stat_id integer," +
						"	item_id integer," +
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
						"	speech_id integer primary key, " +
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
				
				int gameMax, gameLogMax, playerMax, playerStatsMax, itemMax, lootMax, mapMax, locationMax, 
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
				
				// Order of Insertion due to Constraints: Item, Loot, Speech, SpeechOptions, SpeechResponses, 
				// PlayerStats, NPCStats, WinCondition, Location, NPC, Combat, Puzzle, LocationToNPC, 
				// LocationToCombat, LocationToPuzzle, CombatToNPC, NPCToStats, Player, PlayerStats, 
				// PlayerToStats, PlayerInventory, Map, GameLog, Game, UserToGame. 

				insertNewItem(itemList, itemMax); 
				insertNewLoot(lootList, lootMax); 
				
				// Only insert Speech tables if it is the very first insert of a Game. 
				// Speech never changes across games, so no need to repopulate the table. 
				if(gameMax == 0) {
					insertNewSpeech(speechList); 
					insertNewSpeechOption(speechOptions); 
					insertNewSpeechResponse(speechResponses);
				}
				
				insertNewPlayerStats(playerStatsList, playerStatsMax); 
				InsertNewNPCStats(npcStatsList, npcStatsMax); 
				insertNewWinConditions(winConditionList, winConditionMax); 
				insertNewLocations(locationList, locationMax, lootMax, winConditionMax); 
				insertNewNPCs(npcList, npcMax, itemMax);
				insertNewCombat(combatList, combatMax); 
				insertNewPuzzle(puzzleList, puzzleMax, playerStatsMax, itemMax); 
				InsertNewLocationToNPC(locationToNPC, locationMax, npcMax); 
				insertNewLocationToCombat(locationToCombat, locationMax, combatMax); 
				insertNewLocationToPuzzle(locationToPuzzle, locationMax, puzzleMax); 
				InsertNewCombatToNPC(combatToNPC, combatMax, npcMax);
				InsertNewNPCToStats(npcToStats, npcMax, npcStatsMax); 
				insertNewPlayer(playerList.get(0), locationMax, playerMax); 
				insertNewPlayerStats(playerStatsList, playerStatsMax); 
				insertNewPlayerToStats(playerToStats, playerMax, playerStatsMax); 
				insertNewPlayerInventory(playerList.get(0), playerMax, itemMax);
				insertNewMap(mapList.get(0), gameMax, locationMax); 
				insertNewGameLog(gameLogList.get(0), gameMax); 
				
				insertNewGame(gameList.get(0), gameMax); 
				
				// Insert UserToGame based on user_id and the new Game's ID
				insertUserToGameByUserIdAndGameId(user_id, gameMax + 1); 
				
				
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
		db.insertNewUserByUsernameAndPassword("robbyw", "tbags"); 
		db.insertNewUserByUsernameAndPassword("admin", "admin");
		
		System.out.println("Creating new game for User ID 1 and User ID 2...");
		db.createNewGame(1);
		db.createNewGame(2);
		
		System.out.println("Game DB successfully initialized!");
	}
}
