package edu.bit.dlde.genius.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResultsUnit implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int uniqueQueryKey;//唯一查询key

	private List<Results> resultsList;//结果序列
	
	private int totalNum=0;
	
	private long time=0;
	
	public ResultsUnit(){
		
	}
	
	public void addResult(Results results){
		if(resultsList==null){
			resultsList=new ArrayList<Results>();
		}
		resultsList.add(results);
		totalNum++;
	}

	public int getUniqueQueryKey() {
		return uniqueQueryKey;
	}

	public void setUniqueQueryKey(int uniqueQueryKey) {
		this.uniqueQueryKey = uniqueQueryKey;
	}

	public List<Results> getResultsList() {
		return resultsList;
	}

	public void setResultsList(List<Results> resultsList) {
		this.resultsList = resultsList;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	

	
	
}
