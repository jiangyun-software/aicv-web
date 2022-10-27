package com.jiangyun.aicvweb.service;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jiangyun.aicvweb.service.SmsService;
import com.jiangyun.aicvweb.utils.ExceptionUtil;
import com.jiangyun.aicvweb.utils.RedisUtil;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SmsService {
	
//	public static final String SMS_LIMIT_IP_PREFIX = "sms:limit:ip:";
	
	public static final String SMS_CODE_PREFIX = "sms:code:";
	
	@Value("${tencentCloud.sms.template.login}")
	String smsCodeTemplateId;
	
	@Autowired
	RedisUtil redisUtil;
	
	@Autowired
	SmsClient smsClient;
	
	@Autowired
	SendSmsRequest smsRequest;

	public void sendSmsCode(String phonenumber) {
		
		boolean pass = Pattern.matches("^[1][3,4,5,6,7,8,9][0-9]{9}$", phonenumber);
		if (!pass) {
			ExceptionUtil.warningUser("手机号不合法");
		}
		
		Random random = new Random();
		DecimalFormat format = new DecimalFormat("000000");
		String smsCode = format.format(random.nextInt(1000000));
		
		redisUtil.set(SMS_CODE_PREFIX + phonenumber, smsCode, 5L, TimeUnit.MINUTES);
		
		// 发送短信验证码
		smsRequest.setTemplateId(smsCodeTemplateId);
		smsRequest.setTemplateParamSet(new String[] {smsCode, "5"});
		smsRequest.setPhoneNumberSet(new String[] {phonenumber});
		try {
			SendSmsResponse smsResponse = smsClient.SendSms(smsRequest);
			log.info(SendSmsResponse.toJsonString(smsResponse));
		} catch (TencentCloudSDKException e) {
			ExceptionUtil.warningUser("短信验证码发送失败");
		}

	}
	
	public String getSmsCodeByPhone(String phonenumber) {
		String code = redisUtil.get(SMS_CODE_PREFIX + phonenumber);
		return code;
	}

}
