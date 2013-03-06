package com.genius.dao;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.genius.model.ExtractConfiguration;
import com.genius.model.QueryCount;
import com.genius.model.QueryLog;
import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class QueryLogDaoImpl extends MongoDAOBase implements IQueryLogDao {
	/*
	 * 对从数据库取出来的数据进行处理，主要是FilterQueryLog和ProcessQueryLog。
	 */

	@Override
	public void init() {
		try {
			if (datastore == null) {
				if (mongo == null) {
					mongo = new Mongo("10.1.0.171", mongoPort);
				}
				if (morphia == null) {
					morphia = new Morphia();
					morphia.mapPackageFromClass(ExtractConfiguration.class);
				}
				datastore = morphia.createDatastore(mongo, "genius");
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}

	}
	@Override
	public QueryCount[] initial(Date startime, Date endtime) throws UnknownHostException,
			MongoException {
//		System.out.println("starttime:"+startime+" -- endTime:"+endtime);

		List<QueryLog> list = FilterQueryLog(datastore, startime, endtime);
		QueryCount[] querycount = this.ProcessQueryLog(list);
		return querycount;
		}

	/*
	 * 将mongodb中的QueryLog全部取出来，按照给定的开始时间startime进行过滤，将结果存到新的list中 。
	 */
	public List<QueryLog> FilterQueryLog(Datastore datastore, Date startime, Date endtime) {
//		System.out.println(datastore.find(QueryLog.class).asList());
//		System.out.println("starttime:"+startime+" -- endTime:"+endtime);
		List<QueryLog> list1 = datastore.createQuery(QueryLog.class).filter("date >= ",startime).filter("date <= " ,endtime).asList();
		
		
//		filter("overTime >= ", beginDate).filter("overTime < ", endDate);
//		List<QueryLog> list = new ArrayList<QueryLog>();
//		for (int i = 0; i < list1.size(); i++) {
//			QueryLog querylog = list1.get(i);
//			Date date = querylog.getDate();
//			if (date != null) {
//				int num =ChartUtils.dateToInt(date);
//				if (num >= startime) {
//					list.add(querylog);
//				}
//			} else
//				System.err.println("Log not exists");
//		}
		System.out.println("The size of the QueryLog List is : " + list1.size());
		return list1;
	}

	/*
	 * 对QueryLog进行处理，其步骤是 
	 * 1. 新建一个hashmap，读出所有QueryLog中的查询项，如果项存在则它的计数加一；如果不存在，则新插入一个键值对应关系到map中去。 
	 * 2.将已经得到的键值一一映射的map遍历，把它们的键值关系取出来存入到QueryCount数组中， 按count值的大小对其进行排序 。
	 * 3.对于QueryCount这个数组，如果其大小小于10，则取全部数据； 如果大于等于10，则只取前10个数据。
	 */
	@Override
	public QueryCount[] ProcessQueryLog(List<QueryLog> list) {
		
		if(list==null||list.size()==0){
			System.out.println("no queryCount!!");
			return new QueryCount[0];
		}
		
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (QueryLog que : list) {
			String key = que.getQuery();
			if (!map.containsKey(key)) {
				map.put(key, 1);
			} else {
				int count = map.get(key) + 1;
				map.put(key, count);
			}
		}
		System.out.println("the map size is : " + map.size());

		ArrayList<QueryCount> listQueryCount = new ArrayList<QueryCount>();
		for (Map.Entry<String, Integer> entity : map.entrySet()) {
			String query = entity.getKey();
			int count = entity.getValue();
			QueryCount querycount = new QueryCount(query, count);
			listQueryCount.add(querycount);
		}
		Collections.sort(listQueryCount);
		QueryCount[] newquerycount = null;
		if(listQueryCount.size()>10){
			newquerycount = new QueryCount[10];
			int i=0;
			for(QueryCount qc:listQueryCount){
				if(i>9){
					break;
				}
				newquerycount[i]=qc;
				i++;
			}
		}else{
			newquerycount = new QueryCount[listQueryCount.size()];
			int i=0;
			for(QueryCount qc:listQueryCount){
				newquerycount[i]=qc;
				i++;
			}
		}
		
//		QueryCount[] querycount = new QueryCount[listQueryCount.size()];
//		listQueryCount.toArray(querycount);
//		Arrays.sort(querycount);
//		QueryCount[] newquerycount = null;
//		if (querycount.length <= 10) {
//			newquerycount = new QueryCount[querycount.length];
//			for (int i = 0; i <= newquerycount.length - 1; i++) {
//				newquerycount[i] = querycount[newquerycount.length - 1 - i];
//			}
//		} else {
//			newquerycount = new QueryCount[10];
//			for (int i = 0; i <= newquerycount.length - 1; i++) {
//				newquerycount[i] = querycount[newquerycount.length - 1 - i];
//			}
//		}
//		for (QueryCount tempcount : newquerycount) {
//			System.out.println(tempcount);
//		}
		return newquerycount;
	}


}
