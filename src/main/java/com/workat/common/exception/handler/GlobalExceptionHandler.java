package com.workat.common.exception.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.workat.common.exception.base.BusinessException;
import com.workat.common.exception.dto.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BusinessException.class)
	protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
		return new ResponseEntity<>(ErrorResponse.of(e.getCode().value(), e.getMessage()), e.getCode());
	}
}
