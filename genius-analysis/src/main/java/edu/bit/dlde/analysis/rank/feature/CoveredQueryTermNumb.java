package edu.bit.dlde.analysis.rank.feature;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.index.TermPositions;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;
import org.wltea.analyzer.lucene.IKQueryParser;

/**
 * To get covered query term number of "body", "anchor", "title", "url",
 * "whole document". See more at {@link http
 * ://research.microsoft.com/en-us/projects/mslr/feature.aspx} features 1-5.
 * 
 * @author lins
 * @date 2012-5-10
 **/
public class CoveredQueryTermNumb {
	static Logger log = Logger.getLogger(CoveredQueryTermNumb.class);
	IndexReader reader;

	public IndexReader getReader() {
		return reader;
	}

	public void setReader(IndexReader reader) {
		this.reader = reader;
	}

	public CoveredQueryTermNumb() {
		try {
			if (reader == null)
				reader = IndexReader.open(FSDirectory.open(new File(
						"/home/lins/data/index")));
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public CoveredQueryTermNumb(IndexReader reader) {
		this.reader = reader;
	}

	public double[] getValue(ArrayList<String> str, int docId) {
		double[] result = new double[5];

		for (String s : str) {
			ArrayList<Term> terms = new ArrayList<Term>();

			try {
				TermFreqVector[] vs = reader.getTermFreqVectors(docId);
				for (TermFreqVector v : vs) {
					if (v.getField().equals("body")) {
						result[0] += v.getTermFrequencies()[v.indexOf(s)];
						System.out.println(result[0]);
					} else if (v.getField().equals("anchor")) {
						result[1] += v.getTermFrequencies()[v.indexOf(s)];
						System.out.println(result[1]);
					} else if (v.getField().equals("title")) {
						result[2] += v.getTermFrequencies()[v.indexOf(s)];
						System.out.println(result[2]);
					} else if (v.getField().equals("url")) {
						result[3] += v.getTermFrequencies()[v.indexOf(s)];
						System.out.println(result[3]);
					}

				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		int size = str.size();
		result[0] /= size;
		result[1] /= size;
		result[2] /= size;
		result[3] /= size;
		result[4] = result[0] + result[1] + result[2] + result[3];
		return result;
	}

	public static void main(String[] args) throws CorruptIndexException,
			IOException {

		IndexReader reader = IndexReader.open(FSDirectory.open(new File(
				"/home/lins/data/index")));

		Analyzer analyzer = new IKAnalyzer(false);
		TokenStream tokenStream = analyzer.reusableTokenStream("text",
				new StringReader("中华 人民共和国de北京的新华社 新华社"));
		TermAttribute term = (TermAttribute) tokenStream
				.getAttribute(TermAttribute.class);
		ArrayList<String> terms = new ArrayList<String>();
		while (tokenStream.incrementToken()) {
			terms.add(term.term());
		}

		TermEnum termEnum = reader.terms();
		while (termEnum.next()) {
			log.debug("\n" + termEnum.term().field() + "域中出现的词语："
					+ termEnum.term().text());
			log.debug(" 出现改词的文档数=" + termEnum.docFreq());

			TermPositions termPositions = reader.termPositions(termEnum.term());
			int i = 0;
			int j = 0;
			while (termPositions.next()) {
				log.debug("\n" + (i++) + "->" + "    文章编号:"
						+ termPositions.doc() + ", 出现次数:"
						+ termPositions.freq() + "    出现位置：");
				for (j = 0; j < termPositions.freq(); j++)
					log.debug("[" + termPositions.nextPosition() + "]");
				log.debug("\n");
			}
		}
	}
}
