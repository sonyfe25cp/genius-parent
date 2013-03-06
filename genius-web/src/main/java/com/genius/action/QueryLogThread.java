package com.genius.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.genius.dao.GeniusDao;

import com.genius.model.QueryLog;

import edu.bit.dlde.genius.model.Results;
import edu.bit.dlde.genius.model.ResultsUnit;

public class QueryLogThread extends Thread{
	private ResultsUnit unit = null;
	private GeniusDao dao;
	private String q;
	private String qid;
	private String uid;
	QueryLogThread(ResultsUnit unit,String q,String qid,String uid,GeniusDao dao){
		this.unit=unit;
		this.q=q;
		this.qid=qid;
		this.uid=uid;
		this.dao=dao;
		
	}
	public void run(){
//		try {
//			sleep(5000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		//System.out.println("对象中的dao: "+search.getDao());
//		try {
//			sleep(10000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		if(unit.getResultsList()!=null&&unit.getResultsList().size()!=0){
			QueryLog qlog=convert(qid,q,unit,uid);
			System.out.println(dao);
			dao.save(qlog);
		}
		
	}
	public QueryLog convert(String qid, String q, ResultsUnit unit, String userId) {
		QueryLog log = new QueryLog();
		List<String> docIds = new ArrayList<String>();
		for (Results r : unit.getResultsList()) {
			docIds.add(r.getResourceKey());
		}
		log.setDocNum(docIds);
		log.setQid(qid);
		log.setQuery(q);
		log.setUserId(userId);
		log.setDate(new Date(System.currentTimeMillis()));
		return log;
	}
	public GeniusDao getDao() {
		return dao;
	}
	public void setDao(GeniusDao dao) {
		this.dao = dao;
	}
	

}
