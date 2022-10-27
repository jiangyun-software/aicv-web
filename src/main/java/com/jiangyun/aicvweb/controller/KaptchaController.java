package com.jiangyun.aicvweb.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jiangyun.aicvweb.annotation.IgnoreAuth;
import com.jiangyun.aicvweb.common.Result;
import com.jiangyun.aicvweb.entity.Kaptcha;
import com.jiangyun.aicvweb.service.KaptchaService;

@RestController
@RequestMapping("/kaptcha")
public class KaptchaController {
	
	@Autowired
	KaptchaService kaptchaService;
	
	@GetMapping
	@IgnoreAuth
	public Result getKaptcha() throws IOException {
		return Result.ok(kaptchaService.getKaptcha());
	}
	
	@PostMapping
	@IgnoreAuth
	public Result checkKaptcha(@RequestBody @Validated Kaptcha captcha) {
		boolean b = kaptchaService.checkKaptcha(captcha);
		return b ? Result.ok() : Result.error("验证码错误");
	}
	
}
