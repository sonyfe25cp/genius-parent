package com.genius.action;
import java.io.IOException;
import java.util.Random;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.genius.dao.GeniusDao;
import com.genius.utils.QuerySender;
import com.genius.utils.QueryUtils;

import edu.bit.dlde.genius.model.QueryForm;
import edu.bit.dlde.genius.model.ResultsUnit;
import edu.bit.dlde.utils.DLDELogger;

@Controller
public class SearchAction{

	private DLDELogger logger;
	private GeniusDao dao;
	private ResultsUnit unit = null;
	
	private String qid;
	private String uid;
	
	

	
	public SearchAction() {
		
	}
	

	@RequestMapping("/")
	public ModelAndView index() {
		logger.info("mapping to index page");
		return new ModelAndView("index");
	}

	/**
	 * 方法都可以接受的参数(参数数量和顺序没有限制)：
	 * HttpServletRequest,HttpServletResponse,HttpSession(session必须是可用的)
	 * ,PrintWriter,Map,Model,@PathVariable(任意多个)， @RequestParam（任意多个）， @CookieValue
	 * （任意多个），@RequestHeader，Object（pojo对象） ,BindingResult等等
	 * 
	 * 返回值可以是：String(视图名)，void（用于直接response），ModelAndView，Map
	 * ，Model，任意其它任意类型的对象（默认放入model中，名称即类型的首字母改成小写），视图名默认是请求路径
	 * @throws IOException 
	 */
	@RequestMapping(value = "/g", method = RequestMethod.GET)
	public ModelAndView search(@RequestParam(value="q",required=true,defaultValue="") String query,
			@RequestParam(value="pageNo",required=false,defaultValue="1")int pageNo,
			@RequestParam(value="pageSize",required=false,defaultValue="10")int pageSize) throws IOException {

		logger.info("------ i begin to search ------");
//		try {
//			//for get method,you should do this
//			q = new String(q.getBytes("ISO-8859-1"),"utf-8");
//		} catch (UnsupportedEncodingException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		logger.info("q:" + query);
		query=QueryUtils.verifyQuery(query);
		if(query==null){
			ModelAndView mav = new ModelAndView("index");
			return mav;
		}
		QueryForm form = new QueryForm();
		form.setKeyWords(query);
		form.setPageNo(pageNo);
		form.setPageSize(pageSize);
		
	    unit = null;
		unit=QuerySender.sendQuery(form);
		//System.out.println("111111111111");
		//将查询词高亮显示
		if(unit.getResultsList()!=null&&unit.getResultsList().size()>0){
			for(int i=0;i<unit.getResultsList().size();i++){
				//System.out.println(i+"-------------------------------------");
				String title=unit.getResultsList().get(i).getTitle();
				unit.getResultsList().get(i).setTitle(title.replaceAll(query, "<font color='red'>"+query+"</font>"));
				String body=unit.getResultsList().get(i).getBody();
				unit.getResultsList().get(i).setBody(body.replaceAll(query, "<font color='red'>"+query+"</font>"));
			}
		}
        
		

		Random r = new Random();
		long rl = r.nextLong();// random num
		long ct = System.currentTimeMillis();
		 uid = "0";// userId
		 qid = ct + "-" + rl + "-" + uid;

		/*
		 * log the query and results
		 */
		
		 //创建线程，记录查询日志
		 QueryLogThread thread=new QueryLogThread(unit,query,qid,uid,dao);
		 thread.start();
		
		/*
		 * calculate pageNo 
		 */
		
		int totalNum=unit.getTotalNum();
		int totalPage=totalNum%pageSize==0?totalNum/pageSize:totalNum/pageSize+1;
		
		int pb=pageNo-5>0?pageNo-5:1;
		int en=pageNo+5>totalPage?totalPage:pageNo+5;
	
		
		/*
		 * shiyulong,related search function
		 */
		
		//根据查询词从索引中读出十个相关词作为相关搜索
		String relatedwords=null;
		if (query == null || query.trim().equals("")||unit.getResultsList()==null)
			relatedwords=null;
		else
		{
//			 Directory dir = FSDirectory.open(new File("D:\\data\\index\\RelatedWords"));
//			 IndexReader reader = IndexReader.open(dir);
//			 for (int i = 0; i < reader.numDocs(); i++){
//				String keywords=null;
//				keywords = reader.document(i).getField("keywords").stringValue();
//				relatedwords=reader.document(i).getField("relatedwords").stringValue();
//				if(keywords.equals(query)){
//					System.out.println(relatedwords);
//					break;
//				}
//			}
		}
		
		/*
		 * shiyulong
		 */

		ModelAndView mav = new ModelAndView("bingo");
		mav.addObject("query", query);
		mav.addObject("unit", unit);
		mav.addObject("qid", qid);
		mav.addObject("pageNo", pageNo);
		mav.addObject("pb", pb);
		mav.addObject("en", en);
		mav.addObject("tn",totalNum);
		mav.addObject("tp",	totalPage);
		
		/*
		 * syl
		 */
		//mav.addObject("relatedSearch", relatedwords);
		/*
		 * syl
		 */
		return mav;
	}
	
	public DLDELogger getLogger() {
		return logger;
	}

	public void setLogger(DLDELogger logger) {
		this.logger = logger;
	}

	public GeniusDao getDao() {
		return dao;
	}

	public void setDao(GeniusDao dao) {
		this.dao = dao;
	}
}
