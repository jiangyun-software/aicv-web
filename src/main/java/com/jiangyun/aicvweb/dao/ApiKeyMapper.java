package com.jiangyun.aicvweb.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.jiangyun.aicvweb.entity.ApiKey;

@Mapper
public interface ApiKeyMapper {
	
	int insert(ApiKey apiKey);
	
	int update(ApiKey apiKey);
	
	List<ApiKey> list(Long memberId);
	
	ApiKey selectById(Long id);
	
	int deleteById(Long id);
	
	int resetPrimaryKey(String key, Long memberId);
	
	ApiKey selectPrimaryKey(Long memberId);
}
