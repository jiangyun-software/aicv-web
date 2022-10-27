package com.jiangyun.aicvweb.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jiangyun.aicvweb.entity.MobileUploadFileVO;
import com.jiangyun.aicvweb.utils.ExceptionUtil;
import com.jiangyun.aicvweb.utils.RedisUtil;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;

@Service
public class MobileUploadService {
	
	@Value("${tencentCloud.cos.bucketName}")
	String bucketName;
	
	@Value("${tencentCloud.cos.host}")
	String host;
	
	@Autowired
	RedisUtil redisUtil;
	
	@Autowired
	COSClient cosClient;
	
	public String getUploadKey() {
		String key = UUID.randomUUID().toString();
		redisUtil.set("mobileupload:" + key, new ArrayList<String>(), 5L, TimeUnit.MINUTES);
		
		return key;
	}
	
	public int checkUploadKey(String key) {
		List<?> fiels = redisUtil.get("mobileupload:" + key, ArrayList.class);
		if (fiels == null || fiels.size() != 0) {
			ExceptionUtil.warningUser("二维码已过期，请重新扫码");
		}
		return 1;
	}
	
	public int upload(String key, MultipartFile[] files) throws IOException {
		String fileUrl = redisUtil.get("mobileupload:" + key);
		if (fileUrl == null || !fileUrl.equals("0")) {
			ExceptionUtil.warningUser("二维码已过期，请重新扫码");
		}
		
		List<String> urls = new ArrayList<>();
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType("image/*");
		for (MultipartFile file : files) {
			String path = "temp/" + UUID.randomUUID().toString();
			metadata.setContentLength(file.getSize());
			PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, path, file.getInputStream(), metadata);
			cosClient.putObject(putObjectRequest);
			urls.add(host + "/" + bucketName + "/" + path);
		}
		
		redisUtil.set("mobileupload:" + key, StringUtils.join(urls, ","), 5L, TimeUnit.MINUTES);
		
		return files.length;
	}
	
	@Async
	public Future<Map<String, Object>> getUploadFile(String key) throws InterruptedException {
		Map<String, Object> result = new HashMap<>();
		result.put("status", "2");
		
		int times = 15;
		while(times > 0) {
			List<?> fiels = redisUtil.get("mobileupload:" + key, ArrayList.class);
			if (fiels == null) {
				result.put("status", "0");
				times = 0;
			}else if (fiels.size() == 0) {
				Thread.sleep(1000);
				times --;
			} else {
				result.put("status", "1");
				result.put("files", fiels);
				redisUtil.delete("mobileupload:" + key);
				times = 0;
			}
		}
		
		return new AsyncResult<>(result);
	}
	
	public int setUploadFile(MobileUploadFileVO params) {
		String key = params.getKey();
		List<?> fiels = redisUtil.get("mobileupload:" + key, ArrayList.class);
		if (fiels == null || fiels.size() != 0) {
			ExceptionUtil.warningUser("二维码已过期，请重新扫码");
		}
		
		redisUtil.set("mobileupload:" + key, params.getFiles(), 5L, TimeUnit.MINUTES);
		
		return 1;
	}
	

}
