package edu.ycp.cs320.tbag_943.classes;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.ycp.cs320.tbag_943.classes.Puzzle;

public class StatTest {
	private Stat dex1, dex2, dex3; 
	
	@Before
	public void setUp() {
		dex1 = new Stat("dex", 5); 
		dex2 = new Stat("dex", 10); 
		dex3 = new Stat("dex", 10); 
	}
	
	@Test
	public void testCompareTo() {
		// We know dex1 < dex2 == dex3 
		// dex1.compareTo(dex2) should return -1 (dex1 less than dex2)
		// dex2.compareTo(dex3) should return 0
		// dex2.compareTo(dex1) should return 1
		
		assertTrue(dex1.compareTo(dex2) < 0); 
		assertTrue(dex2.compareTo(dex3) == 0);
		assertTrue(dex2.compareTo(dex1) > 0);
		
	}
}
