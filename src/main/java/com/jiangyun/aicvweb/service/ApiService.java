package com.jiangyun.aicvweb.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jiangyun.aicvweb.utils.ExceptionUtil;

import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * api接口调用代理
 */
@Service
@Slf4j
public class ApiService {
	
	private static final String APPLICATION_JSON = "application/json";
	
	@Value("${api.host}")
	String apiHost;
	
	@Autowired
	OkHttpClient httpClient;
	
	public void requestApi(HttpServletRequest req, HttpServletResponse resp) {
		try {
			String url = req.getServletPath().substring(5);
			
			// url参数
			HttpUrl.Builder httpUrlBuilder = buildUrl(url);
			Map<String, String[]> queryParameters = req.getParameterMap();
			for (Entry<String, String[]> params : queryParameters.entrySet()) {
				for (String param : params.getValue()) {
					httpUrlBuilder.addQueryParameter(params.getKey(), param);
				}
			}
			
			Request.Builder requestBuilder = new Request.Builder().url(httpUrlBuilder.build());
			
			// formData参数
			if (req instanceof StandardMultipartHttpServletRequest) {
				StandardMultipartHttpServletRequest multipartReq = (StandardMultipartHttpServletRequest) req;
				MultipartBody.Builder bodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
				for (String fileParam: multipartReq.getMultiFileMap().keySet()) {
					List<MultipartFile> files = multipartReq.getFiles(fileParam);
					for(MultipartFile file : files) {
						bodyBuilder.addFormDataPart(fileParam, file.getName(), RequestBody.create(file.getBytes(), MediaType.parse("application/octet-stream")));
					};
				}
				
				for (Entry<String, String[]> entry: multipartReq.getParameterMap().entrySet()) {
					for (String value: entry.getValue()) {
						bodyBuilder.addFormDataPart(entry.getKey(), value);
					}
				}
				
				requestBuilder.post(bodyBuilder.build());
			} else if (req.getMethod().equals("POST") && req.getContentType().equals(APPLICATION_JSON)) {
				// json参数
				RequestBody body = RequestBody.create(req.getInputStream().readAllBytes(), MediaType.parse(APPLICATION_JSON));
				requestBuilder.post(body);
			}
			
			log.info(requestBuilder.getUrl$okhttp().toString());
			
			Response response = httpClient.newCall(requestBuilder.build()).execute();
			
			if (response.code() != 200) {
				ExceptionUtil.warningUser("api请求失败");
			}
			
			// 响应头
			String contentType = response.header("Content-Type");
			resp.addHeader("Content-Type", contentType);
			// 响应体
			if (contentType.equals(APPLICATION_JSON)) {
				// 解析json重新封装
				String jsonStr = response.body().string();
				JsonObject jo = new Gson().fromJson(jsonStr, JsonObject.class);
				resp.getOutputStream().write(jo.get("parameters").getAsJsonObject()
											.get("__results__").getAsJsonObject()
											.get("poller/rep-0").getAsJsonObject()
											.toString().getBytes());
			} else {
				resp.getOutputStream().write(response.body().bytes());
			}
			
			response.body().close();
		} catch (IOException e) {
			ExceptionUtil.warningUser("api请求失败");
		}
	}
	
	private HttpUrl.Builder buildUrl(String url) {
		String[] apiHostParam = apiHost.split(":");
		
		HttpUrl.Builder builder =  new HttpUrl.Builder()
				.scheme("http")
				.addPathSegments(url)
				.host(apiHostParam[0]);
		if (apiHostParam.length == 2) {
			builder.port(Integer.valueOf(apiHostParam[1]));
		}
		
		return builder;
	}

}
