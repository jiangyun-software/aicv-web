package com.jiangyun.aicvweb.wechat;

import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jiangyun.aicvweb.annotation.IgnoreAuth;

@RestController
@RequestMapping("/wechatCallback")
public class WechatCallbackController {
	
	@Autowired
	WechatCallBackService wechatCallBackService;

	@GetMapping
	@IgnoreAuth
	public String verifyToken(String echostr) {
		return echostr;
	}
	
	@PostMapping
	@IgnoreAuth
	public String callBack(@RequestBody String xml) throws DocumentException {
		wechatCallBackService.callBack(xml);
		return "success";
	}
}
