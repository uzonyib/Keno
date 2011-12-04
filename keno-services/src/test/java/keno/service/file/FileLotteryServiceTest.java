package keno.service.file;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import keno.model.Draw;
import keno.model.NumberState;
import keno.service.LotteryService;

import org.junit.BeforeClass;
import org.junit.Test;

public class FileLotteryServiceTest {
	
	private static LotteryService service;
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
	
	private static List<NumberState> numberStates = new ArrayList<NumberState>(80);
	
	@BeforeClass
	public static void init() throws FileNotFoundException {
		service = new FileLotteryService(FileLotteryServiceTest.class
				.getResource("/keno-test.csv").getFile());
		
		for (int i = 0; i < 80; ++i) {
			numberStates.add(NumberState.ANY);
		}
	}
	
	@Test
	public void testGetMostRecentDraw() {
		Draw draw = service.getMostRecentDraw();
		assertNotNull(draw);
		assertEquals(2011, draw.getYear());
		assertEquals(44, draw.getWeek());
		assertEquals(7, draw.getDay());
		assertEquals("2011-11-06", dateFormat.format(draw.getDate()));
		assertArrayEquals(new byte[] {
				13, 15, 18, 20, 22,
				24, 28, 30, 31, 33,
				36, 37, 47, 52, 54,
				56, 63, 70, 71, 73 },
				draw.getNumbers());
	}
	
	@Test
	public void testListMostRecentDraws() {
		List<Draw> draws = service.listMostRecentDraws(2);
		assertNotNull(draws);
		assertEquals(2, draws.size());
		
		Draw draw = draws.get(0);
		assertNotNull(draw);
		assertEquals(2011, draw.getYear());
		assertEquals(44, draw.getWeek());
		assertEquals(7, draw.getDay());
		assertEquals("2011-11-06", dateFormat.format(draw.getDate()));
		assertArrayEquals(new byte[] {
				13, 15, 18, 20, 22,
				24, 28, 30, 31, 33,
				36, 37, 47, 52, 54,
				56, 63, 70, 71, 73 },
				draw.getNumbers());
		
		draw = draws.get(1);
		assertNotNull(draw);
		assertEquals(2011, draw.getYear());
		assertEquals(44, draw.getWeek());
		assertEquals(6, draw.getDay());
		assertEquals("2011-11-05", dateFormat.format(draw.getDate()));
		assertArrayEquals(new byte[] {
				7, 9, 10, 12, 14,
				22, 27, 28, 37, 39,
				40, 44, 49, 60, 61,
				63, 65, 66, 73, 79 },
				draw.getNumbers());
		
		draws = service.listMostRecentDraws(15);
		assertEquals(10, draws.size());
		
		draws = service.listMostRecentDraws(10, numberStates);
		assertNotNull(draws);
		assertEquals(10, draws.size());
		
		numberStates.set(0, NumberState.SELECTED);
		draws = service.listMostRecentDraws(10, numberStates);
		assertNotNull(draws);
		assertEquals(3, draws.size());
		assertEquals("2011-11-04", dateFormat.format(draws.get(0).getDate()));
		assertEquals("2011-10-31", dateFormat.format(draws.get(1).getDate()));
		assertEquals("2011-10-28", dateFormat.format(draws.get(2).getDate()));
		
		numberStates.set(0, NumberState.UNSELECTED);
		draws = service.listMostRecentDraws(10, numberStates);
		assertNotNull(draws);
		assertEquals(7, draws.size());
		assertEquals("2011-11-06", dateFormat.format(draws.get(0).getDate()));
		assertEquals("2011-11-05", dateFormat.format(draws.get(1).getDate()));
		assertEquals("2011-11-03", dateFormat.format(draws.get(2).getDate()));
		assertEquals("2011-11-02", dateFormat.format(draws.get(3).getDate()));
		assertEquals("2011-11-01", dateFormat.format(draws.get(4).getDate()));
		assertEquals("2011-10-30", dateFormat.format(draws.get(5).getDate()));
		assertEquals("2011-10-29", dateFormat.format(draws.get(6).getDate()));
		
		numberStates.set(1, NumberState.UNSELECTED);
		draws = service.listMostRecentDraws(10, numberStates);
		assertNotNull(draws);
		assertEquals(5, draws.size());
		assertEquals("2011-11-06", dateFormat.format(draws.get(0).getDate()));
		assertEquals("2011-11-05", dateFormat.format(draws.get(1).getDate()));
		assertEquals("2011-11-03", dateFormat.format(draws.get(2).getDate()));
		assertEquals("2011-11-01", dateFormat.format(draws.get(3).getDate()));
		assertEquals("2011-10-29", dateFormat.format(draws.get(4).getDate()));
		
		numberStates.set(0, NumberState.SELECTED);
		numberStates.set(1, NumberState.SELECTED);
		draws = service.listMostRecentDraws(10, numberStates);
		assertNotNull(draws);
		assertTrue(draws.isEmpty());
		
		numberStates.set(0, NumberState.ANY);
		numberStates.set(1, NumberState.ANY);

		Set<Integer> hitCounts = new HashSet<Integer>();
		hitCounts.add(1);
		
		numberStates.set(0, NumberState.SELECTED);
		draws = service.listMostRecentDraws(10, numberStates, hitCounts);
		assertNotNull(draws);
		assertEquals(3, draws.size());
		assertEquals("2011-11-04", dateFormat.format(draws.get(0).getDate()));
		assertEquals("2011-10-31", dateFormat.format(draws.get(1).getDate()));
		assertEquals("2011-10-28", dateFormat.format(draws.get(2).getDate()));
		
		numberStates.set(0, NumberState.UNSELECTED);
		draws = service.listMostRecentDraws(10, numberStates, hitCounts);
		assertNotNull(draws);
		assertTrue(draws.isEmpty());
		
		hitCounts.add(2);
		numberStates.set(0, NumberState.SELECTED);
		numberStates.set(2, NumberState.SELECTED);
		draws = service.listMostRecentDraws(numberStates, hitCounts);
		assertNotNull(draws);
		assertEquals(3, draws.size());
		assertEquals("2011-11-04", dateFormat.format(draws.get(0).getDate()));
		assertEquals("2011-10-31", dateFormat.format(draws.get(1).getDate()));
		assertEquals("2011-10-28", dateFormat.format(draws.get(2).getDate()));

		hitCounts.remove(1);
		draws = service.listMostRecentDraws(10, numberStates, hitCounts);
		assertNotNull(draws);
		assertEquals(2, draws.size());
		assertEquals("2011-11-04", dateFormat.format(draws.get(0).getDate()));
		assertEquals("2011-10-31", dateFormat.format(draws.get(1).getDate()));
		
		numberStates.set(0, NumberState.ANY);
		numberStates.set(2, NumberState.ANY);
	}
	
