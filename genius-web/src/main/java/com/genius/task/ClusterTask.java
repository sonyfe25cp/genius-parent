package com.genius.task;

import java.util.Date;

import com.genius.timer.BaseDailyJob;
import com.genius.timer.DayReference;

public class ClusterTask extends BaseDailyJob{

	@Override
	protected void runInternal(DayReference day, Date beginDate, Date endDate)
			throws InterruptedException {
	}
}