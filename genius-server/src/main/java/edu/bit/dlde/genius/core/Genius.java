package edu.bit.dlde.genius.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.DefaultSimilarity;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import proj.zoie.api.DataConsumer.DataEvent;
import proj.zoie.api.DocIDMapperFactory;
import proj.zoie.api.ZoieException;
import proj.zoie.api.ZoieIndexReader;
import proj.zoie.api.indexing.ZoieIndexableInterpreter;
import proj.zoie.impl.indexing.DefaultIndexReaderDecorator;
import proj.zoie.impl.indexing.DefaultReaderCache;
import proj.zoie.impl.indexing.MemoryStreamDataProvider;
import proj.zoie.impl.indexing.ZoieConfig;
import proj.zoie.impl.indexing.ZoieSystem;
import edu.bit.dlde.genius.handler.GiftInterpreter;
import edu.bit.dlde.genius.model.QueryForm;
import edu.bit.dlde.genius.model.Results;
import edu.bit.dlde.genius.model.ResultsUnit;
import edu.bit.dlde.utils.DLDEConfiguration;
import edu.bit.dlde.utils.DLDELogger;

/**
 * 
 * search
 * 
 * @author ChenJie
 * 
 */
public class Genius {
	private DLDELogger logger = new DLDELogger();

	private ZoieIndexableInterpreter interpreter;

	private static int unique = 1;

	private long delay = 20;
	private Analyzer analyzer = new IKAnalyzer(false);
	private ZoieSystem<IndexReader, Gift> system;
	private String indexPath;

	private List<ZoieIndexReader<IndexReader>> readers = null;
	private IndexSearcher searcher = null;
	private MultiReader reader = null;

	private MemoryStreamDataProvider<Gift> memoryProvider;

	private static Genius genius = new Genius();

	private Query query;
	private Filter filter = null;
	private int maxQuery = 1000;

	private QueryForm queryForm;
	private ResultsUnit unit;
	private List<Results> resList;

	private Genius() {
		init();
	}

	public static Genius getInstance() {
		if (genius == null) {
			return new Genius();
		} else {
			return genius;
		}
	}

	/**
	 * init genius
	 */
	private void init() {
		interpreter = new GiftInterpreter(delay, analyzer);
		try {
			indexPath = DLDEConfiguration
					.getInstance("searchserver.properties").getValue(
							"indexPath");
		} catch (Exception e) {
			logger.error("can't get the indexPath from searchserver.properties, use the default indexPath: ${user.home}/data/index/genius/");
			indexPath = System.getProperty("user.home") + "/data/index/genius/";
		}
		if (indexPath == null) {
			indexPath = System.getProperty("user.home") + "/data/index/genius/";
		}
		logger.info("genius indexPath:" + indexPath);
		system = createZoie(new File(indexPath), true, delay, analyzer, null,
				ZoieConfig.DEFAULT_VERSION_COMPARATOR, true, interpreter);
		system.start();
		memoryProvider = new MemoryStreamDataProvider<Gift>(
				ZoieConfig.DEFAULT_VERSION_COMPARATOR);
		memoryProvider.setMaxEventsPerMinute(Long.MAX_VALUE);
		memoryProvider.setDataConsumer(system);
		memoryProvider.start();
	}

	/**
	 * add new gift to index for search
	 * 
	 * @param gift
	 */
	public void addGift(Gift gift) {
		List<DataEvent<Gift>> list = new ArrayList<DataEvent<Gift>>();
		list.add(new DataEvent<Gift>(gift, "" + unique));
		memoryProvider.addEvents(list);
		memoryProvider.flush();
		try {
			system.syncWithVersion(3000, "" + unique);
		} catch (ZoieException e) {
			e.printStackTrace();
		}
		unique++;
	}

	public void updateGift(Gift gift) {

	}

	public void delteGift(Gift gift) {

	}

	/**
	 * @return how many files in the index
	 * @throws CorruptIndexException
	 * @throws IOException
	 *             Mar 15, 2012
	 */
	public int count() throws CorruptIndexException, IOException {
		int numDocs = 0;
		for (ZoieIndexReader<IndexReader> r : readers) {
			numDocs += r.numDocs();
//			for (int i = 0; i < r.numDocs(); i++) {
//				Document docTmp = r.document(i);
//				logger.info("author:" + docTmp.get("author"));
//				logger.info("body:" + docTmp.get("body"));
//				logger.info("id:" + docTmp.get("id"));
//			}
		}
		logger.info("total : " + numDocs);
		return numDocs;
	}

	/**
	 * rerank through the rank model Mar 15, 2012
	 */
	private void reRank() {
		resList = unit.getResultsList();
	}

