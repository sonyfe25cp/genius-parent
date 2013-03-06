package com.genius.admin.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.genius.dao.GeniusDao;
import com.genius.dao.IDocFeatureVectorDAO;
import com.genius.dao.IIgnoredHotTermsDao;
import com.genius.dao.StatisticRecordMongoDaoImpl;
import com.genius.model.DocFeatureVector;
import com.genius.model.Giftable;
import com.genius.model.HotTerm;
import com.genius.model.HotTermStatisticRecord;
import com.genius.model.IgnoredHotTerms;

import edu.bit.dlde.utils.DLDELogger;

import com.genius.utils.Page;

/**
 * 用来处理“热度词”相关的控制的action。
 * 
 * @author lins
 * @date 2012-5-16
 **/
@Controller
@RequestMapping("/admin/hot")
public class HotTermAction {
	private DLDELogger logger;
	private StatisticRecordMongoDaoImpl srmDAO;
	private GeniusDao dao;
	private IIgnoredHotTermsDao ihtDAO;
	private IDocFeatureVectorDAO dfvDAO;

	public IIgnoredHotTermsDao getIhtDAO() {
		return ihtDAO;
	}

	public void setIhtDAO(IIgnoredHotTermsDao ihtDAO) {
		this.ihtDAO = ihtDAO;
	}

	public GeniusDao getDao() {
		return dao;
	}

	public void setDao(GeniusDao dao) {
		this.dao = dao;
	}

	public DLDELogger getLogger() {
		return logger;
	}

	public void setLogger(DLDELogger logger) {
		this.logger = logger;
	}

	public StatisticRecordMongoDaoImpl getSrmDAO() {
		return srmDAO;
	}

	public void setSrmDAO(StatisticRecordMongoDaoImpl srmDAO) {
		this.srmDAO = srmDAO;
	}

	public IDocFeatureVectorDAO getDfvDAO() {
		return dfvDAO;
	}

	public void setDfvDAO(IDocFeatureVectorDAO dfvDAO) {
		this.dfvDAO = dfvDAO;
	}

