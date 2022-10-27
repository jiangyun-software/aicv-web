package com.jiangyun.aicvweb.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.jiangyun.aicvweb.component.Http;
import com.jiangyun.aicvweb.entity.CutoutVO;
import com.jiangyun.aicvweb.entity.Product;
import com.jiangyun.aicvweb.utils.ExceptionUtil;

@Service
public class CutoutService {
	
	private static final String PRODUCT_KEY_CUTOUT = "cutout";
	
	@Autowired
	ProductService productService;
	
	@Autowired
	Http http;
	
	@Autowired
	CosService cosService;
	
	public String cutout(CutoutVO cutout) {
		Product product = productService.selectByKey(PRODUCT_KEY_CUTOUT);
		
		String image = null;
		try {
			String result =  http.form(product.getUrl(), cutout);
			JsonObject jo = new Gson().fromJson(result, JsonObject.class);
			image = jo.get("image").getAsString();
		} catch (JsonSyntaxException e) {
			ExceptionUtil.warningUser("接口调用失败");
		}
		
		return image;
	}
	
	public String cutout(MultipartFile file) throws IOException {
		String url = cosService.uploadTemp(file);
		
		CutoutVO cutout = new CutoutVO();
		cutout.setData(url);
		return cutout(cutout);
	}

}
