package edu.ycp.cs320.tbag_943.classes;

import java.util.ArrayList; 
import java.util.HashMap; 


public class Map {
	private String icon;
	private HashMap<String, Location> locations; 
	private HashMap<String, ArrayList<String>> connections;  
	
	
	public Map() {
		 this.icon = null; 
		 this.locations = null; 
		 this.connections = null; 
	}

	public Map(HashMap<String, Location> locations, HashMap<String, ArrayList<String>> connections) {
		 this.icon = null; 
		 this.locations = locations; 
		 this.connections = connections; 
		
	}

	public String getIcon() {
		return icon;
	}


	public void setIcon(String icon) {
		this.icon = icon;
	}


	public HashMap<String, Location> getLocations() {
		return locations;
	}


	public void setLocations(HashMap<String, Location> locations) {
		this.locations = locations;
	}
	
	public void addLocation(Location location) {
		this.locations.put(location.getName(), location); 
	}

	public HashMap<String, ArrayList<String>> getConnections() {
		return connections;
	}


	public void setConnections(HashMap<String, ArrayList<String>> connections) {
		this.connections = connections;
	}
	
	public boolean isConnected(Location start, Location end) {
		ArrayList<String> startConnections = connections.get(start.getName()); 
		
		if(startConnections.contains(end.getName())) {
			return true;
		} else {
			return false; 
		}
		
	}
	
	// These methods are used for the front-end map display. 
	// They set the color for each adjacent location on the map, as well
	// as the current location. 
	public String getDirectionColor(Location current, int direction) {
		String roomName = connections.get(current.getName()).get(direction); 
		if(roomName.equals("-1")) {
			return "gray"; 
		} else {
			Location room = locations.get(roomName); 
		
			if(room.isHidden()) {
				return "gray"; 
			} else {
				return "green"; 
			}
		}
		
	}
	
	public String getDirectionName(Location current, int direction) {
		
		String roomName = connections.get(current.getName()).get(direction); 
		if(roomName.equals("-1")) {
			return ""; 
		} else {
			Location room = locations.get(roomName); 
		
			if(room.isHidden()) {
				return ""; 
			} else {
				return roomName; 
			}
		}
	}
	
//	public void getLocationDescription(Location current, int direction) {
//		
//		String roomName = connections.get(current.getName()).get(direction);
//		Location loc = new Location();
//		
//		if(current.getName().equals("Room3")) {
//			System.out.println("Starting Area");
//			loc.setDescription("You find yourself in a lobby/n\" + \r\n" + 
//								"\"The lobby is completely dark ha/n");
//		} else if(current.getName().equals("Room4")) {
//			System.out.println("Room 4");
//		}
//	}

}