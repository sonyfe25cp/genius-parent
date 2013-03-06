package com.genius.model;

import java.util.ArrayList;
import java.util.Date;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;

/**
 * 用来记录最多的词，以及每个词对应的url。存在一个时间来记录统计的时间。
 * 
 * @author lins
 * @date 2012-5-16
 **/
@Entity
public class HotTermStatisticRecord {
	@Id
	private ObjectId id = null;

	@Embedded
	private ArrayList<HotTerm> statisticTerms = new ArrayList<HotTerm>();
	//private StatisticTerm[] statisticTerms = null;
	
	@Indexed
	private Date date;

	private boolean isLatest;
	public HotTermStatisticRecord() {
		date = new Date();
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public ArrayList<HotTerm> getStatisticTerms() {
		return statisticTerms;
	}

	public void setStatisticTerms(ArrayList<HotTerm> ts) {
		this.statisticTerms = ts;
	}

	public void setStatisticTerms(
			edu.bit.dlde.analysis.statistic.TermArrayList.StatisticTerm[] ts) {
		for (edu.bit.dlde.analysis.statistic.TermArrayList.StatisticTerm t : ts) {
			if(t==null)
				break;
			HotTerm tmp = new HotTerm();
			tmp.setCount(t.getCount());
			tmp.setFreq(t.getCount());
			tmp.setTerm(t.getTerm());
			tmp.setUrl2count(t.getUrl2count());
			statisticTerms.add(tmp);
		}
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	
}
