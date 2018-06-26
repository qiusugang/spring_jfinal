package com.xbb.spring_jfinal.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OutLoginController {
	
	@RequestMapping("/outLogin")
	public String outLogin(HttpServletRequest req){
		req.getSession().invalidate();
		return "session清除成功";
	}
}
