package com.genius.admin.action;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.jfree.chart.JFreeChart;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.genius.dao.IQueryLogDao;
import com.genius.dao.IStatisticRecordDao;
import com.genius.model.QueryCount;
import com.genius.model.HotTermStatisticRecord;
import com.genius.utils.ChartUtils;
import com.mongodb.MongoException;

import edu.bit.dlde.utils.DLDELogger;

/**
 * 生成统计图表,图片存放在/cloud/路径下
 * @author C.M
 * @Date 修改时间：5.10
 **/
@Controller
@RequestMapping("/admin/chart")
public class ChartAction {
	private DLDELogger logger;
	private int height = 320;
	private int width = 580;

	private String chartName = "Genius统计报告";

	private IQueryLogDao queryLogDao;
	private IStatisticRecordDao srmDAO;

	public IStatisticRecordDao getSrmDAO() {
		return srmDAO;
	}

	public void setSrmDAO(IStatisticRecordDao srmDAO) {
		this.srmDAO = srmDAO;
	}

	@RequestMapping(value = "/getchart", method = RequestMethod.GET)
	public ModelAndView GetChart() {
		ModelAndView mav = new ModelAndView("chart_create");
		return mav;
	}

	@RequestMapping(value = "/daybarchart", method = RequestMethod.GET)
	public ModelAndView DayBarChart() throws MongoException, IOException {
		ModelAndView mav = new ModelAndView("chart_show");
//		int startime = ChartUtils.dateToInt(new Date());
		Date currentime = new Date();
		Date startime = ChartUtils.getToday(new Date());
//		System.out.println("starttime:"+startime+" -- endTime:"+currentime);
		QueryCount[] querycount = queryLogDao.initial(startime, currentime);
		JFreeChart barchart = ChartUtils.createBarChart(chartName, querycount);
		String oPath = this.getClass().getResource("/").getPath();
		String savepath = oPath.replace("WEB-INF/classes/", "cloud/");
		File image = ChartUtils.saveChart(barchart, new Date(), "day", "bar",
				width, height, savepath);
		String fileName = image.getName();
		String path = "/cloud/" + fileName;
		mav.addObject("path", path);
		return mav;
	}

	@RequestMapping(value = "/daypiechart", method = RequestMethod.GET)
	public ModelAndView DayPieChart() throws MongoException, IOException {
		ModelAndView mav = new ModelAndView("chart_show");
		Date currentime = new Date();
		Date startime = ChartUtils.getToday(new Date());
		QueryCount[] querycount = queryLogDao.initial(startime, currentime);
		JFreeChart piechart = ChartUtils.createPieChart(chartName, querycount);
		String oPath = this.getClass().getResource("/").getPath();
		String savepath = oPath.replace("WEB-INF/classes/", "cloud/");
		File image = ChartUtils.saveChart(piechart, new Date(), "day", "pie",
				width, height, savepath);
		String fileName = image.getName();
		String path = "/cloud/" + fileName;
		mav.addObject("path", path);
		return mav;
	}

	@RequestMapping(value = "/weekbarchart", method = RequestMethod.GET)
	public ModelAndView WeekBarChart() throws MongoException, IOException {
		ModelAndView mav = new ModelAndView("chart_show");
		Date currentime = new Date();
		Date startime = ChartUtils.getDayofWeek(new Date());
		QueryCount[] querycount = queryLogDao.initial(startime, currentime);
		JFreeChart barchart = ChartUtils.createBarChart(chartName, querycount);
		String oPath = this.getClass().getResource("/").getPath();
		String savepath = oPath.replace("WEB-INF/classes/", "cloud/");
		File image = ChartUtils.saveChart(barchart, new Date(), "week", "bar",
				width, height, savepath);
		String fileName = image.getName();
		String path = "/cloud/" + fileName;
		mav.addObject("path", path);
		return mav;
	}

	@RequestMapping(value = "/weekpiechart", method = RequestMethod.GET)
	public ModelAndView WeekPieChart() throws MongoException, IOException {
		ModelAndView mav = new ModelAndView("chart_show");
		Date currentime = new Date();
		Date startime = ChartUtils.getDayofWeek(new Date());
		QueryCount[] querycount = queryLogDao.initial(startime, currentime);
		JFreeChart piechart = ChartUtils.createPieChart(chartName, querycount);
		String oPath = this.getClass().getResource("/").getPath();
		String savepath = oPath.replace("WEB-INF/classes/", "cloud/");
		File image = ChartUtils.saveChart(piechart, new Date(), "week", "pie",
				width, height, savepath);
		String fileName = image.getName();
		String path = "/cloud/" + fileName;
		mav.addObject("path", path);
		return mav;
	}

