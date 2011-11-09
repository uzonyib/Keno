package keno.util.parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;

import keno.model.Draw;

import org.apache.log4j.Logger;

public class DrawFileReader {
	
	private static final Logger LOGGER = Logger.getLogger(DrawFileReader.class);
	
	private static final String SEPARATOR = ";";
	
	private BufferedReader reader;
	private SimpleDateFormat dateFormat;
	
	public DrawFileReader(Reader reader) {
		if (reader == null) {
			throw new IllegalArgumentException("Reader is null.");
		}
		this.reader = new BufferedReader(reader);
		this.dateFormat = new SimpleDateFormat("yyyy.mm.dd.");
	}

	public DrawFileReader(InputStream inputStream) throws FileNotFoundException {
		this(new InputStreamReader(inputStream));
	}
	
	public Draw readNext() throws IOException {
		String line = reader.readLine();
		if (line == null) {
			return null;
		}
		
		String[] fields = line.split(SEPARATOR);
		Draw draw = new Draw();
		
		try {
			if (!fields[0].isEmpty()) {
				draw.setYear(Short.parseShort(fields[0]));
			}
			
			if (!fields[1].isEmpty()) {
				draw.setWeek(Byte.parseByte(fields[1]));
			}
			
			if (!fields[2].isEmpty()) {
				draw.setDay(Byte.parseByte(fields[2]));
			}
			
			if (!fields[3].isEmpty()) {
				draw.setDate(dateFormat.parse(fields[3]));
			}
			
			byte[] numbers = new byte[Draw.NUMBERS_PER_DRAW];
			for (int i = 0; i < 20; ++i) {
				numbers[i] = Byte.parseByte(fields[i + 4]);
			}
			draw.setNumbers(numbers);
			
			return draw;
		} catch (Exception e) {
			LOGGER.warn("Cannot convert record: " + line, e);
			return null;
		}
	}

}
