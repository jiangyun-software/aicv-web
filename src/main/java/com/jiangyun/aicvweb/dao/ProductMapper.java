package com.jiangyun.aicvweb.dao;

import org.apache.ibatis.annotations.Mapper;

import com.jiangyun.aicvweb.entity.Product;

@Mapper
public interface ProductMapper {
	
	Product selectByKey(String key);
}
