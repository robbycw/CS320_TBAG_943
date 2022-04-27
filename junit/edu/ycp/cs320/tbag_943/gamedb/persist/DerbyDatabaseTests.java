package edu.ycp.cs320.tbag_943.gamedb.persist;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.ycp.cs320.tbag_943.classes.*;

public class DerbyDatabaseTests {
	private IDatabase db = null;
	
	@Before
	public void setUp() throws Exception {
		// creating DB instance here
		DatabaseProvider.setInstance(new DerbyDatabase());
		db = DatabaseProvider.getInstance();
	}

	@Test
	public void findNPCIdsByLocationIDtest() {
		//assertEquals(db.findNPCIdsByLocationID(1), );
	}
	
	@Test
	public void testFindGameByGameId() {
		Game g = db.findGameByGameID(2); 
		
		System.out.println("Game ID: " + g.getId());
		System.out.println("Map ID: " + g.getMap().getId());
		System.out.println("Player ID: " + g.getPlayer().getId());
		System.out.println("Player Name: " + g.getPlayer().getName());
		System.out.println("Player Location: " + g.getPlayer().getLocation().getName());

		// Print all Location Names and their NPCs (if any)
		for(Location l : g.getMap().getLocations().values()) {
			System.out.println("Location #" + l.getId() + " has name: " + l.getName()); 
			for(NPC n : l.getNPCs().values()) {
				System.out.println("	NPC " + n.getName() + " is here.");
			}
		}
		
	}


}
