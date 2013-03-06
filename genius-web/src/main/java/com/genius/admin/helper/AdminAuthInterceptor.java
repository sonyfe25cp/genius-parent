package com.genius.admin.helper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.genius.model.Admin;

import edu.bit.dlde.utils.DLDELogger;

public class AdminAuthInterceptor implements HandlerInterceptor{
	private DLDELogger logger=new DLDELogger();
	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object arg2) throws Exception {
		HttpSession session=request.getSession();
		Admin admin=(Admin)session.getAttribute("admin");
		if(admin==null){
			logger.info("no auth to login as admin");
			response.sendRedirect("/loginasadmin");
			return false;
		}else{
			return true;
		}
	}

}
