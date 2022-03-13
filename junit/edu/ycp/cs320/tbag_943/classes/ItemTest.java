package edu.ycp.cs320.tbag_943.classes;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ItemTest {
	private Item modelHealthPotion, modelSword;

	
	@Before
	public void setUp() {
	
		/// setting up a consumable item for testing
		modelHealthPotion = new Item();

		modelHealthPotion.isConsumable(true);
		modelHealthPotion.isWeapon(false);
		modelHealthPotion.setValue(25);
		modelHealthPotion.setHealthGain(25);
		
		// setting up combat weapon for testing
		modelSword = new Item();
		
		modelSword.isConsumable(false);
		modelSword.isWeapon(true);
		modelSword.setValue(125);
		modelSword.setAcuracy((double)0.69);
		modelSword.setDamage(21);
	
		
	}
	
	@Test
	public void testGetValue() {
		// testing health potion
		assertEquals((int)25, modelHealthPotion.getValue());
		
		//testing sword
		assertEquals((int)125, modelSword.getValue());
	}
	
	@Test
	public void testGetIsConsumbale() {
		// testing health potion
		assertEquals(true, modelHealthPotion.getIsConsumbale());
		
		// testing sword
		assertEquals(false, modelSword.getIsConsumbale());
	}
	@Test
	public void testGetIsWeapon() {
		// testing health potion
		assertEquals(false, modelHealthPotion.getIsWeapon());
		
		//test sword
		assertEquals(true, modelSword.getIsWeapon());
	}
	
	@Test
	public void testGetHealthGain() {
		assertEquals((int) 25, modelHealthPotion.getHealthGain());
	}

	@Test
	public void testGetDamage() {
		assertEquals((int) 21, modelSword.getDamage());
	}

	@Test
	public void testGetAcuracy() {
		assertEquals((double)0.69, modelSword.getAcuracy());
	}


}
