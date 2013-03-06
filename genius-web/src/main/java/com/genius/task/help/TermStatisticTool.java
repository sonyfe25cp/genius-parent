package com.genius.task.help;

import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;
import java.util.Iterator;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.genius.model.SensitiveTerm;

import edu.bit.dlde.analysis.statistic.TermArrayList;

/**
 * 用来统计热点词和敏感词的工具
 * 
 * @author lins
 * @date 2012-5-14
 **/
@SuppressWarnings("deprecation")
public class TermStatisticTool {
	// 保存当前处理的网页的热点词
	private TermArrayList cList;
	// 保存所有处理过的网页的热点词
	private TermArrayList ttlList;
	// 保存所有的敏感词
	private Collection<SensitiveTerm> sTerms;
	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Collection<SensitiveTerm> getsTerms() {
		return sTerms;
	}

	public void setsTerms(Collection<SensitiveTerm> sTerms) {
		this.sTerms = sTerms;
	}

	private Analyzer analyzer = new IKAnalyzer(false);

	// 确定TermArrayList的max-size。
	// 之所以用TermArrayList是因为热点词总是很多的词里面的一小部分，不像敏感词需要人为指定且数目是确定的
	private int size = 100;

	public TermStatisticTool() {
		cList = new TermArrayList(size * 2);
		ttlList = new TermArrayList(size * 4);
	}

	public TermStatisticTool(int size) {
		this.size = size;
		cList = new TermArrayList(this.size * 2);
		ttlList = new TermArrayList(this.size * 4);
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void count(String str) throws IOException {
		if (str == null)
			return;

		// use ikanalyzer to tokenizer
		TokenStream tokenStream = analyzer.reusableTokenStream("text",
				new StringReader(str));

		TermAttribute tAttr = (TermAttribute) tokenStream
				.getAttribute(TermAttribute.class);

		// get the term list
		while (tokenStream.incrementToken()) {
			// 统计热度词
			cList.add(tAttr.term());
//			if (tAttr.term().equals("重庆")) {
//				System.out.println(1);
//			}
			// 统计敏感词
			Iterator<SensitiveTerm> it = sTerms.iterator();
			while (it.hasNext()) {
				SensitiveTerm st = it.next();
				for (String stUnit : st.getTerm()){
//					System.out.println(stUnit);
					if (stUnit.equals(tAttr.term())) {
						st.setPrevCount(st.getPrevCount() + 1);
						st.setTtlCount(st.getTtlCount() + 1);
						if (st.getUrl2count().containsKey(url)) {
							st.getUrl2count().put(url,
									st.getUrl2count().get(url) + 1);
						} else {
							st.getUrl2count().put(url, 1L);
						}
					}
				}
			}
			// System.out.print(tAttr.term()+"-");
		}
	}

	/**
	 * 重置当前计数器
	 */
	public void resetCounter() {
		cList = new TermArrayList(size * 2);
	}

	/**
	 * 处理完一个网页需要调用该方法
	 */
	public void oneEpochFinished() {
		// merge tList & ttl
		// 將cList的前20%与ttl合併
		for (int i = 0; i < cList.index * 0.2; i++) {
			int j = ttlList.contains(cList.statisticTerms[i].term);
			if (j != -1) {
				ttlList.statisticTerms[j].count += cList.statisticTerms[i].count;
				ttlList.statisticTerms[j].url2count.put(url,
						cList.statisticTerms[i].count);
			} else {
				// 数组已经饱和
				if (ttlList.index >= size * 4) {
					// 对于后20%的词随机删除一个
					// 是不是可以弄出一个可以自学习的模型来确定词呢？
					int idx2Del = ttlList.index
							- (int) (ttlList.index * 0.2 * Math.random()) - 1;
					ttlList.statisticTerms[idx2Del].count = cList.statisticTerms[i].count;
					ttlList.statisticTerms[idx2Del].term = cList.statisticTerms[i].term;
					ttlList.statisticTerms[idx2Del].url2count.put(url,
							cList.statisticTerms[i].count);
					ttlList.statisticTerms[idx2Del].freq = 0;
				} else {// 数组还未饱和，在末尾添加
					ttlList.statisticTerms[ttlList.index] = cList.statisticTerms[i];
					ttlList.statisticTerms[ttlList.index].url2count.put(url,
							cList.statisticTerms[i].count);
					ttlList.index++;
				}
			}
		}
		// 排个序
		for (int i = 0; i < ttlList.statisticTerms.length
				&& ttlList.statisticTerms[i] != null; i++) {
			for (int j = i; j < ttlList.statisticTerms.length
					&& ttlList.statisticTerms[j] != null; j++) {
				if (ttlList.statisticTerms[i].count < ttlList.statisticTerms[j].count)
					ttlList.exchange(ttlList.statisticTerms[i],
							ttlList.statisticTerms[j]);
			}
		}
		// 重置
		resetCounter();
	}

	public TermArrayList getResult() {
		return ttlList;
	}

//	public static void main(String[] args) throws IOException {
//		TermStatisticTool tool = new TermStatisticTool();
//		File f = new File("/home/lins/data/f");
//		BufferedReader br = new BufferedReader(new FileReader(f));
//		String str;
//		do {
//			str = br.readLine();
//			if (str == null)
//				break;
//			tool.count(str);
//		} while (true);
//		tool.oneEpochFinished();
//		TermArrayList rslt = tool.cList;
//		StatisticTerm[] tList = rslt.statisticTerms;
//	}
}
