package edu.ycp.cs320.tbag_943.gamedb.persist;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.ycp.cs320.tbag_943.classes.Combat;
import edu.ycp.cs320.tbag_943.classes.Game;
import edu.ycp.cs320.tbag_943.classes.Item;
import edu.ycp.cs320.tbag_943.classes.Location;
import edu.ycp.cs320.tbag_943.classes.Loot;
import edu.ycp.cs320.tbag_943.classes.Map;
import edu.ycp.cs320.tbag_943.classes.NPC;
import edu.ycp.cs320.tbag_943.classes.Pair;
import edu.ycp.cs320.tbag_943.classes.Player;
import edu.ycp.cs320.tbag_943.classes.Puzzle;
import edu.ycp.cs320.tbag_943.classes.Speech;
import edu.ycp.cs320.tbag_943.classes.Stat;
import edu.ycp.cs320.tbag_943.classes.User;
import edu.ycp.cs320.tbag_943.classes.WinCondition;

import edu.ycp.cs320.tbag_943.gamedb.persist.*;

public class InitialDataTest {

	// Fields
	
	// Remember that because lists count from 0, the
	// ID X object is stored at index X-1. 
	private List<User> userList;
	private List<Pair<Integer, Integer>> userToGame; 
	private List<Game> gameList;  
	private List<ArrayList<String>> gameLogList; 
	private List<Player> playerList; 
	private List<Map> mapList; 
	private List<Pair<Integer, Integer>> playerToStats;
	private List<Pair<Integer, Integer>> playerInventory; 
	private List<Stat> playerStatsList; 
	private List<Item> itemList; 
	private List<Loot> lootList;
	private List<Location> locationList; 
	private List<Pair<Integer, Integer>> locationToNPC; 
	private List<WinCondition> winConditionList;
	private List<NPC> npcList; 
	private List<Pair<Integer, Integer>> npcToStats; 
	private List<Stat> npcStatsList; 
	private List<Speech> speechList; 
	private HashMap<Integer, ArrayList<String>> speechOptions;
	private HashMap<Integer, ArrayList<String>> speechResponses; 
	private List<Pair<Integer, Integer>> locationToCombat; 
	private List<Pair<Integer, Integer>> combatToNPC; 
	private List<Pair<Integer, Integer>> locationToPuzzle; 
	private List<Combat> combatList; 
	private List<Puzzle> puzzleList; 
	
	@Before
	public void setUp() throws Exception {
		this.userList = new ArrayList<User>();
		this.userToGame = new ArrayList<Pair<Integer, Integer>>();
		this.gameList = new ArrayList<Game>();
		this.gameLogList = new ArrayList<ArrayList<String>>();
		this.playerList = new ArrayList<Player>();
		this.mapList = new ArrayList<Map>();
		this.playerToStats = new ArrayList<Pair<Integer, Integer>>();
		this.playerInventory = new ArrayList<Pair<Integer, Integer>>();
		this.playerStatsList = new ArrayList<Stat>();
		this.itemList = new ArrayList<Item>();
		this.lootList = new ArrayList<Loot>();
		this.locationList = new ArrayList<Location>();
		this.locationToNPC = new ArrayList<Pair<Integer, Integer>>();
		this.winConditionList = new ArrayList<WinCondition>();
		this.npcList = new ArrayList<NPC>();
		this.npcToStats = new ArrayList<Pair<Integer, Integer>>();
		this.npcStatsList = new ArrayList<Stat>();
		this.speechList = new ArrayList<Speech>();
		this.speechOptions = new HashMap<Integer, ArrayList<String>>();
		this.speechResponses = new HashMap<Integer, ArrayList<String>>();
		this.locationToCombat = new ArrayList<Pair<Integer, Integer>>();
		this.combatToNPC = new ArrayList<Pair<Integer, Integer>>();
		this.locationToPuzzle = new ArrayList<Pair<Integer, Integer>>();
		this.combatList = new ArrayList<Combat>();
		this.puzzleList = new ArrayList<Puzzle>();
		
		
	}

