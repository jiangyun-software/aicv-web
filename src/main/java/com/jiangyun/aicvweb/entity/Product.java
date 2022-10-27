package com.jiangyun.aicvweb.entity;

import com.jiangyun.aicvweb.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class Product extends BaseEntity {

	private static final long serialVersionUID = -8803774036011237312L;

	Long id;
	
	String name;
	
	int point;
	
	String url;
	
	String key;
	
	boolean disabled;
	
	String remark;
	
	
}
