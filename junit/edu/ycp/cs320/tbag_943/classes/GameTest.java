package edu.ycp.cs320.tbag_943.classes;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class GameTest {
	private Game model;
	
	@Before
	public void setUp() {
		model = new Game();
	}
	
	@Test
	public void testSetDifficulty() {
		model.setDifficulty(2); 
		assertEquals(2, model.getDifficulty());
	}
	
	@Test
	public void testSetOutputLog() {
		ArrayList<String> out = new ArrayList<String>(); 
		out.add("Test1"); 
		out.add("Test2"); 
		model.setOutputLog(out);
		assertEquals(out.get(0), model.getOutputLog().get(0)); 
		assertEquals(out.get(1), model.getOutputLog().get(1)); 
	}
	
	@Test
	public void testAddOutput() {
		model.addOutput("Test1");
		model.addOutput("Test2");
		model.addOutput("Test3");
		model.addOutput("Test4");
		ArrayList<String> out = model.getOutputLog(); 
		assertEquals(out.get(0), "Test1"); 
		assertEquals(out.get(1), "Test2");
		assertEquals(out.get(2), "Test3");
		assertEquals(out.get(3), "Test4");
		
	}
	
}
