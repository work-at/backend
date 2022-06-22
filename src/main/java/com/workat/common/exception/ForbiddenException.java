package com.workat.common.exception;

import org.springframework.http.HttpStatus;

import com.workat.common.exception.base.BusinessException;

public class ForbiddenException extends BusinessException {

	public ForbiddenException(String msg) {
		super(HttpStatus.FORBIDDEN, msg);
	}

	public ForbiddenException(Throwable t) {
		super(HttpStatus.FORBIDDEN, t);
	}
}
