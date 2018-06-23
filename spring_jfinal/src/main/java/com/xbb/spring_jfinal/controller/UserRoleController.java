package com.xbb.spring_jfinal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jfinal.plugin.activerecord.Db;
import com.xbb.spring_jfinal.kit.AjaxJson;
import com.xbb.spring_jfinal.model.UserRole;
import com.xbb.spring_jfinal.security.event.UrlMapChangeEvent;
import com.xbb.spring_jfinal.service.UserRoleService;

@RestController
@RequestMapping("/userRole")
public class UserRoleController extends BaseController{

	@Autowired
	UserRoleService userRoleService;

	@Autowired
	ApplicationContext ac;
	
	@RequestMapping("/save")
	public AjaxJson save(String userId) {
		String[] roleIds = getParaValues("roleIds[]");
		Db.update(" delete from user_role where userId=?", userId);
		if (null != roleIds) {
			for (String roleId : roleIds) {
				UserRole userRole = new UserRole();
				userRole.setUserId(userId);
				userRole.setRoleId(roleId);
				userRoleService.save(userRole);
			}
		}
		ac.publishEvent(new UrlMapChangeEvent());
		return AjaxJson.success();
	}

	@RequestMapping("/delete")
	public AjaxJson delete(UserRole userRole) {
		if(null==userRole.getUserId()){
			throw new RuntimeException("用户名不能为空");
		}
		List<UserRole> list=findById(userRole);
		for(UserRole userRoles:list){
			userRoleService.deleteById(userRoles.getId());
		}
		ac.publishEvent(new UrlMapChangeEvent());
		return AjaxJson.success().setData(userRole);
	}

	
	@RequestMapping("/findById")
	public List<UserRole> findById(UserRole userRole) {
		if(null==userRole.getUserId()){
			throw new RuntimeException("用户名不能为空");
		}
		return userRoleService.find("SELECT * FROM user_role WHERE userId=?", userRole.getUserId());
	}
}
