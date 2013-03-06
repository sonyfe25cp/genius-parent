package com.genius.action;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.genius.model.Admin;

import edu.bit.dlde.utils.DLDELogger;

@Controller
public class CommonAction {
	private DLDELogger logger;

	@RequestMapping("/")
	public String index() {
		logger.info("mapping to index page");
		return "index";
	}

	@RequestMapping("/index")
	public String index_default() {
		logger.info("mapping to index page");
		return "index";
	}
	
	@RequestMapping("/loginasadmin")
	public String login(){
		return "admin_login";
	}

	@RequestMapping(value="/verifyadmin",method=RequestMethod.POST)
	public String verifyUser(@RequestParam("username") String username,
			@RequestParam("password") String password,HttpSession session){
		if(username.equals("admin")&&password.equals("admin")){
			Admin admin=new Admin();
			admin.setUsername(username);
			session.setAttribute("admin", admin);
			logger.info("success");
			return "redirect:/admin/admin_index";
		}else{
			logger.info("error");
			return "redirect:/loginasadmin";
		}
	}
	
	public DLDELogger getLogger() {
		return logger;
	}

	public void setLogger(DLDELogger logger) {
		this.logger = logger;
	}

}
