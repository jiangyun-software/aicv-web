package com.jiangyun.aicvweb.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiangyun.aicvweb.common.IdGenerator;
import com.jiangyun.aicvweb.dao.ApiKeyMapper;
import com.jiangyun.aicvweb.entity.ApiKey;
import com.jiangyun.aicvweb.utils.ExceptionUtil;
import com.jiangyun.aicvweb.utils.SecurityUtil;

@Service
public class ApiKeyService {
	
	@Autowired
	ApiKeyMapper apiKeyMapper;
	
	public int initPrimaryKey(Long MemberId) {
		ApiKey apiKey = new ApiKey();
		apiKey.setMemberId(MemberId);
		apiKey.setType("0");
		apiKey.setKey(UUID.randomUUID().toString().replace("-", ""));
		
		return apiKeyMapper.insert(apiKey);
	}
	
	public int save(ApiKey apiKey) {
		if (!apiKey.isPermanent() && apiKey.getExpirationDate() == null) {
			ExceptionUtil.warningUser("有效期不能为空");
		}
		
		if (apiKey.getId() == null) {
			apiKey.setId(IdGenerator.getId());
			apiKey.setMemberId(SecurityUtil.getMemberId());
			apiKey.setType("1");
			apiKey.setKey(UUID.randomUUID().toString().replace("-", ""));
			return apiKeyMapper.insert(apiKey);
		} else {
			return apiKeyMapper.update(apiKey);
		}
	}
	
	public List<ApiKey> list() {
		return apiKeyMapper.list(SecurityUtil.getMemberId());
	}
	
	public ApiKey selectById(Long id) {
		return apiKeyMapper.selectById(id);
	}
	
	public int deleteById(Long id) {
		return apiKeyMapper.deleteById(id);
	}
	
	public String resetPrimaryKey() {
		String key = UUID.randomUUID().toString().replace("-", "");
		Long memberId = SecurityUtil.getMemberId();
		apiKeyMapper.resetPrimaryKey(key, memberId);
		return key;
	}
	
	public ApiKey selectPrimaryKey() {
		return apiKeyMapper.selectPrimaryKey(SecurityUtil.getMemberId());
	}
}
