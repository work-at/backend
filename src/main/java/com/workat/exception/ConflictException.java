package com.workat.exception;

import org.springframework.http.HttpStatus;

import com.workat.exception.base.BusinessException;

public class ConflictException extends BusinessException {

	public ConflictException(String msg) {
		super(HttpStatus.CONFLICT, msg);
	}

	public ConflictException(Throwable t) {
		super(HttpStatus.CONFLICT, t);
	}
}
