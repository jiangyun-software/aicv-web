package com.jiangyun.aicvweb.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jiangyun.aicvweb.common.Result;
import com.jiangyun.aicvweb.entity.CutoutVO;
import com.jiangyun.aicvweb.service.CutoutService;

/**
 * 抠图接口
 * @author wing4
 *
 */
@RestController
@RequestMapping("/cutout")
public class CutoutController {
	
	@Autowired
	CutoutService cutoutService;

	@PostMapping(consumes = "application/json")
	public Result cutout(@RequestBody CutoutVO cutout) {
		return Result.ok(cutoutService.cutout(cutout));
	}
	
	@PostMapping(consumes = "multipart/form-data")
	public Result cutout(MultipartFile file) throws IOException {
		return Result.ok(cutoutService.cutout(file));
	}
}
