package com.jiangyun.aicvweb.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.github.pagehelper.ISelect;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.gson.Gson;
import com.jiangyun.aicvweb.utils.ServletUtil;

public class Result implements Serializable {
	
	private static final long serialVersionUID = -6664979058017836345L;
	
	// 接口正常
	public static final int OK = 200;
	
	public static final int ERROR = 500;
	
	private int code;
	
	private Object data;
	
	private String msg;
	
	public Result(int code, Object data, String msg) {
		this.code = code;
		this.data = data;
		this.msg = msg;
	}
	
	public static Result ok(Object data) {
		return new Result(OK, data, "success");
	}
	
	public static Result ok(ISelect iselect) {
		Integer pageNum = ServletUtil.getParameterToInt("pageNum", 1);
		Integer pageSize = ServletUtil.getParameterToInt("pageSize", 10);
		Page<Object> page = PageHelper.startPage(pageNum, pageSize).doSelectPage(iselect);
		Map<String, Object> pageData = new HashMap<>();
		pageData.put("rows", page.getResult());
		pageData.put("total", page.getTotal());
		return ok(pageData);
	}
	
	public static Result ok() {
		return new Result(OK, null, "success");
	}
	
	public static Result error(String msg) {
		return new Result(ERROR, null, msg);
	}
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}
