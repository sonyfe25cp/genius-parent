package com.genius.action;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.bit.dlde.utils.DLDELogger;

@Controller
public class UserOathAction {

	private DLDELogger logger;
	
	
	@RequestMapping("/admin/welcome")
	public String welcome(){
		logger.info("welcome to this admin panel !");
		return "welcome";
	}
	@RequestMapping("/admin/menu")
	public String menu(){
		logger.info("show left menu~");
		return "admin_left";
	}
	@RequestMapping("/admin/admin_index")
	public String index(){
		logger.info("index page");
		return "admin_index";
	}
	@RequestMapping("/admin/logout")
	public String logout(HttpSession session){
		logger.info("admin logout");
		session.invalidate();
		return "index";
	}
	


	public DLDELogger getLogger() {
		return logger;
	}


	public void setLogger(DLDELogger logger) {
		this.logger = logger;
	}
}
