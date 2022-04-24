package edu.ycp.cs320.tbag_943.gamedb.persist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import edu.ycp.cs320.tbag_943.classes.*;

//Code is based on the CS320 Library Example. 
public class InitialData {
	public static List<User> getUser() throws IOException {
		System.out.println("this is a test to see if the method needs to be started somewhere");
		List<User> userList = new ArrayList<User>();
		ReadCSV readUsers = new ReadCSV("User.csv");
		try {
			while (true) {
				List<String> tuple = readUsers.next();
				if (tuple == null) {
					break;
				}
				// Iterates over tuple.
				Iterator<String> i = tuple.iterator();
				User user = new User();
				
				user.setId(Integer.parseInt(i.next()));
				user.setUsername(i.next());
				user.setPassword(i.next());
				System.out.println("");
				
				userList.add(user);
			}
			System.out.println("userList loaded from CSV file");
			return userList;
		} finally {
			readUsers.close();
		}
	}
	
	public static List<Pair<Integer, Integer>> getUserToGame() throws IOException {
		List<Pair<Integer, Integer>> userToGameList = new ArrayList<Pair<Integer, Integer>>(); 
		ReadCSV readUserToGame = new ReadCSV("UserToGame.csv"); 
		try {
			while (true) {
				List<String> tuple = readUserToGame.next();
				if (tuple == null) {
					break;
				}
				// Iterates over tuple.
				Iterator<String> i = tuple.iterator();
				
				int userID = Integer.parseInt(i.next()); 
				int gameID = Integer.parseInt(i.next()); 
				
				Pair<Integer, Integer> userToGame = new Pair<Integer, Integer>(userID, gameID);

				userToGameList.add(userToGame);
			}
			System.out.println("userList loaded from CSV file");
			return userToGameList;
		} finally {
			readUserToGame.close();
		}
	}
	
	// Remember to initialize dependencies first (initialize all locations before initializing Maps)
	public static List<Game> getGame(List<Player> players, List<ArrayList<String>> logs, List<Map> maps,
			List<Combat> combats) throws IOException {
		List<Game> gameList = new ArrayList<Game>(); 
		ReadCSV readGame = new ReadCSV("Game.csv"); 
		
		try {
			while (true) {
				List<String> tuple = readGame.next();
				if (tuple == null) {
					break;
				}
				
				Game game = new Game(); 
				
				// Iterates over tuple.
				Iterator<String> i = tuple.iterator();
				
				// Set fields in Game
				game.setId(Integer.parseInt(i.next()));
				game.setDifficulty(Integer.parseInt(i.next()));
				game.setInCombat(Boolean.parseBoolean(i.next()));
				game.setPlayerTurnTaken(Boolean.parseBoolean(i.next()));
				game.setPlayerCreated(Boolean.parseBoolean(i.next()));
				
				// Create a timer based on timeRemaining and timerRate
				int timeRemaining = Integer.parseInt(i.next()); 
				int timerRate = Integer.parseInt(i.next()); 
				Timer timer = new Timer(); 
				timer.setTime(timeRemaining);
				timer.setTimerRate(timerRate); 
				game.setTimer(timer);
				
				// Setting the log, player, map, and combat will require using
				// their IDs to get the correct item in their respective Lists. 
				// The object with ID X is stored in index X-1 within the list. 
				game.setOutputLog(logs.get(Integer.parseInt(i.next()) - 1));
				game.setPlayer(players.get(Integer.parseInt(i.next()) - 1));
				game.setMap(maps.get(Integer.parseInt(i.next()) - 1));
				// Game doesn't always load with current combat.
				int ccId = Integer.parseInt(i.next()) - 1;
				if(ccId == -2) {
					game.setCurrentCombat(null);
				} else {
					game.setCurrentCombat(combats.get(ccId));
				}
				

				// Add Game to list of Games. 
				gameList.add(game);
			}
			System.out.println("gameList loaded from CSV file");
			return gameList;
		} finally {
			readGame.close();
		}
	}
	
