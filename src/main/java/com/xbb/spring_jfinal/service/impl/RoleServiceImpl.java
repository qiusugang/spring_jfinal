package com.xbb.spring_jfinal.service.impl;


import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.xbb.spring_jfinal.kit.SqlKit;
import com.xbb.spring_jfinal.kit.SqlKit.PageInfo;
import com.xbb.spring_jfinal.model.Role;
import com.xbb.spring_jfinal.service.RoleService;

@Service
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService {

	@Override
	public List<Role> getUserRole(String userId) {
		String sql = super.getSql("role.getUserRole");
		return super.find(sql, userId);
	}

	@Override
	public List<String> roleHashAuthority(String resourceUrl) {
		String sql = getSql("role.roleHashAuthority");
		return Db.query(sql, resourceUrl);
	}
	
	@Override
	public Page<Role> getPage(PageInfo pageInfo, Role role) {
		List<Object> params = new ArrayList<>();
		StringBuffer sql = new StringBuffer(" select * from role where 1=1 ");
		SqlKit.appendEqualCondition(sql, role, params, "role.roleName", "role.roleCode");
		return super.paginate(pageInfo.getPage(), pageInfo.getPageSize(), sql.toString(), params.toArray());
	}

	@Override
	public List<Role> renderUserRole(String userId) {
		StringBuffer sql=new StringBuffer("SELECT role.roleName,role.roleCode FROM user_role  JOIN role " );
		sql.append(" ON user_role.roleId=role.id ");
		sql.append(" WHERE user_role.userId=? ");
		return super.find(sql.toString(), userId);
	}

	@Override
	public Page<Role> pageByUserId(PageInfo pageInfo, String userId) {
		List<Object> params = new ArrayList<>();
		String sql = " select role.*, (select count(*) from user_role where userId='%s' and roleId=role.id) count ";
		StringBuffer sql1 = new StringBuffer(String.format(sql, userId));
		sql1.append(" from role ");
		sql1.append(" where 1=1 ");
		return super.paginate(pageInfo.getPage(), pageInfo.getPageSize(), sql1.toString(), params.toArray());
	}
	
}
