package com.jiangyun.aicvweb.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class InvitationLog {

	private Long id;
	
	private Member member;
	
	private Member invitationMember;
	
	private int point;
	
	private LocalDateTime createTime;
}
