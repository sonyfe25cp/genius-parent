package com.genius.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import edu.bit.dlde.genius.model.QueryForm;
import edu.bit.dlde.genius.model.Results;
import edu.bit.dlde.genius.model.ResultsUnit;

/**
 * 用于分布式合并结果
 * @author ChenJie
 *
 */
public class CombineResults {

	
	public static ResultsUnit Combine(List<ResultsUnit> units,QueryForm query){
		ResultsUnit ru=new ResultsUnit();
		int uniqueQueryKey=0;
		int totalNum=0;
		long time=0;
		List<Results> totalResults=new ArrayList<Results>();
		if(units==null){
			return ru;
		}
		for(ResultsUnit unit:units){
			if(unit!=null&&unit.getTotalNum()>0){//有结果
				totalResults.addAll(unit.getResultsList());//合并结果集
				time=time>unit.getTime()?time:unit.getTime();//最长的时间
				totalNum+=unit.getTotalNum();//总数
			}
			uniqueQueryKey=unit.getUniqueQueryKey();
		}
		if(totalNum==0){//如果没有结果，就不用合并了
			return ru;
		}
		Collections.sort(totalResults, new Comparator<Results>(){
			@Override
			public int compare(Results o1, Results o2) {
				double s1=o1.getScore();
				double s2=o2.getScore();
				if(s1>s2){
					return 1;
				}else{
					return -1;
				}
			}
		});
		List<Results> list=new ArrayList<Results>();
		list.addAll(totalResults.size()>10?totalResults.subList(0, 10):totalResults);//判断大小
		ru.setResultsList(list);
		ru.setTime(time);
		ru.setTotalNum(totalNum);
		ru.setUniqueQueryKey(uniqueQueryKey);
		return ru;
	}
	
	
}
