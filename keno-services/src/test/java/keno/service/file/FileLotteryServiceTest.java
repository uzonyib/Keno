package keno.service.file;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.List;

import keno.model.Draw;
import keno.service.LotteryService;

import org.junit.Before;
import org.junit.Test;

public class FileLotteryServiceTest {
	
	private LotteryService service;
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
	
	@Before
	public void init() throws FileNotFoundException {
		this.service = new FileLotteryService(getClass().getResource("/keno-test.csv").getFile());
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
	}
	
	@Test
	public void testListDraws() {
		List<Draw> draws = service.listDraws();
		assertNotNull(draws);
		assertEquals(10, draws.size());
		for (int i = 0; i < 10; ++i) {
			assertNotNull(draws.get(i));
		}
	}

}
