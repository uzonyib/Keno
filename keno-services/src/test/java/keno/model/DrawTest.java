package keno.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class DrawTest {
	
	private Draw draw;
	private List<NumberState> numberStates;

	@Before
	public void init() {
		draw = new Draw();
		draw.setNumbers(new byte[] {
				13, 15, 18, 20, 22,
				24, 28, 30, 31, 33,
				36, 37, 47, 52, 54,
				56, 63, 70, 71, 73 });
		
		numberStates = new ArrayList<NumberState>();
		for (int i = 0; i < 80; ++i) {
			numberStates.add(NumberState.ANY);
		}
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
		
		assertEquals(0, draw.getHitCount(null));
		assertEquals(0, draw.getHitCount(numberStates));
		numberStates.set(1, NumberState.SELECTED);
		assertEquals(0, draw.getHitCount(numberStates));
		numberStates.set(2, NumberState.UNSELECTED);
		assertEquals(0, draw.getHitCount(numberStates));
		numberStates.set(13, NumberState.SELECTED);
		assertEquals(1, draw.getHitCount(numberStates));
		numberStates.set(15, NumberState.UNSELECTED);
		assertEquals(1, draw.getHitCount(numberStates));
		numberStates.set(63, NumberState.SELECTED);
		assertEquals(2, draw.getHitCount(numberStates));
	}

}
