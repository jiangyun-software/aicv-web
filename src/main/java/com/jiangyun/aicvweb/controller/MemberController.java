package com.jiangyun.aicvweb.controller;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jiangyun.aicvweb.common.Result;
import com.jiangyun.aicvweb.entity.ChangePasswordVO;
import com.jiangyun.aicvweb.entity.ChangePhonenumberVO;
import com.jiangyun.aicvweb.entity.Member;
import com.jiangyun.aicvweb.service.MemberService;
import com.jiangyun.aicvweb.utils.ExceptionUtil;

@RestController
@RequestMapping("/member")
public class MemberController {
	
	@Autowired
	MemberService memberService;

	/**
	 * 查询用户详情
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	public Result selectMemberById(@PathVariable Long id) {
		return Result.ok(memberService.selectMemberById(id));
	}
	
	/**
	 * 查询用户列表
	 * @param member
	 * @return
	 */
	@GetMapping("/list")
	public Result selectMemberList(Member member) {
		return Result.ok(() -> memberService.selectMemberList(member));
	}
	
	/**
	 * 修改手机号
	 * @param changePhonenumberVO
	 * @return
	 */
	@PostMapping("changePhonenumber")
	public Result changePhonenumber(@RequestBody @Validated ChangePhonenumberVO changePhonenumberVO) {
		if (changePhonenumberVO.getType().equals("new")) {
			if (changePhonenumberVO.getNewPhonenumber() == null || !Pattern.matches("^[1][3,4,5,6,7,8,9][0-9]{9}$", changePhonenumberVO.getNewPhonenumber())) {
				ExceptionUtil.warningUser("新手机号格式错误");
			} else if (StringUtils.isBlank(changePhonenumberVO.getNewSmsCode())) {
				ExceptionUtil.warningUser("验证码不能为空");
			}
		}
		
		return Result.ok(memberService.changePhonenumber(changePhonenumberVO));
	}
	
	/**
	 * 修改密码
	 * @param changePhonenumberVO
	 * @return
	 */
	@PostMapping("changePassword")
	public Result changePassword(@RequestBody @Validated ChangePasswordVO changePasswordVO) {
		
		
		return Result.ok(memberService.changePassword(changePasswordVO));
	}
}
