package com.workat.dto;

import static lombok.AccessLevel.*;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class KakaoOAuthTokenRequestDto {
	private String code;

	private KakaoOAuthTokenRequestDto(String code) {
		this.code = code;
	}

	public static KakaoOAuthTokenRequestDto from(String code) {
		return new KakaoOAuthTokenRequestDto(code);
	}
}
