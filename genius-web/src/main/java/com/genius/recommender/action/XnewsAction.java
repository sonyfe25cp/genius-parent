/**
 * 
 */
package com.genius.recommender.action;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.genius.dao.GeniusDao;
import com.genius.dao.UserDao;
import com.genius.model.NewsReport;
import com.genius.model.User;
import com.genius.recommender.dao.RecommenderDao;
import com.genius.recommender.model.XnewsCluster;
import com.genius.recommender.utils.UpdateModels;
import com.genius.utils.ContentFormat;
import com.genius.utils.Page;

import edu.bit.dlde.utils.DLDELogger;

/**
 * @author horizon
 * 
 */
@Controller
@RequestMapping("/xnews")
@SessionAttributes
public class XnewsAction {
	private DLDELogger logger;
//	private XnewsDao xd ;
	private GeniusDao dao;
	private UserDao ud;
	private RecommenderDao rd;

	/**
	 * 
	 */
	public XnewsAction() {
		// TODO Auto-generated constructor stub
	}
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView getXnewsList(HttpSession session,Integer pageNo,Integer pageSize) {
		if(pageNo==null)
		{
			pageNo = 1;
		}
		if(pageSize == null)
		{
			pageSize = 10;
		}
		Page page = new Page(pageNo,pageSize);
		// System.out.println("reach here,"+username+","+password);
		List<NewsReport> lXnews = null;
		String username=(String) session.getAttribute("CurrentUser");
		lXnews = dao.loadNewsReportsInRange(page);
		System.out.println(username);

		ModelAndView mav = new ModelAndView("xnewslist");
		mav.addObject("username", username)
		.addObject("xnewslist", ContentFormat.FormatListNews(lXnews))
		.addObject("totalP", page.getTotalP())
		.addObject("pageNo", pageNo)
		.addObject("pb", page.getShowPageNoBegin())
		.addObject("pe", page.getShowpageNoEnd());
		return mav;
	}
	@RequestMapping(value = "/recommendations", method = RequestMethod.GET)
	public ModelAndView getXnewsRecommendations(HttpSession session,Integer pageNo,Integer pageSize) {
		if(pageNo==null)
		{
			pageNo = 1;
		}
		if(pageSize == null)
		{
			pageSize = 10;
		}
		Page page = new Page(pageNo,pageSize);
		int total = 0;
		// System.out.println("reach here,"+username+","+password);
		List<NewsReport> lXnews = new ArrayList<NewsReport>();
		String username=(String) session.getAttribute("CurrentUser");
		User cUser=ud.getUserById(username);
		if (cUser != null) {
			username = cUser.getUsername();
			//update(cUser);
			total = (int)rd.GetSize(cUser);
			if(total != 0)
			{
				page.setTotal(total);
				lXnews = rd.GetTopScoreItems(pageSize,cUser,(pageNo-1)*pageSize);
			}
			// System.out.println("have a UserName :" + username);
		} 
		System.out.println(username);

		ModelAndView mav = new ModelAndView("xnewsrecommendations");
		mav.addObject("username", username)
		.addObject("recommendations", ContentFormat.FormatListNews(lXnews))
		.addObject("totalP", page.getTotalP())
		.addObject("pageNo", pageNo)
		.addObject("pb", page.getShowPageNoBegin())
		.addObject("pe", page.getShowpageNoEnd());
		return mav;
	}

	@RequestMapping(value = "/boxes", method = RequestMethod.GET)
	public ModelAndView getXnewsBoxes(HttpSession session) {
		// System.out.println("reach here,"+username+","+password);
		List<NewsReport> lXnews = null;
		String username=(String) session.getAttribute("CurrentUser");
		User cUser=ud.getUserById(username);
		List<XnewsCluster> all = rd.GetAllXnewsClusters();
		logger.info("all size: "+all.size());
		if (cUser != null) {
			for(XnewsCluster xc:all)
			{
				for(XnewsCluster uxc:cUser.getXnewsClusters())
				{
					if(xc.getClusterId().equals(uxc.getClusterId()))
					{
						xc.getCenterXnews().setSelected(true);
						break;
					}
				}
			}
		}
		lXnews = new ArrayList<NewsReport>();
		for(XnewsCluster xc:all)
		{
			lXnews.add(xc.getCenterXnews());
		}
		System.out.println(username);
		
		ModelAndView mav = new ModelAndView("xnewsboxes");
		mav.addObject("username", username);
		mav.addObject("xnewsboxes", ContentFormat.FormatBoxNews(lXnews));
		return mav;
	}

	@RequestMapping(value = "/addhistory", method = RequestMethod.POST)
	@ResponseBody
	public String addHistory(HttpSession session,
			@RequestParam("sourceId") String sourceId) {
		String username=(String) session.getAttribute("CurrentUser");
		User cUser=ud.getUserById(username);
		if (cUser != null && sourceId != null) {
			NewsReport xnews = dao.loadNewsReportByID(sourceId);
			cUser.AddAHistory(xnews);
			ud.save(cUser);
		}
		
		return "success";
	}
	@RequestMapping(value = "/addcollection", method = RequestMethod.POST)
	@ResponseBody
	public String addCollection(HttpSession session,
			@RequestParam("sourceId") String sourceId) {
		String username=(String) session.getAttribute("CurrentUser");
		User cUser=ud.getUserById(username);
		if (cUser != null && sourceId != null) {
			NewsReport xnews = dao.loadNewsReportByID(sourceId);
			cUser.AddACollection(xnews);
			UpdateModels.update(cUser,rd);
			ud.save(cUser);
		}
		
		return sourceId;
	}
	@RequestMapping(value = "/selectcluster", method = RequestMethod.POST)
	@ResponseBody
	public String selectCluster(HttpSession session,
			@RequestParam("sourceId") String sourceId) {
		String username=(String) session.getAttribute("CurrentUser");
		User cUser=ud.getUserById(username);
		if (cUser != null && sourceId != null) {
			XnewsCluster xc = rd.GetXnewsClusterById(sourceId);
	
			cUser.addXnewsCluster(xc);
			UpdateModels.update(cUser,rd);
			ud.save(cUser);
		}
		
		return sourceId;
	}
	@RequestMapping(value = "/cancelcluster", method = RequestMethod.POST)
	@ResponseBody
	public String cancelCluster(HttpSession session,
			@RequestParam("sourceId") String sourceId) {
		String username=(String) session.getAttribute("CurrentUser");
		User cUser=ud.getUserById(username);
		if (cUser != null && sourceId != null) {
			XnewsCluster xc = rd.GetXnewsClusterById(sourceId);
			cUser.removeXnewsCluster(xc);
			UpdateModels.update(cUser,rd);
			ud.save(cUser);
		}
		
		return sourceId;
	}


	public DLDELogger getLogger() {
		return logger;
	}

	public void setLogger(DLDELogger logger) {
		this.logger = logger;
	}
	public UserDao getUd() {
		return ud;
	}
	public void setUd(UserDao ud) {
		this.ud = ud;
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

}
