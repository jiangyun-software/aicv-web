package com.jiangyun.aicvweb.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.jiangyun.aicvweb.entity.Kaptcha;
import com.jiangyun.aicvweb.service.KaptchaService;
import com.jiangyun.aicvweb.utils.RedisUtil;

@Service
public class KaptchaService{
	
	@Autowired
    private DefaultKaptcha captchaProducer;
	
	@Autowired
    private RedisUtil redisUtil;

	public Kaptcha getKaptcha() throws IOException {
		String id = UUID.randomUUID().toString();
		String code =  captchaProducer.createText();
		BufferedImage bufferImage = captchaProducer.createImage(code);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bufferImage, "png", bos);
        String img = Base64.getEncoder().encodeToString(bos.toByteArray());
		
		redisUtil.set("kaptcha:" + id, code, 5, TimeUnit.MINUTES);
		
		return new Kaptcha(id, img, null);
	}

	public boolean checkKaptcha(Kaptcha kaptcha) {
		String key = "kaptcha:" + kaptcha.getId();
		String code = redisUtil.get(key);
		boolean b = StringUtils.equals(code, kaptcha.getCode());
		
		return b;
	}

}