	private void pagination() {
		int pageNo = queryForm.getPageNo();
		int pageSize = queryForm.getPageSize();

		int beginNo = (pageNo - 1) * pageSize;
		int endNo = pageNo * pageSize;
		if (resList != null) {
			int size = resList.size();

			beginNo = beginNo > 0 ? beginNo : 0;
			endNo = endNo > size ? size : endNo;

			if (resList.size() > 10) {
				resList = resList.subList(beginNo, endNo);
			}
			unit.setResultsList(resList);
		}
	}
	/**
	 * topK search for the first search
	 * 
	 * @param form
	 *            Mar 15, 2012
	 */
	private void topKSearch() {
		try {
			long beginTime = System.currentTimeMillis();
			parseQueryForm(queryForm);
			readers = system.getIndexReaders();

			reader = new MultiReader(readers.toArray(new IndexReader[readers
					.size()]), false);
			searcher = new IndexSearcher(reader);
			TopDocs hits = searcher.search(query, filter, maxQuery);
			unit = new ResultsUnit();
			int total = hits.totalHits;
			if (hits.totalHits > 0) {
				logger.info("relevant documents count:" + hits.totalHits);

				Document tmpDoc = null;
				Results resTmp = null;
				long t1=System.currentTimeMillis();
				for (ScoreDoc doc : hits.scoreDocs) {
					int docId = doc.doc;
					resTmp = new Results();
					tmpDoc = searcher.doc(docId);
					resTmp.setResourceKey(tmpDoc.get("id"));
					String body = tmpDoc.get("body");
					// if(body.length()<50){
					// total=total-1;
					// continue;
					// }
					resTmp.setBody(body.length() > 100 ? body.substring(0, 99)
							: body);
					resTmp.setAuthor(tmpDoc.get("author"));
					resTmp.setLink(tmpDoc.get("url"));
					resTmp.setTime(tmpDoc.get("date"));
					resTmp.setTitle(tmpDoc.get("title"));
					resTmp.setScore(doc.score);
					unit.addResult(resTmp);
				}
				long t2=System.currentTimeMillis();
				logger.info("used "+(t2-t1)+" ms");
			} else {
				logger.info("no relevant document.");
			}
			long endTime = System.currentTimeMillis();
			long timeUsed = endTime - beginTime;
			unit.setTime(timeUsed);
			unit.setTotalNum(total);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (searcher != null) {
					searcher.close();
					searcher = null;
					reader.close();
					reader = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				system.returnIndexReaders(readers);
			}
		}

	}

	/**
	 * search the query
	 * 
	 * @param form
	 *            Mar 15, 2012
	 */
	public void bingo() {
		if (queryForm == null) {
			logger.error("no queryForm input");
			return;
		}
		topKSearch();
		reRank();
		pagination();
	}

	/**
	 * parse QueryForm to generate query and filter
	 * 
	 * @param form
	 */
	private void parseQueryForm(QueryForm form) {
		String keywords = form.getKeyWords();
		// query=new TermQuery(new Term("content",keywords));
		QueryParser qp = new QueryParser(Version.LUCENE_34, Gift.DEFAULTFIELD,
				analyzer);
		try {
			query = qp.parse(keywords);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		filter = null;
	}

	public void shutDown() {
		try {
			if (searcher != null) {
				searcher.close();
				searcher = null;
				reader.close();
				reader = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			system.returnIndexReaders(readers);
			memoryProvider.stop();
			system.shutdown();
		}
	}

	private ZoieSystem<IndexReader, Gift> createZoie(File idxDir,
			boolean realtime, long delay, Analyzer analyzer,
			DocIDMapperFactory docidMapperFactory,
			Comparator<String> versionComparator, boolean immediateRefresh,
			ZoieIndexableInterpreter interpreter) {
		ZoieConfig config = new ZoieConfig();
		config.setDocidMapperFactory(docidMapperFactory);
		config.setBatchSize(5);
		config.setBatchDelay(20000);
		config.setRtIndexing(realtime);
		config.setVersionComparator(versionComparator);
		config.setSimilarity(new DefaultSimilarity());
		config.setAnalyzer(analyzer);
		if (immediateRefresh) {
			config.setReadercachefactory(DefaultReaderCache.FACTORY);
		}
		ZoieSystem<IndexReader, Gift> idxSystem = new ZoieSystem<IndexReader, Gift>(
				idxDir, interpreter, new DefaultIndexReaderDecorator(), config);
		return idxSystem;
	}

	public ResultsUnit getUnit() {
		return unit;
	}

	public void setUnit(ResultsUnit unit) {
		this.unit = unit;
	}

	public QueryForm getQueryForm() {
		return queryForm;
	}

	public void setQueryForm(QueryForm queryForm) {
		this.queryForm = queryForm;
	}

}