	public static List<ArrayList<String>> getGameLog() throws IOException {
		HashMap<Integer, ArrayList<String>> gameLogList = new HashMap<Integer, ArrayList<String>>(); 
		ReadCSV readGameLog = new ReadCSV("GameLog.csv");
		
		try {
			while (true) {
				List<String> tuple = readGameLog.next();
				if (tuple == null) {
					break;
				}
				
				// Iterates over tuple.
				Iterator<String> i = tuple.iterator();
				
				
				int log_id = Integer.parseInt(i.next()); 
				// Order is not needed for this method. 
				int order = Integer.parseInt(i.next()); 
				String output = i.next(); 
				
				// Determine if log_id is already in Map
				// If so, add onto its ArrayList of Strings. 
				// If not, generate a new ArrayList of Strings
				// with the output element, and add it to the map. 
				
				if(gameLogList.containsKey(log_id)) {
					ArrayList<String> log = gameLogList.get(log_id); 
					log.add(output); 
					gameLogList.put(log_id, log); 
				} else {
					ArrayList<String> log = new ArrayList<String>(); 
					log.add(output); 
					gameLogList.put(log_id, log);
				}
				
			}
			// Store HashMap as an ArrayList. 
			ArrayList<ArrayList<String>> logAList = new ArrayList<ArrayList<String>>();
			for(int j = 1; j <= gameLogList.keySet().size(); j++) {
				logAList.add(gameLogList.get(j)); 
			}
			System.out.println("gameLogList loaded from CSV file");
			return logAList;
		
		} finally {
			readGameLog.close();
		}
	}
	
	public static List<Player> getPlayer(List<Stat> stats, List<Pair<Integer, Integer>> playerToStats,
			List<Item> items, List<Pair<Integer, Integer>> playerInventory, 
			List<Location> locations) throws IOException {
		List<Player> playerList = new ArrayList<Player>(); 
		ReadCSV readPlayer = new ReadCSV("Player.csv"); 
		
		try {
			while (true) {
				List<String> tuple = readPlayer.next();
				if (tuple == null) {
					break;
				}
				
				Player player = new Player(); 
				
				// Iterates over tuple.
				Iterator<String> i = tuple.iterator();
				
				// Set fields in Player
				player.setId(Integer.parseInt(i.next()));
				player.setName(i.next());
				player.setIcon(i.next());
				player.setWeapon(i.next());
				player.setArmor(i.next());
				player.setPlayerCreated(Boolean.parseBoolean(i.next()));
				
				// Set Player Stats
				HashMap<String, Stat> s = new HashMap<String, Stat>();
				for(Pair<Integer, Integer> p : playerToStats) {
					if(p.getLeft() == player.getId()) {
						Stat stat = stats.get(p.getRight() - 1);
						s.put(stat.getName(), stat);
					}
				}

				player.setStats(s);
				
				// Set Player Inventory
				HashMap<String, Item> invent = new HashMap<String, Item>();
				for(Pair<Integer, Integer> p : playerInventory) {
					if(p.getLeft() == player.getId()) {
						Item item = items.get(p.getRight() - 1);
						invent.put(item.getName().toLowerCase(), item);
					}
				}

				player.setInventory(invent);
				
				// Set Player Location
				int location_id = Integer.parseInt(i.next());
				player.move(locations.get(location_id - 1));
				
				// Add Player to list of Players. 
				playerList.add(player);
			}
			System.out.println("playerList loaded from CSV file");
			return playerList;
		} finally {
			readPlayer.close();
		}
	}
	
	public static List<Pair<Integer, Integer>> getPlayerToStats() throws IOException {
		List<Pair<Integer, Integer>> playerToStatsList = new ArrayList<Pair<Integer, Integer>>(); 
		ReadCSV readPlayerToStats = new ReadCSV("PlayerToStats.csv"); 
		try {
			while (true) {
				List<String> tuple = readPlayerToStats.next();
				if (tuple == null) {
					break;
				}
				// Iterates over tuple.
				Iterator<String> i = tuple.iterator();
				
				int playerID = Integer.parseInt(i.next()); 
				int statID = Integer.parseInt(i.next()); 
				
				Pair<Integer, Integer> playerToStats = new Pair<Integer, Integer>(playerID, statID);

				playerToStatsList.add(playerToStats);
			}
			System.out.println("playerToStatsList loaded from CSV file");
			return playerToStatsList;
		} finally {
			readPlayerToStats.close();
		}
	}
	
	public static List<Pair<Integer, Integer>> getPlayerInventory() throws IOException {
		List<Pair<Integer, Integer>> playerInventoryList = new ArrayList<Pair<Integer, Integer>>(); 
		ReadCSV readPlayerInventory = new ReadCSV("PlayerInventory.csv"); 
		try {
			while (true) {
				List<String> tuple = readPlayerInventory.next();
				if (tuple == null) {
					break;
				}
				// Iterates over tuple.
				Iterator<String> i = tuple.iterator();
				
				int playerID = Integer.parseInt(i.next()); 
				int itemID = Integer.parseInt(i.next()); 
				
				Pair<Integer, Integer> playerInventory = new Pair<Integer, Integer>(playerID, itemID);

				playerInventoryList.add(playerInventory);
			}
			System.out.println("playerInventoryList loaded from CSV file");
			return playerInventoryList;
		} finally {
			readPlayerInventory.close();
		}
	}
	
