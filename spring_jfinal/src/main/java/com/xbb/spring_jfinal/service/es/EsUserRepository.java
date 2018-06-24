package com.xbb.spring_jfinal.service.es;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.xbb.spring_jfinal.model.es.EsUser;

public interface EsUserRepository extends ElasticsearchRepository<EsUser, String>{

	
}
