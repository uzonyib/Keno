package keno.util.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;

import keno.model.Draw;

import org.junit.Test;

public class DrawFileReaderTest {
	
	private StringReader stringReader =
		new StringReader("2011;44;7;2011.11.06.;13;15;18;20;22;24;28;30;31;33;36;37;47;52;54;56;63;70;71;73\n"
				+ "1996;12;;;4;7;10;13;15;16;17;19;22;28;36;38;39;44;50;51;52;55;59;67");
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
	
	@Test
	public void testReadNext() throws IOException {
		DrawFileReader reader = new DrawFileReader(stringReader);
		Draw draw = reader.readNext();
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
		
		draw = reader.readNext();
		assertNotNull(draw);
		assertEquals(1996, draw.getYear());
		assertEquals(12, draw.getWeek());
		assertEquals(0, draw.getDay());
		assertNull(draw.getDate());
		assertArrayEquals(new byte[] {
				4, 7, 10, 13, 15,
				16, 17, 19, 22, 28,
				36, 38, 39, 44, 50,
				51, 52, 55, 59, 67 },
				draw.getNumbers());
		
		assertNull(reader.readNext());
	}

}
