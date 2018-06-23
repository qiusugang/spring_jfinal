package com.xbb.spring_jfinal.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.xbb.spring_jfinal.config.DevSetting;
import com.xbb.spring_jfinal.service.MenuService;
import com.xbb.spring_jfinal.service.RoleService;





/**
 * spring security configuration
 * @author xubinbin
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	AuthenticationSuccessHandler successHandler;
	@Autowired
	AuthenticationFailureHandler failureHandler;
	@Autowired 
	UserDetailsService userDetailService;
	@Autowired
	DevSetting devSetting;
	@Autowired
	RoleService roleService;
	@Autowired
	MenuService menuService; 
	@Autowired
	BCryptPasswordEncoder bcryptPasswordEncoder;
	@Autowired
	MyAccessDecisionManager myAccessDecisionManager;
	@Autowired
	FilterInvocationSecurityMetadataSource filterInvocationSecurityMetadataSource;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailService).passwordEncoder(bcryptPasswordEncoder);
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		super.configure(web);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		 http.authorizeRequests().anyRequest().authenticated()
         .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
             public <O extends FilterSecurityInterceptor> O postProcess(O fsi) {
                 fsi.setSecurityMetadataSource(filterInvocationSecurityMetadataSource);
                 fsi.setAccessDecisionManager(myAccessDecisionManager);
                 return fsi;
             }
         });
		http.headers().frameOptions().disable();  
		http.csrf().disable().formLogin().loginPage("/view/login")
			.loginProcessingUrl("/login")
			.successHandler(successHandler)
			.failureHandler(failureHandler);
	}

	

}
