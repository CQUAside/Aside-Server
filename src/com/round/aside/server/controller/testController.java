package com.round.aside.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/test")
public class testController {

    @RequestMapping("/hello")
    public ModelAndView hello(){   
    	ModelAndView mv = new ModelAndView();
        mv.addObject("message", "朱威");  
        mv.setViewName("hello");
        return mv;
        //return "hello";
    }
}

