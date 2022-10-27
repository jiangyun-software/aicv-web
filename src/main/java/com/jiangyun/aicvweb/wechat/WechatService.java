package com.jiangyun.aicvweb.wechat;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jiangyun.aicvweb.utils.ExceptionUtil;
import com.jiangyun.aicvweb.utils.RedisUtil;

import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

@Service
@Slf4j
public class WechatService {
	
	@Value("${wechat.officialAccount.appid}")
	String appid;
	
	@Value("${wechat.officialAccount.appsecret}")
	String appsecret;
	
	@Autowired
	RedisUtil redisUtil;
	
	@Autowired
	@Qualifier("wechat")
	OkHttpClient okHttpClient;
	
	public String getToken() {
		String token = redisUtil.get("officialAccountToken");
		if (token == null) {
			String result = "";
			try {
	        	String url = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s", appid, appsecret);
	            Request request = new Request.Builder().url(url).get().build();
	            result = okHttpClient.newCall(request).execute().body().string();
				JsonObject jo = new Gson().fromJson(result, JsonObject.class);
				token = jo.get("access_token").getAsString();
				redisUtil.set("officialAccountToken", token, 2, TimeUnit.HOURS);
			} catch (Exception e) {
				log.error("获取微信登陆二维码失败:" + result);
				ExceptionUtil.warningUser("获取微信公众号token失败");
			}
		}
		
		return token;
		
	}
	
	public String loginTicket() {
		String token = redisUtil.get("officialAccountToken");
		String url = String.format("https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=%s", token);
		String param = "{\"expire_seconds\": 300, \"action_name\": \"QR_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": \"" + UUID.randomUUID().toString() +"\"}}}";
		RequestBody body = RequestBody.create(param, MediaType.parse("application/json"));
        Request request = new Request.Builder().url(url).post(body).build();
        String ticket = "";
        String result = "";
        try {
        	result = okHttpClient.newCall(request).execute().body().string();
        	JsonObject jo = new Gson().fromJson(result, JsonObject.class);
    		ticket = jo.get("ticket").getAsString();
    		redisUtil.set("login:ticket:" + ticket, "1", 5, TimeUnit.MINUTES);
		} catch (Exception e) {
			log.error("获取微信登陆二维码失败:" + result);
			ExceptionUtil.warningUser("获取微信登陆二维码失败");
		}
		return ticket;
	}
}
