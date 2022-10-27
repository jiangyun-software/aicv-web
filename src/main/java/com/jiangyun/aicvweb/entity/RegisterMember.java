package com.jiangyun.aicvweb.entity;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.jiangyun.aicvweb.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class RegisterMember {

	/**
	 * 手机号
	 */
	@NotEmpty(message="手机号不能为空")
	@Pattern(regexp = "^[1][3,4,5,6,7,8,9][0-9]{9}$", message = "手机号格式有误")
	private String phonenumber;
	
	/**
	 * 密码
	 */
	@NotEmpty(message = "密码不能为空")
	@Length(min = 8, message = "密码长度不能少于8位")
	private String password;
	
	/**
	 * 手机验证码
	 */
	private String smsCode;
	
	/**
	 * 邀请码
	 */
	private String invitationCode;
}
