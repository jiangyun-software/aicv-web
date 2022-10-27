package com.jiangyun.aicvweb.service;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jiangyun.aicvweb.common.IdGenerator;
import com.jiangyun.aicvweb.dao.KeywordPaintingMapper;
import com.jiangyun.aicvweb.entity.KeywordPainting;
import com.jiangyun.aicvweb.entity.Product;
import com.jiangyun.aicvweb.entity.Member;
import com.jiangyun.aicvweb.utils.ExceptionUtil;
import com.jiangyun.aicvweb.utils.SecurityUtil;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
public class KeywordPaintingService {
	
	private static final String KeywordPaintingKey = "keywordPainting";
	
	@Autowired
	OkHttpClient okHttpClient;
	
	@Autowired
	ProductService productService;
	
	@Autowired
	KeywordPaintingMapper keywordPaintingMapper;

	public int submit(KeywordPainting keywordPainting) {
		Product product = productService.selectByKey(KeywordPaintingKey);
		String[] hosts = product.getUrl().split(",");
		String host = hosts[0];
		
		Long memberId = SecurityUtil.getMemberId();
		Member member = new Member();
		member.setId(memberId);
		keywordPainting.setMember(member);
		keywordPainting.setId(IdGenerator.getId());
		
		Map<String, Object> params = new HashMap<>();
		List<String> text = Arrays.asList(keywordPainting.getParams().split(","));
		params.put("text_prompts", text);
		params.put("id", keywordPainting.getId());
		
		
		RequestBody requestBody = RequestBody.create(new Gson().toJson(params), MediaType.parse("application/json"));
		Request request = new Request.Builder().post(requestBody).url("http://" + host).build();
		try {
			Response response = okHttpClient.newCall(request).execute();
			String result = response.body().string();
			JsonObject jo = new Gson().fromJson(result, JsonObject.class);
		} catch (IOException e) {
			ExceptionUtil.warningUser("api调用失败");
		}
		
		return keywordPaintingMapper.submit(keywordPainting);
	}

	public List<KeywordPainting> history() {
		Long memberId = SecurityUtil.getMemberId();
		return keywordPaintingMapper.history(memberId);
	}

	public int delete(Long id) {
		Long memberId = SecurityUtil.getMemberId();
		return keywordPaintingMapper.delete(id, memberId);
	}

	public ResponseEntity<byte[]> download(Long id) throws IOException {
		Long memberId = SecurityUtil.getMemberId();
		KeywordPainting keywordPainting = this.selectById(id);
		if (keywordPainting == null) {
			ExceptionUtil.warningUser("该图片不存在");
		} else if (StringUtils.isBlank(keywordPainting.getImage())) {
			ExceptionUtil.warningUser("图片还没画完，请稍后再试");
		}
		
		Request request = new Request.Builder().get().url(keywordPainting.getImage()).build();
		Response response = okHttpClient.newCall(request).execute();
		byte[] bytes = response.body().bytes();
		
		int download = keywordPaintingMapper.download(id, memberId);
		
		String fileName = StringUtils.substringAfterLast(keywordPainting.getImage(), "/");
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentDispositionFormData("attachment", URLEncoder.encode(fileName,"UTF-8"));
		headers.setContentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentLength(bytes.length);
		headers.set("fileName", URLEncoder.encode(fileName,"UTF-8"));
		// setAccessControlExposeHeaders 设置这个前端才能取到
		headers.setAccessControlExposeHeaders(Arrays.asList(HttpHeaders.CONTENT_DISPOSITION,
		        HttpHeaders.CONTENT_LENGTH, HttpHeaders.CONTENT_TYPE));
		
		return ResponseEntity.status(HttpStatus.OK).headers(headers).body(bytes);
	}
	
	public KeywordPainting selectById(Long id) {
		return keywordPaintingMapper.selectById(id);
	}

}
