package com.jiangyun.aicvweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiangyun.aicvweb.dao.ProductMapper;
import com.jiangyun.aicvweb.entity.Product;

@Service
public class ProductService {
	
	@Autowired
	private ProductMapper productMapper;

	public Product selectByKey(String key) {
		return productMapper.selectByKey(key);
	}

}
