package com.jiangyun.aicvweb.entity;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class ChangePasswordVO {

	private String oldPassword;
	
	@NotEmpty(message = "新密码不能为空")
	@Length(min = 8, message = "新密码长度不能少于8位")
	private String newPassword;
}
