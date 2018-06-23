package com.xbb.spring_jfinal.security;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import com.xbb.spring_jfinal.kit.StringKit;
import com.xbb.spring_jfinal.model.Menu;
import com.xbb.spring_jfinal.service.MenuService;
import com.xbb.spring_jfinal.service.RoleService;




/**
 * 接口资源和角色列表  映射关系类
 * @author xubinbin
 *
 */
@Component
public class MyFilterInvocationSecurityMetadataSource implements org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource {
	private final AntPathMatcher antPathMatcher = new AntPathMatcher();
	@Autowired
	RedisTemplate<Object, Object> redisTemplate;
	@Autowired
	MenuService menuService;
	@Autowired
	RoleService roleSerivce;
	private Map<String, String[]> urlRoleMap;
	public static final String URL_ROLE_MAP_BIGDATAGATEWAY= "URL_ROLE_MAP_BIGDATAGATEWAY";
	
	
	@SuppressWarnings("unchecked")
	private void initRoleMap() {
		urlRoleMap = (Map<String, String[]>) redisTemplate.opsForValue().get(URL_ROLE_MAP_BIGDATAGATEWAY);
		if (null == urlRoleMap) {
			urlRoleMap = new HashMap<>();
			List<Menu> menus = menuService.find(" select distinct url from menu ");
			for (Menu menu : menus) {
				if (!StringKit.isEmpty(menu.getUrl())){
					List<String> roleIds = roleSerivce.roleHashAuthority(menu.getUrl());
					urlRoleMap.put(menu.getUrl(), roleIds.toArray(new String[roleIds.size()]));
				}
			}
			redisTemplate.opsForValue().set(URL_ROLE_MAP_BIGDATAGATEWAY, urlRoleMap, 60, TimeUnit.MINUTES);
		}
	}
	
	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		FilterInvocation fi = (FilterInvocation) object;
		String url = fi.getRequestUrl();
		// String httpMethod = fi.getRequest().getMethod();
		initRoleMap();
		for (Map.Entry<String, String[]> entry : urlRoleMap.entrySet()) {
			if (antPathMatcher.match(entry.getKey(), url)) {
				return SecurityConfig.createList(entry.getValue());
			}
		}
		// 没有匹配到,则不拦截
		return null;
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return FilterInvocation.class.isAssignableFrom(clazz);
	}
	
	public void clearUrlMap() {
		redisTemplate.delete(URL_ROLE_MAP_BIGDATAGATEWAY);
	}
	
}
