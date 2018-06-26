package com.xbb.spring_jfinal.framework.validate;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Config;
import com.jfinal.plugin.activerecord.DbKit;
import com.jfinal.plugin.activerecord.tx.TxConfig;
import com.xbb.spring_jfinal.controller.BaseController;
import com.xbb.spring_jfinal.framework.SpringContext;
import com.xbb.spring_jfinal.kit.AjaxJson;


@Aspect
@Component
public class FormAspect {

	
	Logger log = Logger.getLogger(this.getClass());
	
	@Around("@annotation(com.xbb.spring_jfinal.framework.validate.JsonValidate)")
	@Order(1)
	public Object formValidate(ProceedingJoinPoint pjp) throws Throwable {
		Object controller = pjp.getTarget();
		if (controller instanceof BaseController){
			Method method = ((MethodSignature)pjp.getSignature()).getMethod();
			JsonValidate jsonValidate = method.getAnnotation(JsonValidate.class);
			Class<? extends Validator>[] validators = jsonValidate.value();
			for (Class<? extends Validator> validatorClass : validators){
				Validator validator = SpringContext.getBean(validatorClass);
				AjaxJson bp = validator.check((BaseController)controller);
				if (null != bp){
					if (AjaxJson.class.equals(method.getReturnType())){
						return bp;
					}
					if (method.isAnnotationPresent(ResponseBody.class) && String.class.equals(method.getReturnType())){
						return JsonKit.toJson(bp);
					}
					throw new RuntimeException("表单验证失败");
				}
			}
			return pjp.proceed();
		}
		return pjp.proceed();
	}
	
	@Around("execution(* com.hywa.agricultural.controller..*.*(..))")
	@Order(2)
	public Object handleException(ProceedingJoinPoint pjp){
		try {
			return pjp.proceed();
		} catch (Throwable e) {
			log.error(e.toString());
			e.printStackTrace();
			Method method = ((MethodSignature)pjp.getSignature()).getMethod();
			if (method.isAnnotationPresent(ResponseBody.class) || pjp.getTarget().getClass().isAnnotationPresent(RestController.class)) {
				if (String.class.equals(method.getReturnType())) {
					return JsonKit.toJson(AjaxJson.failure().setMsg(e.getMessage()));
				}
				if (AjaxJson.class.equals(method.getReturnType())) {
					return AjaxJson.failure().setMsg(e.getMessage());
				}
				return null;
			}
		}
		return null;
	}
	
	
	public static Config getConfigWithTxConfig(ProceedingJoinPoint pjp) {
		Method method = ((MethodSignature)pjp.getSignature()).getMethod();
		TxConfig txConfig = method.getAnnotation(TxConfig.class);
		if (txConfig == null)
			txConfig = pjp.getTarget().getClass().getAnnotation(TxConfig.class);
		
		if (txConfig != null) {
			Config config = DbKit.getConfig(txConfig.value());
			if (config == null)
				throw new RuntimeException("Config not found with TxConfig: " + txConfig.value());
			return config;
		}
		return null;
	}
	
	protected int getTransactionLevel(Config config) {
		return config.getTransactionLevel();
	}
	
	
}