	@RequestMapping(value = "/monthbarchart", method = RequestMethod.GET)
	public ModelAndView MonthBarChart() throws MongoException, IOException {
		ModelAndView mav = new ModelAndView("chart_show");
		Date currentime = new Date();
		Date startime = ChartUtils.getDayofMonth(new Date());
		srmDAO.loadLatestStatisticRecord();
		QueryCount[] querycount = queryLogDao.initial(startime, currentime);
		JFreeChart barchart = ChartUtils.createBarChart(chartName, querycount);
		String oPath = this.getClass().getResource("/").getPath();
		String savepath = oPath.replace("WEB-INF/classes/", "cloud/");
		File image = ChartUtils.saveChart(barchart, new Date(), "month", "bar",
				width, height, savepath);
		String fileName = image.getName();
		String path = "/cloud/" + fileName;
		mav.addObject("path", path);
		return mav;
	}

	@RequestMapping(value = "/monthpiechart", method = RequestMethod.GET)
	public ModelAndView MonthPieChart() throws MongoException, IOException {
		ModelAndView mav = new ModelAndView("chart_show");
		Date currentime = new Date();
		Date startime = ChartUtils.getDayofMonth(new Date());
		QueryCount[] querycount = queryLogDao.initial(startime, currentime);
		JFreeChart piechart = ChartUtils.createPieChart(chartName, querycount);
		String oPath = this.getClass().getResource("/").getPath();
		String savepath = oPath.replace("WEB-INF/classes/", "cloud/");
		File image = ChartUtils.saveChart(piechart, new Date(), "month", "pie",
				width, height, savepath);
		String fileName = image.getName();
		String path = "/cloud/" + fileName;
		mav.addObject("path", path);
		return mav;
	}

	/**
	 * 将StatisticRecord转换为QueryCount的方法。。。
	 * 更加理想的方法是将StatisticRecord和QueryCount均实现一个接口
	 */
	private QueryCount[] toQueryRecord(HotTermStatisticRecord sr) {
		if (sr == null)
			return null;

		int size = Math.min(10, sr.getStatisticTerms().size());
		QueryCount[] qc = new QueryCount[size];
		for (int i = 0; i < size; i++) {
			qc[i] = new QueryCount();
			qc[i].setCount(sr.getStatisticTerms().get(i).getCount());
			qc[i].setQuery(sr.getStatisticTerms().get(i).getTerm());
		}
		return qc;
	}

	@RequestMapping(value = "/daybarchart4hot", method = RequestMethod.GET)
	public ModelAndView DayBarChart4Hot() throws MongoException, IOException {
		ModelAndView mav = new ModelAndView("chart_show");
		//int startime = ChartUtils.dateToInt(new Date());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = format.getCalendar();
		cal.add(Calendar.DATE, -1);
		cal.getTime();
		QueryCount[] querycount = toQueryRecord(srmDAO
				.loadStatisticRecordBetween(cal.getTime(), new Date()));
		JFreeChart barchart = ChartUtils.createBarChart(chartName, querycount);
		String oPath = this.getClass().getResource("/").getPath();
		String savepath = oPath.replace("WEB-INF/classes/", "cloud/");
		File image = ChartUtils.saveChart(barchart, new Date(), "day", "bar",
				width, height, savepath);
		String fileName = image.getName();
		String path = "/cloud/" + fileName;
		mav.addObject("path", path);
		return mav;
	}

	@RequestMapping(value = "/daypiechart4hot", method = RequestMethod.GET)
	public ModelAndView DayPieChart4Hot() throws MongoException, IOException {
		ModelAndView mav = new ModelAndView("chart_show");
		//int startime = ChartUtils.dateToInt(new Date());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = format.getCalendar();
		cal.add(Calendar.DATE, -1);
		cal.getTime();
		QueryCount[] querycount = toQueryRecord(srmDAO
				.loadStatisticRecordBetween(cal.getTime(), new Date()));
		JFreeChart piechart = ChartUtils.createPieChart(chartName, querycount);
		String oPath = this.getClass().getResource("/").getPath();
		String savepath = oPath.replace("WEB-INF/classes/", "cloud/");
		File image = ChartUtils.saveChart(piechart, new Date(), "day", "pie",
				width, height, savepath);
		String fileName = image.getName();
		String path = "/cloud/" + fileName;
		mav.addObject("path", path);
		return mav;
	}

