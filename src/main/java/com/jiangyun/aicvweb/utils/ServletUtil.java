package com.jiangyun.aicvweb.utils;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 客户端工具类
 *
 * @author lituo
 */
public class ServletUtil {
    /**
     * 获取String参数
     */
    public static String getParameter(String name) {
        return getRequest().getParameter(name);
    }
    
    public static String getParameter(String name, String defaultValue) {
        String param = getParameter(name);
        return StringUtils.isBlank(param) ? defaultValue : param;
    }

    /**
     * 获取Integer参数
     */
    public static Integer getParameterToInt(String name) {
    	String param = getParameter(name);
        return Integer.valueOf(param);
    }
    
    public static Integer getParameterToInt(String name, int defaultValue) {
    	String param = getParameter(name);
        return StringUtils.isBlank(param) ? defaultValue : Integer.valueOf(param);
    }

    /**
     * 获取request
     */
    public static HttpServletRequest getRequest() {
        return getRequestAttributes().getRequest();
    }

    /**
     * 获取response
     */
    public static HttpServletResponse getResponse() {
        return getRequestAttributes().getResponse();
    }

    public static ServletRequestAttributes getRequestAttributes() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes) attributes;
    }

    public static String getHeader(String name) {
        return getRequest().getHeader(name);
    }
    
    public static String getIp() {
    	String ip = getRequest().getHeader("x-forwarded-for");
    	if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
    		ip = getRequest().getRemoteAddr();
    	}
    	
    	return ip;
    }

    /**
     * 将字符串渲染到客户端
     *
     * @param response 渲染对象
     * @param string   待渲染的字符串
     */
    public static void renderString(HttpServletResponse response, String string) {
        try {
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
