package com.genius.admin.action;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.genius.dao.GeniusDao;
import com.genius.dao.UserDao;
import com.genius.model.NewsReport;
import com.genius.recommender.dao.RecommenderDao;
import com.genius.recommender.model.XnewsCluster;
import com.genius.recommender.utils.UpdateModels;
import com.genius.utils.Page;

import edu.bit.dlde.utils.DLDELogger;

@Controller
@RequestMapping("/admin/xnews")
public class AdminXnewsAction {

	private DLDELogger logger;
	private GeniusDao dao;
	private UserDao ud;
	private RecommenderDao rd;
	
	// for admin below
	@RequestMapping(value="/show",method=RequestMethod.GET)
	public ModelAndView showXnews(Integer pageNo,Integer pageSize,ModelMap model){
		if(pageNo==null)
		{
			pageNo = 1;
		}
		if(pageSize == null)
		{
			pageSize = 15;
		}
		//System.out.println("reach here,"+username+","+password);
		Page page = new Page(pageNo,pageSize);
		
		ModelAndView mav = new ModelAndView("xnews-show");
		List<NewsReport> xs = dao.loadNewsReportsInRange(page);
		mav.addObject("list", xs)
		.addObject("totalP", page.getTotalP())
				.addObject("pageNo", pageNo)
				.addObject("pb", page.getShowPageNoBegin())
				.addObject("pe", page.getShowpageNoEnd())
				.addObject("ps",pageSize);
		return mav;
	}
	// for admin below
	@RequestMapping(value="/showone",method=RequestMethod.GET)
	public ModelAndView showXnews(@RequestParam("id") String id){
		//System.out.println("reach here,"+username+","+password);
		ModelAndView mav = new ModelAndView("xnews");
		mav.addObject("xnews", dao.loadNewsReportByID(id));
		return mav;
	}
	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	@ResponseBody
	public String removeXnews(@RequestParam("newsId") String newsId) {
		System.out.println("reach here,"+newsId);
		UpdateModels.removeANews(dao.loadNewsReportByID(newsId), rd, ud, dao);
		System.out.println("success:"+newsId);
		return "success";
	}
	
	// for admin below
	@RequestMapping(value="/addcluster",method=RequestMethod.POST)
	@ResponseBody
	public String addCluster(@RequestParam("id") String id,@RequestParam("threadhold") String threadhold){
		System.out.println("reach here,"+id);
//		return xid;
		NewsReport ccenter = dao.loadNewsReportByID(id);
		if(ccenter!= null)
		{
			XnewsCluster xc = new XnewsCluster();
			xc.setMinNumber(0);
			try
			{
				Double value = Double.parseDouble(threadhold);
				xc.setThreadhold(value);
				System.out.println("set threadhold to " +threadhold);
			}
			catch (Exception e) {
				// TODO: handle exception
			}
			xc.setCenterXnews(ccenter,dao);			
			if(xc.getClusterMembers()!= null)
			{
				rd.PutXnewsCluster(xc);
				return "success";
			}
		}
		return id;
	}

	public DLDELogger getLogger() {
		return logger;
	}

	public void setLogger(DLDELogger logger) {
		this.logger = logger;
	}
	public RecommenderDao getRd() {
		return rd;
	}
	public void setRd(RecommenderDao rd) {
		this.rd = rd;
	}
	public GeniusDao getDao() {
		return dao;
	}
	public void setDao(GeniusDao dao) {
		this.dao = dao;
	}
	public UserDao getUd() {
		return ud;
	}
	public void setUd(UserDao ud) {
		this.ud = ud;
	}
}
