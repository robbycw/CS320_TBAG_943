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
	public void testSetName() {
		winCondition.setName("Edgar");
		assertEquals("Edgar", winCondition.getName());
	}
	
	@Test
	public void testSetComplete() {
		winCondition.setComplete(true);
		assertEquals(true, winCondition.getComplete());
	}
}
