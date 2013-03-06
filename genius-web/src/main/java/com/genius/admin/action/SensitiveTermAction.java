package com.genius.admin.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.genius.dao.GeniusDao;
import com.genius.dao.ISensitiveTermDao;
import com.genius.model.Giftable;
import com.genius.model.SensitiveTerm;
import com.genius.utils.Page;

import edu.bit.dlde.utils.DLDELogger;

/**
 * 用来处理“敏感词”相关的控制的action。
 * 
 * @author lins
 * @date 2012-5-16
 **/
@Controller
@RequestMapping("/admin/sensitive")
public class SensitiveTermAction {
	private DLDELogger logger;
	// private IStatisticRecordDao srmDAO;
	private ISensitiveTermDao stmDAO;
	private GeniusDao dao;

	public ISensitiveTermDao getStmDAO() {
		return stmDAO;
	}

	public void setStmDAO(ISensitiveTermDao stmDAO) {
		this.stmDAO = stmDAO;
	}

	// public IStatisticRecordDao getSrmDAO() {
	// return srmDAO;
	// }
	//
	// public void setSrmDAO(IStatisticRecordDao srmDAO) {
	// this.srmDAO = srmDAO;
	// }

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

	/**
	 * 分页显示敏感词
	 * 
	 * @param pageNo
	 *            页编号
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView listAll(@RequestParam(value = "pageNo", required = false, defaultValue = "1") int pageNo) {
		pageNo = Math.max(1, pageNo);
		ModelAndView mav = new ModelAndView("sen-list");
		List<SensitiveTerm> sTerm = stmDAO.getSome((pageNo - 1) * 10,
				pageNo * 10);
		if (sTerm != null && sTerm.size() != 0)
			mav.addObject("sterm", sTerm);

		return mav.addObject("pageNo", pageNo).addObject("totalP",
				(stmDAO.getRecordCount() - 1) / 10 + 1);
	}

	/**
	 * 转到敏感词添加页面
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView add() {
		return new ModelAndView("sen-add");
	}

	/**
	 * 添加敏感词
	 * 
	 * @param sterm
	 *            对应输入框里面的一堆敏感词
	 * @param file
	 *            上传的文件
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(String sterm, @RequestParam MultipartFile[] file,
			HttpServletRequest request) {
		// int prevCount = stmDAO.getRecordCount();
		// 添加框里面的
		if (sterm != null && !sterm.trim().equals("")) {
			String[] str = sterm.trim().replaceAll("\n", " ").split(" ");
			for (String s : str) {
				if (s.trim().equals(""))
					continue;
				SensitiveTerm term = new SensitiveTerm();
				ArrayList al = new ArrayList();
				al.addAll(Arrays.asList(s.trim().split("\\|")));
				term.setTerm(al);
				stmDAO.add(term);
			}
		}
		// 添加文件里面的
		for (MultipartFile myfile : file) {
			if (myfile.isEmpty()) {
				logger.info("文件未上传");
			} else {
				logger.info("上传敏感词文件...");
				logger.info("文件长度: " + myfile.getSize());
				logger.info("文件类型: " + myfile.getContentType());
				logger.info("文件名称: " + myfile.getName());
				logger.info("文件原名: " + myfile.getOriginalFilename());
				try {
					BufferedReader br = new BufferedReader(
							new InputStreamReader(myfile.getInputStream()));
					do {
						String line = br.readLine();
						if (line == null)
							break;
						if (line.trim().equals(""))
							continue;
						for (String s : line.split(" ")) {
							SensitiveTerm term = new SensitiveTerm();
							ArrayList al = new ArrayList();
							al.addAll(Arrays.asList(s.trim().split("\\|")));
							term.setTerm(al);
							stmDAO.add(term);
						}
					} while (true);
				} catch (IllegalStateException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				// 如果用的是Tomcat服务器，则文件会上传到\\%TOMCAT_HOME%\\webapps\\YourWebProject\\WEB-INF\\upload\\文件夹中
				// String realPath =
				// request.getSession().getServletContext().getRealPath("/WEB-INF/upload");
				// //这里不必处理IO流关闭的问题，因为FileUtils.copyInputStreamToFile()方法内部会自动把用到的IO流关掉，我是看它的源码才知道的
				// try {
				// FileUtils.copyInputStreamToFile(myfile.getInputStream(), new
				// File(realPath, myfile.getOriginalFilename()));
				// } catch (IOException e) {
				// e.printStackTrace();
				// }
			}
		}

		// if(file!=null){
		// try {
		// BufferedReader br = new BufferedReader(new FileReader(file));
		// do{
		// String line = br.readLine();
		// if(line==null)
		// break;
		// if(line.trim().equals(""))
		// continue;
		// for(String s : line.split(" ")){
		// SensitiveTerm term = new SensitiveTerm();
		// term.setTerm(s);
		// stmDAO.save(term);
		// }
		// }while(true);
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }

		// 跳转
		// int pageNo = (prevCount - 1) / 10 + 1;
		return "redirect:/admin/sensitive/list";
	}

	/**
	 * 删除敏感词
	 * 
	 * @param checkbox
	 *            被选中的checkbox
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public String delete(
			String[] checkbox,
			@RequestParam(value = "pageNo", required = false, defaultValue = "1") int pageNo) {
//		System.out.println(pageNo);
		if (checkbox == null)
			return "redirect:/admin/sensitive/list?pageNo=" + pageNo;
		for (String t : checkbox) {
			System.out.println(t);
			stmDAO.disable(t.substring(1, t.length() - 1));
		}
		return "redirect:/admin/sensitive/list?pageNo=" + pageNo;
	}

	// @RequestMapping(value = "/list", method = RequestMethod.GET)
	// public ModelAndView display() {
	// StatisticRecord sRecord = srmDAO.loadLatestStatisticRecord();
	// if (sRecord == null)
	// return new ModelAndView("sen-list");
	// StatisticTerm[] terms = new StatisticTerm[Math.min(15, sRecord
	// .getStatisticTerms().size())];
	// for (int i = 0; i < terms.length; i++) {
	// terms[i] = sRecord.getStatisticTerms().get(i);
	// }
	// return new ModelAndView("sen-list").addObject("terms", terms);
	// }

	/**
	 * 列出对应某个term的某一页资源。具体注释可以参考HotTermAction的类似方法
	 * 
	 * @param sterm
	 *            路径参数-敏感词
	 * @param pageNo
	 *            请求参数-页面编号，默认为1
	 * @return hot-1-list页面
	 */
	@RequestMapping(value = "/list-one", method = RequestMethod.GET)
	public ModelAndView displayTerm(
			@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "pageNo", required = false, defaultValue = "1") int pageNo) {
		if (id == null)
			return listAll(1);
		SensitiveTerm sTerm = stmDAO.getById(id);
		if (sTerm == null)
			return listAll(1);

		List<Giftable> list = new ArrayList<Giftable>();
		ModelAndView mav = new ModelAndView("sen-1-list");

		HashMap<String, Long> map = (HashMap<String, Long>) sTerm
				.getUrl2count();
		String[] keys = new String[map.keySet().size()];
		Object[] tmp = map.keySet().toArray();
		for (int i = 0; i < keys.length; i++) {
			keys[i] = (String) tmp[i];
		}
		Long[] values = new Long[map.keySet().size()];
		tmp = map.values().toArray();
		for (int i = 0; i < keys.length; i++) {
			values[i] = (Long) tmp[i];
		}
		// 对value进行一个sb的排序
		for (int i = 0; i < keys.length; i++) {
			for (int j = i + 1; j < keys.length; j++) {
				if (((Long) values[i]) < ((Long) values[j])) {
					String tmpK = keys[i];
					Long tmpV = values[i];
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

		Page page = new Page(pageNo);
		page.setPageSize(15);
		page.setTotal(total);
		int totalP = (total % 15 == 0) ? (total / 15) : ((total / 15) + 1);
		mav.addObject("list", listReturn).addObject("sterm", sTerm.getTerm())
				.addObject("pageNo", pageNo)
				.addObject("total", Math.max(totalP, 1))
				.addObject("pb", page.getShowPageNoBegin())
				.addObject("pe", page.getShowpageNoEnd());
		return mav;
	}
}
