package com.jiangyun.aicvweb.entity;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class LoginForm {
	
	@NotEmpty(message="手机号不能为空")
	@Pattern(regexp = "^[1][3,4,5,6,7,8,9][0-9]{9}$", message = "手机号格式有误")
	private String phonenumber;
	
	private String password;
	
	private String smsCode;

}
