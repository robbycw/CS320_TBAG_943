package edu.ycp.cs320.tbag_943.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.ycp.cs320.tbag_943.model.Numbers;

public class NumbersTest {
	private Numbers model;
	
	@Before
	public void setUp() {
		model = new Numbers();
	}
	
	@Test
	public void testSetFirst() {
		model.setFirst(1.0);
		assertEquals((Double) 1.0, model.getFirst());
	}
	
	@Test
	public void testSetSecond() {
		model.setSecond(3.0);
		assertEquals((Double) 3.0, model.getSecond());
	}
	
	@Test
	public void testSetThird() {
		model.setThird(2.0);
		assertEquals((Double) 2.0, model.getThird()); 
	}
	
	@Test
	public void testSetResult() {
		model.setResult(9.0);
		assertEquals((Double) 9.0, model.getResult()); 
	}
	
}
