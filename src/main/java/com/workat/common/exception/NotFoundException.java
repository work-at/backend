package com.workat.common.exception;

import org.springframework.http.HttpStatus;

import com.workat.common.exception.base.BusinessException;

public class NotFoundException extends BusinessException {

	public NotFoundException(String msg) {
		super(HttpStatus.NOT_FOUND, msg);
	}

	public NotFoundException(Throwable t) {
		super(HttpStatus.NOT_FOUND, t);
	}
}
