package com.xbb.spring_jfinal.kit;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import org.springframework.util.ReflectionUtils;
import com.alibaba.fastjson.JSON;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Model;

public class BeanKit {

	public static<T> T modelToBean(Model<?> model, Class<T> clazz) {
		return (T) JSON.parseObject(JsonKit.toJson(model), clazz);
	}
	
	public static<T> void copyProperties(Model<?> model, Object obj, String... ignoreAttrs) {
		String[] attrs = model._getAttrNames();
		List<String> ignoreList = (ignoreAttrs != null ? Arrays.asList(ignoreAttrs) : null);
		for (String attr : attrs) {
			Field filed = ReflectionUtils.findField(obj.getClass(), attr);
			Method setMethod = ReflectionUtils.findMethod(obj.getClass(), "set" + StrKit.firstCharToUpperCase(attr), filed.getType());
			if (null != setMethod && (ignoreList == null || !ignoreList.contains(attr))){
				try {
					setMethod.invoke(obj, (Object)model.get(attr));
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
