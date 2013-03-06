package edu.bit.dlde.analysis.statistic;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

/**
 * 一个用来存储统计StatisticTerm的顺序表。 考虑到内存的限制，顺序表是固定大小的；除此之外，顺序表自动按照词频排列。
 * 
 * @author lins
 * @date 2012-5-14
 **/
public class TermArrayList {
	public StatisticTerm[] statisticTerms;
	// 顺序表的大小,default100
	public int maxSize = 100;
	// 当前指针位置，即 大小
	public int index = 0;
	

	// public StatisticTerm[] tInPages;
	// ttl当前指针位置，即 大小
	// private int ttlIndex = 0;

	public TermArrayList(int size) {
		maxSize = size;
		statisticTerms = new StatisticTerm[size];
		// tInPages = new StatisticTerm[size * 2];
	}

	//private List<String> stopWord = Arrays.asList("");
	/**
	 * 添加一个term，首先查看term所在的位置。 如果位置不为-1，取得那个StatisticTerm，自增计数器，并且将其往前移动
	 */
	public void add(String term) {
		if(term.length()==1)
			return;
		
		int i = contains(term);
		if (i != -1) {
			// 自增词频计数器
			statisticTerms[i].count++;

			// 前移StatisticTerm
			int j = i - 1;
			while (j >= 0 && statisticTerms[j].count < statisticTerms[i].count) {
				j--;
			}
			if (i != j + 1) {
				exchange(statisticTerms[i], statisticTerms[j + 1]);
			}
		} else {
			// 数组已经饱和
			if (index >= maxSize) {
				// 对于后20%的词随机删除一个
				// 是不是可以弄出一个可以自学习的模型来确定词呢？
				int idx2Del = index - (int) (index * 0.2 * Math.random()) - 1;
				statisticTerms[idx2Del].count = 1;
				statisticTerms[idx2Del].term = term;
				statisticTerms[idx2Del].freq = 0;
			} else {// 数组还未饱和，在末尾添加
				statisticTerms[index] = new StatisticTerm();
				statisticTerms[index].count = 1;
				statisticTerms[index].term = term;
				statisticTerms[index].freq = 0;
				index++;
			}
		}
	}

	public int contains(String term) {
		int i = 0;
		for (StatisticTerm t : statisticTerms) {
			if (t == null)
				break;
			if (term.equals(t.term))
				return i;
			i++;
		}
		return -1;
	}

	public void exchange(StatisticTerm t1, StatisticTerm t2) {
		StatisticTerm tmp = t1.clone();
		t1.copy(t2);
		t2.copy(tmp);
	}

	@Entity
	public class StatisticTerm {
		@Id
		private ObjectId id = null;
		public String term;
		public ObjectId getId() {
			return id;
		}

		public void setId(ObjectId id) {
			this.id = id;
		}

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

		public HashMap<String, Integer> getUrl2count() {
			return url2count;
		}

		public void setUrl2count(HashMap<String, Integer> url2count) {
			this.url2count = url2count;
		}

		public int count = 1;
		public double freq;
		public HashMap<String, Integer> url2count = new HashMap<String, Integer>();
		
		// 纯粹值的克隆
		public StatisticTerm clone() {
			StatisticTerm tmp = new StatisticTerm();
			tmp.term = term;
			tmp.count = count;
			tmp.freq = freq;
			tmp.url2count = url2count;
			return tmp;
		}

		// 纯粹值的拷贝
		public void copy(StatisticTerm tmp) {
			term = tmp.term;
			count = tmp.count;
			freq = tmp.freq;
			url2count = tmp.url2count;
		}
	}
}
