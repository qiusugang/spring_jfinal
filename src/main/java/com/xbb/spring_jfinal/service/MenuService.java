package com.xbb.spring_jfinal.service;

import java.util.List;

import com.xbb.spring_jfinal.model.Menu;


public interface MenuService extends BaseService<Menu> {
	
	List<Menu> getMenuByParentId(String parentId);
	
	List<Menu> getAllMenuTree();

	List<Menu> getMenuByUserId(String userId);
	
	public List<Menu> getFirstDegree();
	
	public List<Menu> getByParentId(String parentId);
	
	public boolean hasChild(Menu menu);
	
	public List<Menu> renderRoleMenuTree(String roleId, String parentId);
	
	public boolean initRoleMenu(String roleId, String[] menuIds);
}
