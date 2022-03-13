package edu.ycp.cs320.tbag_943.classes;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import edu.ycp.cs320.tbag_943.model.Numbers;

public class LocationTest {
	private Location room1;
	
	@Before
	public void setUp() {
		room1 = new Location(); 
	}
	
	@Test
	public void testSetName() {
		room1.setName("First"); 
		assertEquals("First", room1.getName()); 
	}
	
	@Test
	public void testSetNPCs() {
		//TODO
	}
	
	@Test
	public void testSetTreasure() {
		//TODO 
	}
	
	@Test
	public void testSetCombats() {
		//TODO
		
	}
	
	public void testSetPuzzles() {
		//TODO
		
	}
	
}
