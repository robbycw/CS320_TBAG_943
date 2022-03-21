package edu.ycp.cs320.tbag_943.controller;

import static org.junit.Assert.*;

import edu.ycp.cs320.tbag_943.classes.*;
import java.util.ArrayList;
import java.util.HashMap; 

import org.junit.Before;
import org.junit.Test;


public class GameControllerTest {
	private Map map1;
	private HashMap<String, ArrayList<String>> connections; 
	private String room1, room2, room3, room4; 
	private ArrayList<String> room1Con, room2Con, room3Con, room4Con; 
	private HashMap<String, Location> locations; 
	private Location r1, r2, r3, r4; 
	private Player player; 
	private Game model; 
	private GameController controller; 
	private Item sword, shield; 
	private HashMap<String, Item> items; 
	private Loot loot; 
	
	@Before
	public void setUp() {
		
		// Sets up for adding connections and rooms to the map. 
		connections = new HashMap<String, ArrayList<String>>();
		room1 = "Room1"; 
		room2 = "Room2"; 
		room3 = "Room3";
		room4 = "Room4"; 
		
		room1Con = new ArrayList<String>(); 
		room1Con.add(room2); // north
		room1Con.add(room3); // east
		room1Con.add(null); // south
		room1Con.add(null); // west
		
		room2Con = new ArrayList<String>(); 
		room2Con.add(null); // north
		room2Con.add(room4); // east
		room2Con.add(room1); // south
		room2Con.add(null); // west
		
		room3Con = new ArrayList<String>(); 
		room3Con.add(room4); // north
		room3Con.add(null); // east
		room3Con.add(null); // south
		room3Con.add(room1); // west
		
		room4Con = new ArrayList<String>(); 
		room4Con.add(null); // north
		room4Con.add(null); // east
		room4Con.add(room3); // south
		room4Con.add(room2); // west
		
		connections.put(room1, room1Con); 
		connections.put(room2, room2Con); 
		connections.put(room3, room3Con); 
		connections.put(room4, room4Con); 
		
		locations = new HashMap<String, Location>(); 
		r1 = new Location(room1); 
		r2 = new Location(room2); 
		r3 = new Location(room3); 
		r4 = new Location(room4); 
		locations.put(room1, r1); 
		locations.put(room2, r2); 
		locations.put(room3,r3); 
		locations.put(room4,r4); 
		
		sword = new Item("sword"); 
		shield = new Item("shield");
		items = new HashMap<String, Item>(); 
		items.put("sword", sword); 
		items.put("shield", shield); 
		
		loot = new Loot(items); 
		
		r1.setTreasure(loot); 
		
		map1 = new Map(locations, connections); 
		player = new Player("Jorady", r1); 
		
		model = new Game(1, map1, player);
		controller = new GameController(model); 
	}
	
	@Test
	public void testMove() {

		// Ensures that player starts in Room1
		Player p1 = controller.getModel().getPlayer(); 
		assertEquals(p1.getLocation().getName(), "Room1"); 
		
		// Should move player north from Room1 to Room2
		controller.move("north");
		assertEquals(p1.getLocation().getName(), "Room2");
		
		// Should move player east from Room2 to Room4
		controller.move("east");
		assertEquals(p1.getLocation().getName(), "Room4");
		
		// Should move player south from Room4 to Room3
		controller.move("south");
		assertEquals(p1.getLocation().getName(), "Room3");
		
		// Should move player west from Room3 to Room1
		controller.move("west");
		assertEquals(p1.getLocation().getName(), "Room1");
				
		// Player cannot move west in Room1, so player should remain in Room1
		controller.move("west");
		assertEquals(p1.getLocation().getName(), "Room1");
	}
	
	@Test
	public void testAttack() {
		
		// TODO
		
	}
	
	@Test
	public void testCollect() {
		
		Player p = controller.getModel().getPlayer(); 
		
		// Checks if player's inventory contains sword after picking it up. 
		controller.collect("sword");
		assertTrue(p.getInventory().containsKey("sword")); 
		assertTrue(p.getInventory().containsValue(sword));
		
		// Ensures sword is removed from the Loot in Room1
		assertFalse(r1.getTreasure().getItems().containsKey("sword")); 
		assertFalse(r1.getTreasure().getItems().containsValue(sword));
		
	}
	
	@Test
	public void testTalk() {
		
		// TODO
			
	}
	
}
