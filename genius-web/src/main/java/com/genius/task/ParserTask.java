package com.genius.task;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bit.mirror.core.Coordinator;
import bit.mirror.data.Seed;
import bit.mirror.data.WebPage;

import com.genius.admin.action.CrawlerAction;
import com.genius.dao.GeniusDao;
import com.genius.dao.IDocFeatureVectorDAO;
import com.genius.dao.IExtractConfigurationDAO;
import com.genius.dao.ISensitiveTermDao;
import com.genius.dao.IStatisticRecordDao;
import com.genius.model.Giftable;
import com.genius.model.GiftableFactory;
import com.genius.task.help.FeatureExtractor;
import com.genius.task.help.ContentExtractorChain;
import com.genius.task.help.TermStatistic;
import com.genius.timer.BaseDailyJob;
import com.genius.timer.DayReference;
import com.genius.utils.IndexSocket;

/**
 * 定时任务，进行抽取正文/统计/抽取特征向量等工作
 * 
 * @author lins
 */
public class ParserTask extends BaseDailyJob {
	private final Logger logger = LoggerFactory.getLogger(ParserTask.class);

	private Coordinator mirrorengineCoordinator;
	private GeniusDao dao;
	private IStatisticRecordDao srmDAO;
	private ISensitiveTermDao stmDAO;
	private IDocFeatureVectorDAO dfvDAO;
	private IExtractConfigurationDAO ecmDAO;

	public ISensitiveTermDao getStmDAO() {
		return stmDAO;
	}

	public void setStmDAO(ISensitiveTermDao stmDAO) {
		this.stmDAO = stmDAO;
	}

	public Logger getLogger() {
		return logger;
	}

	public Coordinator getMirrorengineCoordinator() {
		return mirrorengineCoordinator;
	}

	public void setMirrorengineCoordinator(Coordinator mirrorengineCoordinator) {
		this.mirrorengineCoordinator = mirrorengineCoordinator;
	}

	public GeniusDao getDao() {
		return dao;
	}

	public void setDao(GeniusDao dao) {
		this.dao = dao;
	}

	public IStatisticRecordDao getSrmDAO() {
		return srmDAO;
	}

	public void setSrmDAO(IStatisticRecordDao srmDAO) {
		this.srmDAO = srmDAO;
	}

	public IDocFeatureVectorDAO getDfvDAO() {
		return dfvDAO;
	}

	public void setDfvDAO(IDocFeatureVectorDAO dfvDAO) {
		this.dfvDAO = dfvDAO;
	}

	public IExtractConfigurationDAO getEcmDAO() {
		return ecmDAO;
	}

	public void setEcmDAO(IExtractConfigurationDAO ecmDAO) {
		this.ecmDAO = ecmDAO;
	}

	ContentExtractorChain ceChain;
	TermStatistic tdt;
	FeatureExtractor fe;

	/**
	 * 一系列的初始化，暂时不希望这部分被ioc
	 */
	private void initParsers() {
		CrawlerAction.parserStatus = true;
		// 初始化ContentExtratorChain，用来正文抽取
		ceChain = new ContentExtractorChain();
		ceChain.setEcmDao(ecmDAO);
		// 初始化TermStatistic，用来进行统计
		tdt = new TermStatistic();
		tdt.setSrmDAO(this.srmDAO);
		tdt.setStmDAO(this.stmDAO);
		// 初始化FeatureExtractor，用来进行特征抽取
		fe = new FeatureExtractor();
		fe.setDfvDAO(dfvDAO);
	}

	/**
	 * 一系列的初始化，暂时不希望这部分被ioc
	 */
	private void closeParsers() {
		// 初始化ContentExtratorChain，用来正文抽取
		ceChain = null;
		// 初始化TermStatistic，用来进行统计
		tdt = null;
		// 初始化FeatureExtractor，用来进行特征抽取
		fe = null;
		CrawlerAction.parserStatus = false;
	}

	/**
	 * 进行抽取正文/统计/抽取特征向量等工作的主体方法
	 * 
	 * @see com.genius.timer.BaseDailyJob#runInternal(com.genius.timer.DayReference,
	 *      java.util.Date, java.util.Date)
	 */
	@Override
	public void runInternal(DayReference day, Date beginDate, Date endDate)
			throws InterruptedException {
		logger.info("begin to parse and statistic web pages from "+beginDate.toGMTString()+" to "+endDate.toGMTString());

		initParsers();

		// 从mirrorengine中读取数据
		Iterable<WebPage> webPages = mirrorengineCoordinator.getDao()
				.getWebPagesBetweenDate(beginDate, endDate);
		int count = 0;
		// 开始处理
		for (WebPage webPage : webPages) {
			if(webPage.isParsed){//假如已经解析过，那么执行下一个
				logger.info("page: "+webPage.getUrl()+" has been parsed!");
				continue;
			}
			
			logger.info("url:" + webPage.getUrl().toString());
			if (Thread.interrupted()) {
				logger.error("InterruptedException occurs");
				throw new InterruptedException();
			}

			Seed seedOfWebPage = webPage.getSeed();

			/*** 正文抽取部分 ***/
			try {
				if (seedOfWebPage == null)
					ceChain.extract(webPage.getContent(), webPage.getUrl()
							.toString(), "NEWS");
				else
					ceChain.extract(webPage.getContent(), webPage.getUrl()
							.toString(), seedOfWebPage.getType());
				if (!ceChain.isExtracted())
					continue;
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("extractor html error,continue");
				continue;
			}
			// 初始化抽取结果，socket送与索引，持久化
			Giftable giftable = GiftableFactory.getInstance(ceChain
					.getTheParsedType());
			if (giftable == null)
				continue;
			giftable.setUrl(webPage.getUrl().toString());
			giftable.setSourceHost(webPage.getUrl().getHost());
			giftable.setContent(ceChain.get("content"));
			giftable.setTitle(ceChain.get("title"));
			giftable.setPublishTime(webPage.getFetchDate());
			IndexSocket.send(giftable);
			logger.info("send newsReport.url {} to index server.",
					giftable.getUrl());
			dao.saveGiftable(giftable);

			/*** 统计部分 ***/
			tdt.setSrc(ceChain.get("title"), ceChain.get("content"));
			tdt.setUrl(webPage.getUrl().toString());
			tdt.run();

			/*** 特征提取部分 ***/
			fe.setUrl(webPage.getUrl().toString());
			fe.setProcessedPage(ceChain.get("title"), ceChain.get("content"));
			fe.saveDocFeatureVector();

			//标记为已经解析过，下次不再处理
			webPage.isParsed = true;
			mirrorengineCoordinator.getDao().saveWebPage(webPage);
			
			logger.info("WebPage parsed over: {}", giftable.getUrl());
			count++;
		}
		// 结束统计并且将统计结果存入数据库
		tdt.serialize();

		closeParsers();
		logger.info("{} WebPages indexed.", count);
	}
}
