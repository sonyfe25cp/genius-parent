package com.genius.admin.action;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import bit.mirror.core.Coordinator;
import bit.mirror.data.Seed;

import com.genius.admin.crawler.helper.SeedForm;

import edu.bit.dlde.utils.DLDELogger;

@Controller
@RequestMapping("/admin/seed")
public class SeedAction {

	private DLDELogger logger;
	private Coordinator mirrorengineCoordinator;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView listSeed() {
		Iterable<Seed> itor = mirrorengineCoordinator.getSeeds();

		int count = 0;
		ArrayList<Seed> seeds = new ArrayList<Seed>();
		for (Seed seed : itor) {
			if (count < 10)
				seeds.add(seed);
			count++;
			
		}

		return new ModelAndView("seed-list").addObject("seeds", seeds)
				.addObject("pageNo", 1)
				.addObject("total", (count - 1) / 10 + 1);
	}

	@RequestMapping(value = "/list/{pageNo}", method = RequestMethod.GET)
	public ModelAndView listSeed(@PathVariable("pageNo") int pageNo) {
		Iterable<Seed> itor = mirrorengineCoordinator.getSeeds();

		int count = 0;
		ArrayList<Seed> seeds = new ArrayList<Seed>();
		for (Seed seed : itor) {
			if (count >= (pageNo - 1) * 10 && count < pageNo * 10)
				seeds.add(seed);
			count++;
		}
		return new ModelAndView("seed-list").addObject("seeds", seeds)
				.addObject("pageNo", pageNo)
				.addObject("total", (count - 1) / 10 + 1);
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView addSeed(SeedForm seedForm) {

		return new ModelAndView("seed-add").addObject("seed", null);
	}

	@RequestMapping(value = "/update", method = RequestMethod.GET)
	public ModelAndView updateSeed(SeedForm seedForm) {
		if(seedForm==null)
			return new ModelAndView("seed-add").addObject("seed", null);
		Seed seed = seedForm.getSeed();
		if (seed == null || seed.getName() == null || seed.getName().isEmpty()) {
			logger.error("seed name is null");
			return addSeed(seedForm);
		}
		seed.setName(seed.getName().trim());
		mirrorengineCoordinator.submitSeed(seed);

		return listSeed(1);
	}

	@RequestMapping(value = "/{name}", method = RequestMethod.GET)
	public ModelAndView show(@PathVariable("name") String name) {
		logger.info("begin to show " + name);
		Seed seed = mirrorengineCoordinator.getSeed(name);
		return new ModelAndView("seed-update").addObject("seed", seed);
	}

	@RequestMapping(value = "/delete/{name}", method = RequestMethod.GET)
	public String remove(@PathVariable("name") String name) {
		logger.info("begin to delete " + name);
		mirrorengineCoordinator.deleteSeed(name);
		return "redirect:/admin/seed/list";
	}

	@RequestMapping(value = "/set-enabled/{name}", method = RequestMethod.GET)
	public String seedsSetEnabled(@PathVariable("name") String name) {
		mirrorengineCoordinator.setSeedEnabled(name, true);
		return "redirect:/admin/seed/list";
	}

	@RequestMapping(value = "/set-disabled/{name}", method = RequestMethod.GET)
	public String seedsSetDisabled(@PathVariable("name") String name) {
		mirrorengineCoordinator.setSeedEnabled(name, false);
		return "redirect:/admin/seed/list";
	}

	public DLDELogger getLogger() {
		return logger;
	}

	public void setLogger(DLDELogger logger) {
		this.logger = logger;
	}

	public Coordinator getMirrorengineCoordinator() {
		return mirrorengineCoordinator;
	}

	public void setMirrorengineCoordinator(Coordinator mirrorengineCoordinator) {
		this.mirrorengineCoordinator = mirrorengineCoordinator;
	}
}
