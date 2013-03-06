package com.genius.timer;



public interface DailyJob {
	public void run(DayReference day) throws InterruptedException;
}
