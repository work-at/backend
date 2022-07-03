package com.workat.api.auth.dto;

import static lombok.AccessLevel.*;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class KakaoOauthAccessTokenDto {

	private String accessToken;

	private KakaoOauthAccessTokenDto(String accessToken) {
		this.accessToken = accessToken;
	}

	public static KakaoOauthAccessTokenDto of(String accessToken) {
		return new KakaoOauthAccessTokenDto(accessToken);
	}
}
