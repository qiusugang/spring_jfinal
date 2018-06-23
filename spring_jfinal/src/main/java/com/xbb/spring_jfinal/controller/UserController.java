package com.xbb.spring_jfinal.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.jfinal.plugin.activerecord.Record;
import com.xbb.spring_jfinal.config.SessionConst;
import com.xbb.spring_jfinal.controller.validate.user.UserSaveValidate;
import com.xbb.spring_jfinal.framework.validate.JsonValidate;
import com.xbb.spring_jfinal.kit.AjaxJson;
import com.xbb.spring_jfinal.kit.SqlKit.PageInfo;
import com.xbb.spring_jfinal.model.User;
import com.xbb.spring_jfinal.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController{
	@Autowired
	UserService userService;
	@Autowired
	BCryptPasswordEncoder bcryptPasswordEncoder;

	@Autowired
	RestTemplate restTemplate;

	@RequestMapping("/page")
	public Record getPage(PageInfo pageInfo) {
		User user=simpleModel(User.class);
		return easyuiPageResult(userService.getPage(pageInfo,user));
	}
	
	@RequestMapping("/save")
	@JsonValidate(UserSaveValidate.class)
	public AjaxJson save() {
		User user=simpleModel(User.class);
		user.setPassword(bcryptPasswordEncoder.encode(user.getPassword()));
		Boolean result=userService.save(user);
		return result?AjaxJson.success().setData(user):AjaxJson.failure();
	}
	
	@RequestMapping("/update")
	public AjaxJson update() {
		User user=simpleModel(User.class);
		user.remove("username", "password");
		Boolean result=userService.update(user);
		return result?AjaxJson.success().setData(user):AjaxJson.failure();
	}
	
	@RequestMapping("/updatePwd")
	public AjaxJson updatePwd(HttpServletRequest request, String oldPassword, String newPassword) {
		User user = getSessionAttr(SessionConst.USER);
		if (!bcryptPasswordEncoder.matches(oldPassword, user.getPassword())) {
			return AjaxJson.failure().setMsg("输入密码有误");
		} else {
			user.setPassword(bcryptPasswordEncoder.encode(newPassword));
			userService.update(user);
			setSessionAttr(SessionConst.USER, user);
			return AjaxJson.success();
		}
	}
  	
	@RequestMapping("/delete")
	public AjaxJson delete() {
		User user=simpleModel(User.class);
		Boolean result=userService.delete(user);
		return result?AjaxJson.success().setData(user):AjaxJson.failure();
	}

}