	public static List<Stat> getPlayerStats() throws IOException {
		List<Stat> playerStatsList = new ArrayList<Stat>(); 
		ReadCSV readPlayerStats = new ReadCSV("PlayerStats.csv"); 
		try {
			while (true) {
				List<String> tuple = readPlayerStats.next();
				if (tuple == null) {
					break;
				}
				// Iterates over tuple.
				Iterator<String> i = tuple.iterator();
				
				Stat stat = new Stat(); 
				
				stat.setId(Integer.parseInt(i.next())); 
				stat.setName(i.next());
				stat.setRank(Integer.parseInt(i.next()));
				

				playerStatsList.add(stat);
			}
			System.out.println("playerStatsList loaded from CSV file");
			return playerStatsList;
		} finally {
			readPlayerStats.close();
		}
	}
	
	public static List<Map> getMap(List<Location> locations) throws IOException {
		HashMap<Integer, Map> mapList = new HashMap<Integer, Map>(); 
		ReadCSV readMap = new ReadCSV("Map.csv"); 
		
		try {
			while (true) {
				List<String> tuple = readMap.next();
				if (tuple == null) {
					break;
				}
				// Iterates over tuple.
				Iterator<String> i = tuple.iterator();
				
				int map_id = Integer.parseInt(i.next());
				Map map; 
				
				// See if the map_id is already contained in the List of Maps
				if(mapList.containsKey(map_id)) {
					// map_id already exists, get this map and add additional data. 
					map = mapList.get(map_id); 
					
					// Fetch Connections and Locations HashMaps from Map
					HashMap<String, Location> locationsMap = map.getLocations(); 
					HashMap<String, ArrayList<String>> connectionsMap = map.getConnections(); 
					
					// Get the location from list of Locations with ID
					// If the incoming integer is -1, assign the string "-1" to this value. 
					Location loc = locations.get(Integer.parseInt(i.next()) - 1);
					
					// Add new Location
					locationsMap.put(loc.getName().toLowerCase(), loc); 
					
					// Create and add new connection
					ArrayList<String> con = new ArrayList<String>(); 
					
					// Need to get location names for each connection
					// OR set connection to -1 if no connection in direction.
					for(int j = 0; j < 4; j++) {
						int conId = Integer.parseInt(i.next()) - 1; 
						if(conId == -2) {
							con.add("-1"); 
						} else {
							Location c = locations.get(conId);
							con.add(c.getName().toLowerCase()); 
						}
					}
					connectionsMap.put(loc.getName().toLowerCase(), con); 
					
					// Put locationsMap and connectionsMap back in map
					map.setLocations(locationsMap);
					map.setConnections(connectionsMap);
					
				} else {
					// Map doesn't exist in list yet, make a new map. 
					map = new Map(); 
					map.setId(map_id);
					
					// Initialize Connections and Locations HashMaps for Map
					HashMap<String, Location> locationsMap = new HashMap<String, Location>();
					HashMap<String, ArrayList<String>> connectionsMap = new HashMap<String, ArrayList<String>>(); 
					
					// Get the location from list of Locations with ID
					Location loc = locations.get(Integer.parseInt(i.next()) - 1);
					
					// Add new Location
					locationsMap.put(loc.getName().toLowerCase(), loc); 
					
					// Create and add new connection
					ArrayList<String> con = new ArrayList<String>(); 
					
					// Need to get location names for each connection
					for(int j = 0; j < 4; j++) {
						Location c = locations.get(Integer.parseInt(i.next()) - 1);
						con.add(c.getName().toLowerCase()); 
					}
					connectionsMap.put(loc.getName().toLowerCase(), con); 
					
					// Put locationsMap and connectionsMap back in map
					map.setLocations(locationsMap);
					map.setConnections(connectionsMap);
				}
				
				
				// Put the map into the list. 
				mapList.put(map_id, map); 
			}
			System.out.println("mapList loaded from CSV file");
			
			// Convert HashMap to ArrayList of Maps
			ArrayList<Map> maps = new ArrayList<Map>();
			for(int j = 1; j <= mapList.keySet().size(); j++) {
				maps.add(mapList.get(j));
			}
			
			return maps;
		} finally {
			readMap.close();
		}
	}
	
