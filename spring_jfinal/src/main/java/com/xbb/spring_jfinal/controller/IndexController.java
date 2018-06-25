package com.xbb.spring_jfinal.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jfinal.plugin.activerecord.DbKit;
import com.xbb.spring_jfinal.config.SessionConst;
import com.xbb.spring_jfinal.model.User;

@Controller
public class IndexController extends BaseController{

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
//	@RequestMapping("generate")
//	@ResponseBody
//	public String generate() {
//		String srcPath = "/Users/xubinbin/Desktop/agriculture-bigdata/src/main/java/";
//		MyGenerator generator = new MyGenerator(DbKit.getConfig().getDataSource(), "com.xbb.spring_jfinal.model.base",
//				srcPath + "com/qianchi/bigdata/model/base", "com.xbb.spring_jfinal.model", srcPath + "com/qianchi/bigdata/model",
//				"com.xbb.spring_jfinal.dao", srcPath + "com/qianchi/bigdata/dao", "com.xbb.spring_jfinal.kit",
//				srcPath + "com/qianchi/bigdata/kit");
//		generator.setGenerateDaoInModel(true);
//		generator.setGenerateChainSetter(true);
//		generator.setGenerateDataDictionary(true);
//		generator.generate();
//		return "index";
//	}
	
	@RequestMapping("generate1")
	@ResponseBody
	public String generate1() {

		String srcPath = "/Users/xubinbin/git/spring_jfinal/spring_jfinal/src/main/java/";

		templateservice.MyGenerator generator = new templateservice.MyGenerator(DbKit.getConfig().getDataSource(), "com.xbb.spring_jfinal.model.base",
				srcPath + "com/xbb/spring_jfinal/model/base", "com.xbb.spring_jfinal.model", srcPath + "com/xbb/spring_jfinal/model",
				"com.xbb.spring_jfinal.service", srcPath + "com/xbb/spring_jfinal/service", "com.xbb.spring_jfinal.kit",
				srcPath + "com/xbb/spring_jfinal/kit", "com.xbb.spring_jfinal.controller", srcPath + "com/xbb/spring_jfinal/controller");
		generator.setGenerateDaoInModel(true);
		generator.setGenerateChainSetter(true);
		generator.setGenerateDataDictionary(true);
		generator.generate();
		return "index";
	}

	@RequestMapping(value="/view/{path}",method = RequestMethod.GET)  
	public String view(@PathVariable("path") String path) {
		return path;
	}
	
	@RequestMapping(value="/view/{path1}/{path2}",method = RequestMethod.GET)  
	public String view1(@PathVariable("path1") String path1, @PathVariable("path2") String path2) {
		return path1 + "/" + path2;
	}

	@RequestMapping("/userInfo")
	@ResponseBody
	public User index1(){
		System.out.println(getResponse().encodeRedirectURL("/userinfo"));
		System.out.println(getSession().getId());
		return getSessionAttr(SessionConst.USER);
	}
	
	@ResponseBody
	@RequestMapping("hello")
	public String hello() {
		return "Hello";
	}
	
	static Map<String, HttpSession> map ;
	
	@ResponseBody
	@RequestMapping("hello1")
	public String hello1() {
		getSession(true);
		setSessionAttr("user", "xubinbin");
		return "login";
	}
	
	@RequestMapping("/")
	public String index(){
		return "redirect:/view/login";
	}
}
