package com.jiangyun.aicvweb.kafkalistener;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jiangyun.aicvweb.service.KeywordPaintingService;

@Component
public class KeywordPaintingListener {
	
	@Autowired
	KeywordPaintingService keywordPaintingService;

	@KafkaListener(topics = {"keywordPainting"}, groupId = "groupId")
	public void listener(ConsumerRecord<String,String> record) {
		String message = record.value();
		JsonObject jo = new Gson().fromJson(message, JsonObject.class);
		
//		keywordPaintingService
	}
}