	public static List<Location> getLocation(List<Loot> loots, List<WinCondition> winCs, 
			List<Pair<Integer, Integer>> locToNPC, List<NPC> npcs, List<Pair<Integer, Integer>> locToCombat,
			List<Combat> combats, List<Pair<Integer, Integer>> locToPuzzle, List<Puzzle> puzzles)
					throws IOException {
		List<Location> locationList = new ArrayList<Location>(); 
		ReadCSV readLocation = new ReadCSV("Location.csv"); 
		
		try {
			while (true) {
				List<String> tuple = readLocation.next();
				if (tuple == null) {
					break;
				}
				// Iterates over tuple.
				Iterator<String> i = tuple.iterator();
				
				Location loc = new Location(); 
				// Add fields to Location
				loc.setId(Integer.parseInt(i.next()));
				loc.setName(i.next());
				loc.setDescription(i.next());
				loc.setHidden(Boolean.parseBoolean(i.next()));
				loc.setBlocked(Boolean.parseBoolean(i.next()));
				// the object with ID X will be stored in index X-1 within the list.
				// Locations may not have a Loot ID. ID will be set to -1 in such a case.
				int lootId = Integer.parseInt(i.next()) - 1; 
				if(lootId == -2) {
					loc.setTreasure(null);
				} else {
					loc.setTreasure(loots.get(lootId));
				}
				
				loc.setWinCondition(winCs.get(Integer.parseInt(i.next()) - 1));
				
				// Add NPCs
				HashMap<String, NPC> n = new HashMap<String, NPC>();
				for(Pair<Integer, Integer> p : locToNPC) {
					if(loc.getId() == p.getLeft()) {
						NPC earl = npcs.get(p.getRight() - 1); 
						n.put(earl.getName().toLowerCase(), earl); 
					}
				}
				
				loc.setNPCs(n);
				
				// Add Combats
				ArrayList<Combat> com = new ArrayList<Combat>();
				for(Pair<Integer, Integer> p : locToCombat) {
					if(loc.getId() == p.getLeft()) {
						com.add(combats.get(p.getRight() - 1)); 
					}
				}
				
				loc.setCombats(com);
				
				// Add Puzzles
				ArrayList<Puzzle> puz = new ArrayList<Puzzle>();
				for(Pair<Integer, Integer> p : locToPuzzle) {
					if(loc.getId() == p.getLeft()) {
						puz.add(puzzles.get(p.getRight() - 1)); 
					}
				}
				
				loc.setPuzzles(puz);
				
				// Add location to List. 
				locationList.add(loc);
			}
			System.out.println("locationList loaded from CSV file");
			return locationList;
		} finally {
			readLocation.close();
		}
	}
	
	public static List<WinCondition> getWinCondition() throws IOException {
		List<WinCondition> wcList = new ArrayList<WinCondition>(); 
		ReadCSV readWC = new ReadCSV("WinCondition.csv"); 
		
		try {
			while (true) {
				List<String> tuple = readWC.next();
				if (tuple == null) {
					break;
				}
				// Iterates over tuple.
				Iterator<String> i = tuple.iterator();
				
				WinCondition wc = new WinCondition(); 
				
				// Add fields to WC
				wc.setId(Integer.parseInt(i.next()));
				wc.setComplete(Boolean.parseBoolean(i.next()));
				wc.setLost(Boolean.parseBoolean(i.next()));
				wc.setWonRooms(Boolean.parseBoolean(i.next()));
				wc.setBestCase(Boolean.parseBoolean(i.next()));
				wc.setDefaultCase(Boolean.parseBoolean(i.next()));
				

				// Add WC to WCList. 
				wcList.add(wc);
			}
			System.out.println("WCList loaded from CSV file");
			return wcList;
		} finally {
			readWC.close();
		}
	}
	
	public static List<Loot> getLoot(List<Item> items) throws IOException {
		List<Loot> lootList = new ArrayList<Loot>(); 
		ReadCSV readLoot = new ReadCSV("Loot.csv"); 
		
		try {
			while (true) {
				List<String> tuple = readLoot.next();
				if (tuple == null) {
					break;
				}
				// Iterates over tuple.
				Iterator<String> i = tuple.iterator();
				
				Loot loot = new Loot();
				
				// Add fields to Loot
				loot.setId(Integer.parseInt(i.next()));
				loot.setXP(Integer.parseInt(i.next()));
				loot.setCollected(Boolean.parseBoolean(i.next()));
				// An Item with ID = X will be stored in index X-1 in the list of items. 
				loot.setItems(items.get(Integer.parseInt(i.next()) - 1));

				// Add location to List. 
				lootList.add(loot); 
			}
			System.out.println("lootList loaded from CSV file");
			return lootList;
		} finally {
			readLoot.close();
		}
	}
	
