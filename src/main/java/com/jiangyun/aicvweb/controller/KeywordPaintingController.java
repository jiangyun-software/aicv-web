package com.jiangyun.aicvweb.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jiangyun.aicvweb.common.Result;
import com.jiangyun.aicvweb.entity.KeywordPainting;
import com.jiangyun.aicvweb.service.KeywordPaintingService;

/**
 * 关键字作画
 * @author wing4
 *
 */
@RestController
@RequestMapping("/keywordPainting")
public class KeywordPaintingController {
	
	@Autowired
	KeywordPaintingService keywordPaintingService;

	@PostMapping
	public Result submit(@RequestBody KeywordPainting keywordPainting){
		return Result.ok(keywordPaintingService.submit(keywordPainting));
	}
	
	@GetMapping
	public Result history() {
		return Result.ok(() -> keywordPaintingService.history());
	}
	
	@DeleteMapping
	public Result delete(Long id) {
		return Result.ok(keywordPaintingService.delete(id));
	}
	
	@GetMapping("/download/{id}")
	public ResponseEntity<byte[]> download(@PathVariable Long id) throws IOException {
		
		return keywordPaintingService.download(id);
	}
}
