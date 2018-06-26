package com.xbb.spring_jfinal.security.event;

import org.springframework.context.ApplicationEvent;


/**
 * 权限管理接口地址url及对应的角色关系改变事件
 * @author xubinbin
 *
 */
public class UrlMapChangeEvent extends ApplicationEvent{

	private static final long serialVersionUID = 1L;

	public UrlMapChangeEvent() {
		super("");
	}

}
