package com.jiangyun.aicvweb.component;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jiangyun.aicvweb.utils.ExceptionUtil;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Component
public class Http {

	@Autowired
	OkHttpClient okHttpClient;
	
	public <T> String request(String url, String method, T params) {
		
		return null;
	}
	
	/**
	 * 发送post请求，参数为json字符串，返回字符串
	 * @param url
	 * @param method
	 * @param jsonParams
	 * @return
	 */
	public String post(String url, String jsonParams) {
		RequestBody requestBody = RequestBody.create(jsonParams, MediaType.parse("application/json"));
		Request request = new Request.Builder().url(url).post(requestBody).build();
		String result = null;
		try {
			Response response = okHttpClient.newCall(request).execute();
			result = response.body().string();
		} catch (IOException e) {
			ExceptionUtil.warningUser("接口调用失败");
		}
		return result;
	}
	
	public String post(String url, Object objectPrams) {
		String jsonParams = new Gson().toJson(objectPrams);
		return this.post(url, jsonParams);
	}
	
	public String form(String url, Object objectPrams) {
		JsonObject jo = new Gson().toJsonTree(objectPrams).getAsJsonObject();
		FormBody.Builder bodyBulder = new FormBody.Builder();
		jo.entrySet().forEach(item -> {
			bodyBulder.addEncoded(item.getKey(), item.getValue().getAsString());
		});
		
		Request request = new Request.Builder().url(url).post(bodyBulder.build()).build();
		String result = null;
		try {
			Response response = okHttpClient.newCall(request).execute();
			result = response.body().string();
		} catch (IOException e) {
			ExceptionUtil.warningUser("接口调用失败");
		}
		return result;
	}
	
	public <T> T post(String url, String jsonParams, Class<T> clazz) {
		String result = this.post(url, jsonParams);
		return new Gson().fromJson(result, clazz);
	}
	
	public String get(String url, String method, Object params) {
		if (params != null) {
			StringBuffer query = new StringBuffer("?");
			JsonObject jo = new Gson().toJsonTree(params).getAsJsonObject();
			jo.entrySet().forEach(item -> {
				query.append(item.getKey()).append("=").append(item.getValue().getAsString());
			});
			url += query.toString();
		}
		
		Request request = new Request.Builder().url(url).get().build();
		
		String result = null;
		try {
			Response response = okHttpClient.newCall(request).execute();
			result = response.body().string();
		} catch (IOException e) {
			ExceptionUtil.warningUser("接口调用失败");
		}
		return result;
	}
}