	@RequestMapping(value = "/weekbarchart4hot", method = RequestMethod.GET)
	public ModelAndView WeekBarChart4Hot() throws MongoException, IOException {
		ModelAndView mav = new ModelAndView("chart_show");
		//int startime = ChartUtils.getDayofWeek(new Date());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = format.getCalendar();
		cal.add(Calendar.DATE, -7);
		cal.getTime();
		QueryCount[] querycount = toQueryRecord(srmDAO
				.loadStatisticRecordBetween(cal.getTime(), new Date()));
		JFreeChart barchart = ChartUtils.createBarChart(chartName, querycount);
		String oPath = this.getClass().getResource("/").getPath();
		String savepath = oPath.replace("WEB-INF/classes/", "cloud/");
		File image = ChartUtils.saveChart(barchart, new Date(), "week", "bar",
				width, height, savepath);
		String fileName = image.getName();
		String path = "/cloud/" + fileName;
		mav.addObject("path", path);
		return mav;
	}

	@RequestMapping(value = "/weekpiechart4hot", method = RequestMethod.GET)
	public ModelAndView WeekPieChart4Hot() throws MongoException, IOException {
		ModelAndView mav = new ModelAndView("chart_show");
		//int startime = ChartUtils.getDayofWeek(new Date());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = format.getCalendar();
		cal.add(Calendar.DATE, -7);
		cal.getTime();
		QueryCount[] querycount = toQueryRecord(srmDAO
				.loadStatisticRecordBetween(cal.getTime(), new Date()));
		JFreeChart piechart = ChartUtils.createPieChart(chartName, querycount);
		String oPath = this.getClass().getResource("/").getPath();
		String savepath = oPath.replace("WEB-INF/classes/", "cloud/");
		File image = ChartUtils.saveChart(piechart, new Date(), "week", "pie",
				width, height, savepath);
		String fileName = image.getName();
		String path = "/cloud/" + fileName;
		mav.addObject("path", path);
		return mav;
	}

	@RequestMapping(value = "/monthbarchart4hot", method = RequestMethod.GET)
	public ModelAndView MonthBarChart4Hot() throws MongoException, IOException {
		ModelAndView mav = new ModelAndView("chart_show");
		//int startime = ChartUtils.getDayofMonth(new Date());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = format.getCalendar();
		cal.add(Calendar.DATE, -30);
		cal.getTime();
		QueryCount[] querycount = toQueryRecord(srmDAO
				.loadStatisticRecordBetween(cal.getTime(), new Date()));
		JFreeChart barchart = ChartUtils.createBarChart(chartName, querycount);
		String oPath = this.getClass().getResource("/").getPath();
		String savepath = oPath.replace("WEB-INF/classes/", "cloud/");
		File image = ChartUtils.saveChart(barchart, new Date(), "month", "bar",
				width, height, savepath);
		String fileName = image.getName();
		String path = "/cloud/" + fileName;
		mav.addObject("path", path);
		return mav;
	}

	@RequestMapping(value = "/monthpiechart4hot", method = RequestMethod.GET)
	public ModelAndView MonthPieChart4Hot() throws MongoException, IOException {
		ModelAndView mav = new ModelAndView("chart_show");
		//int startime = ChartUtils.getDayofMonth(new Date());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = format.getCalendar();
		cal.add(Calendar.DATE, -30);
		cal.getTime();
		QueryCount[] querycount = toQueryRecord(srmDAO
				.loadStatisticRecordBetween(cal.getTime(), new Date()));
		JFreeChart piechart = ChartUtils.createPieChart(chartName, querycount);
		String oPath = this.getClass().getResource("/").getPath();
		String savepath = oPath.replace("WEB-INF/classes/", "cloud/");
		File image = ChartUtils.saveChart(piechart, new Date(), "month", "pie",
				width, height, savepath);
		String fileName = image.getName();
		String path = "/cloud/" + fileName;
		mav.addObject("path", path);
		return mav;
	}

	public IQueryLogDao getQueryLogDao() {
		return queryLogDao;
	}

	public void setQueryLogDao(IQueryLogDao queryLogDao) {
		this.queryLogDao = queryLogDao;
	}

	public DLDELogger getLogger() {
		return logger;
	}

	public void setLogger(DLDELogger logger) {
		this.logger = logger;
	}
}
