package com.workat.api.auth.dto;

import static lombok.AccessLevel.*;

import com.workat.domain.auth.AuthCode;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class AuthResponse {

	private AuthCode code;

	private String accessToken;

	private long ouathId;

	private AuthResponse(AuthCode code, long ouathId) {
		this.code = code;
		this.ouathId = ouathId;
	}

	private AuthResponse(AuthCode code, String accessToken) {
		this.code = code;
		this.accessToken = accessToken;
	}

	public static AuthResponse ResponseForLogin(String accessToken) {
		return new AuthResponse(AuthCode.WORK01, accessToken);
	}

	public static AuthResponse ResponseForSignup(long ouathId) {
		return new AuthResponse(AuthCode.WORK02, ouathId);
	}

}
