package com.xbb.spring_jfinal.service.es.impl;


import static org.elasticsearch.index.query.QueryBuilders.termQuery;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Component;

import com.xbb.spring_jfinal.kit.SqlKit.PageInfo;
import com.xbb.spring_jfinal.kit.StringKit;
import com.xbb.spring_jfinal.model.es.EsUser;
import com.xbb.spring_jfinal.service.es.EsUserRepository;
import com.xbb.spring_jfinal.service.es.EsUserService;



@Component
public class EsUserServiceImpl implements EsUserService {

	@Autowired
	ElasticsearchTemplate elasticsearchTemplate; 
	@Autowired
	EsUserRepository esUserRepository;
	@Override
	public Page<EsUser> getPage(PageInfo page, String queryStr) {
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		if (!StringKit.isEmpty(queryStr)) {
			boolQueryBuilder.must(new QueryStringQueryBuilder(queryStr).field("phone").field("username").field("realname").defaultOperator(Operator.AND)) ;
		}
		NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder().withQuery(boolQueryBuilder);
		//searchQueryBuilder.withSort(new FieldSortBuilder("username").unmappedType("boolean").order(SortOrder.ASC));
		searchQueryBuilder.withPageable(PageRequest.of(page.getPage() - 1, page.getPageSize()));
		Page<EsUser> pageInfo = elasticsearchTemplate.queryForPage(searchQueryBuilder.build(), EsUser.class);
		return pageInfo;
	}
	

}
