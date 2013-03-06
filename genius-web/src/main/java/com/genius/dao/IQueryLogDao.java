package com.genius.dao;

import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;

import com.genius.model.QueryCount;
import com.genius.model.QueryLog;
import com.mongodb.MongoException;

/**
 * 日志相关dao
 * @author ChenJie
 * Purpose:根据搜索词条的频度输出柱状图和饼状图；
 * Process：
			1.从数据库读取，挑选出特定时间段的内容；
			2.将挑选出的内容中按关键词出现的次数排序；
			具体方法是：首先使用Map得到键值关系
					然后将Map中的键值关系写入querycount类的数组中进行排序
			3.对于挑选时间的过滤，创建了dateToInt方法取date的年月日从而便于截取时间段
*/
public interface IQueryLogDao {

	
	public QueryCount[] initial(Date startime, Date endtime) throws UnknownHostException, MongoException;
	 
	public QueryCount[] ProcessQueryLog(List<QueryLog> list);
}
