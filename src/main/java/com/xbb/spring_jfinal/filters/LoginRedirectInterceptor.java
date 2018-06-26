package com.xbb.spring_jfinal.filters;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.xbb.spring_jfinal.config.SessionConst;
import com.xbb.spring_jfinal.model.User;



/*拦截器,作为登录验证*/
public class LoginRedirectInterceptor implements HandlerInterceptor{
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		User sessionUser = (User) request.getSession().getAttribute(SessionConst.USER);
		if (null == sessionUser){
			response.sendRedirect(request.getContextPath() + "/view/login");
			return false;
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
	}

}
