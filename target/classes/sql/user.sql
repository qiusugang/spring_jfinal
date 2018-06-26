#sql("usersByUsernameQuery")
	select * from user where username=?
#end

#sql("getPage")
	select * from user
#end
