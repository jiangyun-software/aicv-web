package com.jiangyun.aicvweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jiangyun.aicvweb.common.Result;
import com.jiangyun.aicvweb.service.InvitationLogService;

@RestController
@RequestMapping("/invitationLog")
public class InvitationLogController {
	
	@Autowired
	InvitationLogService invitationLogService;

	@GetMapping
	public Result log() {
		return Result.ok(() -> invitationLogService.list());
	}
	
}
