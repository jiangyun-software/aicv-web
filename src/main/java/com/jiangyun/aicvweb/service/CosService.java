package com.jiangyun.aicvweb.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jiangyun.aicvweb.common.Result;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.Headers;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.GetObjectRequest;
import com.qcloud.cos.model.ListObjectsRequest;
import com.qcloud.cos.model.ObjectListing;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;

@Service
public class CosService {
	
	@Value("${tencentCloud.cos.region}")
	String region;
	
	@Value("${tencentCloud.cos.bucketName}")
	String bucketName;
	
	@Value("${tencentCloud.cos.host}")
	String host;
	
	@Autowired
	COSCredentials cOSCredentials;
	
	@Autowired
	COSClient cosClient;
	

	public String getTempUploadUrl() {
		LocalDate localDate = LocalDate.now();
		String path = String.format("/temp/%d/%d/%d/%s", localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth(), UUID.randomUUID().toString());
		
		ClientConfig clientConfig = new ClientConfig(new Region(region));
		Map<String, String> headers = new HashMap<String, String>();
		headers.put(Headers.HOST, clientConfig.getEndpointBuilder().buildGeneralApiEndpoint(bucketName));
		
		Map<String, String> params = new HashMap<String, String>();
		
		Date expirationDate = new Date(System.currentTimeMillis() + 30L * 60L * 1000L);
		
		return cosClient.generatePresignedUrl(bucketName, path, expirationDate, HttpMethodName.PUT, headers, params).toString();
	}
	
	public String uploadTemp(MultipartFile file) throws IOException {
		LocalDate localDate = LocalDate.now();
		String path = String.format("/temp/%d/%d/%d/%s", localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth(), UUID.randomUUID().toString());
		
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType("image/png");
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, path, file.getInputStream(), metadata);
		PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
		
		return host + path;
	}
	
	public byte[] vewImage(String path) throws IOException {
		GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, path);
		COSObject cosObject = cosClient.getObject(getObjectRequest);
		byte[] bytes = cosObject.getObjectContent().readAllBytes();
		
		return bytes;
	}
}
