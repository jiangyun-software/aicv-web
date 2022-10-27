package com.jiangyun.aicvweb.entity;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class WechatRegister implements Serializable {

	private static final long serialVersionUID = -2263473649772072555L;

	@NotNull
	private String ticket;
	
	@NotNull(message = "手机号不能为空")
	@Pattern(regexp = "^[1][3,4,5,6,7,8,9][0-9]{9}$", message = "手机号格式有误")
	private String phonenumber;
	
	@NotNull(message = "验证码不能为空")
	private String smsCode;
}
