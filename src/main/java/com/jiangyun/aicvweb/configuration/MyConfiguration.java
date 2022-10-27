package com.jiangyun.aicvweb.configuration;

import java.net.InetSocketAddress;
import java.net.Proxy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.region.Region;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;

import io.minio.MinioClient;
import okhttp3.OkHttpClient;

@Configuration
public class MyConfiguration {
	
	@Value("${api.host}")
	String apiHost;
	
	@Value("${minio.url}")
	String minioUrl;
	
	@Value("${minio.username}")
	String minioUsername;
	
	@Value("${minio.password}")
	String minioPassword;
	
	@Value("${tencentCloud.secretId}")
	String tencentSecretId;
	
	@Value("${tencentCloud.secretKey}")
	String tencentSecretKey;
	
	@Value("${tencentCloud.sms.appId}")
	String smsAppId;
	
	@Value("${tencentCloud.sms.signName}")
	String smsSignName;
	
	@Value("${spring.profiles.active}")
	String env;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	@Primary
	public OkHttpClient wechatOkHttpClient() {
		OkHttpClient okHttpClient =  new OkHttpClient.Builder()
				.retryOnConnectionFailure(true)
				.build();
		
		return okHttpClient;
	}
	
	@Bean("wechat")
	public OkHttpClient okHttpClient() {
		OkHttpClient.Builder okHttpClientBuilder =  new OkHttpClient.Builder()
				.retryOnConnectionFailure(true);
		if ("dev".equals(env)) {
			Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("124.223.111.28", 10810));
			okHttpClientBuilder.proxy(proxy);
		}
		
		return okHttpClientBuilder.build();
	}
	
	@Bean
	public MinioClient minioClient() {
		return MinioClient.builder()
	        	.endpoint(minioUrl)
		        .credentials(minioUsername, minioPassword)
		        .build();
	}
	
	@Bean
	public Credential credential() {
		return new Credential(tencentSecretId, tencentSecretKey);
	}
	
	@Bean
	public SmsClient smsClient(Credential credential) {
		return new SmsClient(credential, "ap-guangzhou");
	}
	
	@Bean
	public SendSmsRequest smsRequest() {
		SendSmsRequest req = new SendSmsRequest();
		req.setSmsSdkAppId(smsAppId);
		req.setSignName(smsSignName);
		return req;
	}
	
	@Bean
	public COSCredentials cOSCredentials() {
		return new BasicCOSCredentials(tencentSecretId, tencentSecretKey);
	}
	
	@Bean
	public COSClient cosClient(COSCredentials cOSCredentials) {
		Region region = new Region("ap-guangzhou");
		ClientConfig clientConfig = new ClientConfig(region);
		return new COSClient(cOSCredentials, clientConfig);
	}
	
}
