package com.genius.admin.action;

import java.io.IOException;
import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.genius.dao.QueryLogDaoImpl;
import com.genius.utils.ChartUtils;
import com.genius.utils.PDFUtils;

/**
 * @author C.M
 * 
 * @date 2012-5-12 下午9:07:23
 */

@Controller
@RequestMapping("/admin/pdf")
public class PDFAction {
	
	private  QueryLogDaoImpl queryLogDao;
	
//	private String fontpath = "msyh.ttf";// font用系统默认的字体，宋体之类的就行
	@RequestMapping(value = "/getpdf", method = RequestMethod.GET)
	public ModelAndView getPDF() {
		ModelAndView mav = new ModelAndView("pdf_create");
		return mav;
	}

	@RequestMapping(value = "/optionpdf", method = RequestMethod.GET)
	public ModelAndView getPDFOptions(@RequestParam("option") String option,
			@RequestParam("title")String title, @RequestParam("head") String head,
			@RequestParam("email")String email, @RequestParam("address")String address, 
			@RequestParam("telephone") String telephone) throws IOException {
		if (option.equals("0")) {
			return dayPDF(title, head, email, address, telephone);
		} else if (option.equals("1")) {
			return weekPDF(title, head, email, address, telephone);
		} else if (option.equals("2")) {
			return monthPDF(title, head, email, address, telephone);
		} else {
			return getPDF();
		}
	}

	@RequestMapping(value = "/getcalendarpdf", method = RequestMethod.GET)
	public ModelAndView getCalendarPDF(){
		ModelAndView mav = new ModelAndView("pdf_calendar");
		return mav;
	}
	@RequestMapping(value = "/calendarpdf", method = RequestMethod.GET)
	public ModelAndView calendarPDF(@RequestParam("currentime")String currentime, 
			@RequestParam("startime")String startime, @RequestParam("title")String title, 
			@RequestParam("head") String head, @RequestParam("email")String email, 
			@RequestParam("address")String address, @RequestParam("telephone") String telephone) 
					throws IOException{
		System.out.println("currentime : " + currentime);
		String[] currentdateString = currentime.split("-");
		Date currentdate = new Date(Integer.parseInt(currentdateString[0])-1900, Integer.parseInt(currentdateString[1])-1, 
				Integer.parseInt(currentdateString[2]));
		String[] startimedateString = startime.split("-");
		Date startdate = new Date(Integer.parseInt(startimedateString[0])-1900, Integer.parseInt(startimedateString[1])-1, 
				Integer.parseInt(startimedateString[2]));
		System.out.println("currentimex : " + currentdate);
		System.out.println("startdate : " +startdate);
		ModelAndView mav = PDFUtils.pDFModelAndView("pdf_show", currentdate, startdate, head, email,
				title, address, telephone, queryLogDao);
		return mav;
	}
	
	@RequestMapping(value = "/daypdf", method = RequestMethod.GET)
	public ModelAndView dayPDF(@RequestParam("title")String title, @RequestParam("head") String head,
			@RequestParam("email")String email, @RequestParam("address")String address, 
			@RequestParam("telephone") String telephone)
			throws IOException {
		Date currentime = new Date();
		Date startime = ChartUtils.getToday(new Date());
		ModelAndView mav = PDFUtils.pDFModelAndView("pdf_show", currentime, startime,
				head, email, title, address, telephone, queryLogDao);
		return mav;
		
	}

	@RequestMapping(value = "/weekpdf", method = RequestMethod.GET)
	public ModelAndView weekPDF(@RequestParam("title")String title, @RequestParam("head") String head,
			@RequestParam("email")String email, @RequestParam("address")String address, 
			@RequestParam("telephone") String telephone)
			throws IOException {
		Date currentime = new Date();
		Date startime = ChartUtils.getDayofWeek(new Date());
		ModelAndView mav = PDFUtils.pDFModelAndView("pdf_show", currentime, startime,
				head, email, title, address, telephone, queryLogDao);
		return mav;

	}

	@RequestMapping(value = "/monthpdf", method = RequestMethod.GET)
	public ModelAndView monthPDF(@RequestParam("title")String title, @RequestParam("head") String head,
			@RequestParam("email")String email, @RequestParam("address")String address, 
			@RequestParam("telephone") String telephone)
			throws IOException {
		Date currentime = new Date();
		Date startime = ChartUtils.getDayofMonth(new Date());
		ModelAndView mav = PDFUtils.pDFModelAndView("pdf_show", currentime, startime,
				head, email, title, address, telephone, queryLogDao);
		return mav;

	}

	public QueryLogDaoImpl getQueryLogDAO() {
		return  queryLogDao;
	}
	public void setQueryLogDao(QueryLogDaoImpl queryLogDao) {
		this.queryLogDao = queryLogDao;
	}
}
