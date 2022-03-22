package edu.ycp.cs320.tbag_943.classes;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

public class CombatTest {
	
	NPC jerry, john, oliver, peter, apollo; 
	ArrayList<NPC> order; 
	
	@Before
	public void setUp() {
		
		jerry = new NPC(10, "jerry"); 
		john = new NPC(7, "john"); 
		oliver = new NPC(8, "oliver");
		peter = new NPC(12, "peter");
		apollo = new NPC(20, "apollo");
		
		order = new ArrayList<NPC>(); 
		order.add(jerry); 
		order.add(john);
		order.add(apollo);
		order.add(oliver);
		order.add(peter);
		
	}
	
	@Test
	public void testCalculateTurnOrder() {
		Collections.sort(order);
		
		assertTrue(order.get(0).getName() == "apollo");
		assertTrue(order.get(1).getName() == "peter");
		assertTrue(order.get(2).getName() == "jerry");
		assertTrue(order.get(3).getName() == "oliver");
		assertTrue(order.get(4).getName() == "john");
		
	}
	
}
