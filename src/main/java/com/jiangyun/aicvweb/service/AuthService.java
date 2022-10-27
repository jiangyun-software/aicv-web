package com.jiangyun.aicvweb.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jiangyun.aicvweb.entity.LoginForm;
import com.jiangyun.aicvweb.entity.LoginMember;
import com.jiangyun.aicvweb.entity.Member;
import com.jiangyun.aicvweb.entity.RegisterMember;
import com.jiangyun.aicvweb.entity.WechatRegister;
import com.jiangyun.aicvweb.utils.ExceptionUtil;
import com.jiangyun.aicvweb.utils.RedisUtil;
import com.jiangyun.aicvweb.utils.SecurityUtil;

@Service
public class AuthService {
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	RedisUtil redisUtil;
	
	@Autowired
	SmsService smsService;
	
	@Autowired
	InvitationLogService invitationLogService;
	
	/**
	 * 密码登录
	 */
	public String login(LoginForm loginForm) {
		String phonenumber = loginForm.getPhonenumber();
		String memberCode = loginForm.getSmsCode();
		String password = loginForm.getPassword();
		String smsCode = smsService.getSmsCodeByPhone(phonenumber);
		
		// 登录限制
		int count = redisUtil.keys("limit:login:" + phonenumber + ":*").size();
		if (count > 10) {
			ExceptionUtil.warningUser("密码错误次数过多，请稍后再试");
		}
		
		Member member = memberService.selectMemberByPhonenumber(phonenumber);
		if (member == null) {
			if (StringUtils.isNotBlank(memberCode)) {
				if (StringUtils.isNoneEmpty(smsCode, memberCode) && StringUtils.equals(smsCode, memberCode)) {
					// 注册
					Member registerMember = new Member();
					registerMember.setPhonenumber(phonenumber);
					registerMember.setPassword(registerMember.getPassword());
					memberService.save(member);
				} else {
					ExceptionUtil.warningUser("短信验证码不正确");
				}
			} else {
				redisUtil.set("limit:login:" + phonenumber + ":" + System.currentTimeMillis(), 1, 5, TimeUnit.MINUTES);
				ExceptionUtil.warningUser("手机号或密码错误");
			}
		} else if (StringUtils.isNotBlank(password) && !SecurityUtil.validPassword(password, member.getPassword())) {
			redisUtil.set("limit:login:" + phonenumber + ":" + System.currentTimeMillis(), 1, 5, TimeUnit.MINUTES);
			ExceptionUtil.warningUser("手机号或密码错误");
		} else if (StringUtils.isNotBlank(memberCode) && !StringUtils.equals(smsCode, memberCode)) {
			ExceptionUtil.warningUser("短信验证码不正确");
		}
		
		return createToke(member);
	}
	
	/**
	 * 微信扫码登录
	 * @throws InterruptedException 
	 */
	@Async
	public Future<Map<String, Object>> wechatLogin(String ticket) throws InterruptedException {
		Map<String, Object> result = new HashMap<>();
		String openid = redisUtil.get("login:ticket:" + ticket);
		
		int times = 15;
		while(times > 0) {
			// 未扫描
			if ("1".equals(openid)) {
				Thread.sleep(1000);
				openid = redisUtil.get("login:ticket:" + ticket);
				times --;
				if (times == 0) {
					result.put("status", "noScan");
					return new AsyncResult<Map<String,Object>>(result);
				}
			} else {
				times = 0;
			}
		}
		
		// ticket已过期
		if(StringUtils.isBlank(openid)) {
			result.put("status", "expired");
			return new AsyncResult<Map<String,Object>>(result);
		}
		Member member = memberService.selectMemberByOpenid(openid);
		// 微信未注册
		if (member == null) {
			result.put("status", "unRegister");
			return new AsyncResult<Map<String,Object>>(result);
		}
		
		// 登录
		result.put("status", "success");
		result.put("token", createToke(member));
		
		return new AsyncResult<Map<String,Object>>(result);
	}
	
	/**
	 * 创建token
	 * @param member
	 * @return
	 */
	public String createToke(Member member) {
		String token = UUID.randomUUID().toString();
		LoginMember loginMember = new LoginMember();
		loginMember.setToken(token);
		loginMember.setMember(member);
		
		redisUtil.set("login_token:" + token, loginMember, 2, TimeUnit.HOURS);
		
		return token;
	}
	
	/**
	 * 注册用户
	 */
	@Transactional
	public String register(RegisterMember registerMember) {
		// 手机号注册
		String smsCode = smsService.getSmsCodeByPhone(registerMember.getPhonenumber());
		String memberCode = registerMember.getSmsCode();
		
		boolean b = StringUtils.isNoneEmpty(smsCode, memberCode) && StringUtils.equals(smsCode, memberCode);
		if (!b) {
			ExceptionUtil.warningUser("短信验证码不正确");
		}
		
		Member member = new Member();
		member.setPhonenumber(registerMember.getPhonenumber());
		member.setPassword(registerMember.getPassword());
		memberService.save(member);
		
		if (StringUtils.isNotBlank(registerMember.getInvitationCode())) {
			invitationLogService.save(member.getId(), memberCode);
		}
		
		return createToke(member);
	}
	
	/**
	 * 微信注册
	 */
	public String wechatRegister(WechatRegister wechatRegister) {
		String openid = redisUtil.get("login:ticket:" + wechatRegister.getTicket());
		if(openid == null) {
			ExceptionUtil.warningUser("注册失败");
		}
		
		String smsCode = smsService.getSmsCodeByPhone(wechatRegister.getPhonenumber());
		String memberCode = wechatRegister.getSmsCode();
		
		boolean b = StringUtils.equals(smsCode, memberCode);
		if (!b) {
			ExceptionUtil.warningUser("短信验证码不正确");
		}
		
		Member checkMember = memberService.selectMemberByPhonenumber(wechatRegister.getPhonenumber());
		Member member = new Member();
		member.setPhonenumber(wechatRegister.getPhonenumber());
		member.setOpenid(openid);
		if (checkMember == null) {
			memberService.save(member);
		} else {
			member.setId(checkMember.getId());
			memberService.bindWechat(member);
		}
		
		return createToke(member);
	}

	/**
	 * 退出登录
	 */
	public void logout(String token) {
		redisUtil.delete("login_token:" + token);
	}

	/**
	 * 获取当前登录用户信息
	 */
	public Member getMemberInfo() {
		return SecurityUtil.getMemberInfo();
	}

}
