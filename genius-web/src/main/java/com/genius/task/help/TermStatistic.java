package com.genius.task.help;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.genius.dao.ISensitiveTermDao;
import com.genius.dao.IStatisticRecordDao;
import com.genius.model.SensitiveTerm;
import com.genius.model.HotTermStatisticRecord;


/**
 * 由parsertask调用，出现在正文抽取之后
 * 除此之外，當前还是单线程的。
 * 
 * @author lins
 * @date 2012-5-16
 **/
public class TermStatistic {
	private IStatisticRecordDao srmDAO;
	private ISensitiveTermDao stmDAO;
	TermStatisticTool tool = new TermStatisticTool();
	String[] src;
	String url;
	public ISensitiveTermDao getStmDAO() {
		return stmDAO;
	}

	public void setStmDAO(ISensitiveTermDao stmDAO) {
		this.stmDAO = stmDAO;
		setSenTerms(stmDAO.getAll());
	}

	public IStatisticRecordDao getSrmDAO() {
		return srmDAO;
	}

	public void setSrmDAO(IStatisticRecordDao srmDAO2) {
		this.srmDAO = srmDAO2;
	}

	public void setSrc(String... str) {
		this.src = str;
	}

	public void setUrl(String url) {
		this.url = url;
		tool.setUrl(url);
	}

	public void setSenTerms(Collection<SensitiveTerm> senTerms){
		//重置prevCount,HashMap
		if(senTerms ==null)
			return;
		Iterator<SensitiveTerm> it = senTerms.iterator();
		while(it.hasNext()){
			SensitiveTerm st = it.next();
			st.setPrevCount(0);
			st.setUrl2count(new HashMap<String,Long>());
		}
		
		tool.setsTerms(senTerms);
	}
	
	public void run() {
		for (String s : src) {
			try {
				tool.count(s);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		tool.oneEpochFinished();
	}

	/**
	 * 将检测的结果存储到数据库
	 */
	public void serialize() {
		HotTermStatisticRecord sRecord = new HotTermStatisticRecord();
		sRecord.setStatisticTerms(tool.getResult().statisticTerms);
		if(sRecord.getStatisticTerms().size()!=0)
		srmDAO.saveStatisticRecord(sRecord);
		if(tool.getsTerms().size()!=0)
		stmDAO.saveAll( tool.getsTerms());
	}
}
