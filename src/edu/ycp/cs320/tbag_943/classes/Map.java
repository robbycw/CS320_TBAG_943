package edu.ycp.cs320.tbag_943.classes;

import java.util.ArrayList; 
import java.util.HashMap; 


public class Map {
	private String icon;
	private ArrayList<Location> locations; 
	private HashMap<String, ArrayList<String>> connections; 
	
	
	public Map() {
		 this.icon = null; 
		 this.locations = null; 
		 this.connections = null; 
	}


	public String getIcon() {
		return icon;
	}


	public void setIcon(String icon) {
		this.icon = icon;
	}


	public ArrayList<Location> getLocations() {
		return locations;
	}


	public void setLocations(ArrayList<Location> locations) {
		this.locations = locations;
	}
	
	public void addLocation(Location location) {
		this.locations.add(location); 
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
	
}