	@Test
	public void testReadInitialData() {

		try {
			this.userList.addAll(InitialData.getUser());
			this.userToGame.addAll(InitialData.getUserToGame());
			 
			this.gameLogList.addAll(InitialData.getGameLog());
			
			this.playerToStats.addAll(InitialData.getPlayerToStats());
			this.playerInventory.addAll(InitialData.getPlayerInventory());
			this.playerStatsList.addAll(InitialData.getPlayerStats()); 
			this.itemList.addAll(InitialData.getItem());
			this.lootList.addAll(InitialData.getLoot(itemList));
			
			this.locationToNPC.addAll(InitialData.getLocationToNPC());
			this.winConditionList.addAll(InitialData.getWinCondition());
			
			this.npcToStats.addAll(InitialData.getNPCToStats());
			this.npcStatsList.addAll(InitialData.getNPCStats());
			
			this.speechOptions = InitialData.getSpeechOptions(); 
			this.speechResponses = InitialData.getSpeechResponses(); 
			this.speechList.addAll(InitialData.getSpeech(speechOptions, speechResponses)); 
			
			this.locationToCombat.addAll(InitialData.getLocationToCombat());
			this.combatToNPC.addAll(InitialData.getCombatToNPC());
			this.locationToPuzzle.addAll(InitialData.getLocationToPuzzle()); 
			this.puzzleList.addAll(InitialData.getPuzzle(playerStatsList, itemList));
			
			this.npcList.addAll(InitialData.getNPC(itemList, speechList, npcToStats, npcStatsList));
			this.combatList.addAll(InitialData.getCombat(npcList, combatToNPC));
			this.locationList.addAll(InitialData.getLocation(lootList, winConditionList, 
					locationToNPC, npcList, locationToCombat, combatList, locationToPuzzle, puzzleList));
			this.mapList.addAll(InitialData.getMap(locationList));
			this.playerList.addAll(InitialData.getPlayer(playerStatsList, playerToStats, 
					itemList, playerInventory, locationList));
			this.gameList.addAll(InitialData.getGame(playerList,gameLogList, mapList, combatList));
			
			
		} catch (IOException e) {
			throw new IllegalStateException("Couldn't read initial data", e);
		}
		
		// Try printing out some of the game's information: 
		for(ArrayList<String> i : gameLogList) {
			for(String s : i) {
				System.out.println(s);
			}
		}
		
		System.out.println("-----users list------");
		for(User user: userList) {
			System.out.println("____User " + user.getId());
			System.out.println("id:" + user.getId());
			System.out.println("user name: " +  user.getUsername());
			System.out.println("password: " +  user.getPassword());
			
		}
		System.out.println("----UserToGame-------");
		for(Pair<Integer, Integer> i: userToGame) {
			System.out.print("UserID: " + i.getLeft() + "   ");
			System.out.print("GameID: " + i.getRight());
			System.out.println(" ");
		}
		
		System.out.println("-------Games------");
		for(Game game: gameList) {
			System.out.println("____Game " + game.getId());
			System.out.println("Game_ID: " + game.getId());
			System.out.println("Difficulty: " + game.getDifficulty());
			System.out.println("Time: " + game.getTimer().getTime());
			System.out.println("Character Created?: " + game.getPlayerNotCreated());
			System.out.println("Player: " + game.getPlayer().getName());
			System.out.println("combat: " + game.getCurrentCombat());
		}
		
		System.out.println("-------Player------");
		for(Player player: playerList) {
			System.out.println("___player " + player.getId());
			System.out.println("Player_ID:" + player.getId());
			System.out.println("Player name:" + player.getName());
			System.out.println("Armor: " + player.getArmor());
			System.out.println("Weapon: " + player.getWeapon());
			System.out.println("Location: " + player.getLocation().getName());
		}
		
		System.out.println("-------Map------");
		for(Map map : mapList) {
			System.out.println("Map ID: " + map.getId());
			
			// Print out Locations in Map with Connections
			for(Location l : map.getLocations().values()) {
				System.out.println("	Location ID: " + l.getId());
				System.out.println("	Location Name: " + l.getName());
				
				for(String s : map.getConnections().get(l.getName().toLowerCase())) {
					System.out.println(" 		Connection: " + s); 
				}
				
			}
			// Print out connections:
			
			
			//System.out.println("Map ID: " + map.get);
		}
		
		System.out.println("-------PlayerToStats------");
		for(Pair<Integer, Integer> ps : playerToStats) {
			System.out.print("Left: " + ps.getLeft() + "   " );
			System.out.println("Right: " + ps.getRight());
		}
		
		System.out.println("-------playerInventory------");
		for(Pair<Integer, Integer> pi : playerInventory) {
			System.out.print("Left: " + pi.getLeft() + "   ");
			System.out.println("Right: " + pi.getRight());
		}
		
		System.out.println("------- playerStatsList------");
		for(Stat stat : playerStatsList) {
			System.out.print("Stat_id: " + stat.getId() + "  ");
			System.out.print("Stat_Name: " + stat.getName() + "  ");
			System.out.println("Stat_Rank: " + stat.getRank());
		}
		
		System.out.println("------- itemList------");
		for(Item item : itemList) {
			System.out.println("___item " + item.getId());
			System.out.println("item_Id: " + item.getId());
			System.out.println("item_Name: " + item.getName());
			System.out.println("item_Acuracy: " + item.getAccuracy());
			System.out.println("item_Amount: " + item.getAmount());
			System.out.println("item_Armor: " + item.getArmor());
			System.out.println("item_Damage: " + item.getDamage());
			System.out.println("item_Destination: " + item.getDes());
			System.out.println("item_HealthGain: " + item.getHealthGain());
			System.out.println("item_Value: " + item.getValue());
			System.out.println("item_IsArmor?: " + item.getIsArmor());
			System.out.println("item_IsConsumable: " + item.getIsConsumbale());
			System.out.println("item_IsTool: " + item.getIsTool());
			System.out.println("item_IsWeapon: " + item.getIsWeapon());
		}
		
		System.out.println("-------loot names------");
		for(Loot loot: lootList) {
			System.out.println("____Item " + loot.getId());
			System.out.println("Item_Id: " + loot.getId());
			System.out.println( "Item name: " + loot.getItem().getName());
		}
		
		System.out.println("------locationList-------");
		for(Location loc : locationList) {
			System.out.println("____Location " + loc.getId());
			System.out.println("location_id: " + loc.getId());
			System.out.println("location_name: " + loc.getName());
			System.out.println("location_description: " + loc.getDescription());
			System.out.println("location_Longdescription: " + loc.getLongDescription());
			System.out.println("location_Isblocked: " + loc.getBlocked());
			System.out.println("location_treasure: " + loc.getTreasure());
			System.out.println("location_winCondition: " + loc.getWinCondition());
		}
		
		System.out.println("------locationToNPC-------");
		for(Pair<Integer, Integer> locNPC : locationToNPC) {
			System.out.print("Left: " + locNPC.getLeft() + "  ");
			System.out.println("Right: " + locNPC.getRight());
		}
		
		System.out.println("------winConditionList-------");
		for(WinCondition win : winConditionList) {
			System.out.println("____Win " + win.getId());
			System.out.println("win_ID: " + win.getId());
			System.out.println("win_bestCase: " + win.getBestCase());
			System.out.println("win_complete: " + win.getComplete());
			System.out.println("win_defaultCase: " + win.getDefaultCase());
			System.out.println("win_lost: " + win.getLost());
			System.out.println("win_wonRooms: " + win.getWonRooms());
		}
		
		System.out.println("------npcList-------");
		for(NPC npc : npcList) {
			System.out.println("____NPC " + npc.getId());
			System.out.println("npc_Id: " + npc.getId());
			System.out.println("npc_name: " + npc.getName());
			System.out.println("npc_intimThreshold: " + npc.getIntimidationThreshold());
			System.out.println("npc_perThreshold: " + npc.getPersuasionThreshold());
			System.out.println("npc_attack: " + npc.getAttacks());
			System.out.println("npc_canIntim: " + npc.getCanIntimidate());
			System.out.println("npc_canPersuade: " + npc.getCanPersuade());
			System.out.println("npc_weapon: " + npc.getWeapon());
		}
		
		System.out.println("------npcToStats-------");
		for(Pair<Integer, Integer> npcstat : npcToStats) {
			System.out.print("Left: " + npcstat.getLeft() + "   ");
			System.out.println("Right: " + npcstat.getRight() + "   ");
		}
		
		System.out.println("------npcStatsList-------");
		for(Stat stat  : npcStatsList) {
			System.out.print("Stat_id: " + stat.getId() + "  ");
			System.out.print("Stat_Name: " + stat.getName() + "  ");
			System.out.println("Stat_Rank: " + stat.getRank());
		}
		
		System.out.println("------speechList-------");
		for(Speech speech : speechList) {
			System.out.println("____Speech " + speech.getId());
			System.out.println("speech_Id: " + speech.getId());
			System.out.println("speech_IntimOp: " + speech.getIntimOp());
			System.out.println("speech_intimRes: " + speech.getIntimRes());
			System.out.println("speech_intimResFail: " + speech.getIntimResFail());
			System.out.println("speech_persOp: " + speech.getPersOp());
			System.out.println("speech_persRes: " + speech.getPersRes());
			System.out.println("speech_persResFail: " + speech.getPersResFail());
		}
		
		System.out.println("------speechOptions-------");
		/*for(HashMap<Integer, ArrayList<String> j : speechOptions) {
			
		}*/
		
		System.out.println("------speechResponses-------");
		
		System.out.println("------locationToCombat-------");
		for(Pair<Integer, Integer> locCombat: locationToCombat) {
			System.out.print("Left: " + locCombat.getLeft() + "  ");
			System.out.println("Right: " + locCombat.getRight());
		}
		
		System.out.println("------combatToNPC-------");
		for(Pair<Integer, Integer> comNPC : combatToNPC){
			System.out.print("Left: " + comNPC.getLeft() + " ");
			System.out.println("Right: " + comNPC.getRight());
		}
		
		System.out.println("------locationToPuzzle-------");
		for(Pair<Integer, Integer> locPuzz: locationToPuzzle) {
			System.out.print("Left: " + locPuzz.getLeft() + "  ");
			System.out.println("Rigth: " + locPuzz.getRight());
		}
		
		System.out.println("------combatList-------");
		for(Combat combat : combatList) {
			System.out.println("combat_ID: " + combat.getId());
			System.out.println("combat_getDifficulty: " + combat.getDifficulty());
			System.out.println("combat_getTurn: " + combat.getTurn());
		}
		
		System.out.println("------puzzleList-------");
		for(Puzzle puzzle : puzzleList) {
			System.out.println("puzzle_ID: " + puzzle.getId());
			System.out.println("puzzle_getAnswer: " + puzzle.getAnswer());
			System.out.println("puzzle_getPrompt: " + puzzle.getPrompt());
			System.out.println("puzzle_getRoomCon: " + puzzle.getRoomCon());
			System.out.println("puzzle_getBreakable: " + puzzle.getBreakable());
			System.out.println("puzzle_getLoot: " + puzzle.getLoot());
			System.out.println("puzzle_getReqItem: " + puzzle.getRequiredItem());
			System.out.println("puzzle_getReqSkill: " + puzzle.getRequiredSkill());
			System.out.println("puzzle_getResult: " + puzzle.getResult());
			System.out.println("puzzle.getReward: " + puzzle.getReward());
		}
	}
	
	
	


}
