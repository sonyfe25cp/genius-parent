package com.genius.timer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


public class JobsManager {

	private static final Logger logger = LoggerFactory
			.getLogger(JobsManager.class);

	private Set<String> runningJobs = new HashSet<String>();

	public List<String> getRunningJobs() {
		synchronized (runningJobs) {
			return new ArrayList<String>(runningJobs);
		}
	}

	private boolean tryLockJobName(String name) {
		synchronized (runningJobs) {
			if (runningJobs.contains(name)) {
				return false;
			}
			runningJobs.add(name);
			return true;
		}
	}

	private void unlockJobName(String name) {
		synchronized (runningJobs) {
			runningJobs.remove(name);
		}
	}

	public void setRunningJobs(Set<String> runningJobs) {
		this.runningJobs = runningJobs;
	}

	private Map<String, DailyJob> dailyJobs = new LinkedHashMap<String, DailyJob>();

	public Map<String, DailyJob> getDailyJobs() {
		return dailyJobs;
	}

	@Required
	public void setDailyJobs(Map<String, DailyJob> dailyJobs) {
		this.dailyJobs = dailyJobs;
	}

	@SuppressWarnings("unchecked")
	public void doFullDailyJobYesterday() throws InterruptedException {
		dailyJob(DayReference.getToday().daysBefore(1),
				(Set<String>) (Object) Collections.emptySet());
	}

	public void dailyJob(DayReference dayReference, Set<String> disabledPhases)
			throws InterruptedException {
		String jobString = "dailyJob-" + dayReference.toString();
		boolean canStart = tryLockJobName(jobString);
		if (canStart) {
			try {
				logger.info("Running daily jobs for day {}...", dayReference);

				for (Map.Entry<String, DailyJob> dailyJobEntry : dailyJobs
						.entrySet()) {
					String phase = dailyJobEntry.getKey();
					if (disabledPhases.contains(phase)) {
						logger.info("Phase {} skipped.", phase);
						continue;
					}
					DailyJob dailyJob = dailyJobEntry.getValue();
					logger.info("Starting day {} phase {}...", dayReference,
							phase);
					dailyJob.run(dayReference);
					logger.info("Day {} phase {} done.", dayReference, phase);
				}

				logger.info("Daily jobs of day {} done.", dayReference);
			} finally {
				unlockJobName(jobString);
			}
		} else {
			logger.info("DailyJob of day {} already started", dayReference);
		}
	}

	public void retroJob(DayReference dayReference, Set<String> disabledPhases)
			throws InterruptedException {
		logger.info("Doing retrospective job since {}.", dayReference);

		DayReference todayReference = DayReference.getToday();

		DayReference currentDay = dayReference;

		while (currentDay.compareTo(todayReference) < 0) {
			dailyJob(currentDay, disabledPhases);
			currentDay = currentDay.daysAfter(1);
		}
	}

}
