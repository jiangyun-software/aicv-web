package com.jiangyun.aicvweb.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jiangyun.aicvweb.annotation.IgnoreAuth;
import com.jiangyun.aicvweb.common.Result;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.ListObjectsRequest;
import com.qcloud.cos.model.ObjectListing;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;

import okhttp3.OkHttpClient;

@RestController
@RequestMapping("/test")
public class TestController {
	
	@Autowired
	OkHttpClient httpClient;
	
	@Autowired
	COSClient cosClient;
	
	@PostMapping("/img")
	@IgnoreAuth
	public void img(MultipartFile[] file, HttpServletResponse resp) throws IOException {
		resp.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
		resp.getOutputStream().write(file[1].getBytes());
	}
	
	@PostMapping("/oss/upload")
	@IgnoreAuth
	public Result upload(MultipartFile file) throws IOException{
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType("image/*");
		PutObjectRequest putObjectRequest = new PutObjectRequest("1204-1309389332", file.getOriginalFilename(), file.getInputStream(), metadata);
		PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
		return Result.ok(putObjectResult.getRequestId());
	}
	
	@GetMapping("/oss/fileList")
	@IgnoreAuth
	public Result fileList() throws IOException{
		ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
		listObjectsRequest.setBucketName("1204-1309389332");
		ObjectListing objectListing = cosClient.listObjects(listObjectsRequest);
		List<String> fileList = objectListing.getObjectSummaries().stream().map(item -> item.getKey()).toList();
		return Result.ok(fileList);
	}
	
	
}
