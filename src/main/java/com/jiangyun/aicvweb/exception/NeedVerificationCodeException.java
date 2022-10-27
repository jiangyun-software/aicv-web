package com.jiangyun.aicvweb.exception;

/**
 * 未登录
 * @author wing4
 *
 */
public class NeedVerificationCodeException extends BaseException {

	private static final long serialVersionUID = -4176011463634092613L;

	public NeedVerificationCodeException() {
		super("需要验证码");
	}
}
