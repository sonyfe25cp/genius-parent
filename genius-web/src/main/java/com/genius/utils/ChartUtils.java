package com.genius.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import com.genius.model.QueryCount;

/**
 * for jfreechart
 * 
 * @author ChenJie
 * 
 */
public class ChartUtils {
	private static Color[] colorset = { Color.red, Color.orange, Color.pink,
			Color.magenta, Color.yellow, Color.green, Color.cyan, Color.blue,
			Color.lightGray, Color.black };

	/*
	 * 绘制柱状图
	 */
	public static JFreeChart createBarChart(String chartName,
			QueryCount[] querycount) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (int i = 0; i < querycount.length; i++) {
			String query = querycount[i].getQuery();
			double count = querycount[i].getCount();
			dataset.addValue(count, query, query);
		}
		JFreeChart barchart = ChartFactory.createBarChart(chartName, "词项",
				"计数", dataset, PlotOrientation.VERTICAL, true, true, false);
		barchart.setBackgroundPaint(Color.orange);
		TextTitle title = new TextTitle(chartName, new Font("黑体", Font.BOLD, 16));
		barchart.setTitle(title);
		CategoryPlot barplot = barchart.getCategoryPlot();
		barplot.getDomainAxis().setLabelFont(new Font("宋体", Font.BOLD, 14));
		barplot.getDomainAxis().setTickLabelFont(new Font("宋体",Font.BOLD,12));
		barplot.setBackgroundPaint(Color.white);
		barplot.setRangeGridlinePaint(Color.red);
		barplot.setDomainGridlinesVisible(true);
		barplot.getRangeAxis().setLabelFont(new Font("宋体", Font.BOLD, 14));
		barplot.getRangeAxis().setTickLabelFont(new Font("宋体",Font.BOLD,12));
	    barchart.getLegend().setItemFont(new Font("宋体", Font.BOLD, 12));

		NumberAxis numberaxis = (NumberAxis) barplot.getRangeAxis();
		numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		BarRenderer barrenderer = (BarRenderer) barplot.getRenderer();
		barrenderer.setDrawBarOutline(false);
		barrenderer.setItemMargin(0.2);
		GradientPaint[] paint = new GradientPaint[querycount.length];

		for (int i = 0; i < querycount.length; i++) {
			paint[i] = new GradientPaint(0.0F, 0.0F, colorset[i], 0.0F, 0.0F,
					colorset[i]); // 设定特定颜色
			barrenderer.setSeriesPaint(i, paint[i]); // 设定上面定义的颜色
		}
		return barchart;
	}

	/*
	 * 绘制饼状图
	 */
	public static JFreeChart createPieChart(String chartName,
			QueryCount[] querycount) {
		DefaultPieDataset dataset = new DefaultPieDataset();
		for (int i = 0; i < querycount.length; i++) {
			String query = querycount[i].getQuery();
			int count = querycount[i].getCount();
			dataset.setValue(query, count);
		}
		JFreeChart piechart = ChartFactory.createPieChart(chartName, dataset,
				true, true, false);
		piechart.setTitle(new TextTitle(chartName, new Font("黑体", Font.BOLD, 16)));
		PiePlot pieplot = (PiePlot) piechart.getPlot();
		pieplot.setLabelFont(new Font("宋体", Font.PLAIN, 12));
		pieplot.setNoDataMessage("无数据显示");
		LegendTitle legend = piechart.getLegend();
		if(legend!=null){
			legend.setItemFont(new Font("宋体", Font.BOLD, 12));
		}
		
		return piechart;

	}

	/**
	 * 将绘制出的图形保存至文件
	 * 
	 * @Param path 保存的路径,不含文件名
	 */
	public static File saveChart(JFreeChart chart, Date date, String option,
			String type, int width, int height, String path) throws IOException {
		int date1 = dateToInt(date);
		StringBuilder builder = new StringBuilder();
		builder.append(path);
		builder.append(date1 + "-" + option + "-" + type);
		builder.append("-chart.png");
		String path1 = builder.toString();
		File file = new File(path1);
		ChartUtilities.saveChartAsPNG(file, chart, width, height);
		return file;
	}

	public static Date getToday(Date date){
		date.setHours(0);
		date.setMinutes(0);
		date.setSeconds(0);
		return date;
	}
	
	public static void main(String[] args){
		Date d=getDayofWeek(new Date());
		System.out.println(d);
	}
	
	public static Date getDayofWeek(Date date) {
		int firstday = date.getDate() - date.getDay() + 1; 
//		int date1 = dateToInt(date);
//		int dayofweek = date1 - day;
		date.setHours(0);
		date.setMinutes(0);
		date.setSeconds(0);
		date.setDate(firstday);
		return date;
	}

	public static Date getDayofMonth(Date date) {
//		GregorianCalendar calendar = new GregorianCalendar();
//		int day = calendar.get(Calendar.DAY_OF_MONTH);
//		int date1 = dateToInt(date);
//		int dayofmonth = date1 - day;
		date.setHours(0);
		date.setMinutes(0);
		date.setSeconds(0);
		date.setDate(1);
		return date;
	}

	/*
	 * 对于每一个QueryLog对应的date，取其年月日，最终将其转化成一个int值，方便过滤时的处理。
	 */
	public static int dateToInt(Date datestring) {
		SimpleDateFormat dateformat = new SimpleDateFormat(
				"yyyy.MM.dd.HH.mm.ss");
		String string = dateformat.format(datestring);
		String[] date = string.split("\\.");
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i <= 2; i++) {
			String temp = date[i];
			builder.append(temp);
		}
		String newdate = builder.toString();
		int num = Integer.parseInt(newdate);
		return num;
	}
}
