package com.genius.dao;

import java.util.Date;

import com.genius.model.HotTermStatisticRecord;

/**
 * StatisticRecordDao的接口
 * 
 * @author lins
 * @date 2012-5-16
 **/
public interface IStatisticRecordDao {
	public HotTermStatisticRecord loadLatestStatisticRecord();

	public HotTermStatisticRecord loadLatestStatisticRecordBefore(Date date);

	/**
	 * 将begin，end直接的统计合并返回
	 */
	public HotTermStatisticRecord loadStatisticRecordBetween(Date begin, Date end);

	public void saveStatisticRecord(HotTermStatisticRecord sRecord);
}
