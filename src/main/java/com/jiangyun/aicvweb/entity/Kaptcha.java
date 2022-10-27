package com.jiangyun.aicvweb.entity;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Kaptcha {

	@NotEmpty(message="验证码id不能为空")
	private String id;
	
	private String img;
	
	@NotEmpty(message="验证码不能为空")
	private String code;
}
