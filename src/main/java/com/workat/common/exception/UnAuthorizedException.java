package com.workat.common.exception;

import org.springframework.http.HttpStatus;

import com.workat.common.exception.base.BusinessException;

public class UnAuthorizedException extends BusinessException {

	public UnAuthorizedException(String msg) {
		super(HttpStatus.UNAUTHORIZED, msg);
	}

	public UnAuthorizedException(Throwable t) {
		super(HttpStatus.UNAUTHORIZED, t);
	}
}
