package edu.ycp.cs320.tbag_943.gamedb.persist;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.ycp.cs320.booksdb.model.Author;
import edu.ycp.cs320.booksdb.model.Book;
import edu.ycp.cs320.booksdb.model.BookAuthor;
import edu.ycp.cs320.booksdb.model.Pair;
import edu.ycp.cs320.tbag_943.classes.*; 

//Code comes from CS320 Library Example. 
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
	
	
	
	public int getNumberRowsInTable(String table) {
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				ResultSet resultSet1 = null;
				
				try {
					stmt1 = conn.prepareStatement(
							"select COUNT(*) as total from ?"
					);
					stmt1.setString(1, table);
					
					int rows = 0; 
					
					resultSet1 = stmt1.executeQuery();
					
					// for testing that a result was returned
					Boolean found = false;
					
					while (resultSet1.next()) {
						found = true;
						
						rows = resultSet1.getInt("total");
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
	
	// transaction that retrieves a Book, and its Author by Title
	@Override
	public List<Pair<Author, Book>> findAuthorAndBookByTitle(final String title) {
		return executeTransaction(new Transaction<List<Pair<Author,Book>>>() {
			@Override
			public List<Pair<Author, Book>> execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement(
							"select authors.*, books.* " +
							"  from  authors, books, bookAuthors " +
							"  where books.title = ? " +
							"    and authors.author_id = bookAuthors.author_id " +
							"    and books.book_id     = bookAuthors.book_id"
					);
					stmt.setString(1, title);
					
					List<Pair<Author, Book>> result = new ArrayList<Pair<Author,Book>>();
					
					resultSet = stmt.executeQuery();
					
					// for testing that a result was returned
					Boolean found = false;
					
					while (resultSet.next()) {
						found = true;
						
						Author author = new Author();
						loadAuthor(author, resultSet, 1);
						Book book = new Book();
						loadBook(book, resultSet, 4);
						
						result.add(new Pair<Author, Book>(author, book));
					}
					
					// check if the title was found
					if (!found) {
						System.out.println("<" + title + "> was not found in the books table");
					}
					
					return result;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	
	
	// transaction that retrieves a list of Books with their Authors, given Author's last name
	@Override
	public List<Pair<Author, Book>> findAuthorAndBookByAuthorLastName(final String lastName) {
		return executeTransaction(new Transaction<List<Pair<Author,Book>>>() {
			@Override
			public List<Pair<Author, Book>> execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;

				// try to retrieve Authors and Books based on Author's last name, passed into query
				try {
					stmt = conn.prepareStatement(
							"select authors.*, books.* " +
							"  from  authors, books, bookAuthors " +
							"  where authors.lastname = ? " +
							"    and authors.author_id = bookAuthors.author_id " +
							"    and books.book_id     = bookAuthors.book_id "   +
							"  order by books.title asc, books.published asc"
					);
					stmt.setString(1, lastName);
					
					// establish the list of (Author, Book) Pairs to receive the result
					List<Pair<Author, Book>> result = new ArrayList<Pair<Author,Book>>();
					
					// execute the query, get the results, and assemble them in an ArrayLsit
					resultSet = stmt.executeQuery();
					while (resultSet.next()) {
						Author author = new Author();
						loadAuthor(author, resultSet, 1);
						Book book = new Book();
						loadBook(book, resultSet, 4);
						
						result.add(new Pair<Author, Book>(author, book));
					}
					
					return result;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	
	
	// transaction that retrieves all Books in Library, with their respective Authors
	@Override
	public List<Pair<Author, Book>> findAllBooksWithAuthors() {
		return executeTransaction(new Transaction<List<Pair<Author,Book>>>() {
			@Override
			public List<Pair<Author, Book>> execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement(
							"select authors.*, books.* " +
							"  from authors, books, bookAuthors " +
							"  where authors.author_id = bookAuthors.author_id " +
							"    and books.book_id     = bookAuthors.book_id "   +
							"  order by books.title asc"
					);
					
					List<Pair<Author, Book>> result = new ArrayList<Pair<Author,Book>>();
					
					resultSet = stmt.executeQuery();
					
					// for testing that a result was returned
					Boolean found = false;
					
					while (resultSet.next()) {
						found = true;
						
						Author author = new Author();
						loadAuthor(author, resultSet, 1);
						Book book = new Book();
						loadBook(book, resultSet, 4);
						
						result.add(new Pair<Author, Book>(author, book));
					}
					
					// check if any books were found
					if (!found) {
						System.out.println("No books were found in the database");
					}
					
					return result;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}	
	
	
	// transaction that retrieves all Authors in Library
	@Override
	public List<Author> findAllAuthors() {
		return executeTransaction(new Transaction<List<Author>>() {
			@Override
			public List<Author> execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					stmt = conn.prepareStatement(
							"select * from authors " +
							" order by lastname asc, firstname asc"
					);
					
					List<Author> result = new ArrayList<Author>();
					
					resultSet = stmt.executeQuery();
					
					// for testing that a result was returned
					Boolean found = false;
					
					while (resultSet.next()) {
						found = true;
						
						Author author = new Author();
						loadAuthor(author, resultSet, 1);
						
						result.add(author);
					}
					
					// check if any authors were found
					if (!found) {
						System.out.println("No authors were found in the database");
					}
					
					return result;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	
	
	// transaction that inserts new Book into the Books table
	// also first inserts new Author into Authors table, if necessary
	// and then inserts entry into BookAuthors junction table
	@Override
	public Integer insertBookIntoBooksTable(final String title, final String isbn, final int published, final String lastName, final String firstName) {
		return executeTransaction(new Transaction<Integer>() {
			@Override
			public Integer execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				PreparedStatement stmt2 = null;
				PreparedStatement stmt3 = null;
				PreparedStatement stmt4 = null;
				PreparedStatement stmt5 = null;
				PreparedStatement stmt6 = null;				
				
				ResultSet resultSet1 = null;
				ResultSet resultSet3 = null;
				ResultSet resultSet5 = null;				
				
				// for saving author ID and book ID
				Integer author_id = -1;
				Integer book_id   = -1;

				// try to retrieve author_id (if it exists) from DB, for Author's full name, passed into query
				try {
					stmt1 = conn.prepareStatement(
							"select author_id from authors " +
							"  where lastname = ? and firstname = ? "
					);
					stmt1.setString(1, lastName);
					stmt1.setString(2, firstName);
					
					// execute the query, get the result
					resultSet1 = stmt1.executeQuery();

					
					// if Author was found then save author_id					
					if (resultSet1.next())
					{
						author_id = resultSet1.getInt(1);
						System.out.println("Author <" + lastName + ", " + firstName + "> found with ID: " + author_id);						
					}
					else
					{
						System.out.println("Author <" + lastName + ", " + firstName + "> not found");
				
						// if the Author is new, insert new Author into Authors table
						if (author_id <= 0) {
							// prepare SQL insert statement to add Author to Authors table
							stmt2 = conn.prepareStatement(
									"insert into authors (lastname, firstname) " +
									"  values(?, ?) "
							);
							stmt2.setString(1, lastName);
							stmt2.setString(2, firstName);
							
							// execute the update
							stmt2.executeUpdate();
							
							System.out.println("New author <" + lastName + ", " + firstName + "> inserted in Authors table");						
						
							// try to retrieve author_id for new Author - DB auto-generates author_id
							stmt3 = conn.prepareStatement(
									"select author_id from authors " +
									"  where lastname = ? and firstname = ? "
							);
							stmt3.setString(1, lastName);
							stmt3.setString(2, firstName);
							
							// execute the query							
							resultSet3 = stmt3.executeQuery();
							
							// get the result - there had better be one							
							if (resultSet3.next())
							{
								author_id = resultSet3.getInt(1);
								System.out.println("New author <" + lastName + ", " + firstName + "> ID: " + author_id);						
							}
							else	// really should throw an exception here - the new author should have been inserted, but we didn't find them
							{
								System.out.println("New author <" + lastName + ", " + firstName + "> not found in Authors table (ID: " + author_id);
							}
						}
					}
					
					// now insert new Book into Books table
					// prepare SQL insert statement to add new Book to Books table
					stmt4 = conn.prepareStatement(
							"insert into books (title, isbn, published) " +
							"  values(?, ?, ?) "
					);
					stmt4.setString(1, title);
					stmt4.setString(2, isbn);
					stmt4.setInt(3, published);
					
					// execute the update
					stmt4.executeUpdate();
					
					System.out.println("New book <" + title + "> inserted into Books table");					

					// now retrieve book_id for new Book, so that we can set up BookAuthor entry
					// and return the book_id, which the DB auto-generates
					// prepare SQL statement to retrieve book_id for new Book
					stmt5 = conn.prepareStatement(
							"select book_id from books " +
							"  where title = ? and isbn = ? and published = ? "
									
					);
					stmt5.setString(1, title);
					stmt5.setString(2, isbn);
					stmt5.setInt(3, published);

					// execute the query
					resultSet5 = stmt5.executeQuery();
					
					// get the result - there had better be one
					if (resultSet5.next())
					{
						book_id = resultSet5.getInt(1);
						System.out.println("New book <" + title + "> ID: " + book_id);						
					}
					else	// really should throw an exception here - the new book should have been inserted, but we didn't find it
					{
						System.out.println("New book <" + title + "> not found in Books table (ID: " + book_id);
					}
					
					// now that we have all the information, insert entry into BookAuthors table
					// which is the junction table for Books and Authors
					// prepare SQL insert statement to add new Book to Books table
					stmt6 = conn.prepareStatement(
							"insert into bookAuthors (book_id, author_id) " +
							"  values(?, ?) "
					);
					stmt6.setInt(1, book_id);
					stmt6.setInt(2, author_id);
					
					// execute the update
					stmt6.executeUpdate();
					
					System.out.println("New entry for book ID <" + book_id + "> and author ID <" + author_id + "> inserted into BookAuthors junction table");						
					
					System.out.println("New book <" + title + "> inserted into Books table");					
					
					return book_id;
				} finally {
					DBUtil.closeQuietly(resultSet1);
					DBUtil.closeQuietly(stmt1);
					DBUtil.closeQuietly(stmt2);					
					DBUtil.closeQuietly(resultSet3);
					DBUtil.closeQuietly(stmt3);					
					DBUtil.closeQuietly(stmt4);
					DBUtil.closeQuietly(resultSet5);
					DBUtil.closeQuietly(stmt5);
					DBUtil.closeQuietly(stmt6);
				}
			}
		});
	}
	
	
	// transaction that deletes Book (and possibly its Author) from Library
	@Override
	public List<Author> removeBookByTitle(final String title) {
		return executeTransaction(new Transaction<List<Author>>() {
			@Override
			public List<Author> execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				PreparedStatement stmt2 = null;
				PreparedStatement stmt3 = null;
				PreparedStatement stmt4 = null;
				PreparedStatement stmt5 = null;
				PreparedStatement stmt6 = null;							
				
				ResultSet resultSet1    = null;			
				ResultSet resultSet2    = null;
				ResultSet resultSet5    = null;
				
				try {
					// first get the Author(s) of the Book to be deleted
					// just in case it's the only Book by this Author
					// in which case, we should also remove the Author(s)
					stmt1 = conn.prepareStatement(
							"select authors.* " +
							"  from  authors, books, bookAuthors " +
							"  where books.title = ? " +
							"    and authors.author_id = bookAuthors.author_id " +
							"    and books.book_id     = bookAuthors.book_id"
					);
					
					// get the Book's Author(s)
					stmt1.setString(1, title);
					resultSet1 = stmt1.executeQuery();
					
					// assemble list of Authors from query
					List<Author> authors = new ArrayList<Author>();					
				
					while (resultSet1.next()) {
						Author author = new Author();
						loadAuthor(author, resultSet1, 1);
						authors.add(author);
					}
					
					// check if any Authors were found
					// this shouldn't be necessary, there should not be a Book in the DB without an Author
					if (authors.size() == 0) {
						System.out.println("No author was found for title <" + title + "> in the database");
					}
										
					// now get the Book(s) to be deleted
					// we will need the book_id to remove associated entries in BookAuthors (junction table)
				
					stmt2 = conn.prepareStatement(
							"select books.* " +
							"  from  books " +
							"  where books.title = ? "
					);
					
					// get the Book(s)
					stmt2.setString(1, title);
					resultSet2 = stmt2.executeQuery();
					
					// assemble list of Books from query
					List<Book> books = new ArrayList<Book>();					
				
					while (resultSet2.next()) {
						Book book = new Book();
						loadBook(book, resultSet2, 1);
						books.add(book);
					}
					
					// first delete entries in BookAuthors junction table
					// can't delete entries in Books or Authors tables while they have foreign keys in junction table
					// prepare to delete the junction table entries (bookAuthors)
					stmt3 = conn.prepareStatement(
							"delete from bookAuthors " +
							"  where book_id = ? "
					);
					
					// delete the junction table entries from the DB for this title
					// this works if there are not multiple books with the same name
					stmt3.setInt(1, books.get(0).getBookId());
					stmt3.executeUpdate();
					
					System.out.println("Deleted junction table entries for book(s) <" + title + "> from DB");									
					
					// now delete entries in Books table for this title
					stmt4 = conn.prepareStatement(
							"delete from books " +
							"  where title = ? "
					);
					
					// delete the Book entries from the DB for this title
					stmt4.setString(1, title);
					stmt4.executeUpdate();
					
					System.out.println("Deleted book(s) with title <" + title + "> from DB");									
					
					// now check if the Author(s) have any Books remaining in the DB
					// only need to check if there are any entries in junction table that have this author ID
					for (int i = 0; i < authors.size(); i++) {
						// prepare to find Books for this Author
						stmt5 = conn.prepareStatement(
								"select books.book_id from books, bookAuthors " +
								"  where bookAuthors.author_id = ? "
						);
						
						// retrieve any remaining books for this Author
						stmt5.setInt(1, books.get(i).getAuthorId());
						resultSet5 = stmt5.executeQuery();						

						// if nothing returned, then delete Author
						if (!resultSet5.next()) {
							stmt6 = conn.prepareStatement(
								"delete from authors " +
								"  where author_id = ?"
							);
							
							// delete the Author from DB
							stmt6.setInt(1, authors.get(i).getAuthorId());
							stmt6.executeUpdate();
							
							System.out.println("Deleted author <" + authors.get(i).getLastname() + ", " + authors.get(i).getFirstname() + "> from DB");
							
							// we're done with this, so close it, since we might have more to do
							DBUtil.closeQuietly(stmt6);
						}
						
						// we're done with this, so close it since we might have more to do
						DBUtil.closeQuietly(resultSet5);
						DBUtil.closeQuietly(stmt5);
					}
					return authors;
				} finally {
					DBUtil.closeQuietly(resultSet1);
					DBUtil.closeQuietly(resultSet2);
					
					DBUtil.closeQuietly(stmt1);
					DBUtil.closeQuietly(stmt2);
					DBUtil.closeQuietly(stmt3);					
					DBUtil.closeQuietly(stmt4);					
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
	
	// retrieves Author information from query result set
	private void loadAuthor(Author author, ResultSet resultSet, int index) throws SQLException {
		author.setAuthorId(resultSet.getInt(index++));
		author.setLastname(resultSet.getString(index++));
		author.setFirstname(resultSet.getString(index++));
	}
	
	// retrieves Book information from query result set
	private void loadBook(Book book, ResultSet resultSet, int index) throws SQLException {
		book.setBookId(resultSet.getInt(index++));
//		book.setAuthorId(resultSet.getInt(index++));  // no longer used
		book.setTitle(resultSet.getString(index++));
		book.setIsbn(resultSet.getString(index++));
		book.setPublished(resultSet.getInt(index++));
	}
	
	// retrieves WrittenBy information from query result set
	private void loadBookAuthors(BookAuthor bookAuthor, ResultSet resultSet, int index) throws SQLException {
		bookAuthor.setBookId(resultSet.getInt(index++));
		bookAuthor.setAuthorId(resultSet.getInt(index++));
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
						"	loot_id integer constraint loot_id references Loot," +
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
						"	item_id integer constraint item_id references Item," +
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
	
//  creates the Authors and Books tables
	public void createTablesBAExample() {
		executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				PreparedStatement stmt2 = null;
				PreparedStatement stmt3 = null;				
			
				try {
					stmt1 = conn.prepareStatement(
						"create table authors (" +
						"	author_id integer primary key " +
						"		generated always as identity (start with 1, increment by 1), " +									
						"	lastname varchar(40)," +
						"	firstname varchar(40)" +
						")"
					);	
					stmt1.executeUpdate();
					
					System.out.println("Authors table created");
					
					stmt2 = conn.prepareStatement(
							"create table books (" +
							"	book_id integer primary key " +
							"		generated always as identity (start with 1, increment by 1), " +
//							"	author_id integer constraint author_id references authors, " +  	// this is now in the BookAuthors table
							"	title varchar(70)," +
							"	isbn varchar(15)," +
							"   published integer" +
							")"
					);
					stmt2.executeUpdate();
					
					System.out.println("Books table created");					
					
					stmt3 = conn.prepareStatement(
							"create table bookAuthors (" +
							"	book_id   integer constraint book_id references books, " +
							"	author_id integer constraint author_id references authors " +
							")"
					);
					stmt3.executeUpdate();
					
					System.out.println("BookAuthors table created");					
										
					return true;
				} finally {
					DBUtil.closeQuietly(stmt1);
					DBUtil.closeQuietly(stmt2);
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
				List<Author> authorList;
				List<Book> bookList;
				List<BookAuthor> bookAuthorList;
				
				try {
					authorList     = InitialData.getAuthors();
					bookList       = InitialData.getBooks();
					bookAuthorList = InitialData.getBookAuthors();					
				} catch (IOException e) {
					throw new SQLException("Couldn't read initial data", e);
				}

				PreparedStatement insertAuthor     = null;
				PreparedStatement insertBook       = null;
				PreparedStatement insertBookAuthor = null;

				try {
					// must completely populate Authors table before populating BookAuthors table because of primary keys
					insertAuthor = conn.prepareStatement("insert into authors (lastname, firstname) values (?, ?)");
					for (Author author : authorList) {
//						insertAuthor.setInt(1, author.getAuthorId());	// auto-generated primary key, don't insert this
						insertAuthor.setString(1, author.getLastname());
						insertAuthor.setString(2, author.getFirstname());
						insertAuthor.addBatch();
					}
					insertAuthor.executeBatch();
					
					System.out.println("Authors table populated");
					
					// must completely populate Books table before populating BookAuthors table because of primary keys
					insertBook = conn.prepareStatement("insert into books (title, isbn, published) values (?, ?, ?)");
					for (Book book : bookList) {
//						insertBook.setInt(1, book.getBookId());		// auto-generated primary key, don't insert this
//						insertBook.setInt(1, book.getAuthorId());	// this is now in the BookAuthors table
						insertBook.setString(1, book.getTitle());
						insertBook.setString(2, book.getIsbn());
						insertBook.setInt(3, book.getPublished());
						insertBook.addBatch();
					}
					insertBook.executeBatch();
					
					System.out.println("Books table populated");					
					
					// must wait until all Books and all Authors are inserted into tables before creating BookAuthor table
					// since this table consists entirely of foreign keys, with constraints applied
					insertBookAuthor = conn.prepareStatement("insert into bookAuthors (book_id, author_id) values (?, ?)");
					for (BookAuthor bookAuthor : bookAuthorList) {
						insertBookAuthor.setInt(1, bookAuthor.getBookId());
						insertBookAuthor.setInt(2, bookAuthor.getAuthorId());
						insertBookAuthor.addBatch();
					}
					insertBookAuthor.executeBatch();	
					
					System.out.println("BookAuthors table populated");					
					
					return true;
				} finally {
					DBUtil.closeQuietly(insertBook);
					DBUtil.closeQuietly(insertAuthor);
					DBUtil.closeQuietly(insertBookAuthor);					
				}
			}
		});
	}
	
	// loads data retrieved from CSV files into DB tables in batch mode
	public void loadInitialData() {
		executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				List<Author> authorList;
				List<Book> bookList;
				List<BookAuthor> bookAuthorList;
				
				try {
					authorList     = InitialData.getAuthors();
					bookList       = InitialData.getBooks();
					bookAuthorList = InitialData.getBookAuthors();					
				} catch (IOException e) {
					throw new SQLException("Couldn't read initial data", e);
				}

				PreparedStatement insertAuthor     = null;
				PreparedStatement insertBook       = null;
				PreparedStatement insertBookAuthor = null;

				try {
					// must completely populate Authors table before populating BookAuthors table because of primary keys
					insertAuthor = conn.prepareStatement("insert into authors (lastname, firstname) values (?, ?)");
					for (Author author : authorList) {
//						insertAuthor.setInt(1, author.getAuthorId());	// auto-generated primary key, don't insert this
						insertAuthor.setString(1, author.getLastname());
						insertAuthor.setString(2, author.getFirstname());
						insertAuthor.addBatch();
					}
					insertAuthor.executeBatch();
					
					System.out.println("Authors table populated");
					
					// must completely populate Books table before populating BookAuthors table because of primary keys
					insertBook = conn.prepareStatement("insert into books (title, isbn, published) values (?, ?, ?)");
					for (Book book : bookList) {
//						insertBook.setInt(1, book.getBookId());		// auto-generated primary key, don't insert this
//						insertBook.setInt(1, book.getAuthorId());	// this is now in the BookAuthors table
						insertBook.setString(1, book.getTitle());
						insertBook.setString(2, book.getIsbn());
						insertBook.setInt(3, book.getPublished());
						insertBook.addBatch();
					}
					insertBook.executeBatch();
					
					System.out.println("Books table populated");					
					
					// must wait until all Books and all Authors are inserted into tables before creating BookAuthor table
					// since this table consists entirely of foreign keys, with constraints applied
					insertBookAuthor = conn.prepareStatement("insert into bookAuthors (book_id, author_id) values (?, ?)");
					for (BookAuthor bookAuthor : bookAuthorList) {
						insertBookAuthor.setInt(1, bookAuthor.getBookId());
						insertBookAuthor.setInt(2, bookAuthor.getAuthorId());
						insertBookAuthor.addBatch();
					}
					insertBookAuthor.executeBatch();	
					
					System.out.println("BookAuthors table populated");					
					
					return true;
				} finally {
					DBUtil.closeQuietly(insertBook);
					DBUtil.closeQuietly(insertAuthor);
					DBUtil.closeQuietly(insertBookAuthor);					
				}
			}
		});
	}
	
	// The main method creates the database tables and loads the initial data.
	public static void main(String[] args) throws IOException {
		System.out.println("Creating tables...");
		DerbyDatabase db = new DerbyDatabase();
		db.createTables();
		
		System.out.println("Loading initial data...");
		db.loadInitialData();
		
		System.out.println("Library DB successfully initialized!");
	}
}
