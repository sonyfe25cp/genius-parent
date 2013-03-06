package com.genius.admin.action;

import java.util.ArrayList;
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
import com.genius.model.NewsDistance;
import com.genius.model.NewsReport;
import com.genius.recommender.dao.RecommenderDao;
import com.genius.recommender.model.XnewsCluster;
import com.genius.recommender.utils.UpdateModels;
import com.genius.utils.Page;

import edu.bit.dlde.recommender.utils.clusters.ClusterCenterMethod;
import edu.bit.dlde.recommender.utils.clusters.ClusterCenterMethod.Distance;
import edu.bit.dlde.recommender.utils.clusters.ClusterCenterMethod.Item;
import edu.bit.dlde.utils.DLDELogger;

@Controller
@RequestMapping("/admin/xnewscluster")
public class XnewsClusterAction {

	private DLDELogger logger;
	private RecommenderDao rd;
	private GeniusDao dao;
	private UserDao ud;
	// for admin below
	@RequestMapping(value="/show",method=RequestMethod.GET)
	public ModelAndView showXnewsClusters(ModelMap model){
		//System.out.println("reach here,"+username+","+password);
		//System.out.println("reach here,show");
		ModelAndView mav = new ModelAndView("xnewscluster-show");
		List<XnewsCluster> xs = rd.GetAllXnewsClusters();
		mav.addObject("list", xs);
		return mav;
	}
	// for admin below 
	@RequestMapping(value="/showone",method=RequestMethod.GET)
	public ModelAndView showXnewsCluster(@RequestParam("clusterId")String clusterId){
		//System.out.println("reach here,"+clusterId);
		ModelAndView mav = new ModelAndView("xnewscluster");
		mav.addObject("xnewscluster", rd.GetXnewsClusterById(clusterId));
		return mav;
	}
	// for admin below
	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	@ResponseBody
	public String removeCluster(@RequestParam("clusterId") String clusterId) {
		// System.out.println("reach here,"+username+","+password);
		UpdateModels.removeAXnewsCluster(rd.GetXnewsClusterById(clusterId), rd, ud);
		return "success";
	}
	@RequestMapping(value = "/resetcluster", method = RequestMethod.POST)
	@ResponseBody
	public String ReSetClusters(@RequestParam("sNumber") String sNumber,@RequestParam("sThreadhold") String sThreadhold)
	{	
		System.out.println("reach here");
		Double threadhold = null;
		int Number = 10;
		try
		{
			threadhold = Double.parseDouble(sThreadhold);
			Number = Integer.parseInt(sNumber);
		}
		catch (Exception e) {
			// TODO: handle exception
			return "failed";
		}
		List<Item> sources = new ArrayList<Item>();
		List<NewsReport> allnews = dao.loadNewsReportsInRange(new Page(1, 500));
		System.out.println(allnews.size());
		for(NewsReport nr:allnews)
		{
			sources.add(nr);
		}
		ClusterCenterMethod ccm = new ClusterCenterMethod();
		Distance dis = new NewsDistance();
		List<Item> result = ccm.getKMedoidsCenter(sources, dis, Number);
		System.out.println(result.size());
		if(result!=null)
		{
			//clean olds
			List<XnewsCluster> xcs = rd.GetAllXnewsClusters();
			for(XnewsCluster xc:xcs)
			{
				UpdateModels.removeAXnewsCluster(xc, rd, ud);
			}
			System.out.println("clean all");
			// add new 
			for(Item ccenter:result)
			{
				
				if(ccenter!= null)
				{
					System.out.println(((NewsReport)ccenter).getTitle());
					XnewsCluster xc = new XnewsCluster();
					xc.setMinNumber(0);

					xc.setThreadhold(threadhold);
					System.out.println("set threadhold to " +threadhold);

					xc.setCenterXnews((NewsReport)ccenter,dao);			
					if(xc.getClusterMembers()!= null)
					{
						rd.PutXnewsCluster(xc);
						System.out.println("add one");
					}
				}
			}
		}
		return "success";
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
	public UserDao getUd() {
		return ud;
	}
	public void setUd(UserDao ud) {
		this.ud = ud;
	}
	public GeniusDao getDao() {
		return dao;
	}
	public void setDao(GeniusDao dao) {
		this.dao = dao;
	}
}
