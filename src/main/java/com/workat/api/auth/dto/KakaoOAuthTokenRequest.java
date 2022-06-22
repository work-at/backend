package com.workat.api.auth.dto;

import static lombok.AccessLevel.*;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class KakaoOAuthTokenRequest {
	private String code;

	private KakaoOAuthTokenRequest(String code) {
		this.code = code;
	}

	public static KakaoOAuthTokenRequest of(String code) {
		return new KakaoOAuthTokenRequest(code);
	}
}
