package com.genius.admin.action;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.genius.dao.GeniusDao;
import com.genius.dao.IExtractConfigurationDAO;
import com.genius.task.ParserTask;

import bit.mirror.core.Coordinator;
import bit.mirror.weibo.facade.SinaWeiboCrawler;
import edu.bit.dlde.utils.DLDELogger;

@Controller
@RequestMapping("/admin/crawler")
public class CrawlerAction {

	private DLDELogger logger;
	private Coordinator mirrorengineCoordinator;
	private SinaWeiboCrawler sinaWeiboCrawler;
	private IExtractConfigurationDAO ecmDAO;
	private GeniusDao dao;
	private ParserTask dailyParseData;
	public static boolean crawlerStatus = false;
	public static boolean parserStatus = false;

	public IExtractConfigurationDAO getEcmDAO() {
		return ecmDAO;
	}

	public void setEcmDAO(IExtractConfigurationDAO ecmDAO) {
		this.ecmDAO = ecmDAO;
	}

	public GeniusDao getDao() {
		return dao;
	}

	public void setDao(GeniusDao dao) {
		this.dao = dao;
	}

	public SinaWeiboCrawler getSinaWeiboCrawler() {
		return sinaWeiboCrawler;
	}

	public void setSinaWeiboCrawler(SinaWeiboCrawler sinaWeiboCrawler) {
		this.sinaWeiboCrawler = sinaWeiboCrawler;
	}

	public ParserTask getDailyParseData() {
		return dailyParseData;
	}

	public void setDailyParseData(ParserTask dailyParseData) {
		this.dailyParseData = dailyParseData;
	}

	@RequestMapping(value = "/changec", method = RequestMethod.GET)
	public String changeCrawlerStatus(@RequestParam("switch") boolean flag) {
		logger.info("crawler status change to : " + flag);
		mirrorengineCoordinator.setSuspended(!flag);
		if(flag)
			sinaWeiboCrawler.start();
		else
			sinaWeiboCrawler.stop();
		return "redirect:/admin/crawler/status";
	}

	// 由于是后台，所以无需考虑更多的线程，一个就够
	static Thread thread = null; 

	/**
	 * 当前的想法是直接停止parser线程，而不是sleep，因为完全无法确定sleep多久。
	 */
	@RequestMapping(value = "/changep", method = RequestMethod.GET)
	public String changeParserStatus(@RequestParam("switch") boolean flag) {
		logger.info("parser status change to : " + flag);
		parserStatus = flag;
		if(parserStatus){
			thread = new Thread() {
				public void run() {
					try {
						Date yesterday = new Date(System.currentTimeMillis() - 24*3600*1000);
						dailyParseData.runInternal(null, yesterday, new Date());
					} catch (InterruptedException e) {
						parserStatus = false;
						logger.info("parser interrupted!");
					}
				}
			};
			logger.info("parser start！");
			thread.start();
		}else{
			thread.interrupt();
		}
		return "redirect:/admin/crawler/status";
	}

	@RequestMapping(value = "/status", method = RequestMethod.GET)
	public ModelAndView showStatus() {
		crawlerStatus = !mirrorengineCoordinator.isSuspended();
		long totalSCount = mirrorengineCoordinator.getDao().getSeedCount();
		long newsSCount = mirrorengineCoordinator.getDao().getSeedCount("NEWS");
		long forumSCount = mirrorengineCoordinator.getDao().getSeedCount(
				"FORUM");
		long weiboSCount = mirrorengineCoordinator.getDao().getSeedCount(
				"WEIBO");
		long pageCount = mirrorengineCoordinator.getDao().getWebPageCount();

		long totalXCount = ecmDAO.getCount();
		long newsXCount = ecmDAO.getCount("NEWS");
		long forumXCount = ecmDAO.getCount("FORUM");
		long pageXCount = dao.getGiftableCount();
		return new ModelAndView("crawler-status")
				.addObject("status", crawlerStatus)
				.addObject("pstatus", parserStatus)
				.addObject("totalSCount", totalSCount)
				.addObject("newsSCount", newsSCount)
				.addObject("forumSCount", forumSCount)
				.addObject("weiboSCount", weiboSCount)
				.addObject("pageCount", pageCount)
				.addObject("totalXCount", totalXCount)
				.addObject("newsXCount", newsXCount)
				.addObject("forumXCount", forumXCount)
				.addObject("pageXCount", pageXCount);
	}

	public DLDELogger getLogger() {
		return logger;
	}

	public void setLogger(DLDELogger logger) {
		this.logger = logger;
	}

	public Coordinator getMirrorengineCoordinator() {
		return mirrorengineCoordinator;
	}

	public void setMirrorengineCoordinator(Coordinator mirrorengineCoordinator) {
		this.mirrorengineCoordinator = mirrorengineCoordinator;
	}

	public static boolean isStatus() {
		return crawlerStatus;
	}

	public static void setStatus(boolean status) {
		CrawlerAction.crawlerStatus = status;
	}

}
