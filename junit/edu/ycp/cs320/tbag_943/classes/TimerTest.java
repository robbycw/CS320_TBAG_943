package edu.ycp.cs320.tbag_943.classes;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.ycp.cs320.tbag_943.classes.Timer;

public class TimerTest {
	private Timer timer;
	
	@Before
	public void setUp() {
		timer = new Timer();
	}
	
	@Test
	public void testSetTime() {
		timer.setTime(5);
		assertEquals(5, timer.getTime());
	}
	
	@Test
	public void testSetTimerRate() {
		timer.setTimerRate(10);
		assertEquals(10, timer.getTimerRate());
	}
}