	public static List<Item> getItem() throws IOException {
		List<Item> itemList = new ArrayList<Item>(); 
		ReadCSV readItem = new ReadCSV("item.csv"); 
		
		try {
			while (true) {
				List<String> tuple = readItem.next();
				if (tuple == null) {
					break;
				}
				// Iterates over tuple.
				Iterator<String> i = tuple.iterator();
				
				Item item = new Item();
				
				// Add fields to Item
				item.setId(Integer.parseInt(i.next()));
				item.setName(i.next());
				item.setDes(i.next());
				item.setConsumable(Boolean.parseBoolean(i.next()));
				item.setWeapon(Boolean.parseBoolean(i.next()));
				item.setArmor(Boolean.parseBoolean(i.next()));
				item.setTool(Boolean.parseBoolean(i.next()));
				item.setDamage(Integer.parseInt(i.next()));
				item.setHealthGain(Integer.parseInt(i.next()));
				item.setValue(Integer.parseInt(i.next()));
				item.setAmount(Integer.parseInt(i.next()));
				item.setArmor(Integer.parseInt(i.next()));
				item.setAccuracy(Double.parseDouble(i.next()));
				

				// Add location to List. 
				itemList.add(item);
			}
			System.out.println("itemList loaded from CSV file");
			return itemList;
		} finally {
			readItem.close();
		}
	}
	
	public static List<Combat> getCombat(List<NPC> npcs, List<Pair<Integer, Integer>> combatToNPC) throws IOException {
		List<Combat> combatList = new ArrayList<Combat>(); 
		ReadCSV readCombat = new ReadCSV("Combat.csv"); 
		
		try {
			while (true) {
				List<String> tuple = readCombat.next();
				if (tuple == null) {
					break;
				}
				// Iterates over tuple.
				Iterator<String> i = tuple.iterator();
				
				Combat combat = new Combat(); 
				
				// Add fields to Combat
				combat.setId(Integer.parseInt(i.next()));
				combat.setTurn(Integer.parseInt(i.next()));
				combat.setDifficulty(Integer.parseInt(i.next()));
				combat.setDead(Boolean.parseBoolean(i.next()));
				
				// Initialize HashMap for NPCs in Combat
				HashMap<String, NPC> n = new HashMap<String, NPC>();
				
				// Add NPCs to Combat
				for(Pair<Integer, Integer> c : combatToNPC) {
					if(c.getLeft() == combat.getId()) {
						NPC earl = npcs.get(c.getRight() - 1); 
						n.put(earl.getName().toLowerCase(), earl); 
					}
				}

				combat.setNpcs(n);
				
				// Add Combat to List. 
				combatList.add(combat);
			}
			System.out.println("combatList loaded from CSV file");
			return combatList;
		} finally {
			readCombat.close();
		}
	}
	
	public static List<NPC> getNPC(List<Item> items, List<Speech> speeches, 
			List<Pair<Integer, Integer>> npcToStats, List<Stat> stats) throws IOException {
		List<NPC> npcList = new ArrayList<NPC>(); 
		ReadCSV readNPC = new ReadCSV("NPC.csv"); 
		
		try {
			while (true) {
				List<String> tuple = readNPC.next();
				if (tuple == null) {
					break;
				}
				// Iterates over tuple.
				Iterator<String> i = tuple.iterator();
				
				NPC npc = new NPC(); 
				
				// Add fields to NPC
				npc.setId(Integer.parseInt(i.next()));
				npc.setName(i.next());
				npc.setCombat(Boolean.parseBoolean(i.next()));
				// NPCs may not have a weapon. If so, value of weapon is -1. 
				int weapon = Integer.parseInt(i.next()) - 1;
				if(weapon != -2) {
					npc.setWeapon(items.get(weapon));
				} else {
					npc.setWeapon(null);
				}
				npc.setSpeech(speeches.get(Integer.parseInt(i.next()) - 1));
				npc.setIntimidated(Boolean.parseBoolean(i.next()));
				npc.setCanIntimidate(Boolean.parseBoolean(i.next()));
				npc.setIntimidationThreshold(Integer.parseInt(i.next()));
				npc.setPersuaded(Boolean.parseBoolean(i.next()));
				npc.setCanPersuade(Boolean.parseBoolean(i.next()));
				npc.setPersuasionThreshold(Integer.parseInt(i.next()));
				
				
				// Initialize Hashmap for NPC Stats
				HashMap<String, Stat> s = new HashMap<String, Stat>();
				// Find Pairs storing this NPC's ID. 
				for(Pair<Integer, Integer> p : npcToStats) {
					if(p.getLeft() == npc.getId()) {
						Stat stat = stats.get(p.getRight() - 1); 
						s.put(stat.getName(), stat); 
					}
				}
				
				// Set NPC Stats
				npc.setStats(s);

				// Add NPC to List. 
				npcList.add(npc);
			}
			System.out.println("npcList loaded from CSV file");
			return npcList;
		} finally {
			readNPC.close();
		}
	}
	
