package com.jiangyun.aicvweb.service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jiangyun.aicvweb.common.IdGenerator;
import com.jiangyun.aicvweb.dao.MemberMapper;
import com.jiangyun.aicvweb.entity.ChangePasswordVO;
import com.jiangyun.aicvweb.entity.ChangePhonenumberVO;
import com.jiangyun.aicvweb.entity.LoginMember;
import com.jiangyun.aicvweb.entity.Member;
import com.jiangyun.aicvweb.utils.ExceptionUtil;
import com.jiangyun.aicvweb.utils.RedisUtil;
import com.jiangyun.aicvweb.utils.SecurityUtil;

@Service
public class MemberService {
	
	@Autowired
	private MemberMapper memberMapper;
	
	@Autowired
	private SmsService smsService;
	
	@Autowired
	private RedisUtil redisUtil;
	
	@Autowired
	ApiKeyService apiKeyService;

	/**
	 * 新增用户
	 * @param member
	 * @return
	 */
	@Transactional
	public int save(Member member) {
		Member checkMember = memberMapper.selectMemberByPhonenumber(member.getPhonenumber());
		if (checkMember != null) {
			ExceptionUtil.warningUser("该手机号已注册");
		}
		member.setId(IdGenerator.getId());
		member.setInvitationCode(SecurityUtil.invitationCode(member.getId()));
		if (StringUtils.isNotBlank(member.getPassword())) {
			member.setPassword(SecurityUtil.encodePassword(member.getPassword()));
		}
		
		apiKeyService.initPrimaryKey(member.getId());
		
		return memberMapper.save(member);
	}
	
	/**
	 * 绑定微信
	 * @param member
	 * @return
	 */
	public int bindWechat(Member member) {
		return memberMapper.bindWechat(member);
	}

	/**
	 * 启用禁用
	 * @param id
	 * @param disabled
	 * @return
	 */
	public int disabled(Long id, Boolean disabled) {
		return memberMapper.disabled(id, disabled);
	}

	/**
	 * 查询用户列表
	 * @param member
	 * @return
	 */
	public List<Member> selectMemberList(Member member) {
		return memberMapper.selectMemberList(member);
	}

	/**
	 * 查询用户详情
	 * @param id
	 * @return
	 */
	public Member selectMemberById(Long id) {
		return memberMapper.selectMemberById(id);
	}

	public Member selectMemberByPhonenumber(String phonenumber) {
		return memberMapper.selectMemberByPhonenumber(phonenumber);
	}

	public Member selectMemberByOpenid(String openid) {
		return memberMapper.selectMemberByOpenid(openid);
	}
	
	Member selectMemberByInvitationCode(String invittionCode) {
		return memberMapper.selectMemberByInvitationCode(invittionCode);
	}

	/**
	 * 修改手机号
	 */
	public boolean changePhonenumber(ChangePhonenumberVO changePhonenumberVO) {
		String currentPhonenumber = SecurityUtil.getPhonenumber();
		String oldSmsCod = smsService.getSmsCodeByPhone(changePhonenumberVO.getOldPhonenumber());
		String newSmsCode = smsService.getSmsCodeByPhone(changePhonenumberVO.getNewPhonenumber());
		
		if (!currentPhonenumber.equals(changePhonenumberVO.getOldPhonenumber())) {
			ExceptionUtil.warningUser("只能修改自己的手机号");
		}
		
		if (!StringUtils.equalsAny(changePhonenumberVO.getType(), "old", "new")) {
			ExceptionUtil.warningUser("参数错误");
		}
		
		if (changePhonenumberVO.getType().equals("old") && !StringUtils.equals(oldSmsCod, changePhonenumberVO.getOldSmsCode())) {
			ExceptionUtil.warningUser("验证码错误");
		} else if (changePhonenumberVO.getType().equals("new") && (!StringUtils.equals(oldSmsCod, changePhonenumberVO.getOldSmsCode()) || !StringUtils.equals(newSmsCode, changePhonenumberVO.getNewSmsCode()))) {
			ExceptionUtil.warningUser("验证码错误");
		}
		
		if (changePhonenumberVO.getType().equals("new")) {
			memberMapper.changePhonenumber(SecurityUtil.getMemberId(), changePhonenumberVO.getNewPhonenumber());
			
			LoginMember loginMember = SecurityUtil.getLoginMember();
			loginMember.getMember().setPhonenumber(changePhonenumberVO.getNewPhonenumber());
			redisUtil.set("login_token:" + loginMember.getToken(), loginMember, loginMember.isRememberMe() ? 2 : 30 * 24, TimeUnit.HOURS);
		}
		
		return true;
	}

	/**
	 * 修改密码
	 */
	public int changePassword(ChangePasswordVO changePasswordVO) {
		LoginMember loginMember = SecurityUtil.getLoginMember();
		Member member = this.selectMemberById(loginMember.getMember().getId());
		
		if (member.isInitPassword() && StringUtils.isBlank(changePasswordVO.getOldPassword())) {
			ExceptionUtil.warningUser("旧密码不能为空");
		}else if (member.isInitPassword() && !SecurityUtil.validPassword(changePasswordVO.getOldPassword(), member.getPassword())) {
			ExceptionUtil.warningUser("旧密码错误");
		}
		
		String newPassword = SecurityUtil.encodePassword(changePasswordVO.getNewPassword());
		member.setInitPassword(true);
		loginMember.setMember(member);
		
		redisUtil.set("login_token:" + loginMember.getToken(), loginMember, loginMember.isRememberMe() ? 2 : 30 * 24, TimeUnit.HOURS);
		
		return memberMapper.changePassword(loginMember.getMember().getId(), newPassword);
	}

}
