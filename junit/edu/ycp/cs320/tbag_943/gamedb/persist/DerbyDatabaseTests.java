package edu.ycp.cs320.tbag_943.gamedb.persist;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class DerbyDatabaseTests {
	DerbyDatabase db;
	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void findNPCIdsByLocationIDtest() {
		assertEquals(db.findNPCIdsByLocationID(1), );
	}

}
