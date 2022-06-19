package com.workat.dto;

import static lombok.AccessLevel.*;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoOAuthTokenResponseDto {
	private String accessToken;
	private String tokenType;
	private String refreshToken;
	private int expiresIn;
	private String scope;
	private int refreshTokenExpiresIn;

	@Builder
	public KakaoOAuthTokenResponseDto(String accessToken, String tokenType, String refreshToken, int expiresIn,
		String scope, int refreshTokenExpiresIn) {
		this.accessToken = accessToken;
		this.tokenType = tokenType;
		this.refreshToken = refreshToken;
		this.expiresIn = expiresIn;
		this.scope = scope;
		this.refreshTokenExpiresIn = refreshTokenExpiresIn;
	}
}
