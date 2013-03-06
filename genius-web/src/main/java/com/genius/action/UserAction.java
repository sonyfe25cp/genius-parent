package com.genius.action;


import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.genius.dao.UserDao;
import com.genius.model.User;

import edu.bit.dlde.utils.DLDELogger;

@Controller
@RequestMapping("/user")
@SessionAttributes("CurrentUser") 
public class UserAction {

	private DLDELogger logger;
	private UserDao ud;
	@RequestMapping(value="/login",method=RequestMethod.GET)
	public ModelAndView login(@RequestParam(value="username",required=false) String uname){
		return new ModelAndView("index").addObject("username", uname);
		
	}
	
	@RequestMapping("/register")
	public String register(){
		return "user_register";
	}
	
	@RequestMapping(value="/RegisterVerify",method=RequestMethod.GET)
	@ResponseBody
	public String RegisterVerify(@RequestParam("username") String username,@RequestParam("password") String password ,
			ModelMap model){
		String status = "sucess";
		//System.out.println(username+":  "+password);
		//System.out.println(username.equals("123"));
		if(username=="123"||username.equals("123"))
			status="user exit";
//		else if(password!="123456"&&!password.equals("123456"))
//			status="error password";
//		if(ud.existUser(username, password))
//		{
//			status = "success";
//			model.put("CurrentUser", username);
//			System.out.println("put UserName :" + username);
//		}
//		else if(ud.existUser(username))
//		{
//			status = "error password";
//		}
//		else
//		{
//			status = "user not exist";
//		}
		return status;
	}
	
	@RequestMapping(value="/LoginVerify",method=RequestMethod.GET)
	@ResponseBody
	public String LoginVerify(@RequestParam("username") String username,@RequestParam("password") String password ,
			ModelMap model){
		String status = "flase";
//		System.out.println(username+":  "+password);
//		System.out.println(username.equals("shiyulong"));
//		if(username!="shiyulong"&&!username.equals("shiyulong"))
//			status="user not exit";
//		else if(password!="123456"&&!password.equals("123456"))
//			status="error password";
//		else{
//			model.put("CurrentUser", username);
//			System.out.println("put UserName :" + username);
//		}
			
		if(ud.existUser(username, password))
		{
			status = "success";
			model.put("CurrentUser", username);
			
			
		}
		else if(ud.existUser(username))
		{
			status = "error password";
			
		}
		else
		{
			status = "user not exist";
			
		}
//		else{
//			Cookie ck=new Cookie("username",username);
//			ck.setMaxAge(3600*24*7);
//			HttpServletResponse response;
//			Response.addCookie(ck); 
//		}
		return status;
	}
	
	
	@RequestMapping(value="/verify",method=RequestMethod.GET)
	@ResponseBody
	public String verifyUser(@RequestParam("username") String username,@RequestParam("password") String password ,
			ModelMap model){
		String status = "success";
		System.out.println(username+":  "+password);
		System.out.println(username.equals("shiyulong"));
		if(username!="shiyulong"&&!username.equals("shiyulong"))
			status="user not exit";
		else if(password!="12345678"&&!password.equals("12345678"))
			status="error password";
//		else
//			model.put("CurrentUser", username);
//		if(ud.existUser(username, password))
//		{
//			status = "success";
//			model.put("CurrentUser", username);
//			System.out.println("put UserName :" + username);
//		}
//		else if(ud.existUser(username))
//		{
//			status = "error password";
//		}
//		else
//		{
//			status = "user not exist";
//		}
		return status;
	}

	
	
	
	@RequestMapping(value="/add",method=RequestMethod.GET)
	@ResponseBody
	public String addUser(@RequestParam("username") String username,
			@RequestParam("password") String password,ModelMap model){
		//System.out.println("reach here,"+username+","+password);
		String status = "falied";
		if(ud.existUser(username))
		{
			status = "username exist";
		}
		else
		{
			//
			User nUser = new User();
			nUser.setUsername(username);
			nUser.setPassword(password);
			ud.save(nUser);
			status = "success";
			model.put("CurrentUser", username);
			System.out.println("put UserName :" + username);
		}
		return status;
	}
	@RequestMapping(value="/logout",method = RequestMethod.GET)
	@ResponseBody
	public String logout(ModelMap model){
		logger.info("admin logout");
		model.put("CurrentUser", "");
		System.out.println("remove UserName ");
		//ModelAndView mav=new ModelAndView("index");
		return "success";
	}
//	public ModelAndView logout(ModelMap model){
//		logger.info("admin logout");
//		model.remove("CurrentUser");
//		System.out.println("remove UserName ");
//		ModelAndView mav=new ModelAndView("index");
//		return mav;
//	}
	
//	// for admin below
//	@RequestMapping(value="/show",method=RequestMethod.GET)
//	public ModelAndView showUsers(int pageNo,int pageSize,ModelMap model){
//		UserDao ud = new UserDaoMongoImpl();
//		//System.out.println("reach here,"+username+","+password);
//		Page page = new Page(pageNo,pageSize);
//		
//		ModelAndView mav = new ModelAndView("user-show");
//		List <String> usernames = new ArrayList<String>();
//		List<User> us = ud.getOrderList(pageSize, (pageNo-1)*pageSize);
//		page.setTotal((int)ud.getSize());
//		for(User u:us)
//		{
//			usernames.add(u.getUsername());
//		}
//		mav.addObject("list", usernames).addObject("total", page.getTotal())
//				.addObject("pageNo", pageNo).addObject("pb", page.getShowPageNoBegin()).addObject("pe", page.getShowpageNoEnd());
//		ud.close();
//		return mav;
//	}
//	// for admin below
//	@RequestMapping(value="/showone",method=RequestMethod.GET)
//	public ModelAndView showUser(@RequestParam("username") String username){
//		UserDao ud = new UserDaoMongoImpl();
//		//System.out.println("reach here,"+username+","+password);
//		ModelAndView mav = new ModelAndView("user");
//		mav.addObject("userinf", ud.getUserById(username));
//		ud.close();
//		return mav;
//	}

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
