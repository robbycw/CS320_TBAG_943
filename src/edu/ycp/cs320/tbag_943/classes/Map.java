package edu.ycp.cs320.tbag_943.classes;

import java.util.ArrayList; 
import java.util.HashMap; 


public class Map {
	
	// Note that the key sets for HashMaps are all lowercase.
	// This keeps the user from needing to use capital letters. 
	private String icon;
	private HashMap<String, Location> locations; 
	private HashMap<String, ArrayList<String>> connections;  
	private int id; 
	
	// Default Constructor
	public Map() {
		 this.icon = null; 
		 this.locations = new HashMap<String, Location>(); 
		 this.connections = new HashMap<String, ArrayList<String>>(); 
		 this.id = 0; 
	}

	// Constructor - takes values for locations and connections
	public Map(HashMap<String, Location> locations, HashMap<String, ArrayList<String>> connections) {
		 this.icon = null; 
		 this.locations = locations; 
		 this.connections = connections; 
		
	}

	// 
	// Getters
	//
	public String getIcon() {
		return icon;
	}

	public HashMap<String, Location> getLocations() {
		return locations;
	}
	
	public HashMap<String, ArrayList<String>> getConnections() {
		return connections;
	}
	
	public int getId() {
		return id;
	}



	//
	// Setters
	//
	public void setIcon(String icon) {
		this.icon = icon;
	}

	public void setLocations(HashMap<String, Location> locations) {
		this.locations = locations;
	}
	
	public void setConnections(HashMap<String, ArrayList<String>> connections) {
		this.connections = connections;
	}
	
	public void setId(int id) {
		this.id = id;
	}
			
	//
	// Methods
	// 
	
	// Adds a location to the HashMap of Locations
	public void addLocation(Location location) {

		this.locations.put(location.getName().toLowerCase(), location); 

	}
	
	public boolean isConnected(Location start, Location end) {
		ArrayList<String> startConnections = connections.get(start.getName()); 
		
		if(startConnections.contains(end.getName().toLowerCase())) {
			return true;
		} else {
			return false; 
		}
		
	}
	
	//
	// Methods for Map Display
	//
	// These methods are used for the front-end map display. 
	// They set the color for each adjacent location on the map, as well
	// as the current location. 
	//
	
	public String getDirectionColor(Location current, int direction) {
		
		// Check if there is a connection in the given direction
		String roomName = connections.get(current.getName().toLowerCase()).get(direction); 
		if(roomName.equals("-1")) {
			return "gray"; 
		} else {
			Location room = locations.get(roomName.toLowerCase()); 
		
			if(room.isHidden()) {
				return "gray"; 
			} else {
				return "green"; 
			}
		}
		
	}
	
	public String getDirectionName(Location current, int direction) {
		
		String roomName = connections.get(current.getName().toLowerCase()).get(direction); 
		if(roomName.equals("-1")) {
			return ""; 
		} else {
			Location room = locations.get(roomName.toLowerCase()); 
		
			if(room.isHidden()) {
				return ""; 
			} else {
				return room.getName(); 
			}
		}
	}
	
}