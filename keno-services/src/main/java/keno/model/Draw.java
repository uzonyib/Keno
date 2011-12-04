package keno.model;

import java.util.Collection;
import java.util.Date;

public class Draw {
	
	public static final byte NUMBERS_PER_DRAW = 20;
	
	private short year;
	private byte week;
	private byte day;
	private Date date;
	private byte[] numbers;
	
	public Draw() {
		
	}
	
	public boolean hasNumber(byte number) {
		if (numbers == null) {
			return false;
		}
		
		for (byte n : numbers) {
			if (n == number) {
				return true;
			}
		}
		return false;
	}
	
	public byte getHitCount(Collection<Byte> numbers) {
		if (numbers == null) {
			return 0;
		}
		
		byte hitCount = 0;
		for (Byte number : numbers) {
			if (number != null && hasNumber(number)) {
				++hitCount;
			}
		}
		
		return hitCount;
	}

	public short getYear() {
		return year;
	}

	public void setYear(short year) {
		this.year = year;
	}

	public byte getWeek() {
		return week;
	}

	public void setWeek(byte week) {
		this.week = week;
	}

	public byte getDay() {
		return day;
	}

	public void setDay(byte day) {
		this.day = day;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public byte[] getNumbers() {
		return numbers;
	}

	public void setNumbers(byte[] numbers) {
		this.numbers = numbers;
	}

}
