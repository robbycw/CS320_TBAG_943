package edu.ycp.cs320.tbag_943.classes;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import edu.ycp.cs320.tbag_943.model.Numbers;

public class PlayerTest {
	private Player player;
	private Location location; 
	private String icon; 
	
	@Before
	public void setUp() {
		player = new Player(); 
		location = new Location("Room"); 
		icon = "pathToIcon"; 
	}
	
	@Test
	public void testSetName() {
		player.setName("Johnny"); 
		assertEquals(player.getName(), "Johnny");
	}
	
	@Test
	public void testMove() {
		player.move(location); 
		assertEquals(location.getName(), player.getLocation().getName());
	}
	
	@Test
	public void testSetIcon() {
		player.setIcon(icon);
		assertEquals(player.getIcon(), icon); 
	}
	
	
}
