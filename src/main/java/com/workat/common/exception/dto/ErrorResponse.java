package com.workat.common.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ErrorResponse {

	private int code;

	private String message;

	public static ErrorResponse of(int code, String message) {
		return new ErrorResponse(code, message);
	}
}
