package com.jiangyun.aicvweb.controller;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jiangyun.aicvweb.annotation.IgnoreAuth;
import com.jiangyun.aicvweb.common.Result;
import com.jiangyun.aicvweb.entity.LoginForm;
import com.jiangyun.aicvweb.entity.RegisterMember;
import com.jiangyun.aicvweb.entity.WechatRegister;
import com.jiangyun.aicvweb.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	AuthService authService;
	
	/**
	 * 登录
	 * @param loginForm
	 * @return
	 */
	@PostMapping("/login")
	@IgnoreAuth
	public Result login(@RequestBody LoginForm loginForm) {
		return Result.ok(authService.login(loginForm));
	}
	
	/**
	 * 扫码登录
	 * @param loginForm
	 * @return
	 * @throws InterruptedException 
	 * @throws ExecutionException 
	 */
	@GetMapping("/scanResult")
	@IgnoreAuth
	public Result wechatLogin(@RequestParam String ticket) throws InterruptedException, ExecutionException {
		return Result.ok(authService.wechatLogin(ticket).get());
	}
	
	
	
	/**
	 * 注册用户
	 * @param id
	 * @return
	 */
	@PostMapping("/register")
	@IgnoreAuth
	public Result register(@RequestBody @Validated RegisterMember member) {
		return Result.ok(authService.register(member));
	}
	
	/**
	 * 登录
	 * @param loginForm
	 * @return
	 */
	@PostMapping("/wechatRegister")
	@IgnoreAuth
	public Result wechatRegister(@RequestBody WechatRegister wechatRegister) {
		return Result.ok(authService.wechatRegister(wechatRegister));
	}
	
	/**
	 * 退出
	 * @param token
	 * @return
	 */
	@GetMapping("/logout")
	@IgnoreAuth
	public Result logout(@RequestHeader("authorization") String token) {
		authService.logout(token);
		return Result.ok();
	}
	
	/**
	 * 获取当前用户信息
	 * @return
	 */
	@GetMapping("/memberInfo")
	@IgnoreAuth
	public Result memberInfo() {
		return Result.ok(authService.getMemberInfo());
	}
}
