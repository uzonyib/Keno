package keno.util.stats;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import keno.model.Draw;

import org.junit.Before;
import org.junit.Test;

public class StatsUtilTest {
	
	private List<Draw> draws;
	
	@Before
	public void setUp() {
		draws = new ArrayList<Draw>();
		byte[] numbers = new byte[] {
				1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
				11, 12, 13, 14, 15, 16, 17, 18, 19, 20
		};
		Draw draw = new Draw();
		draw.setNumbers(numbers);
		draws.add(draw);
		
		numbers = new byte[] {
				2, 4, 6, 8, 10, 12, 14, 16, 18, 20,
				22, 24, 26, 28, 30, 32, 34, 36, 38, 40
		};
		draw = new Draw();
		draw.setNumbers(numbers);
		draws.add(draw);
		
		numbers = new byte[] {
				3, 6, 9, 12, 15, 18, 21, 24, 27, 30,
				33, 36, 39, 42, 45, 48, 51, 54, 57, 60
		};
		draw = new Draw();
		draw.setNumbers(numbers);
		draws.add(draw);
	}
	
	@Test
	public void testGetCounts() {
		int[] counts = StatsUtil.getCounts(draws);
		assertEquals(80, counts.length);
		assertEquals(1, counts[0]);
		assertEquals(2, counts[1]);
		assertEquals(3, counts[5]);
		assertEquals(0, counts[22]);
	}

}
