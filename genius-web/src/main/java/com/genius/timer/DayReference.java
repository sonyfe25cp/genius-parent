package com.genius.timer;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Index;
import com.google.code.morphia.annotations.Indexes;

@Embedded
@Indexes({@Index("year,month,day")})
public final class DayReference implements Comparable<DayReference> {
	private int year;
	private int month;
	private int day;

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public DayReference() {
		super();
	}

	public DayReference(int year, int month, int day) {
		super();
		this.year = year;
		this.month = month;
		this.day = day;
	}

	public DayReference(String dayString) {
		super();
		String[] yearMonthDay = dayString.split("-", 3);
		this.year = Integer.parseInt(yearMonthDay[0]);
		this.month = Integer.parseInt(yearMonthDay[1]);
		this.day = Integer.parseInt(yearMonthDay[2]);
	}

	public DayReference(GregorianCalendar gregorianCalendar) {
		this.year = gregorianCalendar.get(Calendar.YEAR);
		this.month = gregorianCalendar.get(Calendar.MONTH)+1;
		this.day = gregorianCalendar.get(Calendar.DAY_OF_MONTH);
	}

	public DayReference(Date date) {
		this(dateToGregorianCalendar(date));
	}

	private static GregorianCalendar dateToGregorianCalendar(Date date) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		return gc;
	}

	public String toString() {
		return String.format("%04d-%02d-%02d", year, month, day);
	}

	public Date toDate() {
		return toGregorianCalendar().getTime();
	}

	public GregorianCalendar toGregorianCalendar() {
		return new GregorianCalendar(year, month-1, day);
	}

	public DayReference daysBefore(int days) {
		GregorianCalendar gc = toGregorianCalendar();
		gc.add(Calendar.DAY_OF_MONTH, -days);
		return new DayReference(gc);
	}

	public DayReference daysAfter(int days) {
		GregorianCalendar gc = toGregorianCalendar();
		gc.add(Calendar.DAY_OF_MONTH, days);
		return new DayReference(gc);
	}

	public Date getBeginningOfDay() {
		return toDate();
	}

	public Date getEndOfDay() {
		GregorianCalendar gc = toGregorianCalendar();
		gc.add(Calendar.DAY_OF_MONTH, 1);
		return gc.getTime();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof DayReference)) {
			return false;
		}

		DayReference dr = (DayReference) obj;

		return year == dr.year && month == dr.month && day == dr.day;
	}

	public int compareTo(DayReference o) {
		if (year != o.year) {
			return year - o.year;
		}
		if (month != o.month) {
			return month - o.month;
		}
		if (day != o.day) {
			return day - o.day;
		}
		return 0;
	}
	
	public static DayReference getToday() {
		return new DayReference(new Date());
	}

}
