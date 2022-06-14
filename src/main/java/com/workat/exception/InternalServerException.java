package com.workat.exception;

import org.springframework.http.HttpStatus;

import com.workat.exception.base.BusinessException;

public class InternalServerException extends BusinessException {

	public InternalServerException(String msg) {
		super(HttpStatus.INTERNAL_SERVER_ERROR, msg);
	}

	public InternalServerException(Throwable t) {
		super(HttpStatus.INTERNAL_SERVER_ERROR, t);
	}
}
