package com.jiangyun.aicvweb.common;

import java.util.stream.Collectors;

import javax.servlet.ServletException;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.jiangyun.aicvweb.exception.NeedVerificationCodeException;
import com.jiangyun.aicvweb.exception.WarningUserException;

/**
 * 全局异常处理
 * @author wing4
 *
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(WarningUserException.class)
	public Result warningUserException(WarningUserException e) {
		return Result.error(e.getMessage());
	}
	
	@ExceptionHandler(NeedVerificationCodeException.class)
	public Result needVerificationCodeException(NeedVerificationCodeException e) {
		return new Result(501, null, "需要验证码");
	}
	
	/**
	 * 参数验证错误
	 * @param e
	 * @return
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Result methodArgumentNotValidException(MethodArgumentNotValidException e) {
		return Result.error(e.getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.joining(",")));
	}
	
	@ExceptionHandler(ServletException.class)
	public Result servletException(ServletException e) {
		return Result.error(e.getMessage());
	}
	
	@ExceptionHandler(Exception.class)
	public Result exception(Exception e) {
		e.printStackTrace();
		return Result.error("系统错误");
	}
}
