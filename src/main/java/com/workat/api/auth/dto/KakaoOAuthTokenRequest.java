package com.workat.api.auth.dto;

import static lombok.AccessLevel.*;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class KakaoOauthTokenRequest {

	private String code;

	private KakaoOauthTokenRequest(String code) {
		this.code = code;
	}

	public static KakaoOauthTokenRequest of(String code) {
		return new KakaoOauthTokenRequest(code);
	}
}
