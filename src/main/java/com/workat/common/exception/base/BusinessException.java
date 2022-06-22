package com.workat.common.exception.base;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

	private final HttpStatus code;

	public BusinessException(HttpStatus code, String msg) {
		super(msg);
		this.code = code;
	}

	public BusinessException(HttpStatus code, Throwable t) {
		super(t);
		this.code = code;
	}
}
