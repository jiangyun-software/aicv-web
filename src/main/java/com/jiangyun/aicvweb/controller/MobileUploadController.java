package com.jiangyun.aicvweb.controller;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jiangyun.aicvweb.annotation.IgnoreAuth;
import com.jiangyun.aicvweb.common.Result;
import com.jiangyun.aicvweb.entity.MobileUploadFileVO;
import com.jiangyun.aicvweb.service.MobileUploadService;

@RestController
@RequestMapping("/mobileUpload")
@IgnoreAuth
public class MobileUploadController {
	
	@Autowired
	MobileUploadService mobileUploadService;

	@GetMapping("/getUploadKey")
	public Result getUploadKey() {
		return Result.ok(mobileUploadService.getUploadKey());
	}
	
	@GetMapping("/checkUploadKey/{key}")
	public Result checkUploadKey(@PathVariable String key) {
		return Result.ok(mobileUploadService.checkUploadKey(key));
	}
	
	@PostMapping("/upload")
	public Result upload(String key, MultipartFile[] files) throws IOException {
		return Result.ok(mobileUploadService.upload(key, files));
	}
	
	@GetMapping("/getUploadFile/{key}")
	public Result getUploadFile(@PathVariable String key) throws InterruptedException, ExecutionException {
		return Result.ok(mobileUploadService.getUploadFile(key).get());
	}
	
	@PostMapping("/setUploadFile")
	public Result setUploadFile(@RequestBody MobileUploadFileVO params) {
		return Result.ok(mobileUploadService.setUploadFile(params));
	}
	
}
