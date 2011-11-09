package keno.model;

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
