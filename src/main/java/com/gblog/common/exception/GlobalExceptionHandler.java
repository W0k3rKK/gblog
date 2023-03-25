package com.gblog.common.exception;

import com.gblog.common.code.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author: W0k3rKK
 * @description 全局异常处理
 * @date: 2023/3/24 22:12
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * 捕捉shiro的异常
	 * @param e
	 * @return
	 */
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(value = ShiroException.class)
	public Result handler(ShiroException e) {
		log.error("运行时异常：----------------{}", e);
		return Result.unAuth(e.getMessage());
	}


	/**
	 * 捕捉实体校验异常
	 * @param e
	 * @return
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public Result handler(MethodArgumentNotValidException e) {
		log.error("实体校验异常：----------------{}", e);
		BindingResult bindingResult = e.getBindingResult();
		ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();

		return Result.notFound(objectError.getDefaultMessage());
	}

	/**
	 * 捕捉Assert的异常
	 * @param e
	 * @return
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = IllegalArgumentException.class)
	public Result handler(IllegalArgumentException e) {
		log.error("Assert异常：----------------{}", e);

		return Result.notFound(e.getMessage());
	}

	/**
	 * 捕捉运行时异常
	 * @param e
	 * @return
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = RuntimeException.class)
	public Result handler(RuntimeException e) {
		log.error("运行时异常：----------------{}", e);
		return Result.notFound(e.getMessage());
	}

}
