package com.jiangyun.aicvweb.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.jiangyun.aicvweb.interceptor.AuthInterceptor;

/**
 * @author wing4
 * url拦截配置
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
	
	@Autowired
	AuthInterceptor authInterceptor;
	
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor);
    }
}