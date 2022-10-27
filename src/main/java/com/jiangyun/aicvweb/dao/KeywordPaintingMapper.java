package com.jiangyun.aicvweb.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.jiangyun.aicvweb.entity.KeywordPainting;

@Mapper
public interface KeywordPaintingMapper {

	int submit(KeywordPainting keywordPainting);
	
	List<KeywordPainting> history(Long userId);
	
	int delete(Long id, Long userId);
	
	int download(Long id, Long userId);
	
	KeywordPainting selectById(Long id);
}
