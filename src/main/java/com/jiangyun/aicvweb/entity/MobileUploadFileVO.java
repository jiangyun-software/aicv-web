package com.jiangyun.aicvweb.entity;

import java.util.List;

import lombok.Data;

@Data
public class MobileUploadFileVO {
	
	String key;
	
	List<String> files;

}
