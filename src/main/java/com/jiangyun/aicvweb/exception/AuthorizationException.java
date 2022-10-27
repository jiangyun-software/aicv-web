package com.jiangyun.aicvweb.exception;

/**
 * 未登录
 * @author wing4
 *
 */
public class AuthorizationException extends BaseException {

	private static final long serialVersionUID = -4176011463634092613L;

	public AuthorizationException() {
		super("认证失败");
	}
}