	public static List<Pair<Integer, Integer>> getNPCToStats() throws IOException {
		List<Pair<Integer, Integer>> npcToStatsList = new ArrayList<Pair<Integer, Integer>>(); 
		ReadCSV readNPCToStats = new ReadCSV("NPCToStats.csv"); 
		try {
			while (true) {
				List<String> tuple = readNPCToStats.next();
				if (tuple == null) {
					break;
				}
				// Iterates over tuple.
				Iterator<String> i = tuple.iterator();
				
				int npcID = Integer.parseInt(i.next()); 
				int statID = Integer.parseInt(i.next()); 
				
				Pair<Integer, Integer> npcToStats = new Pair<Integer, Integer>(npcID, statID);

				npcToStatsList.add(npcToStats);
			}
			System.out.println("npcToStatsList loaded from CSV file");
			return npcToStatsList;
		} finally {
			readNPCToStats.close();
		}
	}
	
	public static List<Stat> getNPCStats() throws IOException {
		List<Stat> npcStatsList = new ArrayList<Stat>(); 
		ReadCSV readNPCStats = new ReadCSV("NPCStats.csv"); 
		try {
			while (true) {
				List<String> tuple = readNPCStats.next();
				if (tuple == null) {
					break;
				}
				// Iterates over tuple.
				Iterator<String> i = tuple.iterator();
				
				Stat stat = new Stat(); 
				
				stat.setId(Integer.parseInt(i.next())); 
				stat.setName(i.next());
				stat.setRank(Integer.parseInt(i.next()));
				

				npcStatsList.add(stat);
			}
			System.out.println("npcStatsList loaded from CSV file");
			return npcStatsList;
		} finally {
			readNPCStats.close();
		}
	}
	
	public static List<Speech> getSpeech(HashMap<Integer, ArrayList<String>> speechOptions, 
			HashMap<Integer, ArrayList<String>> speechResponses) throws IOException {
		List<Speech> speechList = new ArrayList<Speech>(); 
		ReadCSV readSpeech = new ReadCSV("Speech.csv"); 
		
		try {
			while (true) {
				List<String> tuple = readSpeech.next();
				if (tuple == null) {
					break;
				}
				// Iterates over tuple.
				Iterator<String> i = tuple.iterator();
				
				// Make new Speech with speechOptions and speechResponses
				int speech_id = Integer.parseInt(i.next());
				Speech speech = new Speech(speechOptions.get(speech_id), speechResponses.get(speech_id)); 
				
				// Set Speech Fields 
				speech.setId(speech_id);
				speech.setIntimOp(i.next());
				speech.setIntimRes(i.next());
				speech.setIntimResFail(i.next());
				speech.setPersOp(i.next());
				speech.setPersRes(i.next());
				speech.setPersResFail(i.next());
				speech.setPrompt(i.next());
			

				// Add Speech to List
				speechList.add(speech);
			}
			System.out.println("npcToStatsList loaded from CSV file");
			return speechList;
		} finally {
			readSpeech.close();
		}
	}
	
	public static HashMap<Integer, ArrayList<String>> getSpeechOptions() throws IOException {
		HashMap<Integer, ArrayList<String>> speechOptionsList = new HashMap<Integer, ArrayList<String>>(); 
		ReadCSV readSpeechOptions = new ReadCSV("SpeechOptions.csv");
		
		try {
			while (true) {
				List<String> tuple = readSpeechOptions.next();
				if (tuple == null) {
					break;
				}
				
				// Iterates over tuple.
				Iterator<String> i = tuple.iterator();
				
				
				int speech_id = Integer.parseInt(i.next()); 
				// Order is not needed for this method. 
				int order = Integer.parseInt(i.next()); 
				String option = i.next(); 
				
				// Determine if speech_id is already in HashMap
				// If so, add onto its ArrayList of Strings. 
				// If not, generate a new ArrayList of Strings
				// with the output element, and add it to the map. 
				
				if(speechOptionsList.containsKey(speech_id)) {
					ArrayList<String> options = speechOptionsList.get(speech_id); 
					options.add(option); 
					speechOptionsList.put(speech_id, options); 
				} else {
					ArrayList<String> options = new ArrayList<String>(); 
					options.add(option); 
					speechOptionsList.put(speech_id, options); 
				}
				
			}
			
			System.out.println("gameLogList loaded from CSV file");
			return speechOptionsList;
		
		} finally {
			readSpeechOptions.close();
		}
	}
	
