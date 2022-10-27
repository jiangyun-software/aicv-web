package com.jiangyun.aicvweb.utils;

import com.jiangyun.aicvweb.exception.AuthorizationException;
import com.jiangyun.aicvweb.exception.NeedVerificationCodeException;
import com.jiangyun.aicvweb.exception.WarningUserException;

public class ExceptionUtil {

	/**
	 * 接口错误提示
	 * @param msg
	 */
	public static void warningUser(String msg) {
		throw new WarningUserException(msg);
    }
	
	/**
	 * token异常
	 * @param msg
	 */
	public static void invalidToken() {
		throw new AuthorizationException();
    }
	
	/**
	 * token异常
	 * @param msg
	 */
	public static void needVerificationCode() {
		throw new NeedVerificationCodeException();
    }
}
