package com.workat.dto;

import static lombok.AccessLevel.*;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class KakaoOAuthAccessToken {
	private String accessToken;

	private KakaoOAuthAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public static KakaoOAuthAccessToken from(String accessToken) {
		return new KakaoOAuthAccessToken(accessToken);
	}
}
