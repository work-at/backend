package com.workat.common.exception;

import org.springframework.http.HttpStatus;

import com.workat.common.exception.base.BusinessException;

public class ExternalApiException extends BusinessException {

	public ExternalApiException(String msg) {
		super(HttpStatus.SERVICE_UNAVAILABLE, msg);
	}

	public ExternalApiException(Throwable t) {
		super(HttpStatus.SERVICE_UNAVAILABLE, t);
	}
}
