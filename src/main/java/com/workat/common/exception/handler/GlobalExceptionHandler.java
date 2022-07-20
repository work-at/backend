package com.workat.common.exception.handler;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex){
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors()
			.forEach(c -> errors.put(((FieldError) c).getField(), c.getDefaultMessage()));
		return ResponseEntity.badRequest().body(errors);
	}
}
