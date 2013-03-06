package com.genius.task;

import edu.bit.dlde.genius.model.ResultsUnit;
import edu.bit.dlde.utils.DLDELogger;

public class QueryLogUtils {


	private static DLDELogger logger=new DLDELogger();
	public static void logQuery(String qid,String q,ResultsUnit unit,String userId){
		if(unit!=null&&unit.getResultsList()!=null){
			logger.info("-------begin to log query-----------");
			new Thread(new LogRecord(qid,q,unit,userId)).start();
		}
	}
}
