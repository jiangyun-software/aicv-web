package com.jiangyun.aicvweb.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.jiangyun.aicvweb.entity.InvitationLog;

@Mapper
public interface InvitationLogMapper {
	
	int insert(InvitationLog invitationLog);
	
	List<InvitationLog> list(Long memberId);
}
