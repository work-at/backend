package com.workat.exception.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.workat.exception.base.BusinessException;
import com.workat.exception.dto.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BusinessException.class)
	protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
		return new ResponseEntity<>(ErrorResponse.of(e.getCode().value(), e.getMessage()), e.getCode());
	}
}
