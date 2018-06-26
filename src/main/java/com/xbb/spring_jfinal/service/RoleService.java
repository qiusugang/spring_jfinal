package com.xbb.spring_jfinal.service;

import java.util.List;

import com.jfinal.plugin.activerecord.Page;
import com.xbb.spring_jfinal.kit.SqlKit.PageInfo;
import com.xbb.spring_jfinal.model.Role;

public interface RoleService extends BaseService<Role> {

	public List<Role> getUserRole(String userId);
	
	public Page<Role> pageByUserId(PageInfo pageInfo, String userId);
	
	public List<String> roleHashAuthority(String resourceUrl);
	
	public Page<Role> getPage(PageInfo pageInfo, Role user);
	
	List<Role> renderUserRole(String userId);
	
}