	public static HashMap<Integer, ArrayList<String>> getSpeechResponses() throws IOException {
		HashMap<Integer, ArrayList<String>> speechResponsesList = new HashMap<Integer, ArrayList<String>>(); 
		ReadCSV readSpeechResponses = new ReadCSV("SpeechResponses.csv");
		
		try {
			while (true) {
				List<String> tuple = readSpeechResponses.next();
				if (tuple == null) {
					break;
				}
				
				// Iterates over tuple.
				Iterator<String> i = tuple.iterator();
				
				
				int speech_id = Integer.parseInt(i.next()); 
				// Order is not needed for this method. 
				int order = Integer.parseInt(i.next()); 
				String response = i.next(); 
				
				// Determine if speech_id is already in HashMap
				// If so, add onto its ArrayList of Strings. 
				// If not, generate a new ArrayList of Strings
				// with the output element, and add it to the map. 
				
				if(speechResponsesList.containsKey(speech_id)) {
					ArrayList<String> responses = speechResponsesList.get(speech_id); 
					responses.add(response); 
					speechResponsesList.put(speech_id, responses); 
				} else {
					ArrayList<String> responses = new ArrayList<String>(); 
					responses.add(response); 
					speechResponsesList.put(speech_id, responses); 
				}
				
			}
			
			System.out.println("gameLogList loaded from CSV file");
			return speechResponsesList;
		
		} finally {
			readSpeechResponses.close();
		}
	}
	
	public static List<Pair<Integer, Integer>> getLocationToCombat() throws IOException {
		List<Pair<Integer, Integer>> locationToCombatList = new ArrayList<Pair<Integer, Integer>>(); 
		ReadCSV readLocationToCombat = new ReadCSV("LocationToCombat.csv"); 
		try {
			while (true) {
				List<String> tuple = readLocationToCombat.next();
				if (tuple == null) {
					break;
				}
				// Iterates over tuple.
				Iterator<String> i = tuple.iterator();
				
				int locationID = Integer.parseInt(i.next()); 
				int combatID = Integer.parseInt(i.next()); 
				
				Pair<Integer, Integer> locationToCombat = new Pair<Integer, Integer>(locationID, combatID);

				locationToCombatList.add(locationToCombat);
			}
			System.out.println("locationToCombatList loaded from CSV file");
			return locationToCombatList;
		} finally {
			readLocationToCombat.close();
		}
	}
	
	public static List<Pair<Integer, Integer>> getLocationToPuzzle() throws IOException {
		List<Pair<Integer, Integer>> locationToPuzzleList = new ArrayList<Pair<Integer, Integer>>(); 
		ReadCSV readLocationToPuzzle = new ReadCSV("LocationToPuzzle.csv"); 
		try {
			while (true) {
				List<String> tuple = readLocationToPuzzle.next();
				if (tuple == null) {
					break;
				}
				// Iterates over tuple.
				Iterator<String> i = tuple.iterator();
				
				int locationID = Integer.parseInt(i.next()); 
				int puzzleID = Integer.parseInt(i.next()); 
				
				Pair<Integer, Integer> locationToPuzzle = new Pair<Integer, Integer>(locationID, puzzleID);

				locationToPuzzleList.add(locationToPuzzle);
			}
			System.out.println("locationToPuzzle loaded from CSV file");
			return locationToPuzzleList;
		} finally {
			readLocationToPuzzle.close();
		}
	}
	
	public static List<Pair<Integer, Integer>> getCombatToNPC() throws IOException {
		List<Pair<Integer, Integer>> combatToNPCList = new ArrayList<Pair<Integer, Integer>>(); 
		ReadCSV readCombatToNPC = new ReadCSV("CombatToNPC.csv"); 
		try {
			while (true) {
				List<String> tuple = readCombatToNPC.next();
				if (tuple == null) {
					break;
				}
				// Iterates over tuple.
				Iterator<String> i = tuple.iterator();
				
				int combatID = Integer.parseInt(i.next()); 
				int npcID = Integer.parseInt(i.next()); 
				
				Pair<Integer, Integer> combatToNPC = new Pair<Integer, Integer>(combatID, npcID);

				combatToNPCList.add(combatToNPC);
			}
			System.out.println("combatToNPC loaded from CSV file");
			return combatToNPCList;
		} finally {
			readCombatToNPC.close();
		}
	}
	
	public static List<Pair<Integer, Integer>> getLocationToNPC() throws IOException {
		List<Pair<Integer, Integer>> locationToNPCList = new ArrayList<Pair<Integer, Integer>>(); 
		ReadCSV readLocationToNPC = new ReadCSV("LocationToNPC.csv"); 
		try {
			while (true) {
				List<String> tuple = readLocationToNPC.next();
				if (tuple == null) {
					break;
				}
				// Iterates over tuple.
				Iterator<String> i = tuple.iterator();
				
				int locationID = Integer.parseInt(i.next()); 
				int npcID = Integer.parseInt(i.next()); 
				
				Pair<Integer, Integer> npcToStats = new Pair<Integer, Integer>(locationID, npcID);

				locationToNPCList.add(npcToStats);
			}
			System.out.println("npcToStatsList loaded from CSV file");
			return locationToNPCList;
		} finally {
			readLocationToNPC.close();
		}
	}
	
