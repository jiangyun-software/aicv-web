package com.jiangyun.aicvweb.wechat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jiangyun.aicvweb.annotation.IgnoreAuth;
import com.jiangyun.aicvweb.common.Result;

@RestController
@RequestMapping("/wechat")
public class WechatController {
	
	@Autowired
	WechatService wechatService;

	@GetMapping("/loginTicket")
	@IgnoreAuth
	public Result loginTicket() {
		return Result.ok(wechatService.loginTicket());
	}
}
