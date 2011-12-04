package keno.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

public class DrawTest {
	
	private Draw draw;
	private Collection<Byte> requiredNumbers;

	@Before
	public void init() {
		draw = new Draw();
		draw.setNumbers(new byte[] {
				13, 15, 18, 20, 22,
				24, 28, 30, 31, 33,
				36, 37, 47, 52, 54,
				56, 63, 70, 71, 73 });
		
		requiredNumbers = new ArrayList<Byte>();
	}
	
	@Test
	public void testHasNumber() {
		assertTrue(draw.hasNumber((byte) 13));
		assertTrue(draw.hasNumber((byte) 22));
		assertTrue(draw.hasNumber((byte) 47));
		assertTrue(draw.hasNumber((byte) 73));
		
		assertFalse(draw.hasNumber((byte) 1));
		assertFalse(draw.hasNumber((byte) 14));
		assertFalse(draw.hasNumber((byte) 26));
		assertFalse(draw.hasNumber((byte) 75));		
	}
	
	@Test
	public void testGetHitCount() {
		assertEquals(0, draw.getHitCount(null));
		assertEquals(0, draw.getHitCount(requiredNumbers));
		requiredNumbers.add(null);
		assertEquals(0, draw.getHitCount(requiredNumbers));
		requiredNumbers.add((byte) 1);
		assertEquals(0, draw.getHitCount(requiredNumbers));
		requiredNumbers.add((byte) 2);
		assertEquals(0, draw.getHitCount(requiredNumbers));
		requiredNumbers.add((byte) 13);
		assertEquals(1, draw.getHitCount(requiredNumbers));
		requiredNumbers.add((byte) 15);
		assertEquals(2, draw.getHitCount(requiredNumbers));
		requiredNumbers.add((byte) 32);
		assertEquals(2, draw.getHitCount(requiredNumbers));
		requiredNumbers.add((byte) 63);
		assertEquals(3, draw.getHitCount(requiredNumbers));
		requiredNumbers.add((byte) 64);
		assertEquals(3, draw.getHitCount(requiredNumbers));
	}

}
