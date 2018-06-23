package com.xbb.spring_jfinal.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.jfinal.plugin.activerecord.Db;
import com.xbb.spring_jfinal.kit.SqlKit;
import com.xbb.spring_jfinal.kit.StringKit;
import com.xbb.spring_jfinal.model.Menu;
import com.xbb.spring_jfinal.model.RoleMenu;
import com.xbb.spring_jfinal.service.MenuService;


@Service
public class MenuServiceImpl extends BaseServiceImpl<Menu> implements MenuService {

	@Override
	public List<Menu> getMenuByUserId(String userId) {
		List<Menu> menus = getMenuByUserIdAndParentId(userId, null);
		for (Menu menu : menus) {
			List<Menu> childMenus = getMenuByUserIdAndParentId(userId, menu.getId());
			menu.put("childMenus", childMenus);
		}
		return menus;
	}
	
	private List<Menu> getMenuByUserIdAndParentId(String userId, String parentId){
		String sql = super.getSql("menu.getMenuByUserId");
		List<Object> paras = new ArrayList<>();
		String newSql = String.format(sql, userId);
		StringBuffer querySql = new StringBuffer(newSql);
		if (!StringKit.isEmpty(parentId)){
			querySql.append(" and menu.parentId=? ");
			paras.add(parentId);
		}
		else{
			querySql.append("and (length(menu.parentId)=0 or menu.parentId is null ) ");
		}
		SqlKit.appendAscOrderInfo(querySql, "menu.position");
		return super.find(querySql.toString(), paras.toArray());
	}

	@Override
	public List<Menu> getFirstDegree() {
		String sql = " select * from menu where  (LENGTH(parentId)=0 or parentId is null ) order by position asc ";
		return super.find(sql);
	}

	@Override
	public List<Menu> getByParentId(String parentId) {
		String sql = " select * from menu where parentId = ? order by position asc ";
		return super.find(sql, parentId);
	}

	@Override
	public boolean hasChild(Menu menu) {
		String sql = " select count(id) count from menu where parentId = ? ";
		long childCount = Db.findFirst(sql, menu.getStr("id")).getLong("count");
		return childCount > 0;
	}
	
	@Override
	public List<Menu> renderRoleMenuTree(String roleId, String parentId) {
		List<Menu> resources = getRoleMenuByParentId(roleId, parentId);
		for (Menu resource : resources) {
			List<Menu> childs = renderRoleMenuTree(roleId, resource.getStr("id"));
			if (null != childs && !childs.isEmpty()) {
				resource.put("checked", false);
			}
			resource.put("children", childs);
		}
		return resources;
	}
	
	private List<Menu> getRoleMenuByParentId(String roleId, String parentId) {
		List<Object> params = new ArrayList<>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select menu.text,menu.id,menu.iconCls,menu.parentId, ");
		sql.append(" case when (  select count(*) from role_menu  where roleId='${roleId}' and role_menu.menuId=menu.id ) >0 then true else false end checked ");
		sql.append(" from menu ");
		if (StringKit.isEmpty(parentId)) {
			sql.append(" where LENGTH(menu.parentId)=0 or menu.parentId is null order by menu.position asc ");
		} else {
			params.add(parentId);
			sql.append(" where menu.parentId =?  order by menu.position asc ");
		}
		return super.find(sql.toString().replace("${roleId}", roleId), params.toArray());
	}

	@Override
	public boolean initRoleMenu(String roleId, String[] menuIds) {
		deleteRoleMenu(roleId);
		if (null != menuIds) {
			for (String menuId : menuIds) {
				RoleMenu roleMenu = new RoleMenu();
				roleMenu.set("roleId", roleId);
				roleMenu.set("menuId", menuId);
				roleMenu.set("id", UUID.randomUUID().toString()).save();
			}
		}
		return true;
	}
	
	private boolean deleteRoleMenu(String roleId) {
		String sql = " delete from role_menu where roleId=? ";
		return Db.update(sql, roleId) > 0;
	}

	@Override
	public List<Menu> getAllMenuTree() {
		List<Menu> menus = getMenuByParentId(null);
		for (Menu menu : menus) {
			menu.put("childMenus", getMenuByParentId(menu.getId()));
		}
		return menus;
	}

	@Override
	public List<Menu> getMenuByParentId(String parentId) {
		List<Object> paras = new ArrayList<>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from menu where 1=1 ");
		if (StringKit.isEmpty(parentId)) {
			sql.append(" and menu.parentId is null or LENGTH(menu.parentId)=0 ");
		} else {
			paras.add(parentId);
			sql.append(" and menu.parentId =? ");
		}
		SqlKit.appendAscOrderInfo(sql, "menu.position");
		return super.find(sql.toString(), paras.toArray());
	}
	
}
