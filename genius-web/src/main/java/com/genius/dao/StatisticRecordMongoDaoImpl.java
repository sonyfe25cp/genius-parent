package com.genius.dao;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.genius.model.HotTerm;
import com.genius.model.HotTermStatisticRecord;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

/**
 * statisticrecord的dao
 * 
 * @author lins
 * @date 2012-5-16
 **/
public class StatisticRecordMongoDaoImpl extends MongoDAOBase implements
		IStatisticRecordDao {

	@Override
	public HotTermStatisticRecord loadLatestStatisticRecord() {
		return datastore.find(HotTermStatisticRecord.class).order("-date").get();
	}

	/**
	 * 效率上存在问题，建议暂时先别使用
	 * @see com.genius.dao.IStatisticRecordDao#loadLatestStatisticRecordBefore(java.util.Date)
	 */
	@Override
	public HotTermStatisticRecord loadLatestStatisticRecordBefore(Date date) {
		return datastore.find(HotTermStatisticRecord.class).field("date")
				.lessThan(date).order("-date").get();
	}

	@Override
	public void saveStatisticRecord(HotTermStatisticRecord sRecord) {
		datastore.save(sRecord);
	}

	/**
	 * 当前只是把begin和end直接的StatisticRecord合并，只合并计数项
	 * @see com.genius.dao.IStatisticRecordDao#loadStatisticRecordBetween(java.util.Date, java.util.Date)
	 */
	@Override
	public HotTermStatisticRecord loadStatisticRecordBetween(Date begin, Date end) {
		List<HotTermStatisticRecord> srList = datastore.find(HotTermStatisticRecord.class)
				.field("date").lessThan(end).field("date").greaterThan(begin)
				.asList();
		HotTermStatisticRecord result = new HotTermStatisticRecord();
		
		//将符合时间条件的StatisticRecord合并
		ArrayList<HotTerm> ttl = result.getStatisticTerms();
		for (HotTermStatisticRecord sr : srList) {
			ArrayList<HotTerm> crr = sr.getStatisticTerms();
			for (int i = 0; i < 100 && i < crr.size(); i++) {
				boolean flag = false;
				for(HotTerm t : ttl){
					if(t.term.equals(crr.get(i).term)){
						t.count+=crr.get(i).count;
						flag = true;
						break;
					}
				}
				if(!flag){
					ttl.add(crr.get(i));
				}
			}
		}
		
		for(int i = 0; i < ttl.size(); i ++){
			for(int j=i+1; j < ttl.size();j++){
				if(ttl.get(i).count<ttl.get(j).count){
					String tmp1 = ttl.get(i).term;
					int tmp2 = ttl.get(i).count;
					 ttl.get(i).term = ttl.get(j).term;
					 ttl.get(i).count = ttl.get(j).count;
					 ttl.get(j).term = tmp1;
					 ttl.get(j).count = tmp2;
				}
			}
		}
		return result;
	}

	@Override
	public void init() {
		try {
			if (datastore == null) {
				if (mongo == null) {
					mongo = new Mongo(mongoHost, mongoPort);
				}
				if (morphia == null) {
					morphia = new Morphia();
					morphia.mapPackageFromClass(HotTermStatisticRecord.class);
				}
				datastore = morphia.createDatastore(mongo, mongoDbName);
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}
	}

}
