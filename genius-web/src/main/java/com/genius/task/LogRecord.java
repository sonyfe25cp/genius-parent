package com.genius.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.genius.dao.GeniusDao;
import com.genius.model.QueryLog;

import edu.bit.dlde.genius.model.Results;
import edu.bit.dlde.genius.model.ResultsUnit;
import edu.bit.dlde.utils.DLDELogger;

public class LogRecord implements Runnable {

	private DLDELogger logger = new DLDELogger();

	private GeniusDao dao;

	private QueryLog log;

	public LogRecord(String qid, String q, ResultsUnit unit, String userId) {
		convert(qid, q, unit, userId);
	}

	private void convert(String qid, String q, ResultsUnit unit, String userId) {
		log = new QueryLog();
		List<String> docIds = new ArrayList<String>();
		for (Results r : unit.getResultsList()) {
			docIds.add(r.getResourceKey());
		}
		log.setDocNum(docIds);
		log.setQid(qid);
		log.setQuery(q);
		log.setUserId(userId);
		log.setDate(new Date(System.currentTimeMillis()));
	}

	@Override
	public void run() {
		if (dao != null) {
			dao.save(log);
		} else {
			logger.error("dao is null ");
		}

	}

	public GeniusDao getDao() {
		return dao;
	}

	public void setDao(GeniusDao dao) {
		this.dao = dao;
	}
}