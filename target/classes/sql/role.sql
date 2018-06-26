#sql("authoritiesByUsernameQuery")
	select username,user_role.id role 
	from user 
	left join user_role
	on user_role.userId=user.id
	where user.username=?
#end

#sql("getUserRole")
	select role.* 
	from role 
	left join user_role
	on user_role.roleId=role.id
	where user_role.userId=?
#end


#sql("roleHashAuthority")
	select role.id 
	from role 
	left join role_menu
	on role_menu.roleId=role.id
	left join menu
	on role_menu.menuId=menu.id
	where menu.url=?
#end