package com.xbb.spring_jfinal.service;

import com.jfinal.plugin.activerecord.Page;
import com.xbb.spring_jfinal.kit.SqlKit.PageInfo;
import com.xbb.spring_jfinal.model.User;

public interface UserService extends BaseService<User> {
	Page<User> getPage(PageInfo pageInfo,User user);
}
