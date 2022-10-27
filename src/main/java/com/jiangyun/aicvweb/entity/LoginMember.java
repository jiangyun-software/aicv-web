package com.jiangyun.aicvweb.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class LoginMember implements Serializable {

	private static final long serialVersionUID = 2188732046301552577L;
	
	private Member member;
	
	private String token;
	
	private boolean rememberMe;

}
