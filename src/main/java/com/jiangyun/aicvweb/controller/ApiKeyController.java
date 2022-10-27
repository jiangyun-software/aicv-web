package com.jiangyun.aicvweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jiangyun.aicvweb.common.Result;
import com.jiangyun.aicvweb.entity.ApiKey;
import com.jiangyun.aicvweb.service.ApiKeyService;

@RestController
@RequestMapping("/apiKey")
public class ApiKeyController {
	
	@Autowired
	ApiKeyService apiKeyService;

	@PostMapping
	public Result save(@RequestBody ApiKey apiKey) {
		return Result.ok(apiKeyService.save(apiKey));
	}
	
	@GetMapping
	public Result list() {
		return Result.ok(() -> apiKeyService.list());
	}
	
	@GetMapping("/details/{id}")
	public Result selectById(@PathVariable("id") Long id) {
		return Result.ok(apiKeyService.selectById(id));
	}
	
	@DeleteMapping("/{id}")
	public Result deleteById(@PathVariable("id") Long id) {
		return Result.ok(apiKeyService.deleteById(id));
	}
	
	@GetMapping("/primary")
	public Result primary() {
		return Result.ok(apiKeyService.selectPrimaryKey());
	}
	
	@GetMapping("/resetPrimary")
	public Result resetPrimary() {
		return Result.ok(apiKeyService.resetPrimaryKey());
	}
}
