package edu.ycp.cs320.tbag_943.classes;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.ycp.cs320.tbag_943.classes.WinCondition;

public class WinConditionTest {
	private WinCondition winCondition;
	
	@Before
	public void setUp() {
		winCondition = new WinCondition();
	}
	
	@Test
	public void testSetLost() {
		winCondition.setLost(true);
		assertEquals(true, winCondition.getLost());
	}
	
	@Test
	public void testSetWonRooms() {
		winCondition.setWonRooms(true);
		assertEquals(true, winCondition.getWonRooms());
	}
	
	@Test
	public void testSetBestCase() {
		winCondition.setBestCase(true);
		assertEquals(true, winCondition.getBestCase());
	}
	
	@Test
	public void testSetComplete() {
		winCondition.setComplete(true);
		assertEquals(true, winCondition.getComplete());
	}
}
