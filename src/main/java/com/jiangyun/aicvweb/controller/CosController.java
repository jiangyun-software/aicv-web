package com.jiangyun.aicvweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jiangyun.aicvweb.annotation.IgnoreAuth;
import com.jiangyun.aicvweb.common.Result;
import com.jiangyun.aicvweb.service.CosService;

@RestController
@RequestMapping("/cos")
public class CosController {
	
	@Autowired
	CosService cosService;

	@GetMapping("/getTempUploadUrl")
	@IgnoreAuth
	public Result getUploadUrl(){
		return Result.ok(cosService.getTempUploadUrl());
	}
	
}
