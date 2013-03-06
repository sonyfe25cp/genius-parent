package com.genius.admin.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import bit.mirror.core.Coordinator;
import bit.mirror.data.Seed;

import com.genius.dao.IExtractConfigurationDAO;
import com.genius.model.ExtractConfiguration;

import edu.bit.dlde.utils.DLDELogger;

/**
 * 用来处理“抽取”相关的控制的action
 * 
 * @author lins
 * @date 2012-5-16
 */
@Controller
@RequestMapping("/admin/extract")
public class ExtractAction {
	private DLDELogger logger;
	private IExtractConfigurationDAO ecmDAO;
	private Coordinator mirrorengineCoordinator;

	public Coordinator getMirrorengineCoordinator() {
		return mirrorengineCoordinator;
	}

	public void setMirrorengineCoordinator(Coordinator mirrorengineCoordinator) {
		this.mirrorengineCoordinator = mirrorengineCoordinator;
	}

	public IExtractConfigurationDAO getEcmDAO() {
		return ecmDAO;
	}

	public void setEcmDAO(IExtractConfigurationDAO ecmDAO) {
		this.ecmDAO = ecmDAO;
	}

	/**
	 * 用来显示抽取配置的前10个
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView listAll(
			@RequestParam(value = "pageNo", required = false, defaultValue = "1") int pageNo) {
		ArrayList<ExtractConfiguration> extCfgs = (ArrayList<ExtractConfiguration>) ecmDAO
				.loadExtCfgs((pageNo - 1) * 10, pageNo * 10);

		long count = ecmDAO.getCount();
		return new ModelAndView("extract-cfgs-list")
				.addObject("extract_cfgs", extCfgs).addObject("pageNo", pageNo)
				.addObject("total", (count - 1) / 10 + 1);
	}

	/**
	 * 仅仅用来转向extract-cfgs-add页面，具体添加在updateExtcfg(ExtractConfiguration extCfg)
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView add() {
		Iterable<Seed> itor = mirrorengineCoordinator.getSeeds();
		ArrayList<Seed> seeds = new ArrayList<Seed>();
		for (Seed seed : itor) {
			seeds.add(seed);
		}
		boolean noSeed = false;
		if (seeds.size() == 0)
			noSeed = true;
		return new ModelAndView("extract-cfgs-add")
				.addObject("extract_cfgs", null).addObject("seeds", seeds)
				.addObject("noSeed", noSeed);
	}

	/**
	 * 已经选定seed然后转向update表单页面，具体添加在updateExtcfg(ExtractConfiguration extCfg)
	 */
	@RequestMapping(value = "/add/{name}", method = RequestMethod.GET)
	public ModelAndView addBySeed(@PathVariable("name") String name) {
		Iterable<Seed> itor = mirrorengineCoordinator.getSeeds();
		ArrayList<Seed> seeds = new ArrayList<Seed>();
		for (Seed seed : itor) {
			seeds.add(seed);
		}
		boolean noSeed = false;
		if (seeds.size() == 0)
			noSeed = true;
		return new ModelAndView("extract-cfgs-add")
				.addObject("extract_cfgs", null).addObject("sname", name)
				.addObject("seeds", seeds).addObject("noSeed", noSeed);
	}

	/**
	 * 向数据库添加抽取配置，当前的验证仅仅需要名字非空
	 */
	@RequestMapping(value = "/update", method = RequestMethod.GET)
	public ModelAndView update(ExtractConfiguration extCfg) {
		if (extCfg.getName() == null || extCfg.getName().trim().equals(""))
			logger.error("Cfg name is null");
		// System.out.println("save cfg for"+extCfg.getSeed());

		Iterable<Seed> itor = mirrorengineCoordinator.getSeeds();
		ArrayList<Seed> seeds = new ArrayList<Seed>();
		for (Seed seed : itor) {
			seeds.add(seed);
		}
		boolean noSeed = false;

		String val = extCfg.validate();
		logger.info(val);
		if (val.length() == 0) {
			ecmDAO.saveExtCfg(extCfg);
			return listAll(1);
		} else {
			logger.info("validation denied!");
			return new ModelAndView("extract-cfgs-add")
					.addObject("extract_cfgs", extCfg)
					.addObject("error", "----" + val)
					.addObject("sname", extCfg.getSeed())
					.addObject("seeds", seeds).addObject("noSeed", noSeed);
		}
	}

	/**
	 * 读入以name为key的抽取配置，并显示
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ModelAndView showDetail(@PathVariable("id") String id) {
		logger.info("begin to show " + id);
		Iterable<Seed> itor = mirrorengineCoordinator.getSeeds();
		ArrayList<Seed> seeds = new ArrayList<Seed>();
		for (Seed seed : itor) {
			seeds.add(seed);
		}
		boolean noSeed = false;
		if (seeds.size() == 0)
			noSeed = true;
		System.out.println(seeds.size());
		ExtractConfiguration extCfg = ecmDAO.loadExtCfg(id);
		return new ModelAndView("extract-cfgs-add")
				.addObject("extract_cfgs", extCfg)
				.addObject("sname", extCfg.getSeed()).addObject("seeds", seeds)
				.addObject("noSeed", noSeed);
	}

	/**
	 * 读入以name为seed的抽取配置，并显示
	 */
	@RequestMapping(value = "/slist/{name}", method = RequestMethod.GET)
	public ModelAndView listBySeed(
			@PathVariable("name") String name,
			@RequestParam(value = "pageNo", required = false, defaultValue = "1") int pageNo) {
		logger.info("begin to show " + name);
		// System.out.println("update");
		List<ExtractConfiguration> list = ecmDAO.loadExtCfgBySeed(name);
		int total = (list.size() - 1) / 10 + 1;
		if (list != null) {
			list = list.subList((pageNo - 1) * 10,
					Math.min(pageNo * 10, list.size()));
		}
		return new ModelAndView("extract-cfgs-1-list")
				.addObject("extract_cfgs", list).addObject("sname", name)
				.addObject("pageNo", pageNo)
				.addObject("total", Math.max(1, total));
	}

	/**
	 * 删除以name为key的抽取配置
	 */
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public String remove(@PathVariable("id") String id) {
		logger.info("begin to delete " + id);
		ecmDAO.removeExtCfg(id);
		return "redirect:/admin/extract/list";
	}

	/**
	 * 修改以name为key的抽取配置的使用状态
	 */
	@RequestMapping(value = "/set-enabled/{id}", method = RequestMethod.GET)
	public String setEnabled(@PathVariable("id") String id) {
		ecmDAO.changeExtCfgStatus(id);
		return "redirect:/admin/extract/list";
	}

	/**
	 * 修改以name为key的抽取配置的使用状态
	 */
	@RequestMapping(value = "/set-disabled/{name}", method = RequestMethod.GET)
	public String setDisabled(@PathVariable("name") String name) {
		ecmDAO.changeExtCfgStatus(name);
		return "redirect:/admin/extract/list";
	}

	public DLDELogger getLogger() {
		return logger;
	}

	public void setLogger(DLDELogger logger) {
		this.logger = logger;
	}
}
