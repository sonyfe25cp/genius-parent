package com.genius.admin.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * 懒得配置spring mvc，所以有了这个用来转向的的action
 * 它只是为了转向静态网页faq
 * @author lins
 * @date 2012-5-6
 **/
@Controller
@RequestMapping("/admin/faq")
public class FaqAction {
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView display() {
		return new ModelAndView("faq");
	}
}
