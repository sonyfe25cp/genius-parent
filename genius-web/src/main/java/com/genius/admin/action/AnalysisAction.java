package com.genius.admin.action;


import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.genius.dao.GeniusDao;
import com.genius.model.QueryLog;

import edu.bit.dlde.tools.graphics.WordsThumb;
import edu.bit.dlde.utils.DLDELogger;

/**
 * 统计分析类
 * 
 * @author ChenJie
 * 
 */
@Controller
@RequestMapping("/admin/analysis")
public class AnalysisAction {
	private DLDELogger logger;
	private GeniusDao dao;

	@RequestMapping(value = "/cloud", method = RequestMethod.GET)
	public String queryWordsCloud() {
//		logger.info("path:--"+this.getClass().getResource("/").getPath());
		
		String oPath=this.getClass().getResource("/").getPath();
		String path=oPath.replace("WEB-INF/classes/", "cloud/");
		
		
		String qCloud = path+"query-words-cloud.png";
		String bgPng = path+"blank.png";
		
		List<QueryLog> logList=dao.loadQueryLogInRange(null);
		
		HashMap<String,Integer> wordsMap= new HashMap<String,Integer>();
		for(QueryLog log:logList){
			String word=log.getQuery();
			if(wordsMap.containsKey(word)){
				int count=wordsMap.get(word);
				wordsMap.put(word, count+1);
			}else{
				wordsMap.put(word, 1);
			}
		}
		for(Entry<String,Integer> entry:wordsMap.entrySet()){
			logger.info(entry.getKey()+" -- "+entry.getValue());
		}
		WordsThumb.getWordCloud(wordsMap, qCloud, bgPng, null);//
		return "query-words-cloud";
	}

	public DLDELogger getLogger() {
		return logger;
	}

	public void setLogger(DLDELogger logger) {
		this.logger = logger;
	}

	public GeniusDao getDao() {
		return dao;
	}

	public void setDao(GeniusDao dao) {
		this.dao = dao;
	}

}
