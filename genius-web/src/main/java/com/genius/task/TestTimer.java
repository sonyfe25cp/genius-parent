package com.genius.task;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.genius.timer.BaseDailyJob;
import com.genius.timer.DayReference;

import edu.bit.dlde.utils.DLDELogger;

public class TestTimer extends BaseDailyJob{
	
	private DLDELogger logger=new DLDELogger();
	
	// 执行参数
	private String para;

	// 执行方法
	public void run() {
		// 定义输出的格式化日期，以便于区分调用
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		logger.info("the para is : " + para + " ! Time is :"
				+ format.format(new Date()));
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info(" ! Time is :"+ format.format(new Date()));
	}

	public String getPara() {
		return para;
	}

	public void setPara(String para) {
		this.para = para;
	}

	@Override
	protected void runInternal(DayReference day, Date beginDate, Date endDate)
			throws InterruptedException {
		run();
	}
}