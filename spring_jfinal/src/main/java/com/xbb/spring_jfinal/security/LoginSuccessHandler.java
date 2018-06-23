package com.xbb.spring_jfinal.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.xbb.spring_jfinal.config.SessionConst;
import com.xbb.spring_jfinal.kit.AjaxJson;
import com.xbb.spring_jfinal.kit.WebContextKit;
import com.xbb.spring_jfinal.model.User;
import com.xbb.spring_jfinal.service.UserService;




/**
 * 登陆成功事件处理
 * @author xubinbin
 *
 */
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

	@Autowired
	UserService userService;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		String username = request.getParameter("username");
		User user = userService.findFirst("select * from user where username=?", username);
		request.getSession().setAttribute(SessionConst.SESSION_MENU, null);
		request.getSession().setAttribute(SessionConst.USER, user);
		WebContextKit.renderJson(response, AjaxJson.success().setData(request.getSession().getId()));
	}

}
