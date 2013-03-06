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

import com.genius.dao.UserDao;
import com.genius.model.User;
import com.genius.utils.Page;

import edu.bit.dlde.utils.DLDELogger;

@Controller
@RequestMapping("/admin/user")
public class AdminUserAction {

	private DLDELogger logger;
	private UserDao ud;

	// for admin below
	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView showUsers(Integer pageNo, Integer pageSize,
			ModelMap model) {
		if (pageNo == null) {
			pageNo = 1;
		}
		if (pageSize == null) {
			pageSize = 10;
		}
		// System.out.println("reach here,"+username+","+password);
		Page page = new Page(pageNo, pageSize);

		ModelAndView mav = new ModelAndView("user-show");
		List<String> usernames = new ArrayList<String>();
		List<User> us = ud.getOrderList(pageSize, (pageNo - 1) * pageSize);
		page.setTotal((int) ud.getSize());
		for (User u : us) {
			usernames.add(u.getUsername());
		}
		mav.addObject("list", usernames).addObject("totalP", page.getTotalP())
				.addObject("pageNo", pageNo)
				.addObject("pb", page.getShowPageNoBegin())
				.addObject("pe", page.getShowpageNoEnd());
		return mav;
	}

	// for admin below
	@RequestMapping(value = "/showone", method = RequestMethod.GET)
	public ModelAndView showUser(@RequestParam("username") String username) {
		// System.out.println("reach here,"+username+","+password);
		ModelAndView mav = new ModelAndView("user");
		mav.addObject("userinf", ud.getUserById(username));
		return mav;
	}
	// for admin below
	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	@ResponseBody
	public String removeUser(@RequestParam("username") String username) {
		// System.out.println("reach here,"+username+","+password);
		ud.removeUserById(username);
		return "success";
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
}
