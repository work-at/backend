package com.workat.common.exception;

import org.springframework.http.HttpStatus;

import com.workat.common.exception.base.BusinessException;

public class FileUploadException extends BusinessException {

	public FileUploadException(String msg) {
		super(HttpStatus.INTERNAL_SERVER_ERROR, msg);
	}

	public FileUploadException(Throwable t) {
		super(HttpStatus.INTERNAL_SERVER_ERROR, t);
	}
}
