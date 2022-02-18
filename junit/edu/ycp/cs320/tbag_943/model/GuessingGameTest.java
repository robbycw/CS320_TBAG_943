package edu.ycp.cs320.tbag_943.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.ycp.cs320.tbag_943.model.GuessingGame;

public class GuessingGameTest {
	private GuessingGame model;
	
	@Before
	public void setUp() {
		model = new GuessingGame();
	}
	
	@Test
	public void testSetMin() {
		model.setMin(1);
		assertEquals(1, model.getMin());
	}
	
	@Test
	public void testSetMax() {
		model.setMax(100);
		assertEquals(100, model.getMax());
	}
	
	@Test
	public void testSetIsLessThan() {
		int guess = 5; 
		model.setIsLessThan(guess);
		assertEquals(4, model.getMax()); 
	}
	
	@Test
	public void testSetIsGreaterThan() {
		int guess = 25; 
		model.setIsGreaterThan(guess);
		assertEquals(26, model.getMin()); 
	}
	
	@Test
	public void testIsDone() {
		model.setMax(5); 
		model.setMin(5);
		assertTrue(model.isDone());
		
		model.setMax(9); 
		model.setMin(5);
		assertFalse(model.isDone());
	}
}
