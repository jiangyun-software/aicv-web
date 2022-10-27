package com.jiangyun.aicvweb.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jiangyun.aicvweb.annotation.IgnoreAuth;
import com.jiangyun.aicvweb.service.ApiService;

@RestController
public class ApiController {
	
	@Autowired
	ApiService apiService;

	@RequestMapping("/api/**")
	@IgnoreAuth
	public void requestApi(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		apiService.requestApi(req, resp);
	}
	
}
