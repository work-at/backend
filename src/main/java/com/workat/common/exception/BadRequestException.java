package com.workat.common.exception;

import org.springframework.http.HttpStatus;

import com.workat.common.exception.base.BusinessException;

public class BadRequestException extends BusinessException {

	public BadRequestException(String msg) {
		super(HttpStatus.BAD_REQUEST, msg);
	}

	public BadRequestException(Throwable t) {
		super(HttpStatus.BAD_REQUEST, t);
	}
}
