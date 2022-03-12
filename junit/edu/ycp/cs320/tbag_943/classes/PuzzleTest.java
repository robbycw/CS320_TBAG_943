package edu.ycp.cs320.tbag_943.classes;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.ycp.cs320.tbag_943.classes.Puzzle;

public class PuzzleTest {
	private Puzzle puzzle;
	
	@Before
	public void setUp() {
		puzzle = new Puzzle();
	}
	
	@Test
	public void testSetPrompt() {
		puzzle.setPrompt("Test");
		assertEquals("Test", puzzle.getPrompt());
	}
	
	@Test
	public void testSetRequiredSkill() {
		puzzle.setRequiredSkill(5);
		assertEquals(5, puzzle.getRequiredSkill());
	}
	
	@Test
	public void testSetRequiredItem() {
		puzzle.setRequiredItem(10);
		assertEquals(10, puzzle.getRequiredItem());
	}
	
	@Test
	public void testSetResult() {
		puzzle.setResult(true);
		assertEquals(true, puzzle.getResult());
	}
}
