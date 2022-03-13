package edu.ycp.cs320.tbag_943.classes;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap; 

import org.junit.Before;
import org.junit.Test;


public class MapTest {
	private Map map1;
	private HashMap<String, ArrayList<String>> connections; 
	private String room1, room2, room3, room4; 
	private ArrayList<String> room1Con, room2Con, room3Con, room4Con; 
	private ArrayList<Location> locations; 
	private Location r1, r2, r3, r4; 
	
	@Before
	public void setUp() {
		map1 = new Map();
		
		// Sets up for adding connections and rooms to the map. 
		connections = new HashMap<String, ArrayList<String>>();
		room1 = "Room1"; 
		room2 = "Room2"; 
		room3 = "Room3";
		room4 = "Room4"; 
		
		room1Con = new ArrayList<String>(); 
		room1Con.add(room2); 
		room1Con.add(room3); 
		
		room2Con = new ArrayList<String>(); 
		room2Con.add(room1); 
		room2Con.add(room3);
		room2Con.add(room4);
		
		room3Con = new ArrayList<String>(); 
		room3Con.add(room1); 
		room3Con.add(room2);
		
		room4Con = new ArrayList<String>(); 
		room4Con.add(room2); 
		
		connections.put(room1, room1Con); 
		connections.put(room2, room2Con); 
		connections.put(room3, room3Con); 
		connections.put(room4, room4Con); 
		
		locations = new ArrayList<Location>(); 
		r1 = new Location(room1); 
		r2 = new Location(room2); 
		r3 = new Location(room3); 
		r4 = new Location(room4); 
		locations.add(r1); 
		locations.add(r2); 
		locations.add(r3); 
		locations.add(r4); 
		
	}
	
	@Test
	public void testSetIcon() {
		String icon = "SomePath"; 
		map1.setIcon(icon);
		assertEquals("SomePath", map1.getIcon());
	}
	
	@Test
	public void testSetConnections() {
		
		map1.setConnections(connections);
		
		for(String str : map1.getConnections().keySet()) {
			
			ArrayList<String> roomCon = map1.getConnections().get(str); 
			
			for(String room : roomCon) {
				// these nested for each loops will determine if the ArrayLists contain the same elements, 
				// that is, if setConnections worked properly and the ArrayLists in connections 
				// are the same as the ArrayLists in map1.getConnections for each room. 
				assertTrue(connections.get(str).contains(room)); 
			}
			
		}
		
	}
	
	@Test
	public void testSetLocations() {
		
		map1.setLocations(locations);
		
		for(int i = 0; i < map1.getLocations().size(); i++) {
			
			assertEquals(map1.getLocations().get(i).getName(), locations.get(i).getName()); 
			
		}
		
	}
	
	@Test
	public void testIsConnected() {
		
		map1.setConnections(connections);
		
		// List of Connections: 
		// room1: 2,3
		// room2: 1,3,4
		// room3: 1,2
		// room4: 2
		
		assertTrue(map1.isConnected(r1, r2)); 
		assertTrue(map1.isConnected(r1, r3)); 
		assertTrue(map1.isConnected(r2, r3)); 
		assertTrue(map1.isConnected(r4, r2)); 
		assertFalse(map1.isConnected(r4, r3)); 
		assertFalse(map1.isConnected(r4, r1));
			
	}
	
}
