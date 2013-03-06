package com.genius.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

/**
 * 用来保存所有的敏感词的model
 * 
 * @author lins
 * @date 2012-5-17
 **/
@Entity
public class SensitiveTerm {
	@Id
	private ObjectId id = null;

	// 词
	private ArrayList<String> term;

	// 添加该词的时间
	private Date date = new Date();

	// 在哪一url中出现了多少词
	private HashMap<String, Long> url2count = new HashMap<String, Long>();

	// 总共出现了多少次
	private long ttlCount = 0;

	// 上一次出现了多少次
	private long prevCount = 0;

	private boolean enabled = true;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public long getPrevCount() {
		return prevCount;
	}

	public void setPrevCount(long prevCount) {
		this.prevCount = prevCount;
	}

	public HashMap<String, Long> getUrl2count() {
		return url2count;
	}

	public void setUrl2count(HashMap<String, Long> url2count) {
		this.url2count = url2count;
	}

	public long getTtlCount() {
		return ttlCount;
	}

	public void setTtlCount(long count) {
		this.ttlCount = count;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public ArrayList<String> getTerm() {
		return term;
	}

	public void setTerm(ArrayList<String> term) {
		this.term = term;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return -1 if term is contained in strings; 1 if term contains strings; 0
	 *         no relationship exist.
	 */
	public int relationWith(String... strings) {
		if (term.containsAll(Arrays.asList(strings)))
			return 1;

		if (Arrays.asList(strings).containsAll(term))
			return -1;

		return 0;
	}

	/**
	 * @return -1 if term is contained in strings; 1 if term contains strings; 0
	 *         no relationship exist;2 if equal
	 */
	public int relationWith(List<String> strings) {
		// 先查看是不是包含，也就是说假如两个相等的话也就是返回2
		if (term.containsAll(strings)){
			if(strings.containsAll(term))
				return 2;
			return 1;
		}

		if (strings.containsAll(term))
			return -1;

		return 0;
	}
	
	public String getHtmlFitableTerm(){
		StringBuilder sb = new StringBuilder();
		for(String t : term){
			sb.append(t.replaceAll(">", "&gt;")
					.replaceAll("<", "&lt;"));
			sb.append("|");
		}
		return sb.substring(0, sb.length()-1).toString();
	}
//	public static void main(String[] args) {
//		String[] a = { "1", "2" };
//		ArrayList<String> list = new ArrayList<String>();
//		list.add("1");
//		list.add("2");
//		System.out.println(list.containsAll(Arrays.asList(args)));
//	}
}
