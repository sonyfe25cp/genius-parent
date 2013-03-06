package com.genius.model;

import java.util.HashMap;
import java.util.Map;

import com.google.code.morphia.annotations.Embedded;

/**
 * 热度词 ,嵌入到StatisticRecord里面
 * @author lins
 * @date 2012-5-16
 **/
@Embedded
public class HotTerm {
	//词
	public String term;
	//出现次数
	public int count = 1;
	//历史遗留，暂时没用
	public double freq;
	//url对出现次数
	@Embedded
	public Map<String, Integer> url2count = new HashMap<String, Integer>();

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public double getFreq() {
		return freq;
	}

	public void setFreq(double freq) {
		this.freq = freq;
	}

	public Map<String, Integer> getUrl2count() {
		// Map<String, Integer> tmp = new HashMap<String, Integer>();
		// Iterator it = url2count.entrySet().iterator();
		// while(it.hasNext()){
		// Entry e= (Entry)it.next();
		// tmp.put(e.getKey().toString().replace(",","."), (Integer)
		// e.getValue());
		// }
		// return tmp;
		return url2count;
	}

	public void setUrl2count(HashMap<String, Integer> url2count) {
		// Map<String, Integer> tmp = new HashMap<String, Integer>();
		// Iterator it = url2count.entrySet().iterator();
		// while(it.hasNext()){
		// Entry e= (Entry)it.next();
		// tmp.put(e.getKey().toString().replace(".",","), (Integer)
		// e.getValue());
		// }
		// this.url2count = tmp;
		this.url2count = url2count;
	}
	
	// 纯粹值的克隆
	public HotTerm clone() {
		HotTerm tmp = new HotTerm();
		tmp.term = term;
		tmp.count = count;
		tmp.freq = freq;
		return tmp;
	}

	// 纯粹值的拷贝
	public void copy(HotTerm tmp) {
		term = tmp.term;
		count = tmp.count;
		freq = tmp.freq;
	}
}