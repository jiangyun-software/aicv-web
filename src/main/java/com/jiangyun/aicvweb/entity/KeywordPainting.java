package com.jiangyun.aicvweb.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class KeywordPainting {

	Long id;
	
	Member member;
	
	String image;
	
	String keyword;
	
	String params;
	
	boolean download;
	
	LocalDateTime createTime;
	
	long useTime;
}