	public static List<Puzzle> getPuzzle(List<Stat> stats, List<Item> items) throws IOException {
		List<Puzzle> puzzleList = new ArrayList<Puzzle>(); 
		ReadCSV readPuzzle = new ReadCSV("Puzzle.csv"); 
		
		try {
			while (true) {
				List<String> tuple = readPuzzle.next();
				if (tuple == null) {
					break;
				}
				// Iterates over tuple.
				Iterator<String> i = tuple.iterator();
				
				Puzzle puzzle = new Puzzle(); 
				
				// Add fields to Puzzle
				puzzle.setId(Integer.parseInt(i.next()));
				puzzle.setPrompt(i.next());
				puzzle.setAnswer(i.next());
				
				// The Item/Stat/Location ID X is stored in index X-1 in the list
				puzzle.setRequiredItem(items.get(Integer.parseInt(i.next()) - 1));
				puzzle.setRequiredSkill(stats.get(Integer.parseInt(i.next()) - 1));
				puzzle.setResult(Boolean.parseBoolean(i.next())); 
				puzzle.setCanSolve(Boolean.parseBoolean(i.next()));
				puzzle.setSolved(Boolean.parseBoolean(i.next()));
				puzzle.setBreakable(Boolean.parseBoolean(i.next()));
				puzzle.setJumpable(Boolean.parseBoolean(i.next()));
				puzzle.setRoomCon(i.next());

				// Add puzzle to List. 
				puzzleList.add(puzzle);
			}
			System.out.println("puzzleList loaded from CSV file");
			return puzzleList;
		} finally {
			readPuzzle.close();
		}
	}
	
	
	
/*
	// reads initial Author data from CSV file and returns a List of Authors
	public static List<Author> getAuthors() throws IOException {
		List<Author> authorList = new ArrayList<Author>();
		ReadCSV readAuthors = new ReadCSV("authors.csv");
		try {
			// auto-generated primary key for authors table
			Integer authorId = 1;
			while (true) {
				List<String> tuple = readAuthors.next();
				if (tuple == null) {
					break;
				}
				Iterator<String> i = tuple.iterator();
				Author author = new Author();

				// read author ID from CSV file, but don't use it
				// it's there for reference purposes, just make sure that it is correct
				// when setting up the BookAuthors CSV file				
				Integer.parseInt(i.next());
				// auto-generate author ID, instead
				author.setAuthorId(authorId++);				
				author.setLastname(i.next());
				author.setFirstname(i.next());
				authorList.add(author);
			}
			System.out.println("authorList loaded from CSV file");
			return authorList;
		} finally {
			readAuthors.close();
		}
	}
	
	// reads initial Book data from CSV file and returns a List of Books
	public static List<Book> getBooks() throws IOException {
		List<Book> bookList = new ArrayList<Book>();
		ReadCSV readBooks = new ReadCSV("books.csv");
		try {
			// auto-generated primary key for table books
			Integer bookId = 1;
			while (true) {
				List<String> tuple = readBooks.next();
				if (tuple == null) {
					break;
				}
				Iterator<String> i = tuple.iterator();
				Book book = new Book();
				
				// read book ID from CSV file, but don't use it
				// it's there for reference purposes, just make sure that it is correct
				// when setting up the BookAuthors CSV file
				Integer.parseInt(i.next());
				// auto-generate book ID, instead
				book.setBookId(bookId++);				
//				book.setAuthorId(Integer.parseInt(i.next()));  // no longer in books table
				book.setTitle(i.next());
				book.setIsbn(i.next());
				book.setPublished(Integer.parseInt(i.next()));
				
				bookList.add(book);
			}
			System.out.println("bookList loaded from CSV file");			
			return bookList;
		} finally {
			readBooks.close();
		}
	}
	
	// reads initial BookAuthor data from CSV file and returns a List of BookAuthors
	public static List<BookAuthor> getBookAuthors() throws IOException {
		List<BookAuthor> bookAuthorList = new ArrayList<BookAuthor>();
		ReadCSV readBookAuthors = new ReadCSV("book_authors.csv");
		try {
			while (true) {
				List<String> tuple = readBookAuthors.next();
				if (tuple == null) {
					break;
				}
				Iterator<String> i = tuple.iterator();
				BookAuthor bookAuthor = new BookAuthor();
				bookAuthor.setBookId(Integer.parseInt(i.next()));				
				bookAuthor.setAuthorId(Integer.parseInt(i.next()));
				bookAuthorList.add(bookAuthor);
			}
			System.out.println("bookAuthorList loaded from CSV file");			
			return bookAuthorList;
		} finally {
			readBookAuthors.close();
		}
	}
*/
	
}