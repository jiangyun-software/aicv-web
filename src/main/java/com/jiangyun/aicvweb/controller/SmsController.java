package com.jiangyun.aicvweb.controller;

import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jiangyun.aicvweb.annotation.IgnoreAuth;
import com.jiangyun.aicvweb.common.Result;
import com.jiangyun.aicvweb.service.SmsService;

@RestController
@RequestMapping("sms")
public class SmsController {
	
	@Autowired
	SmsService smsService;

	@GetMapping("sendCode/{phonenumber}")
	@IgnoreAuth
	public Result sendSms(
			@PathVariable 
			@Pattern(regexp = "^[1][3,4,5,6,7,8,9][0-9]{9}$", message = "手机号格式有误") 
			String phonenumber)
	{
		smsService.sendSmsCode(phonenumber);
		return Result.ok();
	}
}
