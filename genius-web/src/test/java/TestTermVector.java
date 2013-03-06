//import java.io.File;
//import java.io.IOException;
//
//import org.apache.lucene.analysis.Analyzer;
//import org.apache.lucene.analysis.SimpleAnalyzer;
//import org.apache.lucene.document.Document;
//import org.apache.lucene.index.IndexReader;
//import org.apache.lucene.index.Term;
//import org.apache.lucene.index.TermFreqVector;
//import org.apache.lucene.queryParser.ParseException;
//import org.apache.lucene.queryParser.QueryParser;
//import org.apache.lucene.search.IndexSearcher;
//import org.apache.lucene.search.PhraseQuery;
//import org.apache.lucene.search.Query;
//import org.apache.lucene.search.ScoreDoc;
//import org.apache.lucene.search.TermQuery;
//import org.apache.lucene.search.TopDocs;
//import org.apache.lucene.store.FSDirectory;
//import org.apache.lucene.util.Version;
//
///**
// * 
// * @author lins
// * @date 2012-5-31
// **/
//public class TestTermVector {
//
//	/**
//	 * @param args
//	 * @throws IOException
//	 * @throws ParseException
//	 */
//	public static void main(String[] args) throws IOException, ParseException {
//		IndexReader reader = IndexReader.open(FSDirectory.open(new File(
//				"/home/lins/data/index")));
//		System.out.println(reader + "\tNumDocs = " + reader.numDocs());
//
//		for (int i = 0; i < reader.numDocs(); i++) {
//			// 对contents的token之后的term存于了TermFreqVector
//			String url = reader.document(i).get("url");
//
//			// 通过url检索到对应的doc
//			Term term = new Term("url", url);
//			Query termQuery = new TermQuery(term);
//			IndexSearcher searcher = new IndexSearcher(reader);
//			TopDocs docs = searcher.search(termQuery, 1);
//			ScoreDoc[] scoreDocs = docs.scoreDocs;
//			Document doc;
//			if (scoreDocs != null && scoreDocs.length != 0)
//				doc = searcher.doc(scoreDocs[0].doc);
//			else
//				doc = null;
//			
//			//测试是否找到正确的doc
//			System.out.println(doc.get("title"));
//			System.out.println(reader.document(i).get("title"));
//
//			//获得body的TermFreqVector
//			TermFreqVector termFreqVector = reader.getTermFreqVector(i, "body");
//			if (termFreqVector == null) {
//				System.out.println("termFreqVector is null.");
//				continue;
//			}
//			String fieldName = termFreqVector.getField();//名字
//			String[] terms = termFreqVector.getTerms();//项
//			int[] frequences = termFreqVector.getTermFrequencies();//词频
//
//			System.out.println("FieldName:" + fieldName);
//			for (int j = 0; j < terms.length; j++) {
//				System.out.println("[" + terms[j] + ":" + frequences[j] + "]");
//			}
//		}
//
//	}
//
//}
