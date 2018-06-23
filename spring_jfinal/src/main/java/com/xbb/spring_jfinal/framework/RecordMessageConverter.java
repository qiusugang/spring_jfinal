package com.xbb.spring_jfinal.framework;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.ResolvableType;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.Feature;
import com.jfinal.json.JFinalJson;

public class RecordMessageConverter extends AbstractHttpMessageConverter<Object> {

	static JFinalJson jfinalJson = new JFinalJson();
	static {
		jfinalJson.setDatePattern("yyyy-MM-dd HH:mm:ss");
	}
	
	public RecordMessageConverter() {
		super(Charset.forName("UTF-8"), MediaType.ALL);
		List<MediaType> supportedMediaTypes = new ArrayList<>();
		supportedMediaTypes.add(MediaType.APPLICATION_JSON);
		supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
		supportedMediaTypes.add(MediaType.APPLICATION_ATOM_XML);
		supportedMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
		supportedMediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
		supportedMediaTypes.add(MediaType.APPLICATION_PDF);
		supportedMediaTypes.add(MediaType.APPLICATION_RSS_XML);
		supportedMediaTypes.add(MediaType.APPLICATION_XHTML_XML);
		supportedMediaTypes.add(MediaType.APPLICATION_XML);
		supportedMediaTypes.add(MediaType.IMAGE_GIF);
		supportedMediaTypes.add(MediaType.IMAGE_JPEG);
		supportedMediaTypes.add(MediaType.IMAGE_PNG);
		supportedMediaTypes.add(MediaType.TEXT_EVENT_STREAM);
		supportedMediaTypes.add(MediaType.TEXT_HTML);
		supportedMediaTypes.add(MediaType.TEXT_MARKDOWN);
		supportedMediaTypes.add(MediaType.TEXT_PLAIN);
		supportedMediaTypes.add(MediaType.TEXT_XML);
		super.setSupportedMediaTypes(supportedMediaTypes);
	}

	@Override
	protected boolean supports(Class<?> clazz) {
		return true;
	}

	@Override
	protected Object readInternal(Class<? extends Object> clazz, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {
		return readType(getType(clazz, null), inputMessage);
	}

	@Override
	protected void writeInternal(Object t, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		outputMessage.getBody().write(jfinalJson.toJson(t).getBytes("UTF-8"));
	}

	protected Type getType(Type type, Class<?> contextClass) {
		if (Spring4TypeResolvableHelper.isSupport()) {
			return Spring4TypeResolvableHelper.getType(type, contextClass);
		}
		return type;
	}

	private Object readType(Type type, HttpInputMessage inputMessage) throws IOException {

		try {
			InputStream in = inputMessage.getBody();
			return JSON.parseObject(in, Charset.forName("UTF-8"), type, Feature.AllowArbitraryCommas);
		} catch (JSONException ex) {
			throw new HttpMessageNotReadableException("JSON parse error: " + ex.getMessage(), ex);
		} catch (IOException ex) {
			throw new HttpMessageNotReadableException("I/O error while reading input message", ex);
		}
	}

	private static class Spring4TypeResolvableHelper {
		private static boolean hasClazzResolvableType;

		static {
			try {
				Class.forName("org.springframework.core.ResolvableType");
				hasClazzResolvableType = true;
			} catch (ClassNotFoundException e) {
				hasClazzResolvableType = false;
			}
		}

		private static boolean isSupport() {
			return hasClazzResolvableType;
		}

		@SuppressWarnings("rawtypes")
		private static Type getType(Type type, Class<?> contextClass) {
			if (contextClass != null) {
				ResolvableType resolvedType = ResolvableType.forType(type);
				if (type instanceof TypeVariable) {
					ResolvableType resolvedTypeVariable = resolveVariable((TypeVariable) type,
							ResolvableType.forClass(contextClass));
					if (resolvedTypeVariable != ResolvableType.NONE) {
						return resolvedTypeVariable.resolve();
					}
				} else if (type instanceof ParameterizedType && resolvedType.hasUnresolvableGenerics()) {
					ParameterizedType parameterizedType = (ParameterizedType) type;
					Class<?>[] generics = new Class[parameterizedType.getActualTypeArguments().length];
					Type[] typeArguments = parameterizedType.getActualTypeArguments();

					for (int i = 0; i < typeArguments.length; ++i) {
						Type typeArgument = typeArguments[i];
						if (typeArgument instanceof TypeVariable) {
							ResolvableType resolvedTypeArgument = resolveVariable((TypeVariable) typeArgument,
									ResolvableType.forClass(contextClass));
							if (resolvedTypeArgument != ResolvableType.NONE) {
								generics[i] = resolvedTypeArgument.resolve();
							} else {
								generics[i] = ResolvableType.forType(typeArgument).resolve();
							}
						} else {
							generics[i] = ResolvableType.forType(typeArgument).resolve();
						}
					}

					return ResolvableType.forClassWithGenerics(resolvedType.getRawClass(), generics).getType();
				}
			}

			return type;
		}

		private static ResolvableType resolveVariable(TypeVariable<?> typeVariable, ResolvableType contextType) {
			ResolvableType resolvedType;
			if (contextType.hasGenerics()) {
				resolvedType = ResolvableType.forType(typeVariable, contextType);
				if (resolvedType.resolve() != null) {
					return resolvedType;
				}
			}

			ResolvableType superType = contextType.getSuperType();
			if (superType != ResolvableType.NONE) {
				resolvedType = resolveVariable(typeVariable, superType);
				if (resolvedType.resolve() != null) {
					return resolvedType;
				}
			}
			for (ResolvableType ifc : contextType.getInterfaces()) {
				resolvedType = resolveVariable(typeVariable, ifc);
				if (resolvedType.resolve() != null) {
					return resolvedType;
				}
			}
			return ResolvableType.NONE;
		}
	}

}
