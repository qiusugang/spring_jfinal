package com.xbb.spring_jfinal.kit;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import com.jfinal.kit.JsonKit;
import com.jfinal.render.RenderException;

public abstract class WebContextKit {
	public static void renderJson(HttpServletResponse response, Object obj){
		PrintWriter writer = null;
		try {
			response.setHeader("Pragma", "no-cache");	// HTTP/1.0 caches might not implement Cache-Control and might only implement Pragma: no-cache
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			
			response.setContentType("text/html; charset=UTF-8");
			writer = response.getWriter();
	        writer.write(JsonKit.toJson(obj));
	        writer.flush();
		} catch (IOException e) {
			throw new RenderException(e);
		}
		finally {
			if (writer != null) {
				writer.close();
			}
		}
	}
	
	public static void renderText(HttpServletResponse response, String text){
		PrintWriter writer = null;
		try {
			response.setHeader("Pragma", "no-cache");	// HTTP/1.0 caches might not implement Cache-Control and might only implement Pragma: no-cache
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			
			response.setContentType("text/html; charset=UTF-8");
			writer = response.getWriter();
	        writer.write(text);
	        writer.flush();
		} catch (IOException e) {
			throw new RenderException(e);
		}
		finally {
			if (writer != null) {
				writer.close();
			}
		}
	}
}
