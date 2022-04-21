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
		
	}

}
