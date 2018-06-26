package com.xbb.spring_jfinal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xbb.spring_jfinal.config.DevSetting;
import com.xbb.spring_jfinal.config.SessionConst;
import com.xbb.spring_jfinal.kit.AjaxJson;
import com.xbb.spring_jfinal.kit.StringKit;
import com.xbb.spring_jfinal.model.Menu;
import com.xbb.spring_jfinal.model.Role;
import com.xbb.spring_jfinal.model.User;
import com.xbb.spring_jfinal.security.event.UrlMapChangeEvent;
import com.xbb.spring_jfinal.service.MenuService;


@RestController
@RequestMapping("menu")
public class MenuController extends BaseController {

	@Autowired
	MenuService menuService;
	@Autowired
	DevSetting devSetting;
	@Autowired
	ApplicationContext ac;
	
	@RequestMapping("saveOrUpdate")
	public AjaxJson saveOrUpdate() {
		Menu menu = getModel(Menu.class, "");
		boolean flag = false;
		if (StringKit.isEmpty(menu.getId())) {
			flag = menuService.save(menu);
		} else {
			flag = menuService.update(menu);
		}
		AjaxJson rp = flag ? AjaxJson.success() : AjaxJson.failure();
		ac.publishEvent(new UrlMapChangeEvent());
		rp.setData(menu);
		return rp;
	}

	@RequestMapping("getAdminMenu")
	public List<Menu> getAdminMenu() {
		List<Menu> menus = getSessionAttr(SessionConst.SESSION_MENU);
		if (null == menus) {
			if (devSetting.getDevMode()) {
				menus = menuService.getAllMenuTree();
			} else {
				User sessionUser = getSessionAttr(SessionConst.USER);
				menus = menuService.getMenuByUserId(sessionUser.getId());
			}
			setSessionAttr(SessionConst.SESSION_MENU, menus);
		}
		return menus;
	}

	@RequestMapping("renderAsyncTree")
	public List<Menu> renderAsyncTree() {
		String parentId = getPara("id");
		List<Menu> menu;
		if (StringKit.isEmpty(parentId)) {
			menu = menuService.getFirstDegree();
		} else {
			menu = menuService.getByParentId(parentId);
		}
		initTreeValue(menu);
		return menu;
	}

	@RequestMapping("delete")
	public boolean delete() {
		Menu menu = getModel(Menu.class);
		ac.publishEvent(new UrlMapChangeEvent());
		return menuService.delete(menu);
	}

	private void initTreeValue(List<Menu> records) {
		for (Menu record : records) {
			record.put("state", menuService.hasChild(record) ? "closed" : "open");
		}
	}

	@RequestMapping("renderRoleMenuTree")
	public List<Menu> renderRoleMenuTree() {
		Role role = getModel(Role.class);
		List<Menu> records = menuService.renderRoleMenuTree(role.getStr("id"), null);
		return records;
	}

	@RequestMapping("initRoleMenu")
	public AjaxJson initRoleMenu() {
		String[] menuIds = getParaValues("menuIds[]");
		String roleId = getPara("roleId");
		AjaxJson rp = menuService.initRoleMenu(roleId, menuIds) ? AjaxJson.success() : AjaxJson.failure();
		ac.publishEvent(new UrlMapChangeEvent());
		return rp;
	}

}
