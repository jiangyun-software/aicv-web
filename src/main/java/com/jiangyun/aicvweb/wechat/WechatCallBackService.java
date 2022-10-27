package com.jiangyun.aicvweb.wechat;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiangyun.aicvweb.utils.RedisUtil;

@Service
public class WechatCallBackService {
	
	@Autowired
	RedisUtil redisUtil;

	public void callBack(String xml) throws DocumentException {
		SAXReader reader = new SAXReader();
        Document doc = reader.read(new ByteArrayInputStream(xml.getBytes()));
        Element root = doc.getRootElement();
        List<Element> ticketList = root.elements("Ticket");
        // 扫码回调
        if (ticketList.size() == 1) {
        	String ticket = ticketList.get(0).getText();
        	String openid = root.elements("FromUserName").get(0).getText();
        	redisUtil.set("login:ticket:" + ticket, openid, 5, TimeUnit.MINUTES);
        	return;
        }
        
	}
}
