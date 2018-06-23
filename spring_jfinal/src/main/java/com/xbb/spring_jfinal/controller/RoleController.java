package com.xbb.spring_jfinal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.xbb.spring_jfinal.kit.AjaxJson;
import com.xbb.spring_jfinal.kit.SqlKit.PageInfo;
import com.xbb.spring_jfinal.kit.StringKit;
import com.xbb.spring_jfinal.model.Role;
import com.xbb.spring_jfinal.model.User;
import com.xbb.spring_jfinal.security.event.UrlMapChangeEvent;
import com.xbb.spring_jfinal.service.RoleService;

@RestController
@RequestMapping("role")
public class RoleController extends BaseController{
	@Autowired
	RedisTemplate<Object, Object> redisTemplate;
	@Autowired
	RoleService roleService;
	@Autowired
	ApplicationContext ac;
	
	@RequestMapping("page")
	public Record page(PageInfo pageInfo) {
		Role role = simpleModel(Role.class);
		Page<Role> page = roleService.getPage(pageInfo, role);
		return easyuiPageResult(page);
	}

	@RequestMapping("pageByUserId")
	public Record pageByUserId(PageInfo pageInfo){
		Page<Role>  page = roleService.pageByUserId(pageInfo, getPara("userId"));
		return easyuiPageResult(page);
	}
	
	@RequestMapping("saveOrUpdate")
	public AjaxJson saveOrUpdate(){
		Role role = simpleModel(Role.class);
		boolean flag = false;
		if (StringKit.isEmpty(role.getId())){
			flag = roleService.save(role);
		}
		else{
			flag =roleService.update(role);
		}
		ac.publishEvent(new UrlMapChangeEvent());
		AjaxJson aj = flag ? AjaxJson.success().setData(role) : AjaxJson.failure();
		return aj;
	}
	
	@RequestMapping("delete")
	public AjaxJson delete(){
		Role role = getModel(Role.class);
		boolean flag = roleService.delete(role);
		ac.publishEvent(new UrlMapChangeEvent());
		AjaxJson aj = flag ? AjaxJson.success().setData(role) : AjaxJson.failure();
		return aj;
	}

	@RequestMapping("renderUserRole")
	public List<Role> renderUserRole() {
		User user = getModel(User.class);
		List<Role> records = roleService.renderUserRole(user.getId());
		return records;
	}
}
