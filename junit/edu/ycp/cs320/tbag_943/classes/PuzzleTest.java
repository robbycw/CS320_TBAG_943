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
		puzzle.setRequiredSkill(new Stat("strength",20));
		assertEquals(new Stat("strength",20), puzzle.getRequiredSkill());
	}
	
	@Test
	public void testSetRequiredItem() {
		puzzle.setRequiredItem(new Item("Milk dud"));
		assertEquals(new Item("Milk dud"), puzzle.getRequiredItem());
	}
	
	@Test
	public void testSetResult() {
		puzzle.setResult(true);
		assertEquals(true, puzzle.getResult());
	}
}
