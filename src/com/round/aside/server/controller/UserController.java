package com.round.aside.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.round.aside.server.pojo.User;
import com.round.aside.server.service.UserService;

/**
 * 管理用户登录与注册
 * 
 * @author Administrator
 * 
 */
@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping("/login")
	public ModelAndView login(User u) {
		System.out.println("login:" + u.getUsername());
		ModelAndView mv = new ModelAndView();
		User user = userService.getUser(u.getUsername());
		if (user != null) {
			mv.addObject("message", u.getUsername());
			mv.setViewName("hello");
		} else {
			mv.addObject("message", u.getUsername());
			mv.setViewName("error");
		}
		return mv;
	}

	@RequestMapping(value = "/register", method = { RequestMethod.POST })
	public ModelAndView register(User u) {
		System.out.println("register:" + u.getUsername());
		boolean result = userService.register(u);
		ModelAndView mv = new ModelAndView();
		if (result) {
			mv.addObject("message", "注册成功"+u.getUsername());
			mv.setViewName("hello");
			return mv;
		}else {
			mv.addObject("message", "注册失败"+u.getUsername());
			mv.setViewName("error");
			return mv;
		}
		// return "hello";
	}

	@RequestMapping("/preRegister")
	public String preRegister() {
		System.out.println("preRegister");
		return "register";
		// return "hello";
	}

	@RequestMapping("/preLogin")
	public String preLogin() {
		System.out.println("preLogin");
		return "login";
		// return "hello";
	}
}
