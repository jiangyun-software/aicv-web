package com.jiangyun.aicvweb.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.jiangyun.aicvweb.utils.RedisUtil;
import com.jiangyun.aicvweb.wechat.WechatService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MySchedule {
	
	@Autowired
	WechatService wechatService;
	
	@Autowired
	RedisUtil redisUtil;
	
	/**
	 * 定时刷新微信公众号的token
	 */
	@Scheduled(fixedRate = 300000)
	private void refreshOfficialAccountToken() throws Exception {
		if (redisUtil.lock("officialAccountToken", 180L)) {
			try {
				wechatService.getToken();
			} catch (Exception e) {
				redisUtil.unlock("officialAccountToken");
				log.error("刷新微信公众号token失败");
			}
		}
	}

}