	/**
	 * 显示某一页面的所有热度词
	 * 
	 * @param pageNo
	 *            请求参数-页面编号，默认为1
	 * @return hot-list页面
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView listAll(
			@RequestParam(value = "pageNo", required = false, defaultValue = "1") int pageNo) {
		// 无脑地把近期所有的StatisticRecord读取出来。。。这里可以改进
		long date = new Date().getTime();
		HotTermStatisticRecord sRecord = srmDAO.loadLatestStatisticRecord();
		date = new Date().getTime() - date;
		logger.info(String.valueOf(date));
		HotTerm[] terms = null;
		// 假如什么都读不出来，那么速度返回hot-list
		if (sRecord == null)
			return new ModelAndView("hot-list").addObject("terms", terms);

		IgnoredHotTerms iht = ihtDAO.getIgnored();

		HotTerm[] AllTerms = new HotTerm[sRecord.getStatisticTerms().size()];
		int total = 0;
		for (int i = 0; i < sRecord.getStatisticTerms().size(); i++) {
			if (!iht.getTerms().contains(
					sRecord.getStatisticTerms().get(i).term)) {
				AllTerms[total] = sRecord.getStatisticTerms().get(i);
				total++;
			}
		}
		// 总共的分页数
		// 各种初始化
		Page page = new Page(pageNo);
		page.setPageSize(15);
		page.setTotal(total);
		int totalP = (total % 15 == 0) ? (total / 15) : ((total / 15) + 1);

		// 获取对应pageNo的15个StatisticRecord
		if (pageNo < totalP) {
			terms = new HotTerm[15];
			for (int i = 0; i < 15; i++) {
				System.arraycopy(AllTerms, (pageNo - 1) * 15, terms, 0, 15);

			}
		} else if (pageNo == totalP) {
			int num = total % 15;
			terms = new HotTerm[num];
			for (int i = 0; i < num; i++) {
				System.arraycopy(AllTerms, (pageNo - 1) * 15, terms, 0, num);
			}
		}
		/*
		 * 
		 * terms = new StatisticTerm[Math.min(15,
		 * sRecord.getStatisticTerms().size())];
		 * 
		 * 
		 * for (int i = 0; i < terms.length; i++) { terms[i] =
		 * sRecord.getStatisticTerms().get(i); }
		 */
		return new ModelAndView("hot-list").addObject("terms", terms)
				.addObject("pageNo", pageNo).addObject("totalP", totalP)
				.addObject("pb", page.getShowPageNoBegin())
				.addObject("pe", page.getShowpageNoEnd());
	}

	/**
	 * 列出对应某个term的某一页资源
	 * 
	 * @param term
	 *            路径参数-热度词
	 * @param pageNo
	 *            请求参数-页面编号，默认为1
	 * @return hot-1-list页面
	 */
	@RequestMapping(value = "/list-one/{term}", method = RequestMethod.GET)
	public ModelAndView displayTerm(
			@PathVariable("term") String term,
			@RequestParam(value = "pageNo", required = false, defaultValue = "1") int pageNo) {
		// 无脑地把近期所有的StatisticRecord读取出来。。。这里可以改进
		HotTermStatisticRecord sRecord = srmDAO.loadLatestStatisticRecord();

		// 假如什么都读不出来，那么速度返回hot-list
		if (sRecord == null)
			return listAll(1);
		// System.out.println(term);

		// 无脑初始化
		List<Giftable> list = new ArrayList<Giftable>();
		ModelAndView mav = new ModelAndView("hot-1-list");

		// 首先，根据term找到HotTerm。
		// 事实上另一个想法是在页面里面吋下HotTerm，也就是listAll()方法里面传过去一个HotTerm
		// 然后再将HotTerm传给本方法，那么就省了一次查询数据库。
		// 然后，读出url2count，排个序
		for (HotTerm t : sRecord.getStatisticTerms()) {
			if (t.term.equals(term)) {
				HashMap<String, Integer> map = (HashMap<String, Integer>) t.url2count;
				String[] keys = new String[map.keySet().size()];
				Object[] tmp = map.keySet().toArray();
				for (int i = 0; i < keys.length; i++) {
					keys[i] = (String) tmp[i];
				}
				Integer[] values = new Integer[map.keySet().size()];
				tmp = map.values().toArray();
				for (int i = 0; i < keys.length; i++) {
					values[i] = (Integer) tmp[i];
				}
				// 对value进行一个sb的排序
				for (int i = 0; i < keys.length; i++) {
					for (int j = i + 1; j < keys.length; j++) {
						if (((Integer) values[i]) < ((Integer) values[j])) {
							String tmpK = keys[i];
							Integer tmpV = values[i];
							keys[i] = keys[j];
							values[i] = values[j];
							keys[j] = tmpK;
							values[j] = tmpV;
						}
					}
				}
				int total = 0;
				for (String key : keys) {
					Giftable g = dao.loadGiftableByUrl(key);
					if (g != null)
						list.add(g);
					total++;
				}

				List<Giftable> listReturn = list.subList((pageNo - 1) * 15,
						Math.min(pageNo * 15, list.size()));

				// 初始话各种返回的东西
				Page page = new Page(pageNo);
				page.setPageSize(15);
				page.setTotal(total);
				int totalP = (total % 15 == 0) ? (total / 15)
						: ((total / 15) + 1);
				mav.addObject("list", listReturn).addObject("term", term)
						.addObject("pageNo", pageNo).addObject("total", totalP)
						.addObject("pb", page.getShowPageNoBegin())
						.addObject("pe", page.getShowpageNoEnd());
				return mav;
			}
		}
		// 假如连个p都没有，就回到listAll
		return listAll(1);
	}

	/**
	 * 屏蔽热度词
	 * 
	 * @param checkbox
	 *            被选中的checkbox
	 */
	@RequestMapping(value = "/ignore", method = RequestMethod.GET)
	public String ignoreAdd(String[] checkbox) {
		// System.out.println(checkbox);
		if (checkbox == null)
			return "redirect:/admin/hot/list";
		ihtDAO.addIgnored(checkbox);
		return "redirect:/admin/hot/list";
	}

	@RequestMapping(value = "/ignore_show", method = RequestMethod.GET)
	public ModelAndView ignoreShow(
			@RequestParam(value = "pageNo", required = false, defaultValue = "1") int pageNo) {
		ModelAndView mav = new ModelAndView("hot-ignored");
		IgnoredHotTerms iht = ihtDAO.getIgnored();

		if (iht.getTerms() == null) {
			Page page = new Page(pageNo);
			page.setPageSize(50);
			page.setTotal(0);
			return mav.addObject("pageNo", pageNo).addObject("totalP", 0)
					.addObject("pb", page.getShowPageNoBegin())
					.addObject("pe", page.getShowpageNoEnd());
		}

		int total = iht.getTerms().size();
		String[][] result = new String[10][5];
		Iterator<String> it = iht.getTerms().iterator();
		int i = 0, m = 0, n = 0;
		while (it.hasNext()) {
			String str = it.next();
			if (i >= (pageNo - 1) * 50 && i < (pageNo) * 50) {
				result[m][n] = str;
				n++;
				if (n == 5) {
					n = 0;
					m++;
				}
			}
			if (i >= (pageNo) * 50)
				break;
			i++;
		}

		// 初始话各种返回的东西
		Page page = new Page(pageNo);
		page.setPageSize(50);
		page.setTotal(total);
		int totalP = (total % 50 == 0) ? (total / 50) : ((total / 50) + 1);
		return mav.addObject("terms", result).addObject("pageNo", pageNo)
				.addObject("totalP", totalP)
				.addObject("pb", page.getShowPageNoBegin())
				.addObject("pe", page.getShowpageNoEnd());
	}

	@RequestMapping(value = "/ignore_delete", method = RequestMethod.GET)
	public String ignoreDelete(String[] checkbox) {
		if(checkbox==null||checkbox.length==0){
			return "redirect:/admin/hot/ignore_show";
		}
		for (String s : checkbox) {
			ihtDAO.removeIgnored(s.substring(1, s.length() - 1));
		}

		return "redirect:/admin/hot/ignore_show";
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView searchShow(
			@RequestParam("hotsearch") String hotsearch,
			@RequestParam(value = "pageNo", required = false, defaultValue = "1") int pageNo) {
		if (hotsearch == null || hotsearch.trim().equals(""))
			return listAll(1);

		// 无脑地把近期所有的StatisticRecord读取出来。。。这里可以改进
		HotTermStatisticRecord sRecord = srmDAO.loadLatestStatisticRecord();

		// 假如什么都读不出来，那么速度返回hot-list
		if (sRecord == null)
			return listAll(1);

		HotTerm[] terms = null;

		IgnoredHotTerms iht = ihtDAO.getIgnored();

		HotTerm[] AllTerms = new HotTerm[sRecord.getStatisticTerms().size()];
		int total = 0;
		for (int i = 0; i < sRecord.getStatisticTerms().size(); i++) {
			if (!iht.getTerms().contains(
					sRecord.getStatisticTerms().get(i).term)
					&& sRecord.getStatisticTerms().get(i).term
							.contains(hotsearch)) {
				AllTerms[total] = sRecord.getStatisticTerms().get(i);
				total++;
			}
		}
		// 总共的分页数
		// 各种初始化
		Page page = new Page(pageNo);
		page.setPageSize(15);
		page.setTotal(total);
		int totalP = (total % 15 == 0) ? (total / 15) : ((total / 15) + 1);

		// 获取对应pageNo的15个StatisticRecord
		if (pageNo < totalP) {
			terms = new HotTerm[15];
			for (int i = 0; i < 15; i++) {
				System.arraycopy(AllTerms, (pageNo - 1) * 15, terms, 0, 15);

			}
		} else if (pageNo == totalP) {
			int num = total % 15;
			terms = new HotTerm[num];
			for (int i = 0; i < num; i++) {
				System.arraycopy(AllTerms, (pageNo - 1) * 15, terms, 0, num);
			}
		}

		return new ModelAndView("hot-search-list").addObject("terms", terms)
				.addObject("pageNo", pageNo).addObject("totalP", totalP)
				.addObject("pb", page.getShowPageNoBegin())
				.addObject("pe", page.getShowpageNoEnd())
				.addObject("hotsearch", hotsearch);
	}

	@RequestMapping(value = "/cooccurrence", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject showCooccurence(@RequestParam("term") String term) {
		if (term == null || term.trim().equals(""))
			return null;
		term = term.trim();
		logger.info("look up co-occurrence terms of term '" + term + "'...");

		// 无脑地把近期所有的StatisticRecord读取出来。。。这里可以改进
		HotTermStatisticRecord sRecord = srmDAO.loadLatestStatisticRecord();
		ArrayList<HotTerm> statisticTerms = sRecord.getStatisticTerms();

		// 找到对应的HotTerm
		HotTerm hotTerm = null;
		for (int i = 0; i < statisticTerms.size(); i++) {
			if (statisticTerms.get(i).getTerm().equals(term)) {
				hotTerm = statisticTerms.get(i);
				break;
			}
		}
		if (hotTerm == null)
			return null;
		logger.info("i've find '" + term + "' in db...");

		// 找到出现改hotterm的所有网页
		DocFeatureVector[] dfvs = new DocFeatureVector[hotTerm.getUrl2count()
				.size()];
		double[] weight = new double[hotTerm.getUrl2count().size()];
		double ttl = hotTerm.count;
		Iterator<Entry<String, Integer>> it = hotTerm.getUrl2count().entrySet()
				.iterator();
		int i = 0;
		while (it.hasNext()) {
			Entry<String, Integer> e = it.next();
			String url = e.getKey();
			dfvs[i] = dfvDAO.getFVByUrl(url);
			weight[i] = 1 / ttl * e.getValue();
			i++;
		}
		logger.info("i've find the related urls of '" + term + "'...");

		// 对所有的网页进行统计加权共现的词
		String[] coTerms = new String[400];
		double[] value = new double[400];
		int bound = 0;
		for (i = 0; i < dfvs.length; i++) {
			String[] termsInBody = dfvs[i].getTermsInBody();
			int[] occurrence = dfvs[i].getOccurenceInBody();
			for (int j = 0; j < termsInBody.length; j++) {
				// 先查看是不是已统计这个词
				int k = 0;
				for (k = 0; k < bound; k++) {
					if (termsInBody[j].equals(coTerms[k]))
						break;
				}
				// 无脑冲掉最后一个统计
				k = Math.min(399, k);
				// if(k==399){
				// for(int m = 0; m <=399; m++){
				// for(int n = m+1; n < 399; n++){
				// if (value[m] < value[n]) {
				// // 交换m-1, k
				// String tmpA = coTerms[m];
				// double tmpB = value[m];
				// coTerms[m] = coTerms[n];
				// value[m] = value[n];
				// coTerms[n] = tmpA;
				// value[n] = tmpB;
				// }
				// }
				// }
				// }
				coTerms[k] = termsInBody[j];
				value[k] += weight[i] * occurrence[j];
				if (k >= bound)
					bound++;

				for (int m = k - 1, n = k; m >= 0; m--) {
					if (value[m] < value[n]) {
						// 交换m-1, k
						String tmpA = coTerms[m];
						double tmpB = value[m];
						coTerms[m] = coTerms[n];
						value[m] = value[n];
						coTerms[n] = tmpA;
						value[n] = tmpB;
						n = m;
					}
				}
			}
		}
//		for (int asd = 0; asd < bound; asd++) {
//			System.out.println(coTerms[asd] + ":" + value[asd]);
//		}
		logger.info("i've finished the statistic process...");

		IgnoredHotTerms iht = ihtDAO.getIgnored();
		JSONObject object = new JSONObject();
		int jsonId = 0;
		String related = "";
		int k = 0;
		for (String coTerm : coTerms) {
			if (!iht.getTerms().contains(coTerm) && !coTerm.equals(term)) {
				// 找到对应的HotTerm
				HotTerm tmp = null;
				for (int j = 0; j < statisticTerms.size(); j++) {
					if (statisticTerms.get(j).getTerm().equals(coTerm)) {
						tmp = statisticTerms.get(j);
						break;
					}
				}
				if (tmp != null) {
					object.put("term" + jsonId, tmp.getTerm());
					object.put("relation" + jsonId, value[k]);
					jsonId++;
					related += (tmp.term + " " + value[k] + "; ");

					if (jsonId == 5) {
						logger.info("top 5 most related terms: " + related);
						break;
					}
				}
			}
			k++;
		}
		logger.info("i'll return the top 5 most related terms...");

		return object;
	}
}
