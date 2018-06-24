package com.xbb.spring_jfinal.service.es;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.xbb.spring_jfinal.kit.SqlKit.PageInfo;
import com.xbb.spring_jfinal.model.es.EsUser;

public interface EsUserService {
	public Page<EsUser> getPage(PageInfo page, String queryStr);
}
