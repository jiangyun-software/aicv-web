package com.jiangyun.aicvweb.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jiangyun.aicvweb.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
public class Member extends BaseEntity {

	private static final long serialVersionUID = 8715930575212906594L;
	
	public Member(Long id) {
		this.id = id;
	}

	/**
	 * id
	 */
	private Long id;
	
	/**
	 * 手机号
	 */
	private String phonenumber;
	
	/**
	 * 密码
	 */
	@JsonIgnore
	private String password;
	
	/**
	 * 微信公众号openid
	 */
	private String openid;
	
	/**
	 * 禁用状态
	 */
	private Boolean disabled;
	
	/**
	 * 图片积分
	 */
	private long point;
	
	/**
	 * 微信个人信息
	 */
	private String wechatInfo;
	
	private boolean initPassword;
	
	private String invitationCode;
	
}
