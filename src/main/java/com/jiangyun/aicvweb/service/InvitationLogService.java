package com.jiangyun.aicvweb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiangyun.aicvweb.common.IdGenerator;
import com.jiangyun.aicvweb.dao.InvitationLogMapper;
import com.jiangyun.aicvweb.entity.InvitationLog;
import com.jiangyun.aicvweb.entity.Member;
import com.jiangyun.aicvweb.utils.ExceptionUtil;
import com.jiangyun.aicvweb.utils.SecurityUtil;

@Service
public class InvitationLogService {
	
	@Autowired
	InvitationLogMapper invitationLogMapper;
	
	@Autowired
	MemberService memberService;
	
	public int save(Long invititionMemberId, String invitationCode) {
		Member member = memberService.selectMemberByInvitationCode(invitationCode);
		if (member == null) {
			ExceptionUtil.warningUser("邀请码不存在");
		}
		
		InvitationLog invitationLog = new InvitationLog();
		invitationLog.setId(IdGenerator.getId());
		invitationLog.setMember(member);
		invitationLog.setInvitationMember(new Member(invititionMemberId));
		invitationLog.setPoint(5);
		
		return invitationLogMapper.insert(invitationLog);
	}
	
	public List<InvitationLog> list() {
		return invitationLogMapper.list(SecurityUtil.getMemberId());
	}
}
