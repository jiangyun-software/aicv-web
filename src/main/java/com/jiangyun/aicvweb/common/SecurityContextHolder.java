package com.jiangyun.aicvweb.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;

import com.jiangyun.aicvweb.entity.Member;

/**
 * 获取当前线程变量中的 用户id、用户名称、Token等信息
 *
 * @author wuzhongxian
 */
public class SecurityContextHolder {
    private static final ThreadLocal<Map<String, Object>> THREAD_LOCAL = new ThreadLocal<>();

    public static void set(String key, Object value) {
        Map<String, Object> map = getLocalMap();
        map.put(key, value == null ? StringUtils.EMPTY : value);
    }

    public static String get(String key) {
        Map<String, Object> map = getLocalMap();
        return map.getOrDefault(key, StringUtils.EMPTY).toString();
    }

    public static <T> T get(String key, Class<T> clazz) {
        Map<String, Object> map = getLocalMap();
        return clazz.cast(map.getOrDefault(key, null));
    }
    
    public static void remove(String key) {
        Map<String, Object> map = getLocalMap();
        map.remove(key);
    }

    public static Map<String, Object> getLocalMap() {
        Map<String, Object> map = THREAD_LOCAL.get();
        if (map == null) {
            map = new ConcurrentHashMap<String, Object>();
            THREAD_LOCAL.set(map);
        }
        return map;
    }

    public static void setLocalMap(Map<String, Object> threadLocalMap) {
        THREAD_LOCAL.set(threadLocalMap);
    }

    public static Long getMemberId() {
    	return getMemberInfo().getId();
    }

    public static String getPhonenumber() {
        return getMemberInfo().getPhonenumber();
    }
    
    public static void setMemberInfo(Member member) {
	    set("memberInfo", member);
    }
    
	public static Member getMemberInfo() {
		return get("memberInfo", Member.class);
	}

    public static void remove() {
        THREAD_LOCAL.remove();
    }
}
