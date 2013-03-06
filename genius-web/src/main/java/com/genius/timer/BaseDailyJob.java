package com.genius.timer;

import java.util.Date;


public abstract class BaseDailyJob implements DailyJob {

	@Override
	public void run(DayReference day) throws InterruptedException {
		runInternal(day, day.getBeginningOfDay(), day.getEndOfDay());
	}

	protected abstract void runInternal(DayReference day, Date beginDate,
			Date endDate) throws InterruptedException;

}
