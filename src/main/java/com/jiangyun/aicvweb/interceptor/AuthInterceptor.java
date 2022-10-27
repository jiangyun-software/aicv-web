package com.jiangyun.aicvweb.interceptor;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.jiangyun.aicvweb.annotation.IgnoreAuth;
import com.jiangyun.aicvweb.common.Result;
import com.jiangyun.aicvweb.entity.LoginMember;
import com.jiangyun.aicvweb.utils.RedisUtil;
import com.jiangyun.aicvweb.utils.ServletUtil;

/**
 * 认证拦截器
 * @author wing4
 *
 */
@Component
@Order(1)
public class AuthInterceptor implements HandlerInterceptor {
	
	@Autowired
	RedisUtil redisUtil;

	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}
		
		HandlerMethod handlerMethod = (HandlerMethod) handler;
        //1.获取目标类上的目标注解（可判断目标类是否存在该注解）
		IgnoreAuth annotationInClass = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), IgnoreAuth.class);
        //2.获取目标方法上的目标注解（可判断目标方法是否存在该注解）
		IgnoreAuth annotationInMethod = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), IgnoreAuth.class);
		// 免登录
		if (annotationInClass != null || annotationInMethod != null) {
			return true;
		}
		
		// 获取token
        String token = request.getHeader("authorization");
        if (StringUtils.isBlank(token)) {
        	token = request.getParameter("authorization");
        }
        // token不存在
        if (StringUtils.isBlank(token)) {
        	ServletUtil.renderString(response, new Result(401, null, "认证失败").toString());
        	return false;
        }
        
        // token无效
        LoginMember loginMember = redisUtil.get("login_token:" + token, LoginMember.class);
        if (loginMember == null) {
        	ServletUtil.renderString(response, new Result(401, null, "认证失败").toString());
            return false;
        }
        
        // 刷新token过期时间
        redisUtil.expireAsync("login_token:" + token, 2, TimeUnit.HOURS);
		
		return true;
    }
}
