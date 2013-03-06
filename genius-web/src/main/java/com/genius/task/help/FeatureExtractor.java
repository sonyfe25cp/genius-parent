package com.genius.task.help;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.genius.dao.IDocFeatureVectorDAO;
import com.genius.model.DocFeatureVector;

/**
 * 用于抽取doc的特征，由ParserTask调用
 *@author lins
 *@date 2012-6-3
 **/
@SuppressWarnings("deprecation")
public class FeatureExtractor {
	private boolean flag;
	private IDocFeatureVectorDAO dfvDAO;
	private Analyzer analyzer = new IKAnalyzer(false);
	private String wholePage;
	private String[] processedPage;
	private String url;
	
	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public IDocFeatureVectorDAO getDfvDAO() {
		return dfvDAO;
	}

	public void setDfvDAO(IDocFeatureVectorDAO dfvDAO) {
		this.dfvDAO = dfvDAO;
	}

	public String getWholePage() {
		return wholePage;
	}

	public void setWholePage(String wholePage) {
		this.wholePage = wholePage;
	}

	public String[] getProcessedPage() {
		return processedPage;
	}

	public void setProcessedPage(String... processedPage) {
		this.processedPage = processedPage;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public DocFeatureVector getDocFeatureVecor(){
		flag = false;
		// 假如没有指定url，那么就不处理
		if(url==null||url.trim().equals(""))
			return null;
		
		DocFeatureVector dfv = new DocFeatureVector();
		dfv.setUrl(url);
		flag = true;
		
		/**
		 *  抽取term in body的向量
		 */
		TokenStream tokenStream ;
		try {
			for (String s : processedPage) {
				tokenStream = analyzer.reusableTokenStream("text",
						new StringReader(s));
				TermAttribute tAttr = (TermAttribute) tokenStream
						.getAttribute(TermAttribute.class);
				ArrayList<String> listA = new ArrayList<String>();
				ArrayList<Integer> listB = new ArrayList<Integer>();
				// 统计
				while (tokenStream.incrementToken()) {
					String term = tAttr.term();
					if(term==null||term.trim().equals("")||term.length()<2)
						continue;
					
					int idx= listA.indexOf(term);
					if(idx>=0){
						listB.set(idx, listB.get(idx)+1);
					}else{
						listA.add(term);
						listB.add(1);
					}
				}
				// 按照listB排序
				for(int i =0;i < listA.size(); i++){
					for(int j = i+1; j <listA.size(); j++){
						if(listB.get(i)<listB.get(j)){
							String tmpA;
							Integer tmpB;
							tmpA = listA.get(i);
							tmpB = listB.get(i);
							listB.set(i, listB.get(j));
							listB.set(j, tmpB);
							listA.set(i, listA.get(j));
							listA.set(j, tmpA);
						}
					}
				}
				//存入DocFeatureVector
				dfv.setTermsInBody(listA.toArray(new String[listA.size()]));
				dfv.setOccurenceInBody(listB.toArray(new Integer[listB.size()]));
			}
		} catch (IOException e) {
			flag = false;
			return null;
		}
		
		
		return flag?dfv:null;
	}
	
	public void saveDocFeatureVector(){
		DocFeatureVector docFV = getDocFeatureVecor();
		if(docFV!=null&&flag)
			dfvDAO.saveDocFeatureVector(docFV);
	}
}
