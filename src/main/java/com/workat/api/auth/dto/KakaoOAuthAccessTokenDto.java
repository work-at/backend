package com.workat.api.auth.dto;

import static lombok.AccessLevel.*;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class KakaoOAuthAccessTokenDto {
	private String accessToken;

	private KakaoOAuthAccessTokenDto(String accessToken) {
		this.accessToken = accessToken;
	}

	public static KakaoOAuthAccessTokenDto of(String accessToken) {
		return new KakaoOAuthAccessTokenDto(accessToken);
	}
}
