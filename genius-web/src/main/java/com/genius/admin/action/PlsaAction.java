package com.genius.admin.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.genius.dao.PlsaDAOMongoImpl;

import edu.bit.dlde.analysis.plsa.DocType;
import edu.bit.dlde.utils.DLDELogger;
/**
 *	@author zhangchangmin
 **/
@Controller
@RequestMapping("/admin/plsa")
public class PlsaAction {

//	private DLDELogger logger;
	private PlsaDAOMongoImpl plsaDAO;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView plsaList(@RequestParam(value="pageNo",required=false,defaultValue="1") int pageNo) {
		ModelAndView mav = new ModelAndView("plsa-list");
		HashMap<String,Integer> types_all=plsaDAO.typeList();
		HashMap<String,Integer> types=plsaDAO.typeList((pageNo-1)*10, 10);
		int total=types_all.size();
		int totalP=(total%10==0)?(total/10):((total/10)+1);
		mav.addObject("types",types).addObject("totalP", totalP)
		   .addObject("pageNo", pageNo);
		return mav;
	}

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam(value="type") String type,
			@RequestParam(value="pageNo",required=false,defaultValue="1") int pageNo) {
		List<DocType> docs_all =  plsaDAO.loadDocsByType(type);
		List<DocType> docs=plsaDAO.loadDocsByType(type, (pageNo-1)*10, 10);
		int total=docs_all.size();
		int totalP=(total%10==0)?(total/10):((total/10)+1);
		return new ModelAndView("plsa-show").addObject("docs",docs)
				.addObject("type", type).addObject("totalP", totalP)
				.addObject("pageNo", pageNo);
	}
	@RequestMapping(value = "/classify", method = RequestMethod.GET)
	public String classify(){
		plsaDAO.classify();
		return "redirect:/admin/plsa/list";
	}
	
	public PlsaDAOMongoImpl getPlsaDAO() {
		return plsaDAO;
	}
	public void setPlsaDAO(PlsaDAOMongoImpl plsaDAO) {
		this.plsaDAO = plsaDAO;
	}

}
