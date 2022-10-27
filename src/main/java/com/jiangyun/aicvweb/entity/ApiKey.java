package com.jiangyun.aicvweb.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.jiangyun.aicvweb.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ApiKey extends BaseEntity {

	private static final long serialVersionUID = 5082916783877824652L;

	Long id;
	
	Long memberId;
	
	String key;
	
	String type;
	
	boolean permanent;
	
	LocalDate expirationDate;
	
	boolean disabled;
	
	LocalDateTime createTime;
	
	LocalDateTime updateTime;
	
	String remark;
}
