#sql("getMenuByUserId")
	select menu.* 
	from 
	(	select distinct menu.id
		from user_role
		left join role_menu
		on role_menu.roleId=user_role.roleId
		left join menu
		on role_menu.menuId=menu.id
		where user_role.userId='%s') temMenu
	left join menu
	on temMenu.id=menu.id
	where 1=1
#end