	@Test
	public void testListDraws() {
		List<Draw> draws = service.listMostRecentDraws();
		assertNotNull(draws);
		assertEquals(10, draws.size());
		for (int i = 0; i < 10; ++i) {
			assertNotNull(draws.get(i));
		}
		
		numberStates.set(0, NumberState.SELECTED);
		draws = service.listMostRecentDraws(10, numberStates);
		assertNotNull(draws);
		assertEquals(3, draws.size());
		assertEquals("2011-11-04", dateFormat.format(draws.get(0).getDate()));
		assertEquals("2011-10-31", dateFormat.format(draws.get(1).getDate()));
		assertEquals("2011-10-28", dateFormat.format(draws.get(2).getDate()));
		
		numberStates.set(0, NumberState.UNSELECTED);
		draws = service.listMostRecentDraws(10, numberStates);
		assertNotNull(draws);
		assertEquals(7, draws.size());
		assertEquals("2011-11-06", dateFormat.format(draws.get(0).getDate()));
		assertEquals("2011-11-05", dateFormat.format(draws.get(1).getDate()));
		assertEquals("2011-11-03", dateFormat.format(draws.get(2).getDate()));
		assertEquals("2011-11-02", dateFormat.format(draws.get(3).getDate()));
		assertEquals("2011-11-01", dateFormat.format(draws.get(4).getDate()));
		assertEquals("2011-10-30", dateFormat.format(draws.get(5).getDate()));
		assertEquals("2011-10-29", dateFormat.format(draws.get(6).getDate()));
		
		numberStates.set(1, NumberState.UNSELECTED);
		draws = service.listMostRecentDraws(10, numberStates);
		assertNotNull(draws);
		assertEquals(5, draws.size());
		assertEquals("2011-11-06", dateFormat.format(draws.get(0).getDate()));
		assertEquals("2011-11-05", dateFormat.format(draws.get(1).getDate()));
		assertEquals("2011-11-03", dateFormat.format(draws.get(2).getDate()));
		assertEquals("2011-11-01", dateFormat.format(draws.get(3).getDate()));
		assertEquals("2011-10-29", dateFormat.format(draws.get(4).getDate()));
		
		numberStates.set(0, NumberState.SELECTED);
		numberStates.set(1, NumberState.SELECTED);
		draws = service.listMostRecentDraws(10, numberStates);
		assertNotNull(draws);
		assertTrue(draws.isEmpty());
		
		numberStates.set(0, NumberState.ANY);
		numberStates.set(1, NumberState.ANY);
	}

}
