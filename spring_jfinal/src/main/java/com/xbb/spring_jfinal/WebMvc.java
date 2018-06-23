package com.xbb.spring_jfinal;

import javax.servlet.Filter;

import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.HttpSessionIdResolver;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.xbb.spring_jfinal.filters.LoginJsonFilter;
import com.xbb.spring_jfinal.framework.MySessionIdResolver;
import com.xbb.spring_jfinal.framework.RecordMessageConverter;


@Configuration
@EnableRedisHttpSession
public class WebMvc{
	
	@Bean("pwdEncoder")
	public BCryptPasswordEncoder pwdEncoder(){
		return new BCryptPasswordEncoder();
	}
	
	public static void main(String args[]) {
		System.out.println(new BCryptPasswordEncoder().encode("1"));
	}

	@Bean
	public HttpSessionIdResolver httpSessionStrategy() {
	    return new MySessionIdResolver();
	}
	
	@Bean
    public FilterRegistrationBean<?> testFilterRegistration() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<Filter>();
        registration.setFilter(new LoginJsonFilter());
        registration.addUrlPatterns("/bigdata/*", "/menu/*");
        registration.setName("loginJsonFilter");
        registration.setOrder(2);
        return registration;
    }

	@Bean
	public HttpMessageConverters customConverters() {
		return new HttpMessageConverters(new RecordMessageConverter());
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**");
			}
		};
	}
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
}